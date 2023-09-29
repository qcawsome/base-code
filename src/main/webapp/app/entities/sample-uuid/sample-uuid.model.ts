export interface ISampleUuid {
  id: string;
  uuid?: string | null;
}

export type NewSampleUuid = Omit<ISampleUuid, 'id'> & { id: null };
