package com.yaselak.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yaselak.game.SpaceLanguage;
import com.yaselak.game.addtogdx.AnimatedActor;


/**
 * Created by Yassine on 03-03-18.
 */

public class MenuState extends State {

    private Stage myStage;

    private String vocSensA, vocSensB;

    private Texture background;

    private Table menuTable, topTable, parentTable;

    private TextButton list1a, list1b, checkList1, D, F, autre;
    private ImageButton enveloppe, speaker;

    //test
    private TextureAtlas rocketAtlas;
    private AnimatedActor animatedActor;
    private Animation<TextureRegion> animation;
    private float timePassed = 0;
    ///test
    private boolean startplayState;


    public MenuState(GameStateManager gsm) {
        super(gsm);
        startplayState = false;
        myStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(myStage);


        //test
        rocketAtlas = new TextureAtlas(Gdx.files.internal("rocketanimation/accueilanim.atlas"));
        animatedActor = new AnimatedActor();
        animation = new Animation<TextureRegion>(1/30f, rocketAtlas.getRegions());
        animation.setPlayMode(Animation.PlayMode.LOOP);

        animatedActor.setAnimation(animation);
        ///test



        //by doing divided by 2, I'm zooming in the screen
        cam.setToOrtho(false, SpaceLanguage.WIDTH / 2, SpaceLanguage.HEIGHT / 2);

        background = new Texture("123.jpg");

       // Image animatedLogo = new Image(new Texture("logo.png"));
        //https://stackoverflow.com/questions/21499759/how-can-i-add-an-animation-in-a-table-layout-in-libgdx

        /*The Problem is : I can't use a ttf font AND keep the skin of a texButton
        I need two things, an empty skin and a textbuttonstyle (for up, font, hover, down, etc)
        First I generate the font, then I add to the skin the different state of the buttons
        then I create a textbuttonstyle and I said to it : use these button state and the font
        And finally I add the  textbutton style to the  skin
        */

        //Generate the empty skin
        Skin textButtonSkin = new Skin();
        //Generate the font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Montserrat-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) ((Gdx.graphics.getHeight()*0.08)*0.5);
        BitmapFont yourBitmapFont = generator.generateFont(parameter);
        yourBitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //add the background to the skin
        textButtonSkin.add("normalButton", new Texture("button_black_normal_blue.9.png"));
        textButtonSkin.add("pushedButton", new Texture("button_black_pushed_blue.9.png"));

        // create a new style for the text button
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = textButtonSkin.newDrawable("normalButton");
        textButtonStyle.down = textButtonSkin.newDrawable("pushedButton");
        textButtonStyle.font = yourBitmapFont;
        //add the style to the button, you can have several, just change the name "default"
        textButtonSkin.add("default", textButtonStyle);

        list1a = new TextButton("Jouer", textButtonSkin, "default");
        list1b = new TextButton("Campagnes",textButtonSkin, "default");
        checkList1 = new TextButton("Autres langues", textButtonSkin, "default");

        //The two buttons at the top of the screen
        enveloppe = new ImageButton(createImageButton("envelope.png"));
        speaker =  new ImageButton(createImageButton("speaker.png"));


        listenerButtonToStartPlayState(list1a, "vocfr.txt", "vocan.txt");
        listenerButtonToStartPlayState(list1b, "vocan.txt", "vocfr.txt");

        parentTable = new Table();
        menuTable = new Table();
        topTable = new Table();

        //parentTable.debug();
        parentTable.setPosition(0,0);
        parentTable.top();
        parentTable.setSize(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight()));
        parentTable.add(topTable);
        parentTable.row();
        parentTable.add(menuTable);

        topTable.setSize(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight()));
        topTable.setFillParent(true);
        topTable.top();
        topTable.add(enveloppe).expandX().width((float) (Gdx.graphics.getWidth()*0.15)).height((float) (Gdx.graphics.getWidth()*0.15)).left().padLeft(20).padTop(20);
        topTable.add(speaker).expandX().width((float) (Gdx.graphics.getWidth()*0.15)).height((float) (Gdx.graphics.getWidth()*0.15)).right().padRight(20).padTop(20);
       // topTable.setDebug(true);
        //teest
       // Image tiger2Image = new Image(new TextureRegion(rocketAtlas,));

        menuTable.setFillParent(true);
        menuTable.add(animatedActor).width((float) (Gdx.graphics.getWidth()*0.8)).height((float) (Gdx.graphics.getWidth()*0.8)).padTop(20);;
        menuTable.row();
        menuTable.add(list1a).width((float) (Gdx.graphics.getWidth()*0.8)).height((float) (Gdx.graphics.getHeight()*0.08)).padTop(80);
        menuTable.row();
        menuTable.add(list1b).width((float) (Gdx.graphics.getWidth()*0.8)).height((float) (Gdx.graphics.getHeight()*0.08)).padTop(40);
        menuTable.row();
        menuTable.add(checkList1).width((float) (Gdx.graphics.getWidth()*0.8)).height((float) (Gdx.graphics.getHeight()*0.08)).padTop(40);
        //menuTable.debug();

        myStage.addActor(parentTable);
        myStage.addActor(menuTable);
        myStage.addActor(topTable);
    }

    @Override
    public void handleInput() {
        if (startplayState) {
            gsm.set(new PlayState(gsm, vocSensA, vocSensB));
        }

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

        sb.end();
        myStage.act();
        myStage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
    }

    private TextureRegionDrawable createImageButton(String string){
        Texture myTexture = new Texture(Gdx.files.internal(string));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        return myTexRegionDrawable;
    }

    private void listenerButtonToStartPlayState(final TextButton myButton, final String listSensA, final String listSensB) {
        myButton.addListener(new InputListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.print("eeeeeeeeeeeeee");
                vocSensA = listSensA;
                vocSensB= listSensB;
                startplayState = true;
                return true;
            }
        });
    }

}
