import {Injectable} from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot,
} from '@angular/router';
import {Observable} from 'rxjs';
import {TagsService} from '../services/tags.service';

@Injectable({
  providedIn: 'root',
})
export class TagsResolver implements Resolve<string[]> {
  constructor(private tags: TagsService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<string[]> {
    return this.tags.getTags();
  }
}
