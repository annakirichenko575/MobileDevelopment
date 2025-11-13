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

import com.squareup.picasso.Picasso;

import ru.mirea.kirichenkoal.lesson9.R;
import ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem;
import ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager;

/**
 * Адаптер для отображения списка растений
 */
public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.PlantViewHolder> {

    private final List<PlantItem> plants = new ArrayList<>();
    private OnFavoriteClickListener listener;

    // Интерфейс для клика по кнопке "В избранное"
    public interface OnFavoriteClickListener {
        void onFavoriteClick(PlantItem plant);
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.listener = listener;
    }

    // Устанавливаем список растений
    public void setPlants(List<PlantItem> newPlants) {
        plants.clear();
        if (newPlants != null) {
            plants.addAll(newPlants);
        }
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

        String img = plant.getImageName();
        if (img != null && (img.startsWith("http://") || img.startsWith("https://"))) {
            Picasso.get()
                    .load(img)
                    .placeholder(R.drawable.ic_plant_logo)
                    .error(R.drawable.ic_plant_logo)
                    .into(holder.imagePlant);
        } else {
            int resId = holder.itemView.getContext()
                    .getResources()
                    .getIdentifier(
                            img,
                            "drawable",
                            holder.itemView.getContext().getPackageName()
                    );
            if (resId != 0) {
                holder.imagePlant.setImageResource(resId);
            } else {
                holder.imagePlant.setImageResource(R.drawable.ic_plant_logo);
            }
        }


        // Текст на кнопке
        holder.buttonFavorite.setText(
                plant.isFavorite() ? "Удалить из избранного" : "В избранное"
        );

        // Обработка нажатия
        holder.buttonFavorite.setOnClickListener(v -> {
            if (ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthManager.isGuest()) {
                android.widget.Toast.makeText(
                        v.getContext(),
                        "Авторизуйтесь, чтобы добавлять в избранное",
                        android.widget.Toast.LENGTH_SHORT
                ).show();
                v.getContext().startActivity(
                        new android.content.Intent(
                                v.getContext(),
                                ru.mirea.kirichenkoal.lesson9.presentation.auth.AuthActivity.class
                        )
                );
                return;
            }
            if (listener != null) listener.onFavoriteClick(plant);
        });

        // Обработка клика по всей карточке — открытие PlantDetailActivity
        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(
                    v.getContext(),
                    ru.mirea.kirichenkoal.lesson9.presentation.PlantDetailActivity.class
            );
            intent.putExtra("plant_index", position);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    // === ViewHolder ===
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

    /**
     * Загружает растения из сети и преобразует их в объекты PlantItem.
     */
    public void setPlantsFromApi(List<ru.mirea.kirichenkoal.lesson9.data.network.dto.PlantApiModel> apiPlants) {
        plants.clear();

        if (apiPlants != null) {
            int idCounter = 1;

            // 🔹 список картинок (по индексу)
            String[] urls = {
                    "https://flowry.ru/wp-content/uploads/2023/01/allium.jpg",
                    "https://hoff.ru/upload/medialibrary/fc2/fc2bb3a0acab9a1f44e84a385ebfd53c.jpg",
                    "https://gbcvet.ru/upload/iblock/14f/1rl3h0dfo1kvua1fstqlq3h23geoaa83.jpg",
                    "https://multifoto.ru/upload/medialibrary/68c/01stxou3k1kx7ry6covj9lpb6s64szcr.jpg",
                    "https://flowry.ru/wp-content/uploads/2023/01/agapantus.jpg",
                    "https://img.7dach.ru/image/600/01/50/52/2014/03/20/3bd5c2.jpg"
            };

            int imgIndex = 0;

            for (ru.mirea.kirichenkoal.lesson9.data.network.dto.PlantApiModel apiModel : apiPlants) {
                String title = apiModel.getTitle() != null ? apiModel.getTitle() : "Без названия";
                String desc = (apiModel.getCompleted() != null && apiModel.getCompleted())
                        ? "Растение активное" : "Растение неактивное";

                // 🔹 картинка по индексу
                String imageUrl = urls[imgIndex % urls.length];
                imgIndex++;

                plants.add(new ru.mirea.kirichenkoal.lesson9.domain.models.PlantItem(
                        idCounter++,
                        title,
                        desc,
                        imageUrl,   // теперь это URL, Picasso его поймает
                        false
                ));
            }
        }

        notifyDataSetChanged();
    }
}
