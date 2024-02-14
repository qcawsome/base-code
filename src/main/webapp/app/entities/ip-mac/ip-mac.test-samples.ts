import { IIpMac, NewIpMac } from './ip-mac.model';

export const sampleWithRequiredData: IIpMac = {
  id: 6056,
};

export const sampleWithPartialData: IIpMac = {
  id: 27433,
  mac: 'Plastic network Michigan',
};

export const sampleWithFullData: IIpMac = {
  id: 12201,
  ip: 'solution SAS synergize',
  mac: 'parsing Producer',
  networkStatus: 'interface Gloves',
  agentStatus: 'pink port Bacon',
};

export const sampleWithNewData: NewIpMac = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
