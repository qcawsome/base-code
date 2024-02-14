import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIpMac } from '../ip-mac.model';

@Component({
  selector: 'jhi-ip-mac-detail',
  templateUrl: './ip-mac-detail.component.html',
})
export class IpMacDetailComponent implements OnInit {
  ipMac: IIpMac | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ipMac }) => {
      this.ipMac = ipMac;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
