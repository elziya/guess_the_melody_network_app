package ru.kpfu.itis.db.services;

import ru.kpfu.itis.db.repositories.CategoryRepository;
import ru.kpfu.itis.models.Category;
import ru.kpfu.itis.models.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CategoryServiceImpl implements CategoryService{

    private CategoryRepository categoryRepository;
    protected final int NUM_FOR_GAME = 4;
    private final Random random;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        random = new Random();
    }

    @Override
    public List<Category> findCategoriesForFirstStage() {
        List<Category> allCategories = categoryRepository.findAll();

        Collections.shuffle(allCategories);
        List<Category> categoriesForGame = new ArrayList<>();

        for (int i = 0; i < NUM_FOR_GAME; i++) {
            categoriesForGame.add(allCategories.get(i));
        }

        for (int j = 0; j < categoriesForGame.size(); j++){
            Category category = categoriesForGame.get(j);

            Collections.shuffle(category.getSongs());
            List<Song> songsForCategory = new ArrayList<>();

            for (int i = 0; i < NUM_FOR_GAME; i++) {
                songsForCategory.add(category.getSongs().get(i));
            }
            category.setSongs(songsForCategory);
        }

        return categoriesForGame;
    }
}

