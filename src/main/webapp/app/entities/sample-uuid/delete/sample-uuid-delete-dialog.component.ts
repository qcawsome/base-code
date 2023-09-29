import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISampleUuid } from '../sample-uuid.model';
import { SampleUuidService } from '../service/sample-uuid.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './sample-uuid-delete-dialog.component.html',
})
export class SampleUuidDeleteDialogComponent {
  sampleUuid?: ISampleUuid;

  constructor(protected sampleUuidService: SampleUuidService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.sampleUuidService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
