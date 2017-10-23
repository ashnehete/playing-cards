package in.ashnehete.cards.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by Aashish Nehete on 23-Oct-17.
 */

@IgnoreExtraProperties
public class Game {
    public String name;
    public String state;
    public Map<String, String> players;
    public List<String> winners;
    public String password;

    public Game() {
    }

    public Game(String name, String state, Map<String, String> players, List<String> winners, String password) {

        this.name = name;
        this.state = state;
        this.players = players;
        this.winners = winners;
        this.password = password;
    }
}
