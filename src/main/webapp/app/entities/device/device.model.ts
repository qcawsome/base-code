export interface IDevice {
  id: number;
  name?: string | null;
  agentStatus?: string | null;
  networkStatus?: string | null;
}

export type NewDevice = Omit<IDevice, 'id'> & { id: null };
