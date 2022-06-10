import { Component, OnInit } from '@angular/core';
import {Offer, PaginatedOffers} from '../../../../models/offer';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';
import {Subject, switchMap} from 'rxjs';

@Component({
  selector: 'app-browse-my-offers',
  templateUrl: './browse-my-offers.component.html',
  styleUrls: ['./browse-my-offers.component.scss']
})
export class BrowseMyOffersComponent implements OnInit {
  offers!: PaginatedOffers;
  changePage$ = new Subject<number>();

  constructor(private route: ActivatedRoute, private offersService: OffersService) {
  }

  ngOnInit() {
    this.offers = this.route.snapshot.data['offers'];
    this.changePage$.pipe(
      switchMap(page => this.offersService.getOwnOffers(page, 6))
    ).subscribe(offers => {
      this.offers = offers;
    })
  }

  changePage(page: number) {
    const pagination = this.offers.page;

    if (page < 0 || page >= pagination.totalPages) {
      return;
    }

    this.changePage$.next(page);
  }

  prevPage() {
    const page = this.offers.page.number - 1;
    this.changePage(page);
  }

  nextPage() {
    const page = this.offers.page.number + 1;
    this.changePage(page);
  }
}
