package in.ashnehete.cards.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ashnehete.cards.R;
import in.ashnehete.cards.models.Card;
import in.ashnehete.cards.models.Round;

import static in.ashnehete.cards.AppConstants.NO_NEXT_PLAYER;
import static in.ashnehete.cards.AppConstants.PREF;
import static in.ashnehete.cards.AppConstants.PREF_FIRST_PLAYER;
import static in.ashnehete.cards.AppConstants.PREF_GAME_ID;
import static in.ashnehete.cards.AppConstants.PREF_NEXT_PLAYER;
import static in.ashnehete.cards.AppConstants.TAG;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.button_play)
    Button buttonPlay;
    @BindView(R.id.text_cards)
    TextView textCards;

    DatabaseReference mDatabase;
    FirebaseUser mUser;

    String gameId;
    String firstPlayer;
    String nextPlayer;

    List<Card> cards = new ArrayList<>();
    List<String> stringCards = new ArrayList<>();
    List<Round> rounds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
        gameId = preferences.getString(PREF_GAME_ID, "invalid");
        firstPlayer = preferences.getString(PREF_FIRST_PLAYER, NO_NEXT_PLAYER);
        nextPlayer = preferences.getString(PREF_NEXT_PLAYER, NO_NEXT_PLAYER);

        getCardsData();
    }

    private void getCardsData() {
        // Get the cards
        mDatabase.child("cards").child(gameId).child(mUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cards.clear();
                        Log.i(TAG, "Cards: Data Changed: " + dataSnapshot.toString());
                        for (DataSnapshot cardSnapshot : dataSnapshot.getChildren()) {
                            Card card = new Card(cardSnapshot.getValue(String.class));
                            cards.add(card);
                            stringCards.add(card.toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Cards: DataCancelled: " + databaseError.toString());
                    }
                });

        // Keep a track of the rounds
        mDatabase.child("rounds").child(gameId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        rounds.clear();
                        Log.i(TAG, "Round: Data Changed: " + dataSnapshot.toString());
                        for (DataSnapshot roundSnapshot : dataSnapshot.getChildren()) {
                            Round round = roundSnapshot.getValue(Round.class);
                            if (round != null)
                                rounds.add(round);
                        }

                        setOtherUserPlays();

                        Round latestRound = rounds.get(rounds.size() - 1);
                        if (latestRound.getTurn().equals(mUser.getUid())) {
                            buttonPlay.setEnabled(true);
                        } else {
                            buttonPlay.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Round: DataCancelled: " + databaseError.toString());
                    }
                });
    }

    private void setOtherUserPlays() {
        String text = "";
        for (Round round : rounds) {
            if (round.getPlays() != null) {
                for (Map.Entry<String, String> entry : round.getPlays().entrySet()) {
                    text += entry.getKey().substring(0, 5) + " : " + entry.getValue() + "\n";
                }
            }
        }
        textCards.setText(text);
    }

    @OnClick(R.id.button_play)
    public void play() {
        getCardsDialogBuilder().show();
    }

    private AlertDialog.Builder getCardsDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Your Card")
                .setItems(stringCards.toArray(new String[stringCards.size()]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String selectedCard = stringCards.get(i);
                                Log.i(TAG, "Selected Card: " + selectedCard);

                                int latestRoundId = rounds.size() - 1;
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put(latestRoundId + "/plays/" + mUser.getUid(),
                                        selectedCard);
                                childUpdates.put(latestRoundId + "/turn/", nextPlayer);

                                // Next Round
                                if (nextPlayer.equals(NO_NEXT_PLAYER)) {
                                    childUpdates.put((latestRoundId + 1) + "/turn/", firstPlayer);
                                }

                                mDatabase.child("rounds").child(gameId)
                                        .updateChildren(childUpdates);
                            }
                        });
        return builder;
    }
}
