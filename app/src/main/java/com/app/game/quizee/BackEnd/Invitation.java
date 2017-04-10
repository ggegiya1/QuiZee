package com.app.game.quizee.BackEnd;
import com.app.game.quizee.BackEnd.Player;

import java.util.List;
/**
 * Created by Maude on 2017-04-03.
 */

public class Invitation {
    private int timeToExpire;
    private List<Player> toInvite;
    private boolean areInvited = false;

    public Invitation(Player p1, Player p2, int timeOff ){
        timeToExpire = timeOff;
        toInvite.add(0,p1);
        toInvite.add(1,p2);
    }

    public int getTime(){
        return timeToExpire;
    }

    public List<Player> getVersus(){
        return toInvite;
    }

    public boolean invitePlayers(){
        //do stuffs, if it works,
        areInvited = true;
        //then
        return areInvited;
    }

    public Player getOtherPlayer(Player p1){
        if (toInvite.get(0) == p1){
            return toInvite.get(1);
        }else{
            return toInvite.get(0);
        }
    }
}
