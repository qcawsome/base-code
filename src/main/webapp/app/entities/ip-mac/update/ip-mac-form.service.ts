import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIpMac, NewIpMac } from '../ip-mac.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIpMac for edit and NewIpMacFormGroupInput for create.
 */
type IpMacFormGroupInput = IIpMac | PartialWithRequiredKeyOf<NewIpMac>;

type IpMacFormDefaults = Pick<NewIpMac, 'id'>;

type IpMacFormGroupContent = {
  id: FormControl<IIpMac['id'] | NewIpMac['id']>;
  ip: FormControl<IIpMac['ip']>;
  mac: FormControl<IIpMac['mac']>;
  networkStatus: FormControl<IIpMac['networkStatus']>;
  agentStatus: FormControl<IIpMac['agentStatus']>;
  device: FormControl<IIpMac['device']>;
};

export type IpMacFormGroup = FormGroup<IpMacFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IpMacFormService {
  createIpMacFormGroup(ipMac: IpMacFormGroupInput = { id: null }): IpMacFormGroup {
    const ipMacRawValue = {
      ...this.getFormDefaults(),
      ...ipMac,
    };
    return new FormGroup<IpMacFormGroupContent>({
      id: new FormControl(
        { value: ipMacRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      ip: new FormControl(ipMacRawValue.ip),
      mac: new FormControl(ipMacRawValue.mac),
      networkStatus: new FormControl(ipMacRawValue.networkStatus),
      agentStatus: new FormControl(ipMacRawValue.agentStatus),
      device: new FormControl(ipMacRawValue.device),
    });
  }

  getIpMac(form: IpMacFormGroup): IIpMac | NewIpMac {
    return form.getRawValue() as IIpMac | NewIpMac;
  }

  resetForm(form: IpMacFormGroup, ipMac: IpMacFormGroupInput): void {
    const ipMacRawValue = { ...this.getFormDefaults(), ...ipMac };
    form.reset(
      {
        ...ipMacRawValue,
        id: { value: ipMacRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IpMacFormDefaults {
    return {
      id: null,
    };
  }
}
