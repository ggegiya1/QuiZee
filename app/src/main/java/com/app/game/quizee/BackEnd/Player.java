package com.app.game.quizee.BackEnd;
import java.util.ArrayList;
/**
 * Created by Maude on 2017-04-03.
 */

public class Player {
    private String ID;
    private String name;
    private String image_pth;
    private boolean online = false;
    private ArrayList<Player> friends;
    private int level =0;

    public Player(String p_id, String p_name, String p_img){
        ID = p_id;
        name = p_name;
        image_pth = p_img;
    }

    public boolean isFriend(Player p){
        return friends.contains(p);
    }

    public boolean isOnline(){
        return online;
    }

    public void addFriend(Player p){
        friends.add(p);
    }

    //Getters
    public int getLevel(){
        return level;
    }

    public String getID(){
        return ID;
    }

    public String getName(){
        return name;
    }
    public String getImg(){
        return image_pth;
    }

}
