import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {CreateOfferDto, Offer} from '../../../../models/offer';
import {OffersService} from '../../../../services/offers.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-offer-new',
  templateUrl: './offer-new.component.html',
  styleUrls: ['./offer-new.component.scss'],
})
export class OfferNewComponent implements OnInit {
  fetching = false;
  error: string | null = null;
  form!: FormGroup;

  constructor(private offers: OffersService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    const offer = this.route.snapshot.data["offer"] as Offer;
    this.form = new FormGroup({
      id: new FormControl(offer?.id ?? ''),
      name: new FormControl(offer?.name ?? '', Validators.required),
      description: new FormControl(offer?.description ?? '', Validators.required),
      price: new FormControl(offer?.price ?? 0, Validators.min(0))
    });
  }

  onSubmit() {
    if (!this.form.valid || this.fetching) {
      return;
    }

    this.fetching = true;
    if (this.form.value.id) {
      this.offers.updateOffer(this.form.value).subscribe(result => {
        this.fetching = false;
        this.form.setValue(result);
      });
    }
    else {
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
}
