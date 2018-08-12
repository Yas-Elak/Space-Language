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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yaselak.game.Consts;
import com.yaselak.game.addtogdx.SkinCreation;

public class ThemeState extends State {

    private Stage myStage;

    private Texture background;

    private Table themeTable1, themeTable2;
    private TextButton changeLangage;
    private TextButton changeLangage2;

    private boolean  study;
    private boolean pageOne = true;
    private int langageOne, langageTwo;
    private Group group;

    private Skin textButtonSkin, labelSkin;

    ThemeState(GameStateManager gsm, boolean study) {
        super(gsm);
        myStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(myStage);
        this.study = study;
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    @Override
    public void  init(){
        background = new Texture("spacebackgroundmenu.jpg");
        langageOne = 1;
        langageTwo = 2;
        SkinCreation skinCreation = new SkinCreation();
        textButtonSkin = skinCreation.CreateSkin("fonts/Montserrat-Light.ttf", false, Consts.fontSizeBtn);
        labelSkin = skinCreation.CreateSkin("fonts/Origami.ttf", false, Consts.fontSizeStudies*2);


        Label label = new Label("CATEGORIES", labelSkin, "label");
        Label label2 = new Label("CATEGORIES", labelSkin, "label");

        Image iconPrevious = new Image(new Texture("origamicat/origamiprevious.png"));
        Image iconPrevious2 = new Image(new Texture("origamicat/origamiprevious.png"));


        Image iconFood = new Image(new Texture("origamicat/origamiapple.png"));
        Image iconFox = new Image(new Texture("origamicat/origamifox.png"));
        Image iconCarot = new Image(new Texture("origamicat/origamicarot.png"));
        Image iconBody = new Image(new Texture("origamicat/origamibody.png"));
        Image iconCloud = new Image(new Texture("origamicat/origamicloud.png"));
        Image iconColor = new Image(new Texture("origamicat/origamicolor.png"));
        Image iconFamily = new Image(new Texture("origamicat/origamifamily.png"));
        Image iconOnigiri = new Image(new Texture("origamicat/origamionigiri.png"));
        Image iconPlant = new Image(new Texture("origamicat/origamiplant.png"));
        Image iconTree = new Image(new Texture("origamicat/origamitree.png"));
        Image iconClock = new Image(new Texture("origamicat/origamiclock.png"));
        Image iconVerb = new Image(new Texture("origamicat/origamiverb.png"));

        TextButton humanBody = new TextButton("Corps", textButtonSkin, "default");
        TextButton nature = new TextButton("Nature", textButtonSkin, "default");
        TextButton animals = new TextButton("Animaux", textButtonSkin, "default");
        TextButton timeAndDate = new TextButton("Temps", textButtonSkin, "default");
        TextButton family = new TextButton("Famille", textButtonSkin, "default");
        TextButton food = new TextButton("nourriture", textButtonSkin, "default");
        TextButton vegetables = new TextButton("Légumes", textButtonSkin, "default");
        TextButton fruit = new TextButton("Fruit", textButtonSkin, "default");
        TextButton job = new TextButton("Métiers", textButtonSkin, "default");
        TextButton color = new TextButton("Couleurs", textButtonSkin, "default");
        TextButton climat = new TextButton("Climat", textButtonSkin, "default");
        TextButton plant = new TextButton("Plantes", textButtonSkin, "default");
        TextButton verb = new TextButton("Verbes", textButtonSkin, "default");

        TextButton changeCategorie = new TextButton("Suivant", textButtonSkin, "grey");
        TextButton changeCategorie2 = new TextButton("Précédent", textButtonSkin, "grey");
        changeLangage = new TextButton("FR > ES", textButtonSkin, "grey");
        changeLangage2 = new TextButton("FR > ES", textButtonSkin, "grey");

        group = new Group();
        group.setPosition(0,0);
        group.setWidth(Gdx.graphics.getWidth());
        group.setHeight(Gdx.graphics.getHeight());

        themeTable1 = new Table();
        themeTable1.setPosition(0,0);
        themeTable1.setFillParent(true);
        themeTable1.setSize(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight()));
        themeTable1.top();

        float iconSize = Consts.p08h;

        themeTable1.add(label).colspan(10).expand();
        themeTable1.row();

        themeTable1.add(changeLangage).width(Consts.p80w).height(iconSize).colspan(10).expand();
        themeTable1.row();

        themeTable1.add(iconBody).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable1.add(humanBody).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable1.row();

        themeTable1.add(iconClock).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable1.add(timeAndDate).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable1.row();

        themeTable1.add(iconFox).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable1.add(animals).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable1.row();

        themeTable1.add(iconVerb).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable1.add(verb).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable1.row();

        themeTable1.add(iconFamily).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable1.add(family).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable1.row();

        themeTable1.add(iconPrevious).width(iconSize).height(iconSize).colspan(4).expand();
        themeTable1.add(changeCategorie).width(Consts.p50w).height(iconSize).colspan(6).expand();

        //themeTable1.debug();
        themeTable2 = new Table();
        themeTable2.setPosition(0,0);
        themeTable2.setFillParent(true);
        themeTable2.setSize(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight()));
        themeTable2.top();

