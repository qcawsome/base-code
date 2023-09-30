import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../prime-ng-table.test-samples';

import { PrimeNgTableFormService } from './prime-ng-table-form.service';

describe('PrimeNgTable Form Service', () => {
  let service: PrimeNgTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrimeNgTableFormService);
  });

  describe('Service methods', () => {
    describe('createPrimeNgTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPrimeNgTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            text: expect.any(Object),
            number: expect.any(Object),
            floatNumber: expect.any(Object),
            date: expect.any(Object),
            zoneDate: expect.any(Object),
            image: expect.any(Object),
            longText: expect.any(Object),
          })
        );
      });

      it('passing IPrimeNgTable should create a new form with FormGroup', () => {
        const formGroup = service.createPrimeNgTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            text: expect.any(Object),
            number: expect.any(Object),
            floatNumber: expect.any(Object),
            date: expect.any(Object),
            zoneDate: expect.any(Object),
            image: expect.any(Object),
            longText: expect.any(Object),
          })
        );
      });
    });

    describe('getPrimeNgTable', () => {
      it('should return NewPrimeNgTable for default PrimeNgTable initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPrimeNgTableFormGroup(sampleWithNewData);

        const primeNgTable = service.getPrimeNgTable(formGroup) as any;

        expect(primeNgTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewPrimeNgTable for empty PrimeNgTable initial value', () => {
        const formGroup = service.createPrimeNgTableFormGroup();

        const primeNgTable = service.getPrimeNgTable(formGroup) as any;

        expect(primeNgTable).toMatchObject({});
      });

      it('should return IPrimeNgTable', () => {
        const formGroup = service.createPrimeNgTableFormGroup(sampleWithRequiredData);

        const primeNgTable = service.getPrimeNgTable(formGroup) as any;

        expect(primeNgTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPrimeNgTable should not enable id FormControl', () => {
        const formGroup = service.createPrimeNgTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPrimeNgTable should disable id FormControl', () => {
        const formGroup = service.createPrimeNgTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
