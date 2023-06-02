package ru.kpfu.itis.db.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.kpfu.itis.models.Category;
import ru.kpfu.itis.models.Song;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class CategoryRepositoryJdbcTemplateImpl implements CategoryRepository{

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select category_id, category_name, song_id, pathname, s.name as song_name, " +
            "points from (select category_id, c.name as category_name, song_id from category_song left join category c " +
            "on c.id = category_song.category_id where category_id = ? order by category_id) t left join song s on song_id = s.id;";

    //language=SQL
    private static final String SQL_SELECT_ALL = "select category_id, category_name, song_id, pathname, s.name as song_name, " +
            "points from (select category_id, c.name as category_name, song_id from category_song left join category c " +
            "on c.id = category_song.category_id order by category_id) t left join song s on song_id = s.id order by category_id;";

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final ResultSetExtractor<List<Category>> categoryResultSetExtractor = resultSet -> {
        List<Category> categories = new ArrayList<>();

        Set<Integer> processedCategories = new HashSet<>();
        Category currentCategory = null;

        while (resultSet.next()) {

            if (!processedCategories.contains(resultSet.getInt("category_id"))) {
                currentCategory = Category.builder()
                        .id(resultSet.getInt("category_id"))
                        .name(resultSet.getString("category_name"))
                        .songs(new ArrayList<>())
                        .build();
                categories.add(currentCategory);
            }

            Integer songId = resultSet.getObject("song_id", Integer.class);

            if(songId != null){
                Song song = Song.builder()
                        .id(songId)
                        .name(resultSet.getString("song_name"))
                        .points(resultSet.getInt("points"))
                        .media(makeBytesArray(resultSet.getString("pathname")))
                        .build();
                currentCategory.getSongs().add(song);

            }

            processedCategories.add(currentCategory.getId());
        }
        return categories;
    };

    private byte[] makeBytesArray(String path){
        File file = new File(path);

        byte[] bytes = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private final ResultSetExtractor<Category> simpleCategoryResultSetExtractor = resultSet -> {

        Category category = null;
        while (resultSet.next()) {

            if(category == null){
                category = Category.builder()
                        .id(resultSet.getInt("category_id"))
                        .name(resultSet.getString("category_name"))
                        .songs(new ArrayList<>())
                        .build();
            }

            Song song = Song.builder()
                    .id(resultSet.getInt("song_id"))
                    .name(resultSet.getString("song_name"))
                    .points(resultSet.getInt("points"))
                    .media(makeBytesArray(resultSet.getString("pathname")))
                    .build();

            category.getSongs().add(song);
        }
        return category;
    };

    @Override
    public List<Category> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, categoryResultSetExtractor);
    }

    @Override
    public Optional<Category> findById(int id) {
        try {
            return Optional.of(jdbcTemplate.query(SQL_SELECT_BY_ID, simpleCategoryResultSetExtractor, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

