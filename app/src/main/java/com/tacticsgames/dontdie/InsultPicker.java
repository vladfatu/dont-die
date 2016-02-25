package com.tacticsgames.dontdie;

import java.util.Random;

/**
 * Created by vladfatu on 25/02/2016.
 */
public class InsultPicker {

    private Random random;

    public InsultPicker() {
        random = new Random();
    }

    public int pickInsult(int gameCount) {
        switch (gameCount) {
            case 1: {
                return R.string.game_1;
            }
            case 2: {
                return R.string.game_2;
            }
            case 3: {
                return R.string.game_3;
            }
            case 4: {
                return R.string.game_4;
            }
            default: {
                return getRandomInsult();
            }
        }
    }

    private int getRandomInsult() {
        int insult = random.nextInt(5);
        switch (insult) {
            case 0: {
                return R.string.insult_0;
            }
            case 1: {
                return R.string.insult_1;
            }
            case 2: {
                return R.string.insult_2;
            }
            case 3: {
                return R.string.insult_3;
            }
            case 4:default: {
                return R.string.insult_4;
            }
        }
    }

}
