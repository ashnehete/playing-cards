package in.ashnehete.cards.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.Map;

import static in.ashnehete.cards.AppConstants.GAME_STATE_CREATED;

/**
 * Created by Aashish Nehete on 23-Oct-17.
 */

@IgnoreExtraProperties
public class Game {
    private String name;
    private String state;
    private Map<String, String> players;
    private List<String> winners;
    private String password;

    public Game() {
    }

    public Game(String name, String password, Map<String, String> players) {
        this.name = name;
        this.password = password;
        this.state = GAME_STATE_CREATED;
        this.players = players;
    }

    public Game(String name, String state, Map<String, String> players, List<String> winners, String password) {
        this.name = name;
        this.state = state;
        this.players = players;
        this.winners = winners;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Map<String, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }

    public List<String> getWinners() {
        return winners;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
