import { TestBed } from '@angular/core/testing';

import { ProfileResolver } from './profile.resolver';
import {OffersService} from '../../../services/offers.service';
import {AuthService} from '../../../services/auth.service';
import {of} from 'rxjs';

describe('ProfileResolver', () => {
  let resolver: ProfileResolver;
  let authServiceSpy: any = jasmine.createSpyObj('AuthService', ['getProfileInfo']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: AuthService,
          useValue: authServiceSpy,
        },
      ],
    });
    resolver = TestBed.inject(ProfileResolver);
  });

  it('should be created', () => {
    authServiceSpy.getProfileInfo.and.returnValue(of(null));
    expect(resolver).toBeTruthy();
  });
});
