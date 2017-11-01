package in.ashnehete.cards.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ashnehete.cards.R;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.button_play)
    Button buttonPlay;
    @BindView(R.id.text_cards)
    TextView textCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
    }
}
