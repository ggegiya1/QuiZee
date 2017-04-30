package com.app.game.quizee.backend;

import com.app.game.quizee.R;


/**
 * Power-ups
 */

public enum PowerUp {

    BOMB{
        @Override
        public void apply(Game game) {
            game.deleteTwoIncorrectAnswers();
        }

        @Override
        public int getPrice() {
            return 300;
        }

        @Override
        public String getType() {
            return "50/50";
        }

        @Override
        public String getName(){return "Bomb";}

        @Override
        public int getPosition(){return 1;}

        @Override
        public int getImageId() {
            return R.drawable.ic_bomb;
        }

        @Override
        public int getDescription() {
            return R.string.description_bomb;
        }
    },
    HINT{
        @Override
        public int getPrice() {
            return 75;
        }

        @Override
        public String getType() {
            return "Hint";
        }

        @Override
        public void apply(Game game) {
            game.showHint();
        }

        @Override
        public String getName(){return "Hint";}

        @Override
        public int getPosition(){return 4;}

        @Override
        public int getImageId() {
            return R.drawable.ic_hint;
        }

        @Override
        public int getDescription() {
            return R.string.description_hint;
        }
    },
    ADDTIME{
        @Override
        public void apply(Game game) {
            game.addTime();
        }

        @Override
        public int getPrice() {
            return 100;
        }

        @Override
        public String getType() {
            return "+ 5 dec";
        }

        @Override
        public String getName(){return "AddTime";}

        @Override
        public int getPosition(){return 3;}

        @Override
        public int getImageId() {
            return R.drawable.ic_addtime;
        }

        @Override
        public int getDescription() {
            return R.string.description_add_time;
        }
    },
    SKIP{
        @Override
        public int getPrice() {
            return 100;
        }

        @Override
        public String getType() {
            return "Skip";
        }

        @Override
        public void apply(Game game) {
            game.newQuestion();
        }

        @Override
        public String getName(){return "Skip";}

        @Override
        public int getPosition(){return 2;}

        @Override
        public int getDescription() {
            return R.string.description_skip;
        }

        @Override
        public int getImageId() {
            return R.drawable.ic_skip;
        }
    };

    public abstract int getPrice();

    public abstract String getType();

    public abstract int getImageId();

    public abstract String getName();

    public abstract void apply(Game game);

    public abstract int getPosition();

    public abstract int getDescription();

}
