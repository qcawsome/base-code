import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IpMacComponent } from './list/ip-mac.component';
import { IpMacDetailComponent } from './detail/ip-mac-detail.component';
import { IpMacUpdateComponent } from './update/ip-mac-update.component';
import { IpMacDeleteDialogComponent } from './delete/ip-mac-delete-dialog.component';
import { IpMacRoutingModule } from './route/ip-mac-routing.module';

@NgModule({
  imports: [SharedModule, IpMacRoutingModule],
  declarations: [IpMacComponent, IpMacDetailComponent, IpMacUpdateComponent, IpMacDeleteDialogComponent],
})
export class IpMacModule {}
