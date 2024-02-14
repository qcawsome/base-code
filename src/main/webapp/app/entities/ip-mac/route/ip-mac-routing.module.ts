import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IpMacComponent } from '../list/ip-mac.component';
import { IpMacDetailComponent } from '../detail/ip-mac-detail.component';
import { IpMacUpdateComponent } from '../update/ip-mac-update.component';
import { IpMacRoutingResolveService } from './ip-mac-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ipMacRoute: Routes = [
  {
    path: '',
    component: IpMacComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IpMacDetailComponent,
    resolve: {
      ipMac: IpMacRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IpMacUpdateComponent,
    resolve: {
      ipMac: IpMacRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IpMacUpdateComponent,
    resolve: {
      ipMac: IpMacRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ipMacRoute)],
  exports: [RouterModule],
})
export class IpMacRoutingModule {}
