package com.yaselak.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yaselak.game.Consts;
import com.yaselak.game.addtogdx.SkinCreation;

import java.util.ArrayList;
import java.util.List;


public class ShopState extends State {

    private Stage myStage;

    private Texture background;
    private Label scoreLabel;

    private Skin textButtonSkin;
    private List<TextButton> textButtonList;

    private List<Image> imageList;
    private List<Label> labelList;

    private Preferences prefsBought, prefsScore,prefsUrl;

    private int price, score, counterList;

    public ShopState(GameStateManager gsm) {
        super(gsm);
        myStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(myStage);
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    @Override
    public void init(){

        background = new Texture("spacebackgroundmenu.jpg");
        prefsBought = Gdx.app.getPreferences("MyPrefsBought");
        prefsScore = Gdx.app.getPreferences("MyPrefsScore");
        prefsUrl = Gdx.app.getPreferences("MyPrefsUrl");

        price = 100;
        score = prefsScore.getInteger("highScore");
        Image iconPrevious = new Image(new Texture("origamicat/origamiprevious.png"));

        SkinCreation skinCreation = new SkinCreation();
        Skin labelSkin = skinCreation.CreateSkin("fonts/Origami.ttf",
                false, Consts.fontSizeStudies*2);

        textButtonSkin = skinCreation.CreateSkin("fonts/Montserrat-Light.ttf",
                false, Consts.fontSizeBtn);

        counterList = 0;
        imageList = new ArrayList<Image>();
        textButtonList = new ArrayList<TextButton>();

        for (String pref : Consts.keyPrefsList ) {
            getBought(pref, Consts.urlPrefsList.get(counterList));
            counterList++;
        }
        //loop to change get the item who are active the first time
        for (TextButton tx: textButtonList){
            if (prefsUrl.getString("shipUrlActif").equals(Consts.urlPrefsList.get(textButtonList.indexOf(tx))) ||
            prefsUrl.getString("monsterUrlActif").equals(Consts.urlPrefsList.get(textButtonList.indexOf(tx)))) {
                tx.setText("Actif");
            }
        }

        Table listTable = new Table();

        labelList = new ArrayList<Label>();
        counterList = 0;
        for (Image image : imageList){
            listTable.add(image).width(Consts.p10w).height(Consts.p10w).pad(Consts.p02h);
            listTable.add(textButtonList.get(counterList)).height(Consts.p10w).width(Consts.p70w).row();
            listenerButton(textButtonList.get(counterList));
            counterList++;
        }

        listenerToGoBackWithImage(iconPrevious);

        Label title = new Label("Skins", labelSkin, "label");
        scoreLabel = new Label("Score", labelSkin, "label");
        scoreLabel.setText("Score : " + Integer.toString(score));

        final ScrollPane scroller = new ScrollPane(listTable);
        final Table table = new Table();
        table.setFillParent(true);
        table.add(title).colspan(10).expand().row();
        table.add(scroller).fill().expand().colspan(10).row();
        table.add(iconPrevious).width(Consts.p08h).height(Consts.p08h).colspan(2).expand().pad(Consts.p02h);
        table.add(scoreLabel).colspan(8).expand().pad(Consts.p02h);
        myStage.addActor(table);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            MenuState menuState = new MenuState(gsm);
            gsm.set(menuState);
            menuState.init();
        }

        sb.begin();
        sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        sb.end();
        myStage.act();
        myStage.draw();

    }

    @Override
    public void dispose() {
        myStage.dispose();
        background.dispose();
        textButtonSkin.dispose();
    }

    public void getBought(String myKey, String spaceShipOrMonsterUrl){
        /*This method check if the item is already bought or not. If it's bought then the texture
        * need to be different and also the text on the button. I use Image because we cannot
        * put directly texture in tables
        * the price is the same, I start to add 100 very time to the price, but is way to much*/
        Image image;
        TextButton textButton = new TextButton("", textButtonSkin, "default");
        if (!prefsBought.getBoolean(myKey)){
            if (counterList%2 == 0){
            image = new Image(new Texture("spritespaceshipgame/resxx/shadowspaceship.png"));
            } else image = new Image(new Texture("spritespaceshipgame/resxx/shadowmonster.png"));
            textButton.setText(String.valueOf(price));
        } else {
            image = new Image(new Texture("spritespaceshipgame/resxx/"
                    + spaceShipOrMonsterUrl+".png"));
            textButton.setText("Equiper");
        }
        imageList.add(image);
        textButtonList.add(textButton);
    }

    private void listenerButton(final TextButton myButton) {
        myButton.addListener(new ClickListener() {
            /*the is a clicklistener because the inputlistener I used conflict with the scrollpane
            * when I click I check if it's already bought or not, if not, I update the pref,
            * change the texture and the button text
            * If it's already bought you can equip it. So you need to save it in the pref and change
            * the button text for the item who is not equipped anymore*/
            @Override
            public void clicked (InputEvent event, float x, float y) {
                try{                    //TODO Check if you can buy it with your score
                    //if yes then buy it if no then dialog with no enough money
                    // then retire l'argent
                    if (Integer.parseInt(myButton.getText().toString()) <= score){
                        prefsScore.putInteger("highScore",
                                score - Integer.parseInt(myButton.getText().toString()));
                        prefsBought.putBoolean(Consts.keyPrefsList.get(textButtonList.indexOf(myButton)), true);
                        prefsScore.flush();
                        prefsBought.flush();
                        score = prefsScore.getInteger("highScore");
                        imageList.get(textButtonList.indexOf(myButton)).setDrawable(
                                new TextureRegionDrawable(new TextureRegion(
                                new Texture("spritespaceshipgame/resxx/"+
                                        Consts.urlPrefsList.get(textButtonList.indexOf(myButton))+".png"))));
                        myButton.setText("Equiper");
                        scoreLabel.setText("Score : " + Integer.toString(score));
                    } else {
                        //TODO dialog qui dit tu epeux pas acheter
                        System.out.println("can't buy");
                    }
                } catch (NumberFormatException e){
                    if(textButtonList.indexOf(myButton)%2 == 0) {
                        prefsUrl.putString("shipUrlActif", Consts.urlPrefsList.get(textButtonList.indexOf(myButton)));
                    } else {
                        prefsUrl.putString("monsterUrlActif", Consts.urlPrefsList.get(textButtonList.indexOf(myButton)));
                    }
                    for (TextButton tx: textButtonList){
                        if (tx.getText().toString().equals("Actif")) tx.setText("Equiper");
                    }
                    prefsUrl.flush();
                    textButtonList.get(Consts.urlPrefsList.indexOf(prefsUrl.getString("shipUrlActif"))).setText("Actif");
                    textButtonList.get(Consts.urlPrefsList.indexOf(prefsUrl.getString("monsterUrlActif"))).setText("Actif");
                }
            }
        });
    }

    private void listenerToGoBackWithImage(Image image){
        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                MenuState menuState = new MenuState(gsm);
                gsm.set(menuState);
                menuState.init();
            }
        });
    }

}
