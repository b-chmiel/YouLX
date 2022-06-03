import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {CreateOfferDto, Offer} from '../../../../models/offer';
import {OffersService} from '../../../../services/offers.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-offer-new',
  templateUrl: './offer-new.component.html',
  styleUrls: ['./offer-new.component.scss'],
})
export class OfferNewComponent {
  fetching = false;
  error: string | null = null;
  form = new FormGroup({
    name: new FormControl('', Validators.required),
    description: new FormControl('', Validators.required),
    price: new FormControl(0, Validators.min(0))
  })

  constructor(private offers: OffersService, private router: Router) {
  }

  onSubmit() {
    if (!this.form.valid || this.fetching) {
      return;
    }

    this.fetching = true;
    const offer = this.form.value as CreateOfferDto;
    if (offer.price == 0) {
      delete offer.price;
    }
    this.offers.createOffer(offer).subscribe(result => {
      this.fetching = false;
      if (result) {
        this.router.navigateByUrl(`/offers/owned/${result}`);
      }
      else {
        this.error = 'Could not create offer. Perhaps try again?';
      }
    });
  }
}
