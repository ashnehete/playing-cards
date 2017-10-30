package in.ashnehete.cards.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Aashish Nehete on 27-Oct-17.
 */

@IgnoreExtraProperties
public class CurrentGame {
    private String id;
    private String name;
    private String password;

    public CurrentGame() {
    }

    public CurrentGame(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean hasPassword() {
        return !("".equals(this.password));
    }
}
