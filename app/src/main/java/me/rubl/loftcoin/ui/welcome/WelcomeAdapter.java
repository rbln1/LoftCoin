package me.rubl.loftcoin.ui.welcome;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import me.rubl.loftcoin.R;
import me.rubl.loftcoin.databinding.ItemWelcomeBinding;

public class WelcomeAdapter extends RecyclerView.Adapter<WelcomeAdapter.ViewHolder> {

    private static final int[] IMAGES = {
        R.drawable.welcome_image_one,
        R.drawable.welcome_image_two,
        R.drawable.welcome_image_three
    };
    private static final int[] TITLES = {
        R.string.activity_welcome_title_one,
        R.string.activity_welcome_title_two,
        R.string.activity_welcome_title_three
    };
    private static final int[] SUBTITLES = {
        R.string.activity_welcome_subtitle_one,
        R.string.activity_welcome_subtitle_two,
        R.string.activity_welcome_subtitle_three
    };

    private LayoutInflater inflater;

    @Override
    public int getItemCount() {
        return IMAGES.length;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemWelcomeBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.itemWelcomeImage.setImageResource(IMAGES[position]);
        holder.binding.itemWelcomeTitle.setText(TITLES[position]);
        holder.binding.itemWelcomeSubtitle.setText(SUBTITLES[position]);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemWelcomeBinding binding;

        ViewHolder(@NotNull ItemWelcomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
