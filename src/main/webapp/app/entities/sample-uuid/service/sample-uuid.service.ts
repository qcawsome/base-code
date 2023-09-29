import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISampleUuid, NewSampleUuid } from '../sample-uuid.model';

export type PartialUpdateSampleUuid = Partial<ISampleUuid> & Pick<ISampleUuid, 'id'>;

export type EntityResponseType = HttpResponse<ISampleUuid>;
export type EntityArrayResponseType = HttpResponse<ISampleUuid[]>;

@Injectable({ providedIn: 'root' })
export class SampleUuidService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sample-uuids');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sampleUuid: NewSampleUuid): Observable<EntityResponseType> {
    return this.http.post<ISampleUuid>(this.resourceUrl, sampleUuid, { observe: 'response' });
  }

  update(sampleUuid: ISampleUuid): Observable<EntityResponseType> {
    return this.http.put<ISampleUuid>(`${this.resourceUrl}/${this.getSampleUuidIdentifier(sampleUuid)}`, sampleUuid, {
      observe: 'response',
    });
  }

  partialUpdate(sampleUuid: PartialUpdateSampleUuid): Observable<EntityResponseType> {
    return this.http.patch<ISampleUuid>(`${this.resourceUrl}/${this.getSampleUuidIdentifier(sampleUuid)}`, sampleUuid, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISampleUuid>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISampleUuid[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSampleUuidIdentifier(sampleUuid: Pick<ISampleUuid, 'id'>): number {
    return sampleUuid.id;
  }

  compareSampleUuid(o1: Pick<ISampleUuid, 'id'> | null, o2: Pick<ISampleUuid, 'id'> | null): boolean {
    return o1 && o2 ? this.getSampleUuidIdentifier(o1) === this.getSampleUuidIdentifier(o2) : o1 === o2;
  }

  addSampleUuidToCollectionIfMissing<Type extends Pick<ISampleUuid, 'id'>>(
    sampleUuidCollection: Type[],
    ...sampleUuidsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sampleUuids: Type[] = sampleUuidsToCheck.filter(isPresent);
    if (sampleUuids.length > 0) {
      const sampleUuidCollectionIdentifiers = sampleUuidCollection.map(sampleUuidItem => this.getSampleUuidIdentifier(sampleUuidItem)!);
      const sampleUuidsToAdd = sampleUuids.filter(sampleUuidItem => {
        const sampleUuidIdentifier = this.getSampleUuidIdentifier(sampleUuidItem);
        if (sampleUuidCollectionIdentifiers.includes(sampleUuidIdentifier)) {
          return false;
        }
        sampleUuidCollectionIdentifiers.push(sampleUuidIdentifier);
        return true;
      });
      return [...sampleUuidsToAdd, ...sampleUuidCollection];
    }
    return sampleUuidCollection;
  }
}
