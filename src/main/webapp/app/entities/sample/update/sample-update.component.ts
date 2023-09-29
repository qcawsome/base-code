import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SampleFormService, SampleFormGroup } from './sample-form.service';
import { ISample } from '../sample.model';
import { SampleService } from '../service/sample.service';

@Component({
  selector: 'jhi-sample-update',
  templateUrl: './sample-update.component.html',
})
export class SampleUpdateComponent implements OnInit {
  isSaving = false;
  sample: ISample | null = null;

  editForm: SampleFormGroup = this.sampleFormService.createSampleFormGroup();

  constructor(
    protected sampleService: SampleService,
    protected sampleFormService: SampleFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sample }) => {
      this.sample = sample;
      if (sample) {
        this.updateForm(sample);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sample = this.sampleFormService.getSample(this.editForm);
    if (sample.id !== null) {
      this.subscribeToSaveResponse(this.sampleService.update(sample));
    } else {
      this.subscribeToSaveResponse(this.sampleService.create(sample));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISample>>): void {
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

  protected updateForm(sample: ISample): void {
    this.sample = sample;
    this.sampleFormService.resetForm(this.editForm, sample);
  }
}