        themeTable2.add(label2).colspan(10).expand();
        themeTable2.row();

        themeTable2.add(changeLangage2).width(Consts.p80w).height(iconSize).colspan(10).expand();
        themeTable2.row();

        themeTable2.add(iconOnigiri).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable2.add(food).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable2.row();

        themeTable2.add(iconTree).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable2.add(nature).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable2.row();

        themeTable2.add(iconCloud).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable2.add(climat).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable2.row();

        themeTable2.add(iconPlant).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable2.add(plant).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable2.row();

        themeTable2.add(iconColor).width(iconSize).height(iconSize).colspan(3).expand();
        themeTable2.add(color).width(Consts.p60w).height(iconSize).colspan(7).expand();
        themeTable2.row();

        themeTable2.add(iconPrevious2).width(iconSize).height(iconSize).colspan(4).expand();
        themeTable2.add(changeCategorie2).width(Consts.p50w).height(iconSize).colspan(6).expand();

        group.addActor(themeTable1);

        myStage.addActor(group);

        listenerButtonToStartPlayState(humanBody, "data/spanish/human_body.csv");
        listenerButtonToStartPlayState(nature, "data/spanish/nature.csv");
        listenerButtonToStartPlayState(animals, "data/spanish/animals.csv");
        listenerButtonToStartPlayState(timeAndDate, "data/spanish/periode.csv");
        listenerButtonToStartPlayState(family, "data/spanish/family.csv");
        listenerButtonToStartPlayState(climat, "data/spanish/climat.csv");
        listenerButtonToStartPlayState(color, "data/spanish/colors.csv");
        listenerButtonToStartPlayState(food, "data/spanish/food.csv");
        listenerButtonToStartPlayState(fruit, "data/spanish/fruits.csv");
        listenerButtonToStartPlayState(job, "data/spanish/job.csv");
        listenerButtonToStartPlayState(plant, "data/spanish/plants.csv");
        listenerButtonToStartPlayState(vegetables, "data/spanish/vegetable.csv");
        listenerButtonToStartPlayState(verb, "data/spanish/verbes.csv");

        listenerButtonToNext(changeCategorie);
        listenerButtonToNext(changeCategorie2);

        listenerButtonToChangeLanguage(changeLangage);
        listenerButtonToChangeLanguage(changeLangage2);
        listenerToGoBackWithImage(iconPrevious);
        listenerToGoBackWithImage(iconPrevious2);

    }

    private void listenerButtonToStartPlayState(final TextButton myButton,  final String pathToVocabulary) {
        myButton.addListener(new InputListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                if (study){
                    StudyState studyState = new StudyState(gsm, pathToVocabulary, langageOne, langageTwo);
                    gsm.set(studyState);
                    studyState.init();
                    System.out.println(langageOne +""+langageTwo);

                } else{
                    SpaceShipState spaceShipState =  new SpaceShipState(gsm, pathToVocabulary, langageOne, langageTwo);
                    gsm.set(spaceShipState);
                    spaceShipState.init();
                }

                return true;
            }
        });
    }
    private void listenerButtonToChangeLanguage(final TextButton myButton) {
        myButton.addListener(new InputListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (langageOne == 1) {
                    changeLangage.setText("ES > FR");
                    changeLangage2.setText("ES > FR");
                    langageOne = 2;
                    langageTwo = 1;
                    System.out.println(langageOne +""+langageTwo);


                } else {
                    changeLangage.setText("FR > ES");
                    changeLangage2.setText("FR > ES");
                    langageOne = 1;
                    langageTwo = 2;
                    System.out.println(langageOne +""+langageTwo);

                }

                return true;
            }
        });
    }

    private void listenerButtonToNext(final TextButton myButton) {
        myButton.addListener(new InputListener() {

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (pageOne){
                    group.removeActor(themeTable1);
                    group.addActor(themeTable2);
                    pageOne = false;
                } else {
                    group.removeActor(themeTable2);
                    group.addActor(themeTable1);
                    pageOne = true;
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
                MenuState menuState = new MenuState(gsm);
                gsm.set(menuState);
                menuState.init();
            }
        });
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
        labelSkin.dispose();

    }
}
