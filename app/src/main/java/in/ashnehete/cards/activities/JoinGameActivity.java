package in.ashnehete.cards.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ashnehete.cards.R;
import in.ashnehete.cards.models.CurrentGame;

import static in.ashnehete.cards.AppConstants.PREF;
import static in.ashnehete.cards.AppConstants.PREF_GAME_ID;
import static in.ashnehete.cards.AppConstants.PREF_GAME_ON;
import static in.ashnehete.cards.AppConstants.TAG;
import static in.ashnehete.cards.Util.showToast;

public class JoinGameActivity extends AppCompatActivity {

    @BindView(R.id.recycler_games)
    RecyclerView recyclerGames;

    FirebaseRecyclerAdapter<CurrentGame, CurrentGameViewHolder> adapter;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        attachRecyclerViewAdapter();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void attachRecyclerViewAdapter() {
        Query query = mDatabase.child("current_games");
        FirebaseRecyclerOptions<CurrentGame> options = new FirebaseRecyclerOptions.Builder<CurrentGame>()
                .setQuery(query, CurrentGame.class)
                .build();
        adapter =
                new FirebaseRecyclerAdapter<CurrentGame, CurrentGameViewHolder>(options) {
                    @Override
                    public CurrentGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return new CurrentGameViewHolder(LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_game, parent, false)
                        );
                    }

                    @Override
                    protected void onBindViewHolder(CurrentGameViewHolder holder, int position, CurrentGame model) {
                        holder.bind(model);
                    }
                };
        Log.i(TAG, "Adapter: " + adapter.getItemCount());
        recyclerGames.setLayoutManager(new LinearLayoutManager(this));
        recyclerGames.setAdapter(adapter);
    }

    private AlertDialog.Builder getPasswordDialogBuilder(final CurrentGame currentGame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password Protected");

        final EditText editPassword = new EditText(this);
        editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(editPassword);

        builder.setPositiveButton("ENTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = editPassword.getText().toString();
                if (currentGame.getPassword().equals(input)) {
                    goToWaitActivity(currentGame.getId());
                } else {
                    showToast(getApplicationContext(), "Wrong Password");
                    dialogInterface.cancel();
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        return builder;
    }

    private void goToWaitActivity(String gameId) {
        // Add player to game
        Map<String, Object> childUpdates = new HashMap<>(1);
        childUpdates.put("games/" + gameId + "/players/" + mUser.getUid(), mUser.getDisplayName());
        mDatabase.updateChildren(childUpdates);

        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_GAME_ID, gameId);
        editor.putBoolean(PREF_GAME_ON, true);
        editor.apply();

        Intent intent = new Intent(JoinGameActivity.this, WaitActivity.class);
        startActivity(intent);
    }

    class CurrentGameViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_game_name)
        TextView textGameName;
        @BindView(R.id.image_game_locked)
        ImageView imageGameLocked;

        CurrentGame currentGame;

        public CurrentGameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "JoinGame Game ID: " + currentGame.getId());
                    if (currentGame.hasPassword()) {
                        AlertDialog.Builder builder = getPasswordDialogBuilder(currentGame);
                        builder.show();
                    } else {
                        goToWaitActivity(currentGame.getId());
                    }
                }
            });
        }

        public void bind(CurrentGame currentGame) {
            this.currentGame = currentGame;
            textGameName.setText(currentGame.getName());
            if (!currentGame.hasPassword())
                imageGameLocked.setVisibility(View.GONE);
        }
    }
}
