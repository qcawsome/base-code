import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPrimeNgTable } from '../prime-ng-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../prime-ng-table.test-samples';

import { PrimeNgTableService, RestPrimeNgTable } from './prime-ng-table.service';

const requireRestSample: RestPrimeNgTable = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
  zoneDate: sampleWithRequiredData.zoneDate?.toJSON(),
};

describe('PrimeNgTable Service', () => {
  let service: PrimeNgTableService;
  let httpMock: HttpTestingController;
  let expectedResult: IPrimeNgTable | IPrimeNgTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PrimeNgTableService);
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

    it('should create a PrimeNgTable', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const primeNgTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(primeNgTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrimeNgTable', () => {
      const primeNgTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(primeNgTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrimeNgTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrimeNgTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PrimeNgTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPrimeNgTableToCollectionIfMissing', () => {
      it('should add a PrimeNgTable to an empty array', () => {
        const primeNgTable: IPrimeNgTable = sampleWithRequiredData;
        expectedResult = service.addPrimeNgTableToCollectionIfMissing([], primeNgTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(primeNgTable);
      });

      it('should not add a PrimeNgTable to an array that contains it', () => {
        const primeNgTable: IPrimeNgTable = sampleWithRequiredData;
        const primeNgTableCollection: IPrimeNgTable[] = [
          {
            ...primeNgTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPrimeNgTableToCollectionIfMissing(primeNgTableCollection, primeNgTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrimeNgTable to an array that doesn't contain it", () => {
        const primeNgTable: IPrimeNgTable = sampleWithRequiredData;
        const primeNgTableCollection: IPrimeNgTable[] = [sampleWithPartialData];
        expectedResult = service.addPrimeNgTableToCollectionIfMissing(primeNgTableCollection, primeNgTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(primeNgTable);
      });

      it('should add only unique PrimeNgTable to an array', () => {
        const primeNgTableArray: IPrimeNgTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const primeNgTableCollection: IPrimeNgTable[] = [sampleWithRequiredData];
        expectedResult = service.addPrimeNgTableToCollectionIfMissing(primeNgTableCollection, ...primeNgTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const primeNgTable: IPrimeNgTable = sampleWithRequiredData;
        const primeNgTable2: IPrimeNgTable = sampleWithPartialData;
        expectedResult = service.addPrimeNgTableToCollectionIfMissing([], primeNgTable, primeNgTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(primeNgTable);
        expect(expectedResult).toContain(primeNgTable2);
      });

      it('should accept null and undefined values', () => {
        const primeNgTable: IPrimeNgTable = sampleWithRequiredData;
        expectedResult = service.addPrimeNgTableToCollectionIfMissing([], null, primeNgTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(primeNgTable);
      });

      it('should return initial array if no PrimeNgTable is added', () => {
        const primeNgTableCollection: IPrimeNgTable[] = [sampleWithRequiredData];
        expectedResult = service.addPrimeNgTableToCollectionIfMissing(primeNgTableCollection, undefined, null);
        expect(expectedResult).toEqual(primeNgTableCollection);
      });
    });

    describe('comparePrimeNgTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePrimeNgTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePrimeNgTable(entity1, entity2);
        const compareResult2 = service.comparePrimeNgTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePrimeNgTable(entity1, entity2);
        const compareResult2 = service.comparePrimeNgTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePrimeNgTable(entity1, entity2);
        const compareResult2 = service.comparePrimeNgTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
