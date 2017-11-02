package in.ashnehete.cards.models;

import java.util.HashMap;
import java.util.Map;

import static in.ashnehete.cards.AppConstants.CLUBS;
import static in.ashnehete.cards.AppConstants.CLUBS_ICON;
import static in.ashnehete.cards.AppConstants.DIAMONDS;
import static in.ashnehete.cards.AppConstants.DIAMONDS_ICON;
import static in.ashnehete.cards.AppConstants.HEARTS;
import static in.ashnehete.cards.AppConstants.HEARTS_ICON;
import static in.ashnehete.cards.AppConstants.SPADES;
import static in.ashnehete.cards.AppConstants.SPADES_ICON;

/**
 * Created by Aashish Nehete on 23-Oct-17.
 */

public class Card {
    private char rank; // 2-10, J, Q, K, A
    private char suit; // H, D, C, S
    private Map<Character, Character> suitIcons;

    public Card() {
    }

    public Card(String card) {
        suitIcons = new HashMap<>(4);
        suitIcons.put(HEARTS, HEARTS_ICON);
        suitIcons.put(DIAMONDS, DIAMONDS_ICON);
        suitIcons.put(CLUBS, CLUBS_ICON);
        suitIcons.put(SPADES, SPADES_ICON);
        parseCard(card);
    }

    public static Card fromDisplayString(String displayString) {
        String[] cardValues = displayString.split(" ");
        Card card = new Card();
        card.setRank(cardValues[0].charAt(0));
        card.setSuitIcon(cardValues[0].charAt(0));
        return card;
    }

    public char getRank() {
        return rank;
    }

    public void setRank(char rank) {
        this.rank = rank;
    }

    public char getSuit() {
        return suit;
    }

    public void setSuit(char suit) {
        this.suit = suit;
    }

    public char getSuitIcon() {
        return suitIcons.get(this.getSuit());
    }

    public void setSuitIcon(char suitIcon) {
        for (Map.Entry<Character, Character> suitIconEntry : suitIcons.entrySet()) {
            if (suitIconEntry.getKey() == suitIcon)
                this.suit = suitIconEntry.getValue();
        }
    }

    /**
     * Format - <rank>:<suit>
     *
     * @param card
     */
    private void parseCard(String card) {
        String[] cardValues = card.split(":");
        if (validateCard(cardValues)) {
            this.setRank(cardValues[0].charAt(0));
            this.setSuit(cardValues[1].charAt(0));
        } else {
            throw new IllegalArgumentException("Card Format incorrect: 0th - " + cardValues[0] +
                    " 1st - " + cardValues[1]);
        }
    }

    private boolean validateCard(String[] cardValues) {
        if (cardValues.length != 2) return false;
        // TODO: Add more validations - Rank and Suit constraints, etc.
        return true;
    }

    @Override
    public String toString() {
        return getRank() + ":" + getSuit();
    }
}
