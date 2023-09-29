import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SampleFormService } from './sample-form.service';
import { SampleService } from '../service/sample.service';
import { ISample } from '../sample.model';

import { SampleUpdateComponent } from './sample-update.component';

describe('Sample Management Update Component', () => {
  let comp: SampleUpdateComponent;
  let fixture: ComponentFixture<SampleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sampleFormService: SampleFormService;
  let sampleService: SampleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SampleUpdateComponent],
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
      .overrideTemplate(SampleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SampleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sampleFormService = TestBed.inject(SampleFormService);
    sampleService = TestBed.inject(SampleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sample: ISample = { id: 456 };

      activatedRoute.data = of({ sample });
      comp.ngOnInit();

      expect(comp.sample).toEqual(sample);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISample>>();
      const sample = { id: 123 };
      jest.spyOn(sampleFormService, 'getSample').mockReturnValue(sample);
      jest.spyOn(sampleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sample });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sample }));
      saveSubject.complete();

      // THEN
      expect(sampleFormService.getSample).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sampleService.update).toHaveBeenCalledWith(expect.objectContaining(sample));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISample>>();
      const sample = { id: 123 };
      jest.spyOn(sampleFormService, 'getSample').mockReturnValue({ id: null });
      jest.spyOn(sampleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sample: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sample }));
      saveSubject.complete();

      // THEN
      expect(sampleFormService.getSample).toHaveBeenCalled();
      expect(sampleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISample>>();
      const sample = { id: 123 };
      jest.spyOn(sampleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sample });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sampleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
