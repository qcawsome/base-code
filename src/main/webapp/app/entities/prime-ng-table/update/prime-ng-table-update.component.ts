import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PrimeNgTableFormService, PrimeNgTableFormGroup } from './prime-ng-table-form.service';
import { IPrimeNgTable } from '../prime-ng-table.model';
import { PrimeNgTableService } from '../service/prime-ng-table.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-prime-ng-table-update',
  templateUrl: './prime-ng-table-update.component.html',
})
export class PrimeNgTableUpdateComponent implements OnInit {
  isSaving = false;
  primeNgTable: IPrimeNgTable | null = null;

  editForm: PrimeNgTableFormGroup = this.primeNgTableFormService.createPrimeNgTableFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected primeNgTableService: PrimeNgTableService,
    protected primeNgTableFormService: PrimeNgTableFormService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ primeNgTable }) => {
      this.primeNgTable = primeNgTable;
      if (primeNgTable) {
        this.updateForm(primeNgTable);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('demoApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const primeNgTable = this.primeNgTableFormService.getPrimeNgTable(this.editForm);
    if (primeNgTable.id !== null) {
      this.subscribeToSaveResponse(this.primeNgTableService.update(primeNgTable));
    } else {
      this.subscribeToSaveResponse(this.primeNgTableService.create(primeNgTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrimeNgTable>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(primeNgTable: IPrimeNgTable): void {
    this.primeNgTable = primeNgTable;
    this.primeNgTableFormService.resetForm(this.editForm, primeNgTable);
  }
}
