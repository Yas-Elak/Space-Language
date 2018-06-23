package com.yaselak.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Yassine on 08-03-18.
 */

public class Sprites {

    private int movement;
    private int frameCols, frameRows;
    private String frameLocation;
    private float frameDuration;

    private Animation<TextureRegion> spriteAnimation;
    private Texture spriteSheet;
    private TextureRegion currentFrame;
    private Vector2 position;
    private float stateTime;

    private Rectangle bounds;

    private int spriteWidth;
    private int spriteHeight;


    //for the sprite who doesn't move and are Animated like the ennemi
    public Sprites(String frameLocation, int frameCols, int frameRows, float frameDuration){
        this.frameLocation = frameLocation;
        this.frameCols = frameCols;
        this.frameRows = frameRows;
        this.frameDuration = frameDuration;
        GenerateSprite();
        bounds = new Rectangle(0, 0, 0, 0);
    }
    // for the sprites who moves and are animated, the ship move even if doesn't seems to
    public Sprites(float x, float y, int movement, String frameLocation, int frameCols, int frameRows, float frameDuration){
        this.movement = movement;
        this.frameLocation = frameLocation;
        this.frameCols = frameCols;
        this.frameRows = frameRows;
        this.frameDuration = frameDuration;
        position = new Vector2(x, y);
        GenerateSprite();
        bounds = new Rectangle(position.x, position.y, spriteWidth, spriteHeight);
    }

    public void updateMovement(float dtx, float dty){
        position.add(movement * dtx,dty);
        bounds.setPosition(position.x, position.y);
    }
    //surcharge de méthode pour la balle
    public void updateMovement(float dtx, float dty, boolean shooted, int acceleration){
        if (!shooted)
            position.add(movement * dtx,dty);
        else
            position.add(acceleration * dtx,dty);
        bounds.setPosition(position.x, position.y);
    }
    public void AnimeMe(boolean loop){
        if (loop) {
            stateTime += Gdx.graphics.getDeltaTime();
        }
        currentFrame = spriteAnimation.getKeyFrame(stateTime, true);
    }
    public void GenerateSprite(){
        spriteSheet = new Texture(Gdx.files.internal(frameLocation));
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth() / frameCols,
                spriteSheet.getHeight() / frameRows);
        TextureRegion[] spriteFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                spriteFrames[index++] = tmp[i][j];
            }
        }
        spriteAnimation = new Animation<TextureRegion>(frameDuration, spriteFrames);
        stateTime = 0f;
        spriteWidth = spriteSheet.getWidth()/ frameCols;
        spriteHeight = spriteSheet.getHeight()/ frameCols;

    }
    public Vector2 getPosition() {
        return position;
    }
    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }
    public int getSpriteWidth() {
        return spriteWidth;
    }
    public int getSpriteHeight() {
        return spriteHeight;
    }
    public Animation<TextureRegion> getSpriteAnimation() {
        return spriteAnimation;
    }
    public float getStateTime() {
        return stateTime;
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }
    public void reposition(float x, float y){
        position.set(x, y);
    }
}
