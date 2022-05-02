import {TestBed} from '@angular/core/testing';

import {ToolbarComponent} from './toolbar.component';
import {AuthService} from '../../../services/auth.service';
import {of} from 'rxjs';

describe('ToolbarComponent', () => {
  let authServiceSpy: any = jasmine.createSpyObj('AuthService', ['getProfileInfo']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ToolbarComponent],
      providers: [
        {provide: AuthService, useValue: authServiceSpy},
      ],
    })
      .compileComponents();
  });

  it('should create', () => {
    authServiceSpy.getProfileInfo.and.returnValue(of(null));
    const fixture = TestBed.createComponent(ToolbarComponent);
    fixture.detectChanges();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render application title', () => {
    authServiceSpy.getProfileInfo.and.returnValue(of(null));
    const fixture = TestBed.createComponent(ToolbarComponent);
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('YouLX');
  });

  it('should render user login when authenticated', () => {
    authServiceSpy.getProfileInfo.and.returnValue(of({login: 'user1'}));
    const fixture = TestBed.createComponent(ToolbarComponent);
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('user1');
  });

  it('should render user login when unauthenticated', () => {
    authServiceSpy.getProfileInfo.and.returnValue(of(null));
    const fixture = TestBed.createComponent(ToolbarComponent);
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('Log in');
  });
});
