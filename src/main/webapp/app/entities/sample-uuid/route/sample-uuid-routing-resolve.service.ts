import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISampleUuid } from '../sample-uuid.model';
import { SampleUuidService } from '../service/sample-uuid.service';

@Injectable({ providedIn: 'root' })
export class SampleUuidRoutingResolveService implements Resolve<ISampleUuid | null> {
  constructor(protected service: SampleUuidService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISampleUuid | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sampleUuid: HttpResponse<ISampleUuid>) => {
          if (sampleUuid.body) {
            return of(sampleUuid.body);
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
