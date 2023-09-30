import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrimeNgTable } from '../prime-ng-table.model';
import { PrimeNgTableService } from '../service/prime-ng-table.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './prime-ng-table-delete-dialog.component.html',
})
export class PrimeNgTableDeleteDialogComponent {
  primeNgTable?: IPrimeNgTable;

  constructor(protected primeNgTableService: PrimeNgTableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.primeNgTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
