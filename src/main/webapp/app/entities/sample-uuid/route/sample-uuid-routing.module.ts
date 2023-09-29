import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SampleUuidComponent } from '../list/sample-uuid.component';
import { SampleUuidDetailComponent } from '../detail/sample-uuid-detail.component';
import { SampleUuidUpdateComponent } from '../update/sample-uuid-update.component';
import { SampleUuidRoutingResolveService } from './sample-uuid-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const sampleUuidRoute: Routes = [
  {
    path: '',
    component: SampleUuidComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SampleUuidDetailComponent,
    resolve: {
      sampleUuid: SampleUuidRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SampleUuidUpdateComponent,
    resolve: {
      sampleUuid: SampleUuidRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SampleUuidUpdateComponent,
    resolve: {
      sampleUuid: SampleUuidRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sampleUuidRoute)],
  exports: [RouterModule],
})
export class SampleUuidRoutingModule {}
