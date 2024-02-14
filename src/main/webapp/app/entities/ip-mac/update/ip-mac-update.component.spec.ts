import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IpMacFormService } from './ip-mac-form.service';
import { IpMacService } from '../service/ip-mac.service';
import { IIpMac } from '../ip-mac.model';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';

import { IpMacUpdateComponent } from './ip-mac-update.component';

describe('IpMac Management Update Component', () => {
  let comp: IpMacUpdateComponent;
  let fixture: ComponentFixture<IpMacUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ipMacFormService: IpMacFormService;
  let ipMacService: IpMacService;
  let deviceService: DeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IpMacUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(IpMacUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IpMacUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ipMacFormService = TestBed.inject(IpMacFormService);
    ipMacService = TestBed.inject(IpMacService);
    deviceService = TestBed.inject(DeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Device query and add missing value', () => {
      const ipMac: IIpMac = { id: 456 };
      const device: IDevice = { id: 6274 };
      ipMac.device = device;

      const deviceCollection: IDevice[] = [{ id: 77135 }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const additionalDevices = [device];
      const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ipMac });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(
        deviceCollection,
        ...additionalDevices.map(expect.objectContaining)
      );
      expect(comp.devicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ipMac: IIpMac = { id: 456 };
      const device: IDevice = { id: 51430 };
      ipMac.device = device;

      activatedRoute.data = of({ ipMac });
      comp.ngOnInit();

      expect(comp.devicesSharedCollection).toContain(device);
      expect(comp.ipMac).toEqual(ipMac);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIpMac>>();
      const ipMac = { id: 123 };
      jest.spyOn(ipMacFormService, 'getIpMac').mockReturnValue(ipMac);
      jest.spyOn(ipMacService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ipMac });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ipMac }));
      saveSubject.complete();

      // THEN
      expect(ipMacFormService.getIpMac).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ipMacService.update).toHaveBeenCalledWith(expect.objectContaining(ipMac));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIpMac>>();
      const ipMac = { id: 123 };
      jest.spyOn(ipMacFormService, 'getIpMac').mockReturnValue({ id: null });
      jest.spyOn(ipMacService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ipMac: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ipMac }));
      saveSubject.complete();

      // THEN
      expect(ipMacFormService.getIpMac).toHaveBeenCalled();
      expect(ipMacService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIpMac>>();
      const ipMac = { id: 123 };
      jest.spyOn(ipMacService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ipMac });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ipMacService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDevice', () => {
      it('Should forward to deviceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(deviceService, 'compareDevice');
        comp.compareDevice(entity, entity2);
        expect(deviceService.compareDevice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
