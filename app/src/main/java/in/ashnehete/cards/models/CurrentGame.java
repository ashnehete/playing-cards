package in.ashnehete.cards.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Aashish Nehete on 27-Oct-17.
 */

@IgnoreExtraProperties
public class CurrentGame {
    public String id;
    public String name;
    public String password;

    public CurrentGame() {
    }

    public CurrentGame(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
