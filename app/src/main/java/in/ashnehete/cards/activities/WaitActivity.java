package in.ashnehete.cards.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ashnehete.cards.R;
import in.ashnehete.cards.adapters.PlayersAdapter;
import in.ashnehete.cards.models.Game;
import in.ashnehete.cards.models.Player;

import static in.ashnehete.cards.AppConstants.GAME_STATE_ACTIVE;
import static in.ashnehete.cards.AppConstants.GAME_STATE_DISTRIBUTE;
import static in.ashnehete.cards.AppConstants.NO_NEXT_PLAYER;
import static in.ashnehete.cards.AppConstants.PREF;
import static in.ashnehete.cards.AppConstants.PREF_FIRST_PLAYER;
import static in.ashnehete.cards.AppConstants.PREF_GAME_ID;
import static in.ashnehete.cards.AppConstants.PREF_IS_CREATOR;
import static in.ashnehete.cards.AppConstants.PREF_NEXT_PLAYER;
import static in.ashnehete.cards.AppConstants.TAG;

public class WaitActivity extends AppCompatActivity {

    @BindView(R.id.recycler_players)
    RecyclerView recyclerPlayers;
    @BindView(R.id.button_distribute)
    Button buttonDistribute;

    PlayersAdapter adapter;
    String gameId;
    boolean isCreator;
    List<Player> playerList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
        gameId = preferences.getString(PREF_GAME_ID, "invalid");
        isCreator = preferences.getBoolean(PREF_IS_CREATOR, false);

        Log.i(TAG, "Game ID: " + gameId);
        Log.i(TAG, "is Creator: " + isCreator);

        if (isCreator) {
            buttonDistribute.setVisibility(View.GONE);
        } else {
            buttonDistribute.setVisibility(View.VISIBLE);
        }

        List<Player> dummyList = new ArrayList<>();
        dummyList.addAll(playerList);
        adapter = new PlayersAdapter(dummyList);
        recyclerPlayers.setLayoutManager(new LinearLayoutManager(this));
        recyclerPlayers.setAdapter(adapter);
        setDataChangeListener();
    }

    public void setDataChangeListener() {
        mDatabase.child("games").child(gameId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "DataChanged: " + dataSnapshot.toString());
                Game game = dataSnapshot.getValue(Game.class);
                String nextPlayer = "";
                boolean isPlayer = false;
                if (game != null) {
                    playerList.clear();
                    SortedMap<String, String> players = new TreeMap<>();
                    players.putAll(game.getPlayers());
                    for (Map.Entry<String, String> entry : players.entrySet()) {
                        playerList.add(new Player(
                                entry.getKey(),
                                entry.getValue()
                        ));

                        if (isPlayer) {
                            nextPlayer = entry.getKey();
                            isPlayer = false;
                        }
                        if (entry.getKey().equals(mUser.getUid()))
                            isPlayer = true;
                    }

                    if (nextPlayer.equals(""))
                        nextPlayer = NO_NEXT_PLAYER;

                    adapter.addAll(playerList);

                    if (game.getState().equals(GAME_STATE_ACTIVE)) {
                        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(PREF_FIRST_PLAYER, players.firstKey());
                        editor.putString(PREF_NEXT_PLAYER, nextPlayer);
                        editor.commit();

                        Intent intent = new Intent(WaitActivity.this, GameActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "DataCancelled: " + databaseError.toString());
            }
        });
    }

    @OnClick(R.id.button_distribute)
    public void distribute() {
        Map<String, Object> childUpdates = new HashMap<>(1);
        childUpdates.put("games/" + gameId + "/state/", GAME_STATE_DISTRIBUTE);
        mDatabase.updateChildren(childUpdates);
    }
}
