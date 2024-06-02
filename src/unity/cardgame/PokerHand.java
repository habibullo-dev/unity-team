package unity.cardgame;

import java.util.ArrayList;
import javax.swing.JLabel;

public class PokerHand extends Hand {

	public PokerHand() {
		super(5); // 5 card poker?
	}

	public int[] PokerValue() {
		int[] pv = new int[7];
		boolean flush = false;
		boolean straight = false;
		int highCard = 0;
		int[] cardValues = new int[13];

		// Check for flush
		int clubs = 0, spades = 0, hearts = 0, diamonds = 0;
		for (Card c : hand) {
			switch (c.getSuit()) {
				case Clubs:
					clubs++;
					break;
				case Spades:
					spades++;
					break;
				case Hearts:
					hearts++;
					break;
				case Diamonds:
					diamonds++;
					break;
			}
		}

		if (clubs == 5 || spades == 5 || hearts == 5 || diamonds == 5) {
			flush = true;
		}

		// Check for straight
		boolean[] presentCards = new boolean[13];
		int cardsInSequence = 0;
		int lowStraight = 0;

		for (Card c : hand) {
			int cardValue = c.getCardNumber();
			presentCards[cardValue - 1] = true;
			if (cardValue > highCard) {
				highCard = cardValue;
			}
		}

		for (int i = 0; i < 9; i++) {
			if (presentCards[i] && presentCards[i + 1] && presentCards[i + 2] && presentCards[i + 3]
					&& presentCards[i + 4]) {
				cardsInSequence = 5;
				lowStraight = i + 1;
				break;
			}
		}

		if (cardsInSequence == 5) {
			straight = true;
			highCard = lowStraight + 4;
		} else if (presentCards[0] && presentCards[9] && presentCards[10] && presentCards[11] && presentCards[12]) {
			straight = true;
			highCard = 5;
		}

		// Check for pairs, three of a kind, four of a kind
		for (Card c : hand) {
			int cardValue = c.getCardNumber();
			cardValues[cardValue - 1]++;
		}

		int pairCount = 0;
		int threeOfAKindValue = 0;
		int fourOfAKindValue = 0;

		for (int i = 0; i < 13; i++) {
			if (cardValues[i] == 4) {
				fourOfAKindValue = i + 1;
			} else if (cardValues[i] == 3) {
				threeOfAKindValue = i + 1;
			} else if (cardValues[i] == 2) {
				pairCount++;
				pv[pairCount + 2] = i + 1;
			}
		}

		// Assign hand rank and values
		if (straight && flush) {
			pv[0] = 10; // Royal flush
			if (highCard == 14) {
				pv[1] = 1; // Ace-high straight flush
			} else {
				pv[1] = highCard;
			}
		} else if (fourOfAKindValue != 0) {
			pv[0] = 9; // Four of a kind
			pv[1] = fourOfAKindValue;
			for (int i = 0; i < 13; i++) {
				if (cardValues[i] == 1) {
					pv[2] = i + 1;
					break;
				}
			}
		} else if (threeOfAKindValue != 0 && pairCount == 1) {
			pv[0] = 8; // Full house
			pv[1] = threeOfAKindValue;
			pv[2] = pv[3];
		} else if (flush) {
			pv[0] = 7; // Flush
			pv[1] = highCard;
		} else if (straight) {
			pv[0] = 6; // Straight
			pv[1] = highCard;
		} else if (threeOfAKindValue != 0) {
			pv[0] = 5; // Three of a kind
			pv[1] = threeOfAKindValue;
		} else if (pairCount == 2) {
			pv[0] = 4; // Two pairs
		} else if (pairCount == 1) {
			pv[0] = 3; // One pair
			pv[1] = pv[3];
		} else {
			pv[0] = 2; // High card
			pv[1] = highCard;
		}

		return pv;
	}

	public String showPokerValue() {
		if (PokerValue()[0] == 10) {
			return "Straight Flush";
		} else if (PokerValue()[0] == 9) {
			return "Four of a Kind";
		} else if (PokerValue()[0] == 8) {
			return "Full House";
		} else if (PokerValue()[0] == 7) {
			return "Flush";
		} else if (PokerValue()[0] == 6) {
			return "Straight";
		} else if (PokerValue()[0] == 5) {
			return "Three of a Kind";
		} else if (PokerValue()[0] == 4) {
			return "Two Pair";
		} else if (PokerValue()[0] == 3) {
			return "Pair";
		} else {
			return "Higher Card";
		}

	}

