package com.yaselak.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Sprites {

    private float movement;
    private String packLocation;
    private float frameDuration;

    private Animation spriteAnimation;
    private Vector2 position;

    private Texture onlyTexture;
    private Rectangle bounds;

    private float spriteOriginalWidth,spriteOriginalHeight, spriteDesiredWidth, spriteDesiredHeight;

    //three constructor
    // one for the sprites animated
    public Sprites(float x, float y, float movement, String packLocation,
                   float frameDuration, float pourcentSize) {
        this.movement = movement;
        position = new Vector2(x, y);
        this.packLocation = packLocation;
        this.frameDuration = frameDuration;
        GenerateSpriteAtlas();
        setSpriteSize(pourcentSize);
        bounds = new Rectangle(0, 0, 0, 0);
    }
    // One for the sprite who are not animated (OnlyTexture) and needs bounds
    public Sprites(float x, float y, float movement, String textureLocation, float pourcentSize) {
        this(x,y,movement,textureLocation);
        setSpriteSize(pourcentSize);
        bounds = new Rectangle(0, 0, 0, 0);
    }
    //One for the sprite who are not animated (OnlyTexture) and doesn't need bounds
    public Sprites(float x, float y, float movement, String textureLocation) {
        this.movement = movement;
        position = new Vector2(x, y);
        onlyTexture = new Texture(Gdx.files.internal(textureLocation));
        this.spriteOriginalWidth = onlyTexture.getWidth();
        this.spriteOriginalHeight = onlyTexture.getHeight();
    }

    //Generate the animation for the first constructor
    public void GenerateSpriteAtlas() {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal(packLocation));
        this.spriteOriginalWidth = textureAtlas.getRegions().get(1).originalWidth;
        this.spriteOriginalHeight = textureAtlas.getRegions().get(1).originalHeight;
        spriteAnimation = new Animation(frameDuration, textureAtlas.getRegions());
    }

    public void setBoundsSize(float x, float y, float width, float height) {
        bounds.setPosition(x,y);
        bounds.setWidth(width);
        bounds.setHeight(height);
    }

    public void updateMovement(float dtx, float dty) {
        position.add(movement * dtx, dty);
        if (bounds != null) bounds.setPosition(position.x, position.y);
    }

    //OverWrite for the bullet who change the movement
    public void updateMovement(float dtx, float dty, boolean shooted, float acceleration) {
        if (!shooted) position.add(movement * dtx, dty);
        else position.add(acceleration * dtx, dty);
        bounds.setPosition(position.x, position.y);
    }

    public void reposition(float x, float y) {
        position.set(x, y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(bounds);
    }

    public void hideBullet() {
        this.onlyTexture = new Texture("spritespaceshipgame/bullethidden.png");
    }
    public void unHideBullet(){
        this.onlyTexture = new Texture("spritespaceshipgame/bullet.png");
    }

    public void setSpriteSize(float constante) {
        //original height / original width) x new width = new height
            this.spriteDesiredWidth = constante;
            this.spriteDesiredHeight = (spriteOriginalHeight / spriteOriginalWidth) * spriteDesiredWidth;
    }
    //----------------------------Getter and setter-------------------------------------------------
    public Animation<TextureRegion> getSpriteAnimation() {
        return spriteAnimation;
    }

    public float getSpriteDesiredWidth() {
        return spriteDesiredWidth;
    }
    public float getSpriteDesiredHeight() {
        return spriteDesiredHeight;
    }

    public void setSpriteOriginalWidth(int spriteOriginalWidth) {
        this.spriteOriginalWidth = spriteOriginalWidth;
    }
    public float getSpriteOriginalWidth() {
        return spriteOriginalWidth;
    }

    public void setSpriteOriginalHeight(int spriteOriginalHeight) {
        this.spriteOriginalHeight = spriteOriginalHeight;
    }
    public float getSpriteOriginalHeight() {
        return spriteOriginalHeight;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Texture getOnlyTexture() {
        return onlyTexture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getMovement() {
        return movement;
    }
    public void setMovement(float movement) {
        this.movement = movement;
    }

    public void dispose(){
        if (onlyTexture!= null) onlyTexture.dispose();
    }
}

