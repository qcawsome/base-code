import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrimeNgTable } from '../prime-ng-table.model';
import { PrimeNgTableService } from '../service/prime-ng-table.service';

@Injectable({ providedIn: 'root' })
export class PrimeNgTableRoutingResolveService implements Resolve<IPrimeNgTable | null> {
  constructor(protected service: PrimeNgTableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrimeNgTable | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((primeNgTable: HttpResponse<IPrimeNgTable>) => {
          if (primeNgTable.body) {
            return of(primeNgTable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
