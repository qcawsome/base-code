import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISampleUuid } from '../sample-uuid.model';

@Component({
  selector: 'jhi-sample-uuid-detail',
  templateUrl: './sample-uuid-detail.component.html',
})
export class SampleUuidDetailComponent implements OnInit {
  sampleUuid: ISampleUuid | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sampleUuid }) => {
      this.sampleUuid = sampleUuid;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
