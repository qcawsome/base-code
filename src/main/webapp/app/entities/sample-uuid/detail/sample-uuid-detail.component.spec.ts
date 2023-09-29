import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SampleUuidDetailComponent } from './sample-uuid-detail.component';

describe('SampleUuid Management Detail Component', () => {
  let comp: SampleUuidDetailComponent;
  let fixture: ComponentFixture<SampleUuidDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SampleUuidDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sampleUuid: { id: 'dc4279ea-cfb9-11ec-9d64-0242ac120002' } }) },
        },
      ],
    })
      .overrideTemplate(SampleUuidDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SampleUuidDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sampleUuid on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sampleUuid).toEqual(expect.objectContaining({ id: 'dc4279ea-cfb9-11ec-9d64-0242ac120002' }));
    });
  });
});
