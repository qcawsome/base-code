import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrimeNgTableComponent } from './list/prime-ng-table.component';
import { PrimeNgTableDetailComponent } from './detail/prime-ng-table-detail.component';
import { PrimeNgTableUpdateComponent } from './update/prime-ng-table-update.component';
import { PrimeNgTableDeleteDialogComponent } from './delete/prime-ng-table-delete-dialog.component';
import { PrimeNgTableRoutingModule } from './route/prime-ng-table-routing.module';

import { TableModule } from 'primeng/table';

@NgModule({
  imports: [SharedModule, PrimeNgTableRoutingModule, TableModule],
  declarations: [PrimeNgTableComponent, PrimeNgTableDetailComponent, PrimeNgTableUpdateComponent, PrimeNgTableDeleteDialogComponent],
})
export class PrimeNgTableModule {}
