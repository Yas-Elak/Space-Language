package com.yaselak.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Yassine on 04-03-18.
 */

public class Bullet {
    private static final int MOVEMENT = 25;
    private static final int SHOOTED = 200;


    private Vector3 position;
    private Texture bullet;
    private Rectangle bounds;

    private int bulletHeight;
    private int bulletWidth;

    public Bullet(float x, float y){
        position = new Vector3(x, y, 0);
        bullet = new Texture("bullet1.png");
        bulletHeight = bullet.getHeight();
        bulletWidth = bullet.getWidth();
        bounds = new Rectangle(x,y,bullet.getWidth() / 3, bullet.getHeight());

    }

    public void update(float dt, boolean shooted){
        if (!shooted)
            position.add(MOVEMENT * dt,0 , 0);
        else
            position.add(SHOOTED * dt,0 , 0);
        bounds.setPosition(position.x, position.y);
    }

    public void reposition(float x, float y){
        position.set(x, y,0);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return bullet;
    }

    public static int getMOVEMENT() {
        return MOVEMENT;
    }

    public int getBulletHeight() {
        return bulletHeight;
    }

    public int getBulletWidth() {
        return bulletWidth;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
