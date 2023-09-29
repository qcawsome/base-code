import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SampleUuidFormService, SampleUuidFormGroup } from './sample-uuid-form.service';
import { ISampleUuid } from '../sample-uuid.model';
import { SampleUuidService } from '../service/sample-uuid.service';

@Component({
  selector: 'jhi-sample-uuid-update',
  templateUrl: './sample-uuid-update.component.html',
})
export class SampleUuidUpdateComponent implements OnInit {
  isSaving = false;
  sampleUuid: ISampleUuid | null = null;

  editForm: SampleUuidFormGroup = this.sampleUuidFormService.createSampleUuidFormGroup();

  constructor(
    protected sampleUuidService: SampleUuidService,
    protected sampleUuidFormService: SampleUuidFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sampleUuid }) => {
      this.sampleUuid = sampleUuid;
      if (sampleUuid) {
        this.updateForm(sampleUuid);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sampleUuid = this.sampleUuidFormService.getSampleUuid(this.editForm);
    if (sampleUuid.id !== null) {
      this.subscribeToSaveResponse(this.sampleUuidService.update(sampleUuid));
    } else {
      this.subscribeToSaveResponse(this.sampleUuidService.create(sampleUuid));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISampleUuid>>): void {
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

  protected updateForm(sampleUuid: ISampleUuid): void {
    this.sampleUuid = sampleUuid;
    this.sampleUuidFormService.resetForm(this.editForm, sampleUuid);
  }
}
