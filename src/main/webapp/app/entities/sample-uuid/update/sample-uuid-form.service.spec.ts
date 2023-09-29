import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sample-uuid.test-samples';

import { SampleUuidFormService } from './sample-uuid-form.service';

describe('SampleUuid Form Service', () => {
  let service: SampleUuidFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SampleUuidFormService);
  });

  describe('Service methods', () => {
    describe('createSampleUuidFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSampleUuidFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
          })
        );
      });

      it('passing ISampleUuid should create a new form with FormGroup', () => {
        const formGroup = service.createSampleUuidFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uuid: expect.any(Object),
          })
        );
      });
    });

    describe('getSampleUuid', () => {
      it('should return NewSampleUuid for default SampleUuid initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSampleUuidFormGroup(sampleWithNewData);

        const sampleUuid = service.getSampleUuid(formGroup) as any;

        expect(sampleUuid).toMatchObject(sampleWithNewData);
      });

      it('should return NewSampleUuid for empty SampleUuid initial value', () => {
        const formGroup = service.createSampleUuidFormGroup();

        const sampleUuid = service.getSampleUuid(formGroup) as any;

        expect(sampleUuid).toMatchObject({});
      });

      it('should return ISampleUuid', () => {
        const formGroup = service.createSampleUuidFormGroup(sampleWithRequiredData);

        const sampleUuid = service.getSampleUuid(formGroup) as any;

        expect(sampleUuid).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISampleUuid should not enable id FormControl', () => {
        const formGroup = service.createSampleUuidFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSampleUuid should disable id FormControl', () => {
        const formGroup = service.createSampleUuidFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
