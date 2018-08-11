package com.yaselak.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yaselak.game.Consts;
import com.yaselak.game.addtogdx.AnimatedActor;
import com.yaselak.game.addtogdx.SkinCreation;

public class MenuState extends State {

    private Stage myStage;

    private TextureAtlas rocketAtlas;
    private Texture background;

    private Skin textButtonSkin;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        myStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(myStage);
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    @Override
    public void init(){
        Gdx.input.setCatchBackKey(true);

        background = new Texture("spacebackgroundmenu.jpg");

        //The logo animation,  Need the object AnimatedActor because I want to put it in a table
        // and you can't put an animation in a table because it's not usually an actor
        rocketAtlas = new TextureAtlas(Gdx.files.internal("animationaccueil/spaceshipanimation/spaceshipanimation.pack"));
        AnimatedActor animatedActor = new AnimatedActor();
        Animation<TextureRegion> animation = new Animation<TextureRegion>(1 / 30f, rocketAtlas.getRegions());
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animatedActor.setAnimation(animation);
        //end of the logo animation

        //I use my SkinCreation object to create a skin for my buttons
        SkinCreation skinCreation = new SkinCreation();
        textButtonSkin = skinCreation.CreateSkin("fonts/Montserrat-Light.ttf",false, Consts.fontSizeBtn);

        TextButton playBtn = new TextButton("Etudier", textButtonSkin);
        TextButton survivalBtn = new TextButton("Jouer", textButtonSkin);
        TextButton campagnBtn = new TextButton("Magasin", textButtonSkin);
        TextButton otherLanguageBtn = new TextButton("+ de languages", textButtonSkin);

        //TODO add listener for the other state
        listenerButtonToStartThemeState(playBtn, true);
        listenerButtonToStartThemeState(survivalBtn, false);
        listenerButtonToStartShopState(campagnBtn);


        Table menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.setPosition(0, 0);
        menuTable.top();
        menuTable.setSize(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight()));
        menuTable.add(animatedActor).width((float) (Gdx.graphics.getWidth()*0.8)).height((float) (Gdx.graphics.getWidth()*0.8)).expandY();
        menuTable.row();
        menuTable.add(playBtn).width((float) (Gdx.graphics.getWidth()*0.8)).height((float) (Gdx.graphics.getHeight()*0.08)).expandY();
        menuTable.row();
        menuTable.add(survivalBtn).width((float) (Gdx.graphics.getWidth()*0.8)).height((float) (Gdx.graphics.getHeight()*0.08)).expandY();
        menuTable.row();
        menuTable.add(campagnBtn).width((float) (Gdx.graphics.getWidth()*0.8)).height((float) (Gdx.graphics.getHeight()*0.08)).expandY();
        menuTable.row();
        //menuTable.add(otherLanguageBtn).width((float) (Gdx.graphics.getWidth()*0.8)).height((float) (Gdx.graphics.getHeight()*0.08)).expandY();

        myStage.addActor(menuTable);
        System.out.println(playBtn.getHeight());
        Preferences prefsUrl = Gdx.app.getPreferences("MyPrefsUrl");
        Preferences prefsBought = Gdx.app.getPreferences("MyPrefsBought");
        Preferences prefsScore = Gdx.app.getPreferences("MyPrefsScore");

       //  prefsBought.clear();        prefsScore.clear();        prefsUrl.clear();

        if (!prefsScore.contains("firstTime")) {

            System.out.println("I put prefs+++++++++++++++++++++++++++");
            prefsUrl.putString("shipUrlActif", "spaceship1");
            prefsUrl.putString("monsterUrlActif", "monster1");
            prefsScore.putInteger("highScore", 200);
            prefsScore.putBoolean("soundOn", false);
            prefsScore.putBoolean("boughtTheGame", false);
            prefsScore.putBoolean("firstTime", false);
            prefsBought.putBoolean("spaceship1Bought", true);
            prefsBought.putBoolean("spaceship2Bought", false);
            prefsBought.putBoolean("spaceship3Bought", false);
            prefsBought.putBoolean("spaceship4Bought", false);
            prefsBought.putBoolean("spaceship5Bought", false);
            prefsBought.putBoolean("spaceship6Bought", false);
            prefsBought.putBoolean("spaceship7Bought", false);
            prefsBought.putBoolean("spaceship8Bought", false);
            prefsBought.putBoolean("spaceship9Bought", false);
            prefsBought.putBoolean("spaceship10Bought", false);
            prefsBought.putBoolean("monster1Bought", true);
            prefsBought.putBoolean("monster2Bought", false);
            prefsBought.putBoolean("monster3Bought", false);
            prefsBought.putBoolean("monster4Bought", false);
            prefsBought.putBoolean("monster5Bought", false);
            prefsBought.putBoolean("monster6Bought", false);
            prefsBought.putBoolean("monster7Bought", false);
            prefsBought.putBoolean("monster8Bought", false);
            prefsBought.putBoolean("monster9Bought", false);
            prefsBought.putBoolean("monster10Bought", false);
            prefsBought.flush();
            prefsScore.flush();
            prefsUrl.flush();

        } else System.out.println("I DONT PUT PREFS");

    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
           Gdx.app.exit();
        }
        sb.begin();
        sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb.end();
        myStage.act();
        myStage.draw();
    }
    @Override
    public void dispose() {
        background.dispose();
        rocketAtlas.dispose();
        textButtonSkin.dispose();
    }
    private void listenerButtonToStartThemeState(final TextButton myButton, final boolean study) {
        myButton.addListener(new InputListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                ThemeState themeState = new ThemeState(gsm, study);
                gsm.set(themeState);
                themeState.init();
                return true;
            }
        });
    }

    private void listenerButtonToStartShopState(final TextButton myButton) {
        myButton.addListener(new InputListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                ShopState shopState = new ShopState(gsm);
                gsm.set(shopState);
                shopState.init();
                return true;
            }
        });
    }




}
