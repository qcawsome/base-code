import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ip-mac.test-samples';

import { IpMacFormService } from './ip-mac-form.service';

describe('IpMac Form Service', () => {
  let service: IpMacFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IpMacFormService);
  });

  describe('Service methods', () => {
    describe('createIpMacFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIpMacFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ip: expect.any(Object),
            mac: expect.any(Object),
            networkStatus: expect.any(Object),
            agentStatus: expect.any(Object),
            device: expect.any(Object),
          })
        );
      });

      it('passing IIpMac should create a new form with FormGroup', () => {
        const formGroup = service.createIpMacFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ip: expect.any(Object),
            mac: expect.any(Object),
            networkStatus: expect.any(Object),
            agentStatus: expect.any(Object),
            device: expect.any(Object),
          })
        );
      });
    });

    describe('getIpMac', () => {
      it('should return NewIpMac for default IpMac initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIpMacFormGroup(sampleWithNewData);

        const ipMac = service.getIpMac(formGroup) as any;

        expect(ipMac).toMatchObject(sampleWithNewData);
      });

      it('should return NewIpMac for empty IpMac initial value', () => {
        const formGroup = service.createIpMacFormGroup();

        const ipMac = service.getIpMac(formGroup) as any;

        expect(ipMac).toMatchObject({});
      });

      it('should return IIpMac', () => {
        const formGroup = service.createIpMacFormGroup(sampleWithRequiredData);

        const ipMac = service.getIpMac(formGroup) as any;

        expect(ipMac).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIpMac should not enable id FormControl', () => {
        const formGroup = service.createIpMacFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIpMac should disable id FormControl', () => {
        const formGroup = service.createIpMacFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
