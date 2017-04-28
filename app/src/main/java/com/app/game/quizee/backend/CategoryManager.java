package com.app.game.quizee.backend;

import com.app.game.quizee.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gia on 15/04/17.
 */

public class CategoryManager {

    private final Map<String, Category> allCategories;

    private static CategoryManager instance;

    private CategoryManager() {
        allCategories = new HashMap<>();
        init();
    }

    public static CategoryManager getInstance(){
        if (instance == null){
            instance = new CategoryManager();
        }
        return instance;
    }

    private void init(){
        add(new Category(9, "General Knowledge", "General Knowledge", 0, R.drawable.ic_general_knowledge));
        add(new Category(12, "Entertainment: Music", "Music", 200, R.drawable.music_category_icon));
        add(new Category(15, "Entertainment: Video Games", "Video Games", 500, R.drawable.videogames_category_icon));
        add(new Category(18, "Science: Computers", "Computers", 300, R.drawable.ic_computer));
        add(new Category(22, "Geography", "Geography", 100, R.drawable.ic_geography));
        add(new Category(23, "History", "History", 150 , R.drawable.ic_history));
        add(new Category(25, "Art", "Art", 50, R.drawable.ic_art));
        add(new Category(10, "Entertainment: Books",    "Books",100, R.drawable.ic_practice));
        add(new Category(11,"Entertainment: Film", "Film",50, R.drawable.ic_movie));
        add(new Category(13,"Entertainment: Musicals & Theatres", "Musicals & Theatres",50 , R.drawable.ic_movie));
        add(new Category(14,"Entertainment: Television", "Television",100 , R.drawable.ic_television));
        add(new Category(16,"Entertainment: Board Games", "Board Games",100, R.drawable.ic_boardgame));
        add(new Category(17,"Science & Nature", "Science & Nature",200, R.drawable.ic_nature));
        add(new Category(26,"Celebrities", "Celebrities",200 , R.drawable.ic_celebrities));
        add(new Category(27,"Animals", "Animals",100 , R.drawable.ic_animals));
        add(new Category(24,"Politics", "Politics",300 , R.drawable.ic_politics));
        add(new Category(19,"Science: Mathematics", "Mathematics",400 ,R.drawable.ic_mathematics));
        add(new Category(20,"Mythology", "Mythology",0 ,R.drawable.ic_mythology));
        add(new Category(21,"Sports", "Sports",0 ,R.drawable.ic_sports));
        add(new Category(28,"Vehicles", "Vehicles",0 ,R.drawable.ic_vehicules));
        add(new Category(29,"Entertainment: Comics", "Comics",0, R.drawable.ic_comics));
        add(new Category(31,"Entertainment: Japanese Anime & Manga", "Japanese Anime",0, R.drawable.ic_japanese_anime));
        add(new Category(32,"Entertainment: Cartoon & Animations", "Cartoon",0, R.drawable.ic_television));
        add(new Category(30,"Science: Gadgets", "Gadgets",0, R.drawable.ic_gadgets));
    }

    private void add(Category category){
        allCategories.put(category.getName(), category);
    }

    public Category getCategoryByName(String categoryName) {
        return this.allCategories.get(categoryName);
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(allCategories.values());
    }
}
