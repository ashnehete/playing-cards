package in.ashnehete.cards.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ashnehete.cards.R;
import in.ashnehete.cards.models.CurrentGame;

import static in.ashnehete.cards.AppConstants.TAG;

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
                    if (currentGame.hasPassword()) {
                        Intent intent = new Intent(JoinGameActivity.this, WaitActivity.class);
                        startActivity(intent);
                    } else {

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
