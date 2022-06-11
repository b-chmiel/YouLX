export interface CreateOfferDto {
  name: string;
  description: string;
  price?: number;
}

export interface PaginatedOffers {
  _embedded: {
    offers: Offer[]
  },
  page: {
    size: number,
    totalElements: number,
    totalPages: number,
    number: number
  }
}

export interface Offer {
  id: string;
  name: string;
  description: string | null;
  coverUrl: string | null;
  imageUrls?: string[];
  price: number | null;
  creationDate: string;
  tags: {name: string}[];
  user: {
    login: string,
    firstName?: string,
    lastName?: string,
    email?: string,
    phone?: string
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
