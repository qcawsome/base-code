import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISample, NewSample } from '../sample.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISample for edit and NewSampleFormGroupInput for create.
 */
type SampleFormGroupInput = ISample | PartialWithRequiredKeyOf<NewSample>;

type SampleFormDefaults = Pick<NewSample, 'id'>;

type SampleFormGroupContent = {
  id: FormControl<ISample['id'] | NewSample['id']>;
  text: FormControl<ISample['text']>;
};

export type SampleFormGroup = FormGroup<SampleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SampleFormService {
  createSampleFormGroup(sample: SampleFormGroupInput = { id: null }): SampleFormGroup {
    const sampleRawValue = {
      ...this.getFormDefaults(),
      ...sample,
    };
    return new FormGroup<SampleFormGroupContent>({
      id: new FormControl(
        { value: sampleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      text: new FormControl(sampleRawValue.text),
    });
  }

  getSample(form: SampleFormGroup): ISample | NewSample {
    return form.getRawValue() as ISample | NewSample;
  }

  resetForm(form: SampleFormGroup, sample: SampleFormGroupInput): void {
    const sampleRawValue = { ...this.getFormDefaults(), ...sample };
    form.reset(
      {
        ...sampleRawValue,
        id: { value: sampleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SampleFormDefaults {
    return {
      id: null,
    };
  }
}
