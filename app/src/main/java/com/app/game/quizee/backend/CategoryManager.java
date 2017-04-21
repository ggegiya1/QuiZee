package com.app.game.quizee.backend;

import com.app.game.quizee.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        add(new Category(9, "General Knowledge", 0, R.drawable.ic_general_knowledge));
        add(new Category(12, "Music", 200, R.drawable.music_category_icon));
        add(new Category(15, "Video Games", 500, R.drawable.videogames_category_icon));
        add(new Category(18, "Computers", 300, R.drawable.ic_computer));
        add(new Category(22, "Geography", 100, R.drawable.ic_geography));
        add(new Category(23, "History", 150 , R.mipmap.ic_launcher));
        add(new Category(25, "Art", 50, R.drawable.ic_art));
        add(new Category(10,"Books",100, R.drawable.ic_practice));
        add(new Category(11,"Film",50, R.drawable.ic_movie));
        add(new Category(13,"Musicals & Theatres",50 , R.mipmap.ic_launcher));
        add(new Category(14,"Television",100 , R.drawable.ic_television));
        add(new Category(16,"Board Games",100, R.mipmap.ic_launcher));
        add(new Category(17,"Science & Nature",200, R.drawable.ic_nature));
        add(new Category(26,"Celebrities",200 , R.mipmap.ic_launcher));
        add(new Category(27,"Animals",100 , R.mipmap.ic_launcher));
        add(new Category(24,"Politics",300 , R.mipmap.ic_launcher));
        add(new Category(19,"Mathematics",400 ,R.mipmap.ic_launcher));
        add(new Category(20,"Mythology",0 ,R.mipmap.ic_launcher));
        add(new Category(21,"Sports",0 ,R.mipmap.ic_launcher));
        add(new Category(19,"Vehicles",0 ,R.mipmap.ic_launcher));
        add(new Category(20,"Comics",0, R.mipmap.ic_launcher));
        add(new Category(21,"Japanese Anime",0, R.mipmap.ic_launcher));
        add(new Category(22,"Cartoon",0, R.mipmap.ic_launcher));
        add(new Category(23,"Gadgets",0, R.mipmap.ic_launcher));
    }

    private void add(Category category){
        allCategories.put(category.getName(), category);
    }

    public Category getCategoryByName(String categoryName) {
        return this.allCategories.get(categoryName);
    }

    public List<Category> getAllCategoriesSortedByPrice() {
        List<Category> categories = new ArrayList<>(allCategories.values());
        // sor categories by price
        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return Double.compare(o1.getPrice(), o2.getPrice());
            }
        });
        return categories;
    }
}
