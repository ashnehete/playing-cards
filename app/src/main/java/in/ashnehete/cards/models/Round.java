package in.ashnehete.cards.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

/**
 * Created by Aashish Nehete on 23-Oct-17.
 */

@IgnoreExtraProperties
public class Round {
    private Map<String, String> plays;
    private String turn;

    public Round() {
    }

    public Round(Map<String, String> plays, String turn) {
        this.plays = plays;
        this.turn = turn;
    }

    public Map<String, String> getPlays() {
        return plays;
    }

    public void setPlays(Map<String, String> plays) {
        this.plays = plays;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }
}
