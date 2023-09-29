export interface ISample {
  id: number;
  text?: string | null;
}

export type NewSample = Omit<ISample, 'id'> & { id: null };