	public void dealerExchange(Deck d, JLabel[] dc) {
		ArrayList<Card> keepcards = new ArrayList<Card>();

		if (PokerValue()[0] == 10 || PokerValue()[0] == 8 || PokerValue()[0] == 7 || PokerValue()[0] == 6) {
			return;
		} else if (PokerValue()[0] == 9) {
			int val = 0;
			if (hand.get(0).getCardNumber() == hand.get(1).getCardNumber()) {
				val = hand.get(0).getCardNumber();
			}

			if (hand.get(3).getCardNumber() == hand.get(4).getCardNumber()) {
				val = hand.get(0).getCardNumber();
			}
			Card c = null;

			for (int i = 0; i < 5; i++) {
				c = hand.get(i);

				if (c.getCardNumber() == val) {
					keepcards.add(c);
				} else if (c.getCardNumber() < 9) {
					keepcards.add(d.dealCard());
				} else {
					keepcards.add(c);
				}
			}
		}

		else if (PokerValue()[0] == 5) {
			int val = 0;
			if (hand.get(0).getCardNumber() == hand.get(1).getCardNumber()) {
				val = hand.get(0).getCardNumber();
			}

			else if (hand.get(3).getCardNumber() == hand.get(4).getCardNumber()) {
				val = hand.get(0).getCardNumber();
			} else if (hand.get(2).getCardNumber() == hand.get(3).getCardNumber()) {
				val = hand.get(0).getCardNumber();
			}
			if (hand.get(0).getCardNumber() == hand.get(4).getCardNumber()) {
				val = hand.get(0).getCardNumber();
			}

			Card c = null;

			for (int i = 0; i < 5; i++) {
				c = hand.get(i);

				if (c.getCardNumber() == val) {
					keepcards.add(c);
				} else {
					keepcards.add(d.dealCard());
				}
			}
		}

		else if (PokerValue()[0] == 4) {
			int val1 = -1;
			int val2 = -1;

			for (int i = 0; i < 5; i++) {
				Card c = hand.get(i);
				for (int j = 0; j < 5; j++) {
					Card a = hand.get(j);

					if (c.getCardNumber() == a.getCardNumber() && c != a) {
						if (val1 == -1) {
							val1 = c.getCardNumber();
						} else {
							val2 = c.getCardNumber();
						}
					}
				}
			}

			for (int i = 0; i < 5; i++) {
				Card b = hand.get(i);

				if (b.getCardNumber() == val1 || b.getCardNumber() == val2) {
					keepcards.add(b);
				} else {
					keepcards.add(d.dealCard());
				}
			}
		}

		else if (PokerValue()[0] == 3) {
			int val = -1;

			for (int i = 0; i < 5; i++) {
				Card c = hand.get(i);
				for (int j = 0; j < 5; j++) {
					Card a = hand.get(j);

					if (c.getCardNumber() == a.getCardNumber() && c != a) {
						val = c.getCardNumber();
						break;
					}
				}
			}

			for (int i = 0; i < 5; i++) {
				Card b = hand.get(i);

				if (b.getCardNumber() == val) {
					keepcards.add(b);
				} else {
					keepcards.add(d.dealCard());
				}
			}
		}

		else {
			int clubs = 0, spades = 0, diamonds = 0, hearts = 0;
			int n = hand.size();
			ArrayList<Card> temp = hand;

			for (int pass = 1; pass < n; pass++) {
				for (int i = 0; i < n - pass; i++) {
					if (temp.get(i).getCardNumber() > temp.get(i + 1).getCardNumber()) {
						Card t = temp.get(i);
						temp.set(i, temp.get(i + 1));
						temp.set(i + 1, t);
					}
				}
			}

			for (int i = 0; i < 5; i++) {
				if (getCard(i).getSuit().equals("Clubs")) {
					clubs++;
					if (clubs >= 3) {
						for (int j = 0; j < 5; j++) {
							if (hand.get(j).getSuit().equals("Clubs")) {
								keepcards.add(hand.get(j));
							} else
								keepcards.add(d.dealCard());
						}
					}
				} else if (getCard(i).getSuit().equals("Spades")) {
					spades++;
					if (spades >= 3) {
						for (int j = 0; j < 5; j++) {
							if (hand.get(j).getSuit().equals("Spades")) {
								keepcards.add(hand.get(j));
							} else
								keepcards.add(d.dealCard());
						}
					}
				} else if (getCard(i).getSuit().equals("Hearts")) {
					hearts++;
					if (hearts >= 3) {

						for (int j = 0; j < 5; j++) {
							if (hand.get(j).getSuit().equals("Hearts")) {
								keepcards.add(hand.get(j));
							} else
								keepcards.add(d.dealCard());
						}
					}
				} else {
					diamonds++;
					if (diamonds >= 3) {
						for (int j = 0; j < 5; j++) {
							if (hand.get(j).getSuit().equals("Diamonds")) {
								keepcards.add(hand.get(j));
							} else
								keepcards.add(d.dealCard());
						}
					}
				}

			}
			if (keepcards.size() == 0) {
				int s = 0;
				ArrayList<Card> st = new ArrayList<Card>();

				for (int i = 0; i < 4; i++) {
					if (temp.get(i + 1).getCardNumber() - temp.get(i).getCardNumber() == 1) {
						s++;
						if (!st.contains(temp.get(i))) {
							st.add(temp.get(i));
						}
						if (!st.contains(temp.get(i + 1))) {
							st.add(temp.get(i + 1));
						}

					}

				}

				if (s >= 2 && st.get(st.size() - 1).getCardNumber() - st.get(0).getCardNumber() < 4) {
					keepcards = st;
					for (int i = keepcards.size(); i <= 5; i++) {
						keepcards.add(d.dealCard());
					}
				}

			}

			if (keepcards.size() == 0) {
				/**
				 * Keep 2 highest values
				 */
				keepcards.add(temp.get(3));
				keepcards.add(temp.get(4));

				keepcards.add(d.dealCard());
				keepcards.add(d.dealCard());
				keepcards.add(d.dealCard());
			}

		}

		clearHand();
		for (int i = 0; i < 5; i++) {
			hand.add(keepcards.get(i));
		}

	}

