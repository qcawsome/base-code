import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrimeNgTable, NewPrimeNgTable } from '../prime-ng-table.model';

export type PartialUpdatePrimeNgTable = Partial<IPrimeNgTable> & Pick<IPrimeNgTable, 'id'>;

type RestOf<T extends IPrimeNgTable | NewPrimeNgTable> = Omit<T, 'date' | 'zoneDate'> & {
  date?: string | null;
  zoneDate?: string | null;
};

export type RestPrimeNgTable = RestOf<IPrimeNgTable>;

export type NewRestPrimeNgTable = RestOf<NewPrimeNgTable>;

export type PartialUpdateRestPrimeNgTable = RestOf<PartialUpdatePrimeNgTable>;

export type EntityResponseType = HttpResponse<IPrimeNgTable>;
export type EntityArrayResponseType = HttpResponse<IPrimeNgTable[]>;

@Injectable({ providedIn: 'root' })
export class PrimeNgTableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prime-ng-tables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(primeNgTable: NewPrimeNgTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(primeNgTable);
    return this.http
      .post<RestPrimeNgTable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(primeNgTable: IPrimeNgTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(primeNgTable);
    return this.http
      .put<RestPrimeNgTable>(`${this.resourceUrl}/${this.getPrimeNgTableIdentifier(primeNgTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(primeNgTable: PartialUpdatePrimeNgTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(primeNgTable);
    return this.http
      .patch<RestPrimeNgTable>(`${this.resourceUrl}/${this.getPrimeNgTableIdentifier(primeNgTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPrimeNgTable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPrimeNgTable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPrimeNgTableIdentifier(primeNgTable: Pick<IPrimeNgTable, 'id'>): number {
    return primeNgTable.id;
  }

  comparePrimeNgTable(o1: Pick<IPrimeNgTable, 'id'> | null, o2: Pick<IPrimeNgTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getPrimeNgTableIdentifier(o1) === this.getPrimeNgTableIdentifier(o2) : o1 === o2;
  }

  addPrimeNgTableToCollectionIfMissing<Type extends Pick<IPrimeNgTable, 'id'>>(
    primeNgTableCollection: Type[],
    ...primeNgTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const primeNgTables: Type[] = primeNgTablesToCheck.filter(isPresent);
    if (primeNgTables.length > 0) {
      const primeNgTableCollectionIdentifiers = primeNgTableCollection.map(
        primeNgTableItem => this.getPrimeNgTableIdentifier(primeNgTableItem)!
      );
      const primeNgTablesToAdd = primeNgTables.filter(primeNgTableItem => {
        const primeNgTableIdentifier = this.getPrimeNgTableIdentifier(primeNgTableItem);
        if (primeNgTableCollectionIdentifiers.includes(primeNgTableIdentifier)) {
          return false;
        }
        primeNgTableCollectionIdentifiers.push(primeNgTableIdentifier);
        return true;
      });
      return [...primeNgTablesToAdd, ...primeNgTableCollection];
    }
    return primeNgTableCollection;
  }

  protected convertDateFromClient<T extends IPrimeNgTable | NewPrimeNgTable | PartialUpdatePrimeNgTable>(primeNgTable: T): RestOf<T> {
    return {
      ...primeNgTable,
      date: primeNgTable.date?.format(DATE_FORMAT) ?? null,
      zoneDate: primeNgTable.zoneDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPrimeNgTable: RestPrimeNgTable): IPrimeNgTable {
    return {
      ...restPrimeNgTable,
      date: restPrimeNgTable.date ? dayjs(restPrimeNgTable.date) : undefined,
      zoneDate: restPrimeNgTable.zoneDate ? dayjs(restPrimeNgTable.zoneDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPrimeNgTable>): HttpResponse<IPrimeNgTable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPrimeNgTable[]>): HttpResponse<IPrimeNgTable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
