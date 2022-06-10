import {Offer} from './offer';

export interface Conversation {
  id: string,
  posterId: string,
  browserId: string,
  offer: Offer
}

export interface Message {
  content: string,
  time: Date,
  userId: string,
  owned: boolean
}

export interface PostMessage {
  content: string
}
