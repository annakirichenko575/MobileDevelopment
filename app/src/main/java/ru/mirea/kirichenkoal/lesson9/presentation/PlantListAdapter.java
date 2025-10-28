package ru.mirea.kirichenkoal.lesson9.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;

public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.PlantViewHolder> {

    private final List<PlantItem> plants = new ArrayList<>();
    private OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(PlantItem plant);
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.listener = listener;
    }

    public void setPlants(List<PlantItem> newPlants) {
        plants.clear();
        plants.addAll(newPlants);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plant_card, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        PlantItem plant = plants.get(position);
        holder.textName.setText(plant.getName());
        holder.textDescription.setText(plant.getDescription());

        int resId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(plant.getImageName(), "drawable",
                        holder.itemView.getContext().getPackageName());
        if (resId != 0) {
            holder.imagePlant.setImageResource(resId);
        } else {
            holder.imagePlant.setImageResource(R.drawable.ic_plant_logo);
        }

        holder.buttonFavorite.setText(plant.isFavorite() ? "Удалить из избранного" : "В избранное");

        holder.buttonFavorite.setOnClickListener(v -> {
            if (listener != null) listener.onFavoriteClick(plant);
        });
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePlant;
        TextView textName;
        TextView textDescription;
        Button buttonFavorite;

        PlantViewHolder(View itemView) {
            super(itemView);
            imagePlant = itemView.findViewById(R.id.imagePlant);
            textName = itemView.findViewById(R.id.textPlantName);
            textDescription = itemView.findViewById(R.id.textPlantDescription);
            buttonFavorite = itemView.findViewById(R.id.buttonFavorite);
        }
    }
}
