import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPrimeNgTable, NewPrimeNgTable } from '../prime-ng-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrimeNgTable for edit and NewPrimeNgTableFormGroupInput for create.
 */
type PrimeNgTableFormGroupInput = IPrimeNgTable | PartialWithRequiredKeyOf<NewPrimeNgTable>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPrimeNgTable | NewPrimeNgTable> = Omit<T, 'zoneDate'> & {
  zoneDate?: string | null;
};

type PrimeNgTableFormRawValue = FormValueOf<IPrimeNgTable>;

type NewPrimeNgTableFormRawValue = FormValueOf<NewPrimeNgTable>;

type PrimeNgTableFormDefaults = Pick<NewPrimeNgTable, 'id' | 'zoneDate'>;

type PrimeNgTableFormGroupContent = {
  id: FormControl<PrimeNgTableFormRawValue['id'] | NewPrimeNgTable['id']>;
  text: FormControl<PrimeNgTableFormRawValue['text']>;
  number: FormControl<PrimeNgTableFormRawValue['number']>;
  floatNumber: FormControl<PrimeNgTableFormRawValue['floatNumber']>;
  date: FormControl<PrimeNgTableFormRawValue['date']>;
  zoneDate: FormControl<PrimeNgTableFormRawValue['zoneDate']>;
  image: FormControl<PrimeNgTableFormRawValue['image']>;
  imageContentType: FormControl<PrimeNgTableFormRawValue['imageContentType']>;
  longText: FormControl<PrimeNgTableFormRawValue['longText']>;
};

export type PrimeNgTableFormGroup = FormGroup<PrimeNgTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PrimeNgTableFormService {
  createPrimeNgTableFormGroup(primeNgTable: PrimeNgTableFormGroupInput = { id: null }): PrimeNgTableFormGroup {
    const primeNgTableRawValue = this.convertPrimeNgTableToPrimeNgTableRawValue({
      ...this.getFormDefaults(),
      ...primeNgTable,
    });
    return new FormGroup<PrimeNgTableFormGroupContent>({
      id: new FormControl(
        { value: primeNgTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      text: new FormControl(primeNgTableRawValue.text),
      number: new FormControl(primeNgTableRawValue.number),
      floatNumber: new FormControl(primeNgTableRawValue.floatNumber),
      date: new FormControl(primeNgTableRawValue.date),
      zoneDate: new FormControl(primeNgTableRawValue.zoneDate),
      image: new FormControl(primeNgTableRawValue.image),
      imageContentType: new FormControl(primeNgTableRawValue.imageContentType),
      longText: new FormControl(primeNgTableRawValue.longText),
    });
  }

  getPrimeNgTable(form: PrimeNgTableFormGroup): IPrimeNgTable | NewPrimeNgTable {
    return this.convertPrimeNgTableRawValueToPrimeNgTable(form.getRawValue() as PrimeNgTableFormRawValue | NewPrimeNgTableFormRawValue);
  }

  resetForm(form: PrimeNgTableFormGroup, primeNgTable: PrimeNgTableFormGroupInput): void {
    const primeNgTableRawValue = this.convertPrimeNgTableToPrimeNgTableRawValue({ ...this.getFormDefaults(), ...primeNgTable });
    form.reset(
      {
        ...primeNgTableRawValue,
        id: { value: primeNgTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PrimeNgTableFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      zoneDate: currentTime,
    };
  }

  private convertPrimeNgTableRawValueToPrimeNgTable(
    rawPrimeNgTable: PrimeNgTableFormRawValue | NewPrimeNgTableFormRawValue
  ): IPrimeNgTable | NewPrimeNgTable {
    return {
      ...rawPrimeNgTable,
      zoneDate: dayjs(rawPrimeNgTable.zoneDate, DATE_TIME_FORMAT),
    };
  }

  private convertPrimeNgTableToPrimeNgTableRawValue(
    primeNgTable: IPrimeNgTable | (Partial<NewPrimeNgTable> & PrimeNgTableFormDefaults)
  ): PrimeNgTableFormRawValue | PartialWithRequiredKeyOf<NewPrimeNgTableFormRawValue> {
    return {
      ...primeNgTable,
      zoneDate: primeNgTable.zoneDate ? primeNgTable.zoneDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
