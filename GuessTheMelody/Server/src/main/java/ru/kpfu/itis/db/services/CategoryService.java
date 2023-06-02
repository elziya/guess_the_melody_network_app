package ru.kpfu.itis.db.services;

import ru.kpfu.itis.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findCategoriesForFirstStage();
}
