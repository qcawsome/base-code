import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'sample',
        data: { pageTitle: 'demoApp.sample.home.title' },
        loadChildren: () => import('./sample/sample.module').then(m => m.SampleModule),
      },
      {
        path: 'sample-uuid',
        data: { pageTitle: 'demoApp.sampleUuid.home.title' },
        loadChildren: () => import('./sample-uuid/sample-uuid.module').then(m => m.SampleUuidModule),
      },
      {
        path: 'prime-ng-table',
        data: { pageTitle: 'demoApp.primeNgTable.home.title' },
        loadChildren: () => import('./prime-ng-table/prime-ng-table.module').then(m => m.PrimeNgTableModule),
      },
      {
        path: 'device',
        data: { pageTitle: 'demoApp.device.home.title' },
        loadChildren: () => import('./device/device.module').then(m => m.DeviceModule),
      },
      {
        path: 'ip-mac',
        data: { pageTitle: 'demoApp.ipMac.home.title' },
        loadChildren: () => import('./ip-mac/ip-mac.module').then(m => m.IpMacModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
