package ru.mirea.kirichenkoal.lesson9.data.network;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.kirichenkoal.lesson9.data.network.models.ApiPlant;

public class PlantNetworkApi {

    private static final List<ApiPlant> MOCK_PLANTS = createMockPlants();

    public List<ApiPlant> searchPlants(String query) {
        // Имитация сетевого запроса с задержкой
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<ApiPlant> results = new ArrayList<>();
        for (ApiPlant plant : MOCK_PLANTS) {
            if (plant.name.toLowerCase().contains(query.toLowerCase()) ||
                    plant.scientificName.toLowerCase().contains(query.toLowerCase())) {
                results.add(plant);
            }
        }
        return results;
    }

    public ApiPlant getPlantById(int id) {
        // Имитация сетевого запроса
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (ApiPlant plant : MOCK_PLANTS) {
            if (plant.id == id) {
                return plant;
            }
        }
        return null;
    }

    private static List<ApiPlant> createMockPlants() {
        List<ApiPlant> plants = new ArrayList<>();
        plants.add(new ApiPlant(1, "Фикус Бенджамина", "Ficus benjamina",
                "Тутовые", "Популярное комнатное растение с мелкими листьями",
                "Полив умеренный, свет рассеянный", ""));
        plants.add(new ApiPlant(2, "Алоэ Вера", "Aloe vera",
                "Асфоделовые", "Лечебное растение с сочными листьями",
                "Редкий полив, яркий свет", ""));
        plants.add(new ApiPlant(3, "Монстера", "Monstera deliciosa",
                "Ароидные", "Растение с крупными резными листьями",
                "Регулярный полив, полутень", ""));
        plants.add(new ApiPlant(4, "Сансевиерия", "Sansevieria trifasciata",
                "Спаржевые", "Неприхотливое растение с длинными листьями",
                "Редкий полив, любое освещение", ""));
        plants.add(new ApiPlant(5, "Спатифиллум", "Spathiphyllum",
                "Ароидные", "Растение с белыми цветами и темными листьями",
                "Влажная почва, тень", ""));
        return plants;
    }
}