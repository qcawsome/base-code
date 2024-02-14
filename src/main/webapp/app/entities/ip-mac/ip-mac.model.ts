import { IDevice } from 'app/entities/device/device.model';

export interface IIpMac {
  id: number;
  ip?: string | null;
  mac?: string | null;
  networkStatus?: string | null;
  agentStatus?: string | null;
  device?: Pick<IDevice, 'id' | 'name'> | null;
}

export type NewIpMac = Omit<IIpMac, 'id'> & { id: null };
