package com.app.game.quizee.layout;

import com.app.game.quizee.backend.Category;

/**
 * Created by gia on 18/04/17.
 */

/**
 * Decorator class to keep Category unchanged
 */
public class SelectableCategory {

    private boolean selected;
    private Category category;

    SelectableCategory(Category category) {
        this.category = category;
    }

    boolean isSelected() {
        return selected;
    }

    void select() {
        this.selected = true;
    }

    void unSelect(){
        this.selected = false;
    }

    public Category getCategory() {
        return category;
    }
}