	public int bet(int amount) {
		int pv = PokerValue()[0];
		if (amount >= 500) {
			if (pv < 4) {
				return -1;
			} else if (pv < 7) {
				return amount;
			} else {
				return amount + 100;
			}
		}

		else if (amount >= 100) {
			if (pv < 3) {
				return -1;
			} else if (pv < 7) {
				return amount + 100;
			} else {
				return amount * 2;
			}
		} else if (amount >= 10) {
			if (pv < 3) {
				return amount;
			} else if (pv < 7) {
				return amount * 2;
			} else {
				return amount * 5;
			}
		} else {
			if (pv < 3) {
				return amount;
			} else if (pv < 7) {
				return amount + 100;
			} else {
				return amount + 700;
			}
		}

	}

	public int higherHand(PokerHand dh) {

		Card temp[] = new Card[hand.size()];
		int n = temp.length;

		for (int i = 0; i < hand.size(); i++) {
			temp[i] = hand.get(i);
		}

		for (int pass = 1; pass < n; pass++) {
			for (int i = 0; i < n - pass; i++) {
				if (temp[i].getCardNumber() > temp[i + 1].getCardNumber()) {
					Card t = temp[i];
					temp[i] = temp[i + 1];
					temp[i + 1] = t;
				}
			}
		}

		Card temp2[] = new Card[dh.hand.size()];
		int num = temp2.length;

		for (int i = 0; i < dh.hand.size(); i++) {
			temp2[i] = dh.hand.get(i);
		}

		for (int pass = 1; pass < num; pass++) {
			for (int i = 0; i < num - pass; i++) {
				if (temp2[i].getCardNumber() > temp2[i + 1].getCardNumber()) {
					Card t = temp2[i];
					temp2[i] = temp2[i + 1];
					temp2[i + 1] = t;
				}
			}
		}

		for (int i = 0; i < temp.length; i++) {
			if (temp[i].getCardNumber() > temp2[i].getCardNumber()) {
				return -1;
			} else if (temp[i].getCardNumber() < temp2[i].getCardNumber()) {
				return 1;
			} else {
				continue;
			}
		}

		return 0;
	}
}
