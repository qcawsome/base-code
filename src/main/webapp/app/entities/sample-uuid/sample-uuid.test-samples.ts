import { ISampleUuid, NewSampleUuid } from './sample-uuid.model';

export const sampleWithRequiredData: ISampleUuid = {
  id: '66cdc0e6-3ad0-495a-916c-fc4dab2358f0',
};

export const sampleWithPartialData: ISampleUuid = {
  id: '66cdc0e6-3ad0-495a-916c-fc4dab2358f1',
};

export const sampleWithFullData: ISampleUuid = {
  id: '66cdc0e6-3ad0-495a-916c-fc4dab2358f2',
  uuid: '66cdc0e6-3ad0-495a-916c-fc4dab2358f2',
};

export const sampleWithNewData: NewSampleUuid = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
