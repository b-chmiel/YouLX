export interface CreateOfferDto {
  name: string;
  description: string;
}

export interface Offer {
  id: string;
  name: string;
  description: string | null;
  coverUrl: string | null;
  price: number | null;
}
