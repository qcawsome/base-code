import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SampleUuidComponent } from './list/sample-uuid.component';
import { SampleUuidDetailComponent } from './detail/sample-uuid-detail.component';
import { SampleUuidUpdateComponent } from './update/sample-uuid-update.component';
import { SampleUuidDeleteDialogComponent } from './delete/sample-uuid-delete-dialog.component';
import { SampleUuidRoutingModule } from './route/sample-uuid-routing.module';

@NgModule({
  imports: [SharedModule, SampleUuidRoutingModule],
  declarations: [SampleUuidComponent, SampleUuidDetailComponent, SampleUuidUpdateComponent, SampleUuidDeleteDialogComponent],
})
export class SampleUuidModule {}
