import { Component } from '@angular/core';
import {Offer} from "../../models/offer";

@Component({
  selector: 'home-offers',
  templateUrl: './offers.component.html',
  styleUrls: ['./offers.component.scss']
})
export class OffersComponent {
  offers: Offer[] = [
    {
      id: '2yrlx',
      title: 'Brand new shoes!',
      categories: ['clothing', 'shoes'],
      coverUrl: 'https://api.lorem.space/image/shoes?w=400&h=225&q=1',
      price: 17.20
    },
    {
      id: 'gk23s',
      title: 'Will give old shoes for free',
      categories: ['clothing', 'shoes', 'used'],
      coverUrl: 'https://api.lorem.space/image/shoes?w=400&h=225&q=2',
      price: null
    },
    {
      id: 'gfasd',
      title: 'Great neighbourhood, good location, big house! The best offer!',
      categories: ['property'],
      coverUrl: 'https://api.lorem.space/image/house?w=400&h=225',
      price: 600_000
    },
    {
      id: 'fgass',
      title: 'New furniture for sale!',
      categories: ['furniture'],
      coverUrl: 'https://api.lorem.space/image/furniture?w=400&h=225&q=1',
      price: 270
    },
    {
      id: 'asdg',
      title: 'Used furniture! Cheap!!!',
      categories: ['furniture', 'used'],
      coverUrl: 'https://api.lorem.space/image/furniture?w=400&h=225&q=2',
      price: 50
    },
    {
      id: 'ssss',
      title: 'Vintage furniture, renovated!',
      categories: ['furniture'],
      coverUrl: 'https://api.lorem.space/image/furniture?w=400&h=225&q=3',
      price: 220
    }
  ];
}
