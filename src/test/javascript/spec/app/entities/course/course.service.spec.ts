import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { CourseService } from 'app/entities/course/course.service';
import { ICourse, Course } from 'app/shared/model/course.model';
import { CourseLevel } from 'app/shared/model/enumerations/course-level.model';

describe('Service Tests', () => {
  describe('Course Service', () => {
    let injector: TestBed;
    let service: CourseService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourse;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(CourseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Course(
        0,
        'image/png',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        0,
        CourseLevel.BEGINNER,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Course', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Course(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Course', () => {
        const returnedFromService = Object.assign(
          {
            picture: 'BBBBBB',
            title: 'BBBBBB',
            subtitle: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            price: 1,
            level: 'BBBBBB',
            tutor: 'BBBBBB',
            contactInfo: 'BBBBBB',
            requirements: 'BBBBBB',
            description: 'BBBBBB',
            location: 'BBBBBB',
            hours: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Course', () => {
        const returnedFromService = Object.assign(
          {
            picture: 'BBBBBB',
            title: 'BBBBBB',
            subtitle: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            price: 1,
            level: 'BBBBBB',
            tutor: 'BBBBBB',
            contactInfo: 'BBBBBB',
            requirements: 'BBBBBB',
            description: 'BBBBBB',
            location: 'BBBBBB',
            hours: 1
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Course', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
