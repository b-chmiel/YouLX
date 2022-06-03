import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import {CreateOfferDto, Offer} from '../models/offer';

@Injectable({
  providedIn: 'root',
})
export class OffersService {
  private readonly offersUrl = '/api/offers';
  private readonly ownOffersUrl = '/api/me/offers';

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

        return offer;
      })),
      catchError(err => of([] as Offer[])));
  }

  getOwnOffers(page: number, size: number, statuses: string = 'ALL'): Observable<Offer[]> {
    if (page < 0 || size < 1) {
      return of([] as Offer[]);
    }

    return this.http.get<GetOffersResponse>(this.ownOffersUrl, {
      params: {
        page,
        size,
        statuses,
      },
    }).pipe(
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

        return offer;
      })),
      catchError(err => of([] as Offer[])));
  }

  getOffer(offerId: string): Observable<Offer | null> {
    return this.http.get<Offer>(`${this.offersUrl}/${offerId}`).pipe(catchError(_ => of(null)));
  }

  createOffer(createOfferDto: CreateOfferDto): Observable<string | null> {
    return this.http.post(this.offersUrl, createOfferDto, {observe: 'response'}).pipe(map(t => {
      if (t.status == 201) {
        const location = t.headers.get('location')?.split('/').pop();

        if (location) {
          return location;
        }
      }
      return null;
    }));
  }

  updateOffer(offer: Offer): Observable<Offer> {
    return this.http.put<Offer>(this.offersUrl + '/' + offer.id, offer);
  }

  closeOffer(offer: Offer): Observable<Offer | null> {
    const closeUrl = offer._links?.close?.href;

    if (closeUrl) {
      return this.http.post<Offer>(closeUrl, {});
    }

    return of(null);
  }

  publishOffer(offer: Offer): Observable<Offer | null> {
    const publishUrl = offer._links?.publish?.href;

    if (publishUrl) {
      return this.http.post<Offer>(publishUrl, {});
    }

    return of(null);
  }
}

interface GetOffersResponse {
  _embedded: {
    offers: Offer[]
  };
}
