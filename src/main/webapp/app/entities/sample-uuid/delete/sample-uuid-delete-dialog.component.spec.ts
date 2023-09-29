jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SampleUuidService } from '../service/sample-uuid.service';

import { SampleUuidDeleteDialogComponent } from './sample-uuid-delete-dialog.component';

describe('SampleUuid Management Delete Component', () => {
  let comp: SampleUuidDeleteDialogComponent;
  let fixture: ComponentFixture<SampleUuidDeleteDialogComponent>;
  let service: SampleUuidService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SampleUuidDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(SampleUuidDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SampleUuidDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SampleUuidService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('66cdc0e6-3ad0-495a-916c-fc4dab2358f2');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
