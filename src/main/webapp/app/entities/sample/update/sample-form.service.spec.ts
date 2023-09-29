import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sample.test-samples';

import { SampleFormService } from './sample-form.service';

describe('Sample Form Service', () => {
  let service: SampleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SampleFormService);
  });

  describe('Service methods', () => {
    describe('createSampleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSampleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            text: expect.any(Object),
          })
        );
      });

      it('passing ISample should create a new form with FormGroup', () => {
        const formGroup = service.createSampleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            text: expect.any(Object),
          })
        );
      });
    });

    describe('getSample', () => {
      it('should return NewSample for default Sample initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSampleFormGroup(sampleWithNewData);

        const sample = service.getSample(formGroup) as any;

        expect(sample).toMatchObject(sampleWithNewData);
      });

      it('should return NewSample for empty Sample initial value', () => {
        const formGroup = service.createSampleFormGroup();

        const sample = service.getSample(formGroup) as any;

        expect(sample).toMatchObject({});
      });

      it('should return ISample', () => {
        const formGroup = service.createSampleFormGroup(sampleWithRequiredData);

        const sample = service.getSample(formGroup) as any;

        expect(sample).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISample should not enable id FormControl', () => {
        const formGroup = service.createSampleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSample should disable id FormControl', () => {
        const formGroup = service.createSampleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
