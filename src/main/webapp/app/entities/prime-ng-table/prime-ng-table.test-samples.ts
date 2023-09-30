import dayjs from 'dayjs/esm';

import { IPrimeNgTable, NewPrimeNgTable } from './prime-ng-table.model';

export const sampleWithRequiredData: IPrimeNgTable = {
  id: 7651,
};

export const sampleWithPartialData: IPrimeNgTable = {
  id: 33116,
  text: 'structure',
  floatNumber: 88505,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithFullData: IPrimeNgTable = {
  id: 66991,
  text: 'Loan Jewelery sky',
  number: 65482,
  floatNumber: 37273,
  date: dayjs('2023-09-29'),
  zoneDate: dayjs('2023-09-29T14:12'),
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  longText: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewPrimeNgTable = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
