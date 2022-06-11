import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {PaginatedOffers} from '../../../../models/offer';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';
import {Subject, switchMap} from 'rxjs';

@Component({
  selector: 'home-offers',
  templateUrl: './offers.component.html',
  styleUrls: ['./offers.component.scss'],
})
export class OffersComponent implements OnInit {
  offers!: PaginatedOffers;
  query: string = '';
  tags: string[] = [];
  refresh$ = new Subject();

  constructor(private route: ActivatedRoute, private service: OffersService) {
  }

  ngOnInit() {
    this.offers = this.route.snapshot.data['offers'];
    this.refresh$.pipe(
      switchMap(_ => this.service.getOffers(this.offers.page.number, 6, this.query, this.tags)),
    ).subscribe(offers => {
      this.offers = offers;
    });
  }

  changePage(page: number) {
    const pagination = this.offers.page;
    if (page == pagination.number || page < 0 || page > pagination.totalPages) {
      return;
    }

    if (page > pagination.number && (this.offers._embedded.offers.length < pagination.size || this.offers._embedded.offers.length == pagination.totalElements)) {
      return;
    }

    this.offers.page.number = page;
    this.refresh$.next(true);
  }

  searchOffers(query: string) {
    this.query = query;
    this.refresh$.next(true);
  }

  searchTags(tags: string[]) {
    this.tags = tags;
    this.refresh$.next(true);
  }
}
