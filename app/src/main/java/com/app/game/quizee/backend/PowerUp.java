package com.app.game.quizee.backend;
import com.app.game.quizee.R;

/**
 * Enumeration of the different power-ups objects and their attributes
 */

public enum PowerUp {

    BOMB{
        @Override
        public void apply(Game game) {
            game.deleteTwoIncorrectAnswers();
        }

        @Override
        public int getPrice() {
            return 75;
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

        @Override
        public int getColorRessouce() {
            return R.color.bombButton;
        }
    },
    HINT{
        @Override
        public int getPrice() {
            return 40;
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

        @Override
        public int getColorRessouce() {
            return R.color.hintButton;
        }
    },
    ADDTIME{
        @Override
        public void apply(Game game) {
            game.addTime();
        }

        @Override
        public int getPrice() {
            return 50;
        }

        @Override
        public String getType() {
            return "+ 10 dec";
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

        @Override
        public int getColorRessouce() {
            return R.color.timeButton;
        }
    },
    SKIP{
        @Override
        public int getPrice() {
            return 50;
        }

        @Override
        public String getType() {
            return "Skip";
        }

        @Override
        public void apply(Game game) {
            game.skipQuestion();
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

        @Override
        public int getColorRessouce() {
            return R.color.skipButton;
        }
    };

    /**
     * Used to implement Game interface
     */
    public abstract void apply(Game game);

    /**
     * Getters
     */
    public abstract int getPrice();
    public abstract String getType();
    public abstract int getImageId();
    public abstract String getName();
    public abstract int getPosition();
    public abstract int getDescription();
    public abstract int getColorRessouce();



}
