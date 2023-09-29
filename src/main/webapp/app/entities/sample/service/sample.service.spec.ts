import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISample } from '../sample.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sample.test-samples';

import { SampleService } from './sample.service';

const requireRestSample: ISample = {
  ...sampleWithRequiredData,
};

describe('Sample Service', () => {
  let service: SampleService;
  let httpMock: HttpTestingController;
  let expectedResult: ISample | ISample[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SampleService);
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

    it('should create a Sample', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sample = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sample).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sample', () => {
      const sample = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sample).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sample', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sample', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sample', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSampleToCollectionIfMissing', () => {
      it('should add a Sample to an empty array', () => {
        const sample: ISample = sampleWithRequiredData;
        expectedResult = service.addSampleToCollectionIfMissing([], sample);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sample);
      });

      it('should not add a Sample to an array that contains it', () => {
        const sample: ISample = sampleWithRequiredData;
        const sampleCollection: ISample[] = [
          {
            ...sample,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSampleToCollectionIfMissing(sampleCollection, sample);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sample to an array that doesn't contain it", () => {
        const sample: ISample = sampleWithRequiredData;
        const sampleCollection: ISample[] = [sampleWithPartialData];
        expectedResult = service.addSampleToCollectionIfMissing(sampleCollection, sample);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sample);
      });

      it('should add only unique Sample to an array', () => {
        const sampleArray: ISample[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sampleCollection: ISample[] = [sampleWithRequiredData];
        expectedResult = service.addSampleToCollectionIfMissing(sampleCollection, ...sampleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sample: ISample = sampleWithRequiredData;
        const sample2: ISample = sampleWithPartialData;
        expectedResult = service.addSampleToCollectionIfMissing([], sample, sample2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sample);
        expect(expectedResult).toContain(sample2);
      });

      it('should accept null and undefined values', () => {
        const sample: ISample = sampleWithRequiredData;
        expectedResult = service.addSampleToCollectionIfMissing([], null, sample, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sample);
      });

      it('should return initial array if no Sample is added', () => {
        const sampleCollection: ISample[] = [sampleWithRequiredData];
        expectedResult = service.addSampleToCollectionIfMissing(sampleCollection, undefined, null);
        expect(expectedResult).toEqual(sampleCollection);
      });
    });

    describe('compareSample', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSample(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSample(entity1, entity2);
        const compareResult2 = service.compareSample(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSample(entity1, entity2);
        const compareResult2 = service.compareSample(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSample(entity1, entity2);
        const compareResult2 = service.compareSample(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
