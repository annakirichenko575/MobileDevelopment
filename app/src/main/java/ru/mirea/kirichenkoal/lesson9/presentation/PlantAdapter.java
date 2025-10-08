package ru.mirea.kirichenkoal.lesson9.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.domain.models.Plant;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private List<Plant> plants = new ArrayList<>();

    public PlantAdapter() {
        // Пустой конструктор
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plants.get(position);
        holder.tvPlantName.setText(plant.getName());
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlantName;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlantName = itemView.findViewById(R.id.tvPlantName);
        }
    }
}
