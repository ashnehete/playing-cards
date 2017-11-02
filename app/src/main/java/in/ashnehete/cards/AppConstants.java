package in.ashnehete.cards;

/**
 * Created by Aashish Nehete on 23-Oct-17.
 */

public class AppConstants {
    public static final String TAG = "Cards";

    // Preferences
    public static final String PREF = "cards_prefs";
    public static final String PREF_GAME_ON = "game_on";
    public static final String PREF_GAME_ID = "game_id";
    public static final String PREF_IS_CREATOR = "is_creator";
    public static final String PREF_FIRST_PLAYER = "first_player";
    public static final String PREF_NEXT_PLAYER = "next_player";

    public static final int RC_SIGN_IN = 10;

    public static final char HEARTS = 'H';
    public static final char DIAMONDS = 'D';
    public static final char CLUBS = 'C';
    public static final char SPADES = 'S';

    public static final char HEARTS_ICON = '♥';
    public static final char DIAMONDS_ICON = '♦';
    public static final char CLUBS_ICON = '♣';
    public static final char SPADES_ICON = '♠';

    public static final String GAME_STATE_CREATED = "CREATED";
    public static final String GAME_STATE_SHUFFLE = "SHUFFLE";
    public static final String GAME_STATE_DISTRIBUTE = "DISTRIBUTE";
    public static final String GAME_STATE_ACTIVE = "ACTIVE";
    public static final String GAME_STATE_ENDED = "ENDED";

    public static final String NO_NEXT_PLAYER = "NO_NEXT_PLAYER";
}
