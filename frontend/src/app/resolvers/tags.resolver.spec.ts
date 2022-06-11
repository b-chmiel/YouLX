import { TestBed } from '@angular/core/testing';

import { TagsResolver } from './tags.resolver';
import {TagsService} from '../services/tags.service';

describe('TagsResolver', () => {
  let resolver: TagsResolver;
  let tagsServiceSpy: any = jasmine.createSpyObj('TagsService', ['getTags']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: TagsService, useValue: tagsServiceSpy
        }
      ]
    });
    resolver = TestBed.inject(TagsResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
