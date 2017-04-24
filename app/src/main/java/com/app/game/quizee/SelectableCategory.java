package com.app.game.quizee;

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

    void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Category getCategory() {
        return category;
    }
}
