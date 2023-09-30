import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrimeNgTableComponent } from '../list/prime-ng-table.component';
import { PrimeNgTableDetailComponent } from '../detail/prime-ng-table-detail.component';
import { PrimeNgTableUpdateComponent } from '../update/prime-ng-table-update.component';
import { PrimeNgTableRoutingResolveService } from './prime-ng-table-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const primeNgTableRoute: Routes = [
  {
    path: '',
    component: PrimeNgTableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrimeNgTableDetailComponent,
    resolve: {
      primeNgTable: PrimeNgTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrimeNgTableUpdateComponent,
    resolve: {
      primeNgTable: PrimeNgTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrimeNgTableUpdateComponent,
    resolve: {
      primeNgTable: PrimeNgTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(primeNgTableRoute)],
  exports: [RouterModule],
})
export class PrimeNgTableRoutingModule {}
