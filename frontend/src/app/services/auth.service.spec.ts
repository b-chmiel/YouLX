import {TestBed} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import Profile from '../models/profile';

describe('AuthService', () => {
  let service: AuthService;
  let httpTest: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(AuthService);
    httpTest = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTest.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return profile info', () => {
    const user = {
      login: 'user1'
    } as Profile;

    // @ts-ignore
    service.getProfileInfo().subscribe(profile => expect(profile.login).toEqual(user.login));

    const request = httpTest.expectOne('/api/profile');
    request.flush(user);
  });

  it('should return null on error', () => {
    service.getProfileInfo().subscribe(profile => expect(profile).toBeNull());
    const request = httpTest.expectOne('/api/profile');
    request.flush('', {status: 403, statusText: 'not authorized'})
  });
});
