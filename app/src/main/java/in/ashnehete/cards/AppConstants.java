package in.ashnehete.cards;

/**
 * Created by Aashish Nehete on 23-Oct-17.
 */

public class AppConstants {
    public static final String TAG = "Cards";

    // Preferences
    public static final String PREF_GAME_ON = "game_on";
    public static final String PREF_GAME_ID = "game_id";
    public static final String PREF_IS_CREATOR = "is_creator";

    public static final int RC_SIGN_IN = 10;

    public static char HEARTS = 'H';
    public static char DIAMONDS = 'D';
    public static char CLUBS = 'C';
    public static char SPADES = 'S';

    public static char HEARTS_ICON = '♥';
    public static char DIAMONDS_ICON = '♦';
    public static char CLUBS_ICON = '♣';
    public static char SPADES_ICON = '♠';

    public static String GAME_STATE_CREATED = "CREATED";
    public static String GAME_STATE_SHUFFLE = "SHUFFLE";
    public static String GAME_STATE_DISTRIBUTE = "DISTRIBUTE";
    public static String GAME_STATE_ACTIVE = "ACTIVE";
    public static String GAME_STATE_ENDED = "ENDED";
}
