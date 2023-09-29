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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
