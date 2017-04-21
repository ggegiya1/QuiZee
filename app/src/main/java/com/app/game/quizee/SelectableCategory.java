package com.app.game.quizee;

import com.app.game.quizee.backend.Category;

/**
 * Created by gia on 18/04/17.
 */

/**
 * Decorator class to keep Category unchanged
 */
public class SelectableCategory extends Category {

    private boolean selected;

    SelectableCategory(Category category) {
        super(category.getId(), category.getName(), category.getDisplayName(), category.getPrice(), category.getImageId());
    }

    boolean isSelected() {
        return selected;
    }

    void setSelected(boolean selected) {
        this.selected = selected;
    }

}
