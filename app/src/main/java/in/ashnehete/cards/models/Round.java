package in.ashnehete.cards.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

/**
 * Created by Aashish Nehete on 23-Oct-17.
 */

@IgnoreExtraProperties
public class Round {
    public Map<String, String> plays;
    public String turn;

    public Round() {
    }

    public Round(Map<String, String> plays, String turn) {

        this.plays = plays;
        this.turn = turn;
    }
}
