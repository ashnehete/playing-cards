package in.ashnehete.cards.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ashnehete.cards.R;
import in.ashnehete.cards.models.Game;

import static in.ashnehete.cards.Util.showToast;

public class NewGameActivity extends AppCompatActivity {

    private static final String TAG = "NewGameActivity";
    @BindView(R.id.edit_new_name)
    EditText editNewGame;
    @BindView(R.id.edit_new_password)
    EditText editNewPassword;
    @BindView(R.id.button_new_game_submit)
    Button buttonSubmit;
    @BindView(R.id.progress_new_game_submit)
    ProgressBar progressSubmit;

    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressSubmit.setVisibility(View.VISIBLE);
                buttonSubmit.setEnabled(false);

                String name = editNewGame.getText().toString();
                String password = editNewPassword.getText().toString();

                Map<String, String> players = new HashMap<String, String>();
                players.put(mUser.getUid(), mUser.getDisplayName());
                Game game = new Game(name, password, players);

                String key = mDatabase.child("games").push().getKey();
                Log.i(TAG, "Game Key: " + key);

                mDatabase.child("games").child(key).setValue(game).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "Success: " + task.isSuccessful());
                        if (task.isComplete()) {
                            progressSubmit.setVisibility(View.GONE);
                            buttonSubmit.setEnabled(true);
                        }
                        if (task.isSuccessful()) {
                            showToast(NewGameActivity.this, "Game Created");
                        }
                    }
                });
            }
        });
    }
}