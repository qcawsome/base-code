import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISample, NewSample } from '../sample.model';

export type PartialUpdateSample = Partial<ISample> & Pick<ISample, 'id'>;

export type EntityResponseType = HttpResponse<ISample>;
export type EntityArrayResponseType = HttpResponse<ISample[]>;

@Injectable({ providedIn: 'root' })
export class SampleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/samples');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sample: NewSample): Observable<EntityResponseType> {
    return this.http.post<ISample>(this.resourceUrl, sample, { observe: 'response' });
  }

  update(sample: ISample): Observable<EntityResponseType> {
    return this.http.put<ISample>(`${this.resourceUrl}/${this.getSampleIdentifier(sample)}`, sample, { observe: 'response' });
  }

  partialUpdate(sample: PartialUpdateSample): Observable<EntityResponseType> {
    return this.http.patch<ISample>(`${this.resourceUrl}/${this.getSampleIdentifier(sample)}`, sample, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISample>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISample[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSampleIdentifier(sample: Pick<ISample, 'id'>): number {
    return sample.id;
  }

  compareSample(o1: Pick<ISample, 'id'> | null, o2: Pick<ISample, 'id'> | null): boolean {
    return o1 && o2 ? this.getSampleIdentifier(o1) === this.getSampleIdentifier(o2) : o1 === o2;
  }

  addSampleToCollectionIfMissing<Type extends Pick<ISample, 'id'>>(
    sampleCollection: Type[],
    ...samplesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const samples: Type[] = samplesToCheck.filter(isPresent);
    if (samples.length > 0) {
      const sampleCollectionIdentifiers = sampleCollection.map(sampleItem => this.getSampleIdentifier(sampleItem)!);
      const samplesToAdd = samples.filter(sampleItem => {
        const sampleIdentifier = this.getSampleIdentifier(sampleItem);
        if (sampleCollectionIdentifiers.includes(sampleIdentifier)) {
          return false;
        }
        sampleCollectionIdentifiers.push(sampleIdentifier);
        return true;
      });
      return [...samplesToAdd, ...sampleCollection];
    }
    return sampleCollection;
  }
}
