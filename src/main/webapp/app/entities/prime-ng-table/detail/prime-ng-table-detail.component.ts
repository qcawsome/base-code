import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrimeNgTable } from '../prime-ng-table.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-prime-ng-table-detail',
  templateUrl: './prime-ng-table-detail.component.html',
})
export class PrimeNgTableDetailComponent implements OnInit {
  primeNgTable: IPrimeNgTable | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ primeNgTable }) => {
      this.primeNgTable = primeNgTable;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
