package com.app.game.quizee.BackEnd;

/**
 * CETTE CLASSE N'EST PAS UTILISÉE POUR LE MOMENT..PERTINENT?
 */
//TODO ^^

public class Item {
    private final String type;
    private final int cost;
    private final int imageId;


    Item(String type, int cost, int image_id){
        this.type = type;
        this.cost = cost;
        this.imageId = image_id;
    }

    public String getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public int getImageId() {
        return imageId;
    }
}
