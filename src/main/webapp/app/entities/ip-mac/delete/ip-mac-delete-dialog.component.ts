import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIpMac } from '../ip-mac.model';
import { IpMacService } from '../service/ip-mac.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './ip-mac-delete-dialog.component.html',
})
export class IpMacDeleteDialogComponent {
  ipMac?: IIpMac;

  constructor(protected ipMacService: IpMacService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ipMacService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
