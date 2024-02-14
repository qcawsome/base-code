import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIpMac } from '../ip-mac.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ip-mac.test-samples';

import { IpMacService } from './ip-mac.service';

const requireRestSample: IIpMac = {
  ...sampleWithRequiredData,
};

describe('IpMac Service', () => {
  let service: IpMacService;
  let httpMock: HttpTestingController;
  let expectedResult: IIpMac | IIpMac[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IpMacService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a IpMac', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ipMac = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ipMac).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IpMac', () => {
      const ipMac = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ipMac).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IpMac', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IpMac', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IpMac', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIpMacToCollectionIfMissing', () => {
      it('should add a IpMac to an empty array', () => {
        const ipMac: IIpMac = sampleWithRequiredData;
        expectedResult = service.addIpMacToCollectionIfMissing([], ipMac);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ipMac);
      });

      it('should not add a IpMac to an array that contains it', () => {
        const ipMac: IIpMac = sampleWithRequiredData;
        const ipMacCollection: IIpMac[] = [
          {
            ...ipMac,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIpMacToCollectionIfMissing(ipMacCollection, ipMac);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IpMac to an array that doesn't contain it", () => {
        const ipMac: IIpMac = sampleWithRequiredData;
        const ipMacCollection: IIpMac[] = [sampleWithPartialData];
        expectedResult = service.addIpMacToCollectionIfMissing(ipMacCollection, ipMac);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ipMac);
      });

      it('should add only unique IpMac to an array', () => {
        const ipMacArray: IIpMac[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ipMacCollection: IIpMac[] = [sampleWithRequiredData];
        expectedResult = service.addIpMacToCollectionIfMissing(ipMacCollection, ...ipMacArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ipMac: IIpMac = sampleWithRequiredData;
        const ipMac2: IIpMac = sampleWithPartialData;
        expectedResult = service.addIpMacToCollectionIfMissing([], ipMac, ipMac2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ipMac);
        expect(expectedResult).toContain(ipMac2);
      });

      it('should accept null and undefined values', () => {
        const ipMac: IIpMac = sampleWithRequiredData;
        expectedResult = service.addIpMacToCollectionIfMissing([], null, ipMac, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ipMac);
      });

      it('should return initial array if no IpMac is added', () => {
        const ipMacCollection: IIpMac[] = [sampleWithRequiredData];
        expectedResult = service.addIpMacToCollectionIfMissing(ipMacCollection, undefined, null);
        expect(expectedResult).toEqual(ipMacCollection);
      });
    });

    describe('compareIpMac', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIpMac(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIpMac(entity1, entity2);
        const compareResult2 = service.compareIpMac(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIpMac(entity1, entity2);
        const compareResult2 = service.compareIpMac(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIpMac(entity1, entity2);
        const compareResult2 = service.compareIpMac(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
