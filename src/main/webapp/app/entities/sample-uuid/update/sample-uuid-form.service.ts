import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISampleUuid, NewSampleUuid } from '../sample-uuid.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISampleUuid for edit and NewSampleUuidFormGroupInput for create.
 */
type SampleUuidFormGroupInput = ISampleUuid | PartialWithRequiredKeyOf<NewSampleUuid>;

type SampleUuidFormDefaults = Pick<NewSampleUuid, 'id'>;

type SampleUuidFormGroupContent = {
  id: FormControl<ISampleUuid['id'] | NewSampleUuid['id']>;
  uuid: FormControl<ISampleUuid['uuid']>;
};

export type SampleUuidFormGroup = FormGroup<SampleUuidFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SampleUuidFormService {
  createSampleUuidFormGroup(sampleUuid: SampleUuidFormGroupInput = { id: null }): SampleUuidFormGroup {
    const sampleUuidRawValue = {
      ...this.getFormDefaults(),
      ...sampleUuid,
    };
    return new FormGroup<SampleUuidFormGroupContent>({
      id: new FormControl(
        { value: sampleUuidRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      uuid: new FormControl(sampleUuidRawValue.uuid),
    });
  }

  getSampleUuid(form: SampleUuidFormGroup): ISampleUuid | NewSampleUuid {
    return form.getRawValue() as ISampleUuid | NewSampleUuid;
  }

  resetForm(form: SampleUuidFormGroup, sampleUuid: SampleUuidFormGroupInput): void {
    const sampleUuidRawValue = { ...this.getFormDefaults(), ...sampleUuid };
    form.reset(
      {
        ...sampleUuidRawValue,
        id: { value: sampleUuidRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SampleUuidFormDefaults {
    return {
      id: null,
    };
  }
}
