import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PrimeNgTableFormService } from './prime-ng-table-form.service';
import { PrimeNgTableService } from '../service/prime-ng-table.service';
import { IPrimeNgTable } from '../prime-ng-table.model';

import { PrimeNgTableUpdateComponent } from './prime-ng-table-update.component';

describe('PrimeNgTable Management Update Component', () => {
  let comp: PrimeNgTableUpdateComponent;
  let fixture: ComponentFixture<PrimeNgTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let primeNgTableFormService: PrimeNgTableFormService;
  let primeNgTableService: PrimeNgTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PrimeNgTableUpdateComponent],
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
      .overrideTemplate(PrimeNgTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrimeNgTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    primeNgTableFormService = TestBed.inject(PrimeNgTableFormService);
    primeNgTableService = TestBed.inject(PrimeNgTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const primeNgTable: IPrimeNgTable = { id: 456 };

      activatedRoute.data = of({ primeNgTable });
      comp.ngOnInit();

      expect(comp.primeNgTable).toEqual(primeNgTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrimeNgTable>>();
      const primeNgTable = { id: 123 };
      jest.spyOn(primeNgTableFormService, 'getPrimeNgTable').mockReturnValue(primeNgTable);
      jest.spyOn(primeNgTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ primeNgTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: primeNgTable }));
      saveSubject.complete();

      // THEN
      expect(primeNgTableFormService.getPrimeNgTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(primeNgTableService.update).toHaveBeenCalledWith(expect.objectContaining(primeNgTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrimeNgTable>>();
      const primeNgTable = { id: 123 };
      jest.spyOn(primeNgTableFormService, 'getPrimeNgTable').mockReturnValue({ id: null });
      jest.spyOn(primeNgTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ primeNgTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: primeNgTable }));
      saveSubject.complete();

      // THEN
      expect(primeNgTableFormService.getPrimeNgTable).toHaveBeenCalled();
      expect(primeNgTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPrimeNgTable>>();
      const primeNgTable = { id: 123 };
      jest.spyOn(primeNgTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ primeNgTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(primeNgTableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
