package com.yaselak.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Yassine on 03-03-18.
 * Super Class State
 */

public abstract class State {
    //All of my state will need a Camera, it's here
    protected OrthographicCamera cam;
    //Vector3 is x, y and z coordinate
    protected Vector3 mouse;
    //GameStateManager is the next Class I'll do who will Manage the different state
    protected GameStateManager gsm;

    public State(GameStateManager gsm){
        this.gsm = gsm;
        cam = new OrthographicCamera();
        mouse = new Vector3();
    }

    public abstract void init();
    //an update will need de deltatime (dt), it's the time difference
    //between a frame rendered and the next frame rendered
    public abstract void update(float dt);

    //Container for everything who will appear on the screen
    public abstract void render(SpriteBatch sb);

    public abstract void dispose();
}
