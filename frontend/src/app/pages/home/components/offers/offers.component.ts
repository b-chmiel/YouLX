import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Offer} from '../../../../models/offer';
import {ActivatedRoute} from '@angular/router';
import {OffersService} from '../../../../services/offers.service';
import {Observable, Subject, switchMap} from 'rxjs';

@Component({
  selector: 'home-offers',
  templateUrl: './offers.component.html',
  styleUrls: ['./offers.component.scss'],
})
export class OffersComponent implements OnInit {
  offers: Offer[] = [];
  searchString: string = '';
  searchObservable = new Subject<string>();

  constructor(private route: ActivatedRoute, private service: OffersService) {
  }

  ngOnInit() {
    this.offers = this.route.snapshot.data['offers'];
    this.searchObservable.pipe(
      switchMap(query => this.service.getOffers(0, 10, query))
    ).subscribe(offers => {
      this.offers = offers;
    })
  }

  searchOffers(query: string) {
    this.searchString = query;
    this.searchObservable.next(query);
  }
}
