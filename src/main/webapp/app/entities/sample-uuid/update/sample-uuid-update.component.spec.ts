import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SampleUuidFormService } from './sample-uuid-form.service';
import { SampleUuidService } from '../service/sample-uuid.service';
import { ISampleUuid } from '../sample-uuid.model';

import { SampleUuidUpdateComponent } from './sample-uuid-update.component';

describe('SampleUuid Management Update Component', () => {
  let comp: SampleUuidUpdateComponent;
  let fixture: ComponentFixture<SampleUuidUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sampleUuidFormService: SampleUuidFormService;
  let sampleUuidService: SampleUuidService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SampleUuidUpdateComponent],
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
      .overrideTemplate(SampleUuidUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SampleUuidUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sampleUuidFormService = TestBed.inject(SampleUuidFormService);
    sampleUuidService = TestBed.inject(SampleUuidService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sampleUuid: ISampleUuid = { id: 456 };

      activatedRoute.data = of({ sampleUuid });
      comp.ngOnInit();

      expect(comp.sampleUuid).toEqual(sampleUuid);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISampleUuid>>();
      const sampleUuid = { id: 123 };
      jest.spyOn(sampleUuidFormService, 'getSampleUuid').mockReturnValue(sampleUuid);
      jest.spyOn(sampleUuidService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sampleUuid });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sampleUuid }));
      saveSubject.complete();

      // THEN
      expect(sampleUuidFormService.getSampleUuid).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sampleUuidService.update).toHaveBeenCalledWith(expect.objectContaining(sampleUuid));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISampleUuid>>();
      const sampleUuid = { id: 123 };
      jest.spyOn(sampleUuidFormService, 'getSampleUuid').mockReturnValue({ id: null });
      jest.spyOn(sampleUuidService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sampleUuid: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sampleUuid }));
      saveSubject.complete();

      // THEN
      expect(sampleUuidFormService.getSampleUuid).toHaveBeenCalled();
      expect(sampleUuidService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISampleUuid>>();
      const sampleUuid = { id: 123 };
      jest.spyOn(sampleUuidService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sampleUuid });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sampleUuidService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
