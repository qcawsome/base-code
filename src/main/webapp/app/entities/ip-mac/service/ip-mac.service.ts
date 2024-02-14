import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIpMac, NewIpMac } from '../ip-mac.model';

export type PartialUpdateIpMac = Partial<IIpMac> & Pick<IIpMac, 'id'>;

export type EntityResponseType = HttpResponse<IIpMac>;
export type EntityArrayResponseType = HttpResponse<IIpMac[]>;

@Injectable({ providedIn: 'root' })
export class IpMacService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ip-macs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ipMac: NewIpMac): Observable<EntityResponseType> {
    return this.http.post<IIpMac>(this.resourceUrl, ipMac, { observe: 'response' });
  }

  update(ipMac: IIpMac): Observable<EntityResponseType> {
    return this.http.put<IIpMac>(`${this.resourceUrl}/${this.getIpMacIdentifier(ipMac)}`, ipMac, { observe: 'response' });
  }

  partialUpdate(ipMac: PartialUpdateIpMac): Observable<EntityResponseType> {
    return this.http.patch<IIpMac>(`${this.resourceUrl}/${this.getIpMacIdentifier(ipMac)}`, ipMac, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIpMac>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIpMac[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIpMacIdentifier(ipMac: Pick<IIpMac, 'id'>): number {
    return ipMac.id;
  }

  compareIpMac(o1: Pick<IIpMac, 'id'> | null, o2: Pick<IIpMac, 'id'> | null): boolean {
    return o1 && o2 ? this.getIpMacIdentifier(o1) === this.getIpMacIdentifier(o2) : o1 === o2;
  }

  addIpMacToCollectionIfMissing<Type extends Pick<IIpMac, 'id'>>(
    ipMacCollection: Type[],
    ...ipMacsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ipMacs: Type[] = ipMacsToCheck.filter(isPresent);
    if (ipMacs.length > 0) {
      const ipMacCollectionIdentifiers = ipMacCollection.map(ipMacItem => this.getIpMacIdentifier(ipMacItem)!);
      const ipMacsToAdd = ipMacs.filter(ipMacItem => {
        const ipMacIdentifier = this.getIpMacIdentifier(ipMacItem);
        if (ipMacCollectionIdentifiers.includes(ipMacIdentifier)) {
          return false;
        }
        ipMacCollectionIdentifiers.push(ipMacIdentifier);
        return true;
      });
      return [...ipMacsToAdd, ...ipMacCollection];
    }
    return ipMacCollection;
  }
}
