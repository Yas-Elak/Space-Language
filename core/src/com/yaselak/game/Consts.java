package com.yaselak.game;

import com.badlogic.gdx.Gdx;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Yassine on 04-03-18.
 */

public class Consts {

    //font size
    public static final int fontSizeBtn = (int) ((Gdx.graphics.getHeight()*0.04));
    public static final int fontSizeStudies = (int) ((Gdx.graphics.getHeight()*0.03));
    public static final int fontSizeAnswer = (int) ((Gdx.graphics.getHeight()*0.04));

    public static final float p02h = (float) (Gdx.graphics.getHeight()*0.02);

    public static final float p05h = (float) (Gdx.graphics.getHeight()*0.05);
    public static final float p08h = (float) (Gdx.graphics.getHeight()*0.08);

    public static final float p10h = (float) (Gdx.graphics.getHeight()*0.1);
    public static final float p15h = (float) (Gdx.graphics.getHeight()*0.15);

    public static final float p20h = (float) (Gdx.graphics.getHeight()*0.2);
    public static final float p30h = (float) (Gdx.graphics.getHeight()*0.3);
    public static final float p40h = (float) (Gdx.graphics.getHeight()*0.4);
    public static final float p50h = (float) (Gdx.graphics.getHeight()*0.5);
    public static final float p60h = (float) (Gdx.graphics.getHeight()*0.6);

    public static final float p01w = (float) (Gdx.graphics.getWidth()*0.01);

    public static final float p02w = (float) (Gdx.graphics.getWidth()*0.02);
    public static final float p03w = (float) (Gdx.graphics.getWidth()*0.03);

    public static final float p05w = (float) (Gdx.graphics.getWidth()*0.05);
    public static final float p11w = (float) (Gdx.graphics.getWidth()*0.11);

    public static final float p10w = (float) (Gdx.graphics.getWidth()*0.10);
    public static final float p12w = (float) (Gdx.graphics.getWidth()*0.12);

    public static final float p15w = (float) (Gdx.graphics.getWidth()*0.15);
    public static final float p20w = (float) (Gdx.graphics.getWidth()*0.20);
    public static final float p23w = (float) (Gdx.graphics.getWidth()*0.23);

    public static final float p50w = (float) (Gdx.graphics.getWidth()*0.5);
    public static final float p60w = (float) (Gdx.graphics.getWidth()*0.6);
    public static final float p70w = (float) (Gdx.graphics.getWidth()*0.7);

    public static final float p80w = (float) (Gdx.graphics.getWidth()*0.8);

    public static final float bkgSize = (float) (Gdx.graphics.getHeight()*0.25);
    public static final int atmoSize = 34;

    public static final float screenWidth = (Gdx.graphics.getWidth());

    public static final List<String> keyPrefsList = Arrays.asList
            ("spaceship1Bought", "monster1Bought", "spaceship2Bought", "monster2Bought",
                    "spaceship3Bought", "monster3Bought", "spaceship4Bought", "monster4Bought",
                    "spaceship5Bought", "monster5Bought", "spaceship6Bought", "monster6Bought",
                    "spaceship7Bought", "monster7Bought", "spaceship8Bought", "monster8Bought",
                    "spaceship9Bought", "monster9Bought", "spaceship10Bought", "monster10Bought");

    public static final List<String> urlPrefsList = Arrays.asList(
            "spaceship1", "monster1","spaceship2", "monster2","spaceship3", "monster3",
            "spaceship4", "monster4","spaceship5", "monster5","spaceship6", "monster6",
            "spaceship7", "monster7","spaceship8", "monster8","spaceship9", "monster9",
            "spaceship10", "monster10");

    //OUT OF SCREEN ENEMY POSITION
    //in play state this var change to 150 if the user is wrong then go back to 50

}
