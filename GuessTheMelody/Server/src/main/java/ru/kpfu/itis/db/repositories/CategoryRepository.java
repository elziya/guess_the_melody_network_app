package ru.kpfu.itis.db.repositories;

import ru.kpfu.itis.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(int id);
    List<Category> findAll();
}
