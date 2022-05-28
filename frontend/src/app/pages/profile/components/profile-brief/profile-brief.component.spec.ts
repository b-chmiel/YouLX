import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileBriefComponent } from './profile-brief.component';

describe('ProfileBriefComponent', () => {
  let component: ProfileBriefComponent;
  let fixture: ComponentFixture<ProfileBriefComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfileBriefComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileBriefComponent);
    component = fixture.componentInstance;
    component.profile = {
      login: 'johnsmith',
      email: 'john.smith@email.com',
      lastName: 'smith',
      firstName: 'john'
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
