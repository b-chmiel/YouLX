import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, map, of} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TagsService {
  private readonly tagsUrl = '/api/tags';

  constructor(private http: HttpClient) {
  }

  getTags() {
    return this.http.get<{name: string}[]>(this.tagsUrl).pipe(
      catchError(_ => of([])),
      map(tags => tags.map(tag => tag.name))
    );
  }
}
