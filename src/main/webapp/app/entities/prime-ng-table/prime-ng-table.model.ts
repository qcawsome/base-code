import dayjs from 'dayjs/esm';

export interface IPrimeNgTable {
  id: number;
  text?: string | null;
  number?: number | null;
  floatNumber?: number | null;
  date?: dayjs.Dayjs | null;
  zoneDate?: dayjs.Dayjs | null;
  image?: string | null;
  imageContentType?: string | null;
  longText?: string | null;
}

export type NewPrimeNgTable = Omit<IPrimeNgTable, 'id'> & { id: null };
