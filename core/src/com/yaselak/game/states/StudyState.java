package com.yaselak.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yaselak.game.Consts;
import com.yaselak.game.DAL.WordProvider;
import com.yaselak.game.addtogdx.SkinCreation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yassine on 20-07-18.
 */

public class StudyState extends State{


    private Stage myStage;

    private Texture background;

    private TextButton hideLanguage;
    private java.util.List<java.util.List> listOfLists;

    private boolean hidden;

    String vocabularyPath;
    int languageOne, languageTwo;

    List<Label> labelList;
    public StudyState(GameStateManager gsm, String vocabularyPath, int languageOne, int languageTwo) {
        super(gsm);
        myStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(myStage);
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.vocabularyPath = vocabularyPath;
        this.languageOne = languageOne;
        this.languageTwo = languageTwo;

    }
    @Override
    public void init(){
        hidden = false;

        background = new Texture("spacebackgroundmenu.jpg");

        Image iconPrevious = new Image(new Texture("origamicat/origamiprevious.png"));

        SkinCreation skinCreation = new SkinCreation();
        Skin labelSkin = skinCreation.CreateSkin("fonts/NotoSans-Light.ttf",
                false, Consts.fontSizeStudies);
        Skin textButtonSkin = skinCreation.CreateSkin("fonts/Montserrat-Light.ttf",
                false, Consts.fontSizeBtn);



        WordProvider wordProvider = new WordProvider();
        listOfLists = wordProvider.readTheFile(vocabularyPath, languageOne, languageTwo);

        Table listTable = new Table();

        //listTable.debug();
        labelList = new ArrayList<Label>();
        hideLanguage = new TextButton("Cacher", textButtonSkin, "grey");

        for (java.util.List list : listOfLists ){

            // I go through listOfLists wo contains a list of list whith 3 string
            Image borderTable = new Image(new Texture("bordertable.png"));
            Label label1 = new Label(String.valueOf(list.get(1)), labelSkin, "label");
            //I add the firs word in a table
            listTable.add(label1).expandX().padTop(20).row();

            Label label2 = new Label(String.valueOf(list.get(2)), labelSkin, "label");
            //I add the second word in a new list of label to be able to hcnage ir
            // easily later AND in the table

            labelList.add(label2);

            listTable.add(label2).expandX().padTop(20).row();
            listTable.add(borderTable).width(Consts.p80w).height(2).padTop(20).row();
        }
        final ScrollPane scroller = new ScrollPane(listTable);
        final Table table = new Table();
        table.setFillParent(true);
        table.add(scroller).fill().expand().colspan(8).row();
        table.add(iconPrevious).width(Consts.p08h).height(Consts.p08h).colspan(1).pad(Consts.p02h);
        table.add(hideLanguage).width(Consts.p50w).height(Consts.p08h).colspan(7).pad(Consts.p02h);
        myStage.addActor(table);

        listenerButtonToHide(hideLanguage);
        listenerToGoBackWithImage(iconPrevious);

    }

    @Override
    public void update(float dt) {

    }



    private void listenerButtonToHide(final TextButton myButton) {
        myButton.addListener(new InputListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!hidden){
                    for (Label label : labelList){
                        label.setText(" ");
                    }
                    hidden = true;
                } else {
                    for (int i = 0; i < labelList.size(); i++){
                        labelList.get(i).setText(String.valueOf(listOfLists.get(i).get(2)));
                    }
                    hidden = false;
                }
                return true;
            }
        });
    }
    private void listenerToGoBackWithImage(Image image){
        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                ThemeState themeState = new ThemeState(gsm, true);
                gsm.set(themeState);
                themeState.init();
            }
        });
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

    }
}


