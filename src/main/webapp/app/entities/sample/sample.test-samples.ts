import { ISample, NewSample } from './sample.model';

export const sampleWithRequiredData: ISample = {
  id: 15510,
};

export const sampleWithPartialData: ISample = {
  id: 1637,
};

export const sampleWithFullData: ISample = {
  id: 95182,
  text: 'Steel Leu Glens',
};

export const sampleWithNewData: NewSample = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
