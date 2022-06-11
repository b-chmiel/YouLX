import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import {CreateOfferDto, Offer, PaginatedOffers} from '../models/offer';

@Injectable({
  providedIn: 'root',
})
export class OffersService {
  private readonly offersUrl = '/api/offers';
  private readonly ownOffersUrl = '/api/me/offers';

  constructor(private http: HttpClient) {
  }

  getOffers(page: number, size: number, query?: string, tags: string[] = []): Observable<PaginatedOffers> {
    if (page < 0 || size < 1) {
      return of({
        _embedded: {
          offers: []
        },
        page: {
          number: 0,
          size: 0,
          totalPages: 0,
          totalElements: 0
        }
      });
    }

    const params = {
      page,
      size,
      query: '',
      tags: tags.join(';')
    };

    if (query) {
      params.query = query
    }

    return this.http.get<PaginatedOffers>(this.offersUrl, {params})
      .pipe(
        map(v => {
          v._embedded.offers = v._embedded.offers.map(offer => {
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
          });

          return v;
        }),
        catchError(_ => of({
          _embedded: {
            offers: []
          },
          page: {
            number: 0,
            size: 0,
            totalPages: 0,
            totalElements: 0
          }
        }))
      );
  }

  getOwnOffers(page: number, size: number, statuses: string = 'ALL', query?: string): Observable<PaginatedOffers> {
    if (page < 0 || size < 1) {
      return of({
        _embedded: {
          offers: []
        },
        page: {
          number: 0,
          size: 0,
          totalPages: 0,
          totalElements: 0
        }
      });
    }

    const params = {
      page,
      size,
      statuses,
      query: ''
    };

    if (query) {
      params.query = query
    }

    return this.http.get<PaginatedOffers>(this.ownOffersUrl, {params})
      .pipe(
        map(v => {
          v._embedded.offers = v._embedded.offers.map(offer => {
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
          });

          return v;
        }),
        catchError(_ => of({
          _embedded: {
            offers: []
          },
          page: {
            number: 0,
            size: 0,
            totalPages: 0,
            totalElements: 0
          }
        }))
      );
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

  uploadPhoto(offerId: string, photo: File): Observable<boolean> {
    const formData = new FormData();
    formData.append("file", photo);
    return this.http.post(this.offersUrl + '/' + offerId + '/photos', formData, {observe: 'response'}).pipe(map(t => t.status == 200))
  }

  deletePhoto(offerId: string, photoId: string): Observable<boolean> {
    return this.http.delete(this.offersUrl + '/' + offerId + '/photos/' + photoId, {observe: 'response'}).pipe(map(t => t.status == 200));
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
