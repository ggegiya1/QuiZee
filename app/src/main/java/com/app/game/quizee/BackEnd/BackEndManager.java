package com.app.game.quizee.BackEnd;

import com.app.game.quizee.CategorySelectionActivity;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Maude on 2017-04-06.
 */

public class BackEndManager {
    public static Hashtable<String,Integer> item_cost = new Hashtable <String,Integer>();
    public static ArrayList<Category> mes_cate = new ArrayList<>();

    public static void create_item(){
        item_cost.put("Bomb", 50);
        item_cost.put("Skip", 250);
        item_cost.put("Time", 150);
        item_cost.put("Hint", 100);
    }

    public static void create_category() {
        //TODO PUT IN DB

        Category category = new Category(0, "General Knowledge", 0);
        mes_cate.add(category);
        Category category1 = new Category(1, "Entertainment: Music", 200);
        mes_cate.add(category1);
        Category category2 = new Category(2, "Entertainment: Video Games", 500);
        mes_cate.add(category2);
        Category category3 = new Category(3, "Science: Computers", 300);
        mes_cate.add(category3);
        Category category4 = new Category(4, "Geography", 100);
        mes_cate.add(category4);
        Category category5 = new Category(5, "History", 150);
        mes_cate.add(category5);
        Category category6 = new Category(6, "Art", 50);
        mes_cate.add(category6);
        Category category7 = new Category(7,"Entertainment: Books",100);
        mes_cate.add(category7);
        Category category8 = new Category(8,"Entertainment: Film",50);
        mes_cate.add(category8);
        Category category9 = new Category(9,"Entertainment: Musicals & Theatres",50);
        mes_cate.add(category9);
        Category category10 = new Category(10,"Entertainment: Television",100);
        mes_cate.add(category10);
        Category category11 = new Category(11,"Entertainment: Board Games",100);
        mes_cate.add(category11);
        Category category12 = new Category(12,"Science & Nature",200);
        mes_cate.add(category12);
        Category category13 = new Category(13,"Celebrities",200);
        mes_cate.add(category13);
        Category category14 = new Category(14,"Animals",100);
        mes_cate.add(category14);
        Category category15 = new Category(15,"Politics",300);
        mes_cate.add(category15);
        Category category16 = new Category(16,"Science: Mathematics",400);
        mes_cate.add(category16);
        Category category17 = new Category(17,"Mythology",0);
        mes_cate.add(category17);
        Category category18 = new Category(18,"Sports",0);
        mes_cate.add(category18);
        Category category19 = new Category(19,"Vehicles",0);
        mes_cate.add(category19);
        Category category20 = new Category(20,"Entertainment: Comics",0);
        mes_cate.add(category20);
        Category category21 = new Category(21,"Entertainment: Japanese Anime & Manga",0);
        mes_cate.add(category21);
        Category category22 = new Category(22,"Entertainment: Cartoon & Animations",0);
        mes_cate.add(category22);
        Category category23 = new Category(23,"Science: Gadgets",0);
        mes_cate.add(category23);
    }

    public static Category find_cate(String categoryName) {

        for (int i = 0; i < mes_cate.size(); ++i){
            System.out.println(mes_cate.get(i).get_name());
            System.out.println(categoryName);
            if (mes_cate.get(i).get_name().equals(categoryName)){
                return mes_cate.get(i);
            }
        }
        return null;
    }
}
