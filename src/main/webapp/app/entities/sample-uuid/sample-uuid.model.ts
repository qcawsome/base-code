export interface ISampleUuid {
  id: number;
  uuid?: string | null;
}

export type NewSampleUuid = Omit<ISampleUuid, 'id'> & { id: null };
