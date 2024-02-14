import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIpMac } from '../ip-mac.model';
import { IpMacService } from '../service/ip-mac.service';

@Injectable({ providedIn: 'root' })
export class IpMacRoutingResolveService implements Resolve<IIpMac | null> {
  constructor(protected service: IpMacService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIpMac | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ipMac: HttpResponse<IIpMac>) => {
          if (ipMac.body) {
            return of(ipMac.body);
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
