import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IpMacDetailComponent } from './ip-mac-detail.component';

describe('IpMac Management Detail Component', () => {
  let comp: IpMacDetailComponent;
  let fixture: ComponentFixture<IpMacDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IpMacDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ipMac: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IpMacDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IpMacDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ipMac on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ipMac).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
