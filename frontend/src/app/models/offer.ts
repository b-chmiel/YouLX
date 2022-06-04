export interface CreateOfferDto {
  name: string;
  description: string;
  price?: number;
}

export interface Offer {
  id: string;
  name: string;
  description: string | null;
  coverUrl: string | null;
  imageUrls?: string[];
  price: number | null;
  creationDate: string;
  user: {
    login: string
  };
  status: string;
  _links: {
    close?: {
      href: string
    },
    publish?: {
      href: string
    }
  };
}
