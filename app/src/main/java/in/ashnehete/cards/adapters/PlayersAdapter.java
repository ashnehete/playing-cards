package in.ashnehete.cards.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ashnehete.cards.R;
import in.ashnehete.cards.models.Player;

/**
 * Created by Aashish Nehete on 01-Nov-17.
 */

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder> {

    public List<Player> players;

    public PlayersAdapter(List<Player> players) {
        this.players = players;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false));
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        holder.bind(players.get(position));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void addAll(List<Player> players) {
        for (int i = 0; i < getItemCount(); i++) {
            this.players.remove(i);
        }
        notifyItemRangeRemoved(0, getItemCount());
        this.players = players;
        notifyDataSetChanged();
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_player)
        TextView textGameName;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Player player) {
            textGameName.setText(player.getName());
        }
    }
}
