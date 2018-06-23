package com.yaselak.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Yassine on 06-03-18.
 */

public class Explosion {

    private static final int FRAME_COLS = 7, FRAME_ROWS = 1;

    private Animation<TextureRegion> explostionAnimation;
    private Texture explosionSheet;
    private TextureRegion currentFrame;
    float stateTime;

    private int explosionWidth;
    private int explosionHeight;

    public Explosion(){
        explosionSheet = new Texture(Gdx.files.internal("fx2.png"));
        TextureRegion[][] tmp = TextureRegion.split(explosionSheet,
                explosionSheet.getWidth() / FRAME_COLS,
                explosionSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] explosionFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                explosionFrames[index++] = tmp[i][j];
            }
        }
        explostionAnimation = new Animation<TextureRegion>(0.09f, explosionFrames);
        stateTime = 0f;
        explosionWidth = explosionSheet.getWidth()/FRAME_COLS;
        explosionHeight = explosionSheet.getHeight()/FRAME_ROWS;

    }

    public void explose(boolean exploser){
        if (exploser) {
            stateTime += Gdx.graphics.getDeltaTime();
        }
        currentFrame = explostionAnimation.getKeyFrame(stateTime, true);
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Animation<TextureRegion> getExplostionAnimation() {
        return explostionAnimation;
    }

    public float getStateTime() {
        return stateTime;
    }

    public int getExplosionHeight() {
        return explosionHeight;
    }

    public int getExplosionWidth() {
        return explosionWidth;
    }
}