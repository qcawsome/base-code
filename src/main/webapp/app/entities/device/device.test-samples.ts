import { IDevice, NewDevice } from './device.model';

export const sampleWithRequiredData: IDevice = {
  id: 67542,
};

export const sampleWithPartialData: IDevice = {
  id: 92570,
  agentStatus: 'payment',
  networkStatus: 'responsive open-source Lake',
};

export const sampleWithFullData: IDevice = {
  id: 60687,
  name: 'Maine',
  agentStatus: 'Communications',
  networkStatus: 'optical quantifying Functionality',
};

export const sampleWithNewData: NewDevice = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
