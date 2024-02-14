import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IpMacFormService, IpMacFormGroup } from './ip-mac-form.service';
import { IIpMac } from '../ip-mac.model';
import { IpMacService } from '../service/ip-mac.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';

@Component({
  selector: 'jhi-ip-mac-update',
  templateUrl: './ip-mac-update.component.html',
})
export class IpMacUpdateComponent implements OnInit {
  isSaving = false;
  ipMac: IIpMac | null = null;

  devicesSharedCollection: IDevice[] = [];

  editForm: IpMacFormGroup = this.ipMacFormService.createIpMacFormGroup();

  constructor(
    protected ipMacService: IpMacService,
    protected ipMacFormService: IpMacFormService,
    protected deviceService: DeviceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDevice = (o1: IDevice | null, o2: IDevice | null): boolean => this.deviceService.compareDevice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ipMac }) => {
      this.ipMac = ipMac;
      if (ipMac) {
        this.updateForm(ipMac);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ipMac = this.ipMacFormService.getIpMac(this.editForm);
    if (ipMac.id !== null) {
      this.subscribeToSaveResponse(this.ipMacService.update(ipMac));
    } else {
      this.subscribeToSaveResponse(this.ipMacService.create(ipMac));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIpMac>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ipMac: IIpMac): void {
    this.ipMac = ipMac;
    this.ipMacFormService.resetForm(this.editForm, ipMac);

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing<IDevice>(this.devicesSharedCollection, ipMac.device);
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing<IDevice>(devices, this.ipMac?.device)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }
}
