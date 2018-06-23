package com.yaselak.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yaselak.game.SpaceLanguage;

/**
 * Created by Yassine on 20-06-18.
 */

public class TestState extends State {

    private Stage myStage;
    private TextureAtlas rocketAtlas;
    private Animation animation;
    private float timePassed = 0;
    private Texture background;



    public TestState(GameStateManager gsm) {
        super(gsm);
        myStage = new Stage(new ScreenViewport());
        cam.setToOrtho(false, SpaceLanguage.WIDTH / 2, SpaceLanguage.HEIGHT / 2);

        background = new Texture("123.jpg");

        Gdx.input.setInputProcessor(myStage);
        rocketAtlas = new TextureAtlas(Gdx.files.internal("rocketanimation/accueilanim.atlas"));
        animation = new Animation(1/30f, rocketAtlas.getRegions());
    }


        @Override
    public void handleInput() {

        }



    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0);
        //sb.draw(currentFrame, 50, 50); // Draw current frame at (50, 50)
        timePassed += Gdx.graphics.getDeltaTime();
        sb.draw((TextureRegion) animation.getKeyFrame(timePassed, true),0,0, 200,200);
        sb.end();
         myStage.act();
        myStage.draw();
    }



    public void dispose(){

    }
}
