package com.yaselak.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Yassine on 04-03-18.
 */

public class Ennemi {
    public static final int ENNEMI_WIDTH = 23;

    private int movement = 0;

    private Texture ennemi;
    private Vector2 posEnnemi;
    private Rectangle bounds;

    private int ennemiWidth;
    private int ennemiHeight;

    public Ennemi(float x, float y){
        ennemi = new Texture("jelly.png");
        posEnnemi = new Vector2(x, y);
        bounds = new Rectangle(posEnnemi.x, posEnnemi.y, ennemi.getWidth(), ennemi.getHeight());
        ennemiWidth = ennemi.getWidth();
        ennemiHeight = ennemi.getHeight();

    }

    public boolean collides(Rectangle player){
        return player.overlaps(bounds);
    }
    public void update(float dt){
        posEnnemi.add(movement * dt,0);
        bounds.setPosition(posEnnemi.x+5, posEnnemi.y);
    }

    public void reposition(float x, float y){
        posEnnemi.set(x, y);
        bounds.setPosition(posEnnemi.x, posEnnemi.y);
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getMovement() {
        return movement;
    }

    public Texture getEnnemi() {
        return ennemi;
    }

    public Vector2 getPosEnnemi() {
        return posEnnemi;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getEnnemiWidth() {
        return ennemiWidth;
    }

    public int getEnnemiHeight() {
        return ennemiHeight;
    }
}
