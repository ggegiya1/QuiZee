package com.app.game.quizee.BackEnd;

import com.app.game.quizee.CategorySelectionActivity;

import java.util.ArrayList;

/**
 * Created by Maude on 2017-04-06.
 */

public class BackEndManager {
    public BackEndManager(){

    }

    public static Category find_cate(String categoryName){
        for (int i = 0; i < CategorySelectionActivity.mes_cate.size(); ++i){
            System.out.println(CategorySelectionActivity.mes_cate.get(i).get_name());
            System.out.println(categoryName);
            if (CategorySelectionActivity.mes_cate.get(i).get_name().equals(categoryName)){
                return CategorySelectionActivity.mes_cate.get(i);
            }
        }
        return null;
    }
}
