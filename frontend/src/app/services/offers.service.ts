import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import {Offer} from '../pages/home/models/offer';

@Injectable({
  providedIn: 'root',
})
export class OffersService {
  private readonly offersUrl = '/api/offers';

  constructor(private http: HttpClient) {
  }

  getOffers(page: number, size: number): Observable<Offer[]> {
    if (page < 0 || size < 1) {
      return of([] as Offer[]);
    }

    return this.http.get<GetOffersResponse>(this.offersUrl).pipe(
      map(value => value._embedded.offers.map(offer => {
        if (offer.price === undefined) {
          offer.price = null;
        }

        if (offer.coverUrl === undefined) {
          offer.coverUrl = null;
        }

        if (offer.description === undefined) {
          offer.description = null;
        }
        console.log(offer);

        return offer;
      })),
      catchError(err => of([] as Offer[])));
  }
}

interface GetOffersResponse {
  _embedded: {
    offers: Offer[]
  }
}
