package com.yaselak.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yaselak.game.Consts;
import com.yaselak.game.DAL.WordProvider;
import com.yaselak.game.addtogdx.SkinCreation;
import com.yaselak.game.sprites.Sprites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayState extends State{

    private Stage myStage = new Stage(new ScreenViewport());

    private float timePassed = 0;
    private float counterExplosionDeltaTime =0;
    //the sped must be different for every screen, If I dot It by pixel, the speed will be faster
    //on small screen, so i do a speed with a ration of the screen.
    private float speedRatioBullet = Consts.p12w;
    private float speedRationBulletShooted = Consts.p50w;
    private float speedRatioSpaceship = Consts.p12w;
    //the space between the left side of the screen and the ship
    private float spaceBetweenShipNScreen = Consts.p12w;
    private float enemyOutOfScreen = Consts.p10w;

    private int counterOfGoodAnswer = 0;
    private int[] counterBtnArray = {0,0,0,0,0};
    //counterBtnArray to count the number of time I resized the font (see the method adaptTextSize)

    private boolean triggerExplosionAnimation = false;
    private boolean dialogEnding = false;

    private List<TextButton> textButtons = new ArrayList<TextButton>();
    private Random rand = new Random();

    //The different skin for my buttons and label and co
    private Skin textButtonSkin0, textButtonSkin1,textButtonSkin2,textButtonSkin3,textButtonSkinScore;
    private Skin textAnswerButtonSkin;
    //I need several skincreation because if I had only one I could't change the color of only
    //one button, so it is a skin creation by object
    private SkinCreation skinCreation0, skinCreation1, skinCreation2, skinCreation3, skinCreationAnswer;

    //my label who goes at top left of screen to count the good answer and his counter
    private Label intGoodAnswer;

    //my sprite object and textures
    private Sprites spaceShip, bkg1, bkg2, shipFire, bullet, explosion, enemy;
    private Texture tableBackground, atmoshpere;
    private Vector2 atmoPos1,atmoPos2;
    private TextButton WordToTranslateButton, scoreFinal, quit, startAgain;
    private ProgressBar progressBar;

    //I will put the words in these arrays,the list are used to take the words of the array randomly
    private List<List<String>> listOfLists;
    private List<Integer> listOfRandomisedOrderForButton, listOfId;

    private Table table, tableEnd;
    private Group group;

    //The position of the enemy always change, so I need to variable to get the enemy position
    // when it collide and give it to the explosion, If I don't do that, hte explosion will
    //follow the enemy out of screen with te reposition
    private float lastSpeedOfEnnemi, posEnnemiXAtCollide, posEnnemiYatCollide;

    //langueA is the langage we need to translate, langueB is the langagein the answers
    private int counterForListOfLists, langueA, langueB;

    private String vocabularyPath;

    //to kown if the answer is good, if the user answered, if the buttons are clickable and if
    //the user shooted
    private boolean isYourAnswerGood, answered, canIclick, shooted, setFinalScore;

    private Preferences prefsScore, prefsUrl;

    PlayState(GameStateManager gsm, String vocabularyPath, int langueA, int langueB){
        super(gsm);
        //the camera who zoom and focus on half my area
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //need this because I use it again outside of the constructor
        this.langueA = langueA;
        this.langueB = langueB;
        this.vocabularyPath = vocabularyPath;
    }
    @Override
    public void init(){
        prefsScore = Gdx.app.getPreferences("MyPrefsScore");
        prefsUrl = Gdx.app.getPreferences("MyPrefsUrl");

        setFinalScore = false;
        skinCreation0 = new SkinCreation();
        skinCreation1 = new SkinCreation();
        skinCreation2 = new SkinCreation();
        skinCreation3 = new SkinCreation();
        SkinCreation skinCreationScore = new SkinCreation();


        //I need all these skin because I want to be able to change the font size on Only one button
        //at the time. So One skin by button
        textButtonSkin0 = skinCreation0.CreateSkin("fonts/NotoSans-Light.ttf",
                false, Consts.fontSizeBtn);
        textButtonSkin1 = skinCreation1.CreateSkin("fonts/NotoSans-Light.ttf",
                false, Consts.fontSizeBtn);
        textButtonSkin2 = skinCreation2.CreateSkin("fonts/NotoSans-Light.ttf",
                false, Consts.fontSizeBtn);
        textButtonSkin3 = skinCreation3.CreateSkin("fonts/NotoSans-Light.ttf",
                false, Consts.fontSizeBtn);
        textButtonSkinScore = skinCreationScore.CreateSkin("fonts/NotoSans-Light.ttf",
                false, Consts.fontSizeBtn);

        skinCreationAnswer = new SkinCreation();
        textAnswerButtonSkin = skinCreationAnswer.CreateSkin("fonts/NotoSans-Light.ttf",
                true, Consts.fontSizeAnswer);
        intGoodAnswer = new Label("0", textButtonSkinScore
                , "label");
        listOfId = new ArrayList<Integer>();
        listOfLists = new ArrayList<List<String>>();
        WordProvider wordProvider = new WordProvider();

        listOfLists =  wordProvider.readTheFile(vocabularyPath, langueA,langueB);

        Collections.shuffle(listOfLists);
        randomisedArray();// need array with numbers for choosing the word and buttons
        initializeSprites();
        initializeTextButtons();
        progressBar = skinCreation0.progressBar();
        generateTable();

        Gdx.input.setInputProcessor(myStage);
        myStage.addActor(intGoodAnswer);
        myStage.addActor(group);

    }
    // the update method do all the calculation for the render method
    @Override
    public void update(float dt) {
        //the update method is called in the render of the main class, as the render of this class,
        //so both are on a infinite loop, I'll use the update method to position the different sprites
        // and make the follow the camera. I'll check too if the user answered to the question
        if (!dialogEnding) {
            if (progressBar.getValue() > 0) {
                // I make sure the atmosphere scroll and I update the position of the sprites
                updateAtmosphere();
                spaceShip.updateMovement(dt, 0);
                bkg1.updateMovement(dt, 0);
                bkg2.updateMovement(dt, 0);
                shipFire.updateMovement(dt,0);
                //update the movement of the background is way more complicated than the
                //movement of the rest, so It has is own method
                updateBkg();

                bullet.updateMovement(dt, 0, shooted, speedRationBulletShooted);
                enemy.updateMovement(dt,0);

                //Here I'll change the buttons color if it's a good or bad answer, and the color
                // will last until the enemy is back on the screen
                // if the user shoot or if the enemy is of screen, the user can't click
                // on the buttons
                if (shooted || enemy.getPosition().x > spaceShip.getPosition().x + Gdx.graphics.getWidth()) {
                    if (shooted) bullet.unHideBullet();
                    canIclick = false;
                } else {
                    canIclick = true;
                    //as soon as he can click I'll reinitialisze the color
                    // of the buttons and the buttons
                    if (answered) {
                        answered = false;
                        for (TextButton tb: textButtons){
                            tb.setStyle(skinCreation0.getTextButtonStyle());
                        }
                        randomisedTheButtonText(counterForListOfLists);
                    }
                }
                //what happens if the bullet collide with the enemy or if the spaceship
                // collide with the enemy
                objectCollide();
            } else if (progressBar.getValue() == 0) {
                if (!setFinalScore) {
                    scoreFinal.setText("Score : " + String.valueOf(counterOfGoodAnswer) + "\n" + "Vous pouvez acheter\ndes skins avec vos scores.\nIl vous en reste : " + Integer.toString(prefsScore.getInteger("highScore") + counterOfGoodAnswer));
                    prefsScore.putInteger("highScore", prefsScore.getInteger("highScore") + counterOfGoodAnswer);
                    prefsScore.flush();
                    setFinalScore = true;
                }
                group.removeActor(table);
                quit.setStyle(skinCreation0.getTextButtonStyle());
                startAgain.setStyle(skinCreation0.getTextButtonStyle());
                group.addActor(tableEnd);
            }
            //I set the cam on my ship, Like that the cam follow it and it doesns't seem to move.
            //the cam x y is at the center, so if I set the cam on my spaceship pposition only,
            // the spaceship will appear at the center
            cam.position.x = spaceShip.getPosition().x  + cam.viewportWidth/2 - spaceBetweenShipNScreen;
            cam.update();
        }
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
        timePassed += Gdx.graphics.getDeltaTime();
        sb.draw(tableBackground, (cam.position.x - cam.viewportWidth/2), (0), Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());

        sb.draw(bkg1.getOnlyTexture(),
                bkg1.getPosition().x, bkg1.getPosition().y, bkg1.getSpriteOriginalWidth(),
                bkg1.getSpriteOriginalHeight());
        sb.draw(bkg2.getOnlyTexture(),
                bkg2.getPosition().x, bkg2.getPosition().y, bkg2.getSpriteOriginalWidth(),
                bkg2.getSpriteOriginalHeight());

        sb.draw(bullet.getOnlyTexture(),bullet.getPosition().x, bullet.getPosition().y,
                bullet.getSpriteDesiredWidth(), bullet.getSpriteDesiredHeight());

        sb.draw(shipFire.getSpriteAnimation().getKeyFrame(timePassed,true),
                shipFire.getPosition().x, shipFire.getPosition().y,
                shipFire.getSpriteDesiredWidth(), shipFire.getSpriteDesiredHeight());

        sb.draw(spaceShip.getOnlyTexture(),
                spaceShip.getPosition().x, spaceShip.getPosition().y,
                spaceShip.getSpriteDesiredWidth(), spaceShip.getSpriteDesiredHeight());

        if (explosion.getSpriteAnimation().isAnimationFinished(counterExplosionDeltaTime)){
            triggerExplosionAnimation = false;
            counterExplosionDeltaTime = 0;
        }
        if (triggerExplosionAnimation) {
            counterExplosionDeltaTime += Gdx.graphics.getDeltaTime();
            sb.draw(explosion.getSpriteAnimation().getKeyFrame(counterExplosionDeltaTime,
                    true),
                    (posEnnemiXAtCollide) + enemy.getSpriteDesiredWidth()/2
                            - explosion.getSpriteDesiredWidth()/2
                    , (posEnnemiYatCollide) + enemy.getSpriteDesiredHeight()/2 -
                            explosion.getSpriteDesiredHeight()/2,
                    Consts.p08h, Consts.p08h);
        }

        sb.draw(enemy.getOnlyTexture(), enemy.getPosition().x , enemy.getPosition().y,
                enemy.getSpriteDesiredWidth(),enemy.getSpriteDesiredHeight());

        sb.draw(atmoshpere, atmoPos1.x, atmoPos1.y,Gdx.graphics.getWidth(),
                (Gdx.graphics.getWidth()/atmoshpere.getWidth())*atmoshpere.getHeight());
        sb.draw(atmoshpere, atmoPos2.x, atmoPos2.y,Gdx.graphics.getWidth(),
                (Gdx.graphics.getWidth()/atmoshpere.getWidth())*atmoshpere.getHeight());

        sb.end();
        myStage.act();
        myStage.draw();
    }
    @Override
    public void dispose() {
        myStage.dispose();
        textButtonSkin0.dispose();
        textButtonSkin1.dispose();
        textButtonSkin2.dispose();
        textButtonSkin3.dispose();
        textAnswerButtonSkin.dispose();
        textButtonSkinScore.dispose();
        spaceShip.dispose();
        bkg1.dispose();
        bkg2.dispose();
        bullet.dispose();
        explosion.dispose();
        enemy.dispose();
        atmoshpere.dispose();
        tableBackground.dispose();
    }

    //--------------------------------------THE METHODS/FUNCTIONS-----------------------------------
    private void randomisedArray(){
        //need a list of length 4 to choose the button who will contains the answers randomly
        listOfRandomisedOrderForButton = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
        Collections.shuffle(listOfRandomisedOrderForButton);
        //the counter will be used to iterate threw the list who is alread shuffled
        counterForListOfLists = 0;
    }//Commented

    private void initializeSprites(){
        // the sprite who doesn't seem to move on the screen
        //background = new Texture("spritespaceshipgame/spacebkghorizontal.jpg");
        tableBackground = new Texture("spritespaceshipgame/bkg/tablebackground.png");

        bkg1 = new Sprites(cam.position.x - cam.viewportWidth/2 - spaceBetweenShipNScreen,
                (Gdx.graphics.getHeight() - (Consts.bkgSize)),Consts.p11w,
                "spritespaceshipgame/bkg/bkgmauve.jpg");
        bkg1.setSpriteOriginalWidth(Gdx.graphics.getWidth());
        bkg1.setSpriteOriginalHeight((int) Consts.bkgSize);

        bkg2 = new Sprites(bkg1.getPosition().x + bkg1.getSpriteOriginalWidth(),
                (Gdx.graphics.getHeight() - (Consts.bkgSize)), Consts.p11w,
                "spritespaceshipgame/bkg/bkgmauve.jpg");
        bkg2.setSpriteOriginalWidth(Gdx.graphics.getWidth());
        bkg2.setSpriteOriginalHeight((int) Consts.bkgSize);

        //Whatever the Y of the spaceship initialization, I will reposition it right after.
        spaceShip = new Sprites(0, 0,
                speedRatioSpaceship,"spritespaceshipgame/"+getRes()+"/"+prefsUrl.getString("shipUrlActif")+".png",
                Consts.p23w);

        //You think I just position the spaceShip, why do I do a reposition right after ?
        //it's because when I initiate the object spaceship, I need the height of the spaceship to
        //position it. But I just have the texture height, not the height it will be on the screen
        //so I do a reposition right after with the good height
        spaceShip.reposition(0, ((Gdx.graphics.getHeight()) - ((Consts.bkgSize)/2) -
                (spaceShip.getSpriteDesiredHeight()/2)));
        spaceShip.setBoundsSize(spaceShip.getPosition().x,
                ((Gdx.graphics.getHeight()) - ((Consts.bkgSize)/2)
                        - (spaceShip.getSpriteDesiredHeight()/2)),
                spaceShip.getSpriteDesiredWidth(),
                spaceShip.getSpriteDesiredHeight());

        shipFire = new Sprites(0,0, speedRatioSpaceship,
                "spritespaceshipgame/fire/fire.pack",
                1/30f, Consts.p20w);
        shipFire.reposition(0-shipFire.getSpriteDesiredWidth(),
                ((Gdx.graphics.getHeight()) - ((Consts.bkgSize)/2) -
                        (shipFire.getSpriteDesiredHeight()/2)));

        intGoodAnswer.setPosition(Consts.p02w,
                Gdx.graphics.getHeight()-intGoodAnswer.getHeight()-Consts.p02w);

        bullet = new Sprites(0,0, speedRatioBullet,
                "spritespaceshipgame/bullet.png", Consts.p02h);
        bullet.reposition(spaceShip.getPosition().x +(Consts.p15h/2),spaceShip.getPosition().y
                + (spaceShip.getSpriteDesiredHeight()/2)-bullet.getSpriteDesiredHeight()/2);
        bullet.setBoundsSize(bullet.getPosition().x, bullet.getPosition().y,
                bullet.getSpriteDesiredWidth(), bullet.getSpriteDesiredHeight());
        bullet.hideBullet();

        enemy = new Sprites(0,0,0,"spritespaceshipgame/"+getRes()+"/"+prefsUrl.getString("monsterUrlActif")+".png",
                Consts.p20w);
        enemy.reposition(cam.position.x + cam.viewportWidth/2 +enemyOutOfScreen,
                ((Gdx.graphics.getHeight()) - ((Consts.bkgSize)/2) -
                        ((enemy.getSpriteDesiredHeight())/2)));
        enemy.setBoundsSize(enemy.getPosition().x, enemy.getPosition().y,
                enemy.getSpriteDesiredWidth(), enemy.getSpriteDesiredHeight());

        explosion = new Sprites(0,0,0,
                "spritespaceshipgame/explosion/explosion.pack",
                1/20f,Consts.p15w);

        //don't need the sprite class, a simple teture will do because, no animation and no bounds
        atmoshpere = new Texture("spritespaceshipgame/atmosphere.png");
        //I'll need two atmosphere, as soon as the first one go out of the screen, it goes back
        //behind the second one and is ready one more time to pass in front of the camera
        atmoPos1 = new Vector2((cam.position.x - cam.viewportWidth/2) -50,
                (Gdx.graphics.getHeight() - (Consts.bkgSize)) - atmoshpere.getHeight());
        atmoPos2 = new Vector2((cam.position.x - cam.viewportWidth/2) -50 + atmoshpere.getWidth(),
                (Gdx.graphics.getHeight() - (Consts.bkgSize)) - atmoshpere.getHeight());
    } //Commented
    private void initializeTextButtons(){
        //Create the button and put the skin on it;
        WordToTranslateButton = new TextButton("Answer Button", textAnswerButtonSkin, "white");
        textButtons.add(new TextButton("Text Button", textButtonSkin0,"default"));
        textButtons.add(new TextButton("Text Button", textButtonSkin1,"default"));
        textButtons.add(new TextButton("Text Button", textButtonSkin2,"default"));
        textButtons.add(new TextButton("Text Button", textButtonSkin3,"default"));
        for (TextButton tb : textButtons){
            listenerButton(tb);
        }
        randomisedTheButtonText(counterForListOfLists);

        quit = new TextButton("Quitter", textButtonSkin0, "default");
        startAgain = new TextButton("Rejouer", textButtonSkin0, "default");
        listenerButtonquit(quit);
        listenerButtonStartAgain(startAgain);
    } //Commented
    private void generateTable(){

        group = new Group();
        group.setPosition(0,0);
        group.setSize(Gdx.graphics.getWidth(),
                (Gdx.graphics.getHeight()-((Consts.bkgSize+atmoshpere.getHeight()))));
        group.setWidth(Gdx.graphics.getWidth());
        group.setHeight(Gdx.graphics.getHeight());

        //I start my table and put all the button and label I need in it, nothin complicate
        //then I add the table to the group, I do the second table who will containt the score
        //and a word for when the player has finished to play
        table = new Table();
        table.setSize(Gdx.graphics.getWidth(),
                (Gdx.graphics.getHeight()-((Consts.bkgSize+atmoshpere.getHeight()))));
        table.setPosition(0,0);
        table.defaults().fillX().padBottom(10).padTop(10).expandY();
        table.top();

        table.add(progressBar).width((int) (Gdx.graphics.getWidth()*0.8)).expandY();
        table.row();
        table.add(WordToTranslateButton).expand().fill().width((float) (Gdx.graphics.getWidth()*0.9))
                .height((float) (Gdx.graphics.getHeight()*0.15)).expandY();
        table.row();

        for (TextButton tb : textButtons) {
            table.add(tb).expand().fill().width((float) (Gdx.graphics.getWidth()*0.8))
                    .height((float) (Gdx.graphics.getHeight()*0.1)).expandY();
            table.row();
        }

        //second table
        scoreFinal = new TextButton("", textAnswerButtonSkin, "white");

        tableEnd = new Table();
        tableEnd.setSize(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight()-((Consts.bkgSize+atmoshpere.getHeight()))));
        tableEnd.setPosition(0,0);
        tableEnd.defaults().fillX().padBottom(10).padTop(10).expandY();
        tableEnd.top();

        tableEnd.add(scoreFinal).expand().fill().width((float) (Gdx.graphics.getWidth()*0.9))
                .height((float) (Gdx.graphics.getHeight()*0.30)).expandY().row();
        tableEnd.add(startAgain).expand().fill().width((float) (Gdx.graphics.getWidth()*0.8))
                .height((float) (Gdx.graphics.getHeight()*0.1)).expandY().row();
        tableEnd.add(quit).expand().fill().width((float) (Gdx.graphics.getWidth()*0.8))
                .height((float) (Gdx.graphics.getHeight()*0.1)).expandY();
        group.addActor(table);

    } //Commented

    private void updateAtmosphere(){
        /*I have two atmoshepere right next to each other, they pas the screen from left to right
        * and give the de impression that it's infinite because, when the first one get out
        * of the screen, it teleport at the other side of the screen and is ready to pass again*/
        if(cam.position.x - (cam.viewportWidth/2) > atmoPos1.x + atmoshpere.getWidth()){
            atmoPos1.add(atmoshpere.getWidth() *2 ,0);
        }
        if(cam.position.x - (cam.viewportWidth/2) > atmoPos2.x + atmoshpere.getWidth()){
            atmoPos2.add(atmoshpere.getWidth() *2 ,0);
        }
    } //Commented
    private void updateBkg(){
        //This method replace the backgroudn who is out of the screen next to the ther background
        //like that the backgrounds pass in front of the screen indefinitely
        if(cam.position.x - (cam.viewportWidth/2) >
                bkg1.getPosition().x + bkg1.getSpriteOriginalWidth()){
            bkg1.reposition(bkg2.getPosition().x + bkg2.getSpriteOriginalWidth(),
                    (Gdx.graphics.getHeight() - (Consts.bkgSize)));
        }
        if(cam.position.x - (cam.viewportWidth/2) >
                bkg2.getPosition().x + bkg2.getSpriteOriginalWidth()){
            bkg2.reposition(bkg1.getPosition().x + bkg1.getSpriteOriginalWidth(),
                    (Gdx.graphics.getHeight() - (Consts.bkgSize)));
        }
    } //commented
    private void listenerButton(final TextButton myButton){
        myButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                /*I need the speed of the enemy for later (is it normal or superfast ?
                * Then I have a CanIClick boolean who need to be true, like that the user can click
                * on the buttons only when I want to*/
                lastSpeedOfEnnemi = enemy.getMovement();
                if (canIclick) {
                    answered = true;
                    /*i can't compare the word to know if the answer is good, I need compare
                     when I click on the button I can know which button it is. Then I compare it
                    with the listOfId because I put the index of the good button in first place
                    with the help of the method mySwitchButton*/
                    if (textButtons.indexOf(myButton) == listOfId.get(0)){
                        isYourAnswerGood = true;
                        myButton.setStyle(skinCreation0.getTextButtonStyleGreen());
                        Collections.shuffle(listOfRandomisedOrderForButton);
                    } else {
                        isYourAnswerGood = false;
                        myButton.setStyle(skinCreation0.getTextButtonStyleRed());
                        correctionAnswerInGreen();
                    }
                    listOfId.clear();
                    shooted = true;
                    //I shuffle the list who help me to randomised the button
                    Collections.shuffle(listOfRandomisedOrderForButton);
                }
                return true;
            }
        });

    } //Commented
    private void listenerButtonquit(final TextButton myButton){
        myButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //quit the playstate and restart the menustae
                dispose();
                MenuState menuState = new MenuState(gsm);
                gsm.push(menuState);
                menuState.init();
                return true;
            }
        });

    } //Commented
    private void listenerButtonStartAgain(final TextButton myButton){
        myButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //quit the playstate and restart the menustae
                dispose();
                PlayState playState =  new PlayState(gsm, vocabularyPath, langueA, langueB);
                gsm.set(playState);
                playState.init();
                return true;
            }
        });

    } //Commented
    private void randomisedTheButtonText(int x){
        /*I need to initialise the text on my button, so I need a method who will
        set the text of the word to find (in the label) and put the good answer
        on one button randomly and 3 different wrong answers on the other buttons.
        For that I need the first number I'll find in the list of randomised integers and
        after three other number of the listOfRandomisedOrder for the wrong answer button
        I set the label with the word the user need to translate*/
        listOfId.clear();

        WordToTranslateButton.setText(String.valueOf(listOfLists.get(x).get(1)));
        counterBtnArray[4] = adaptTextSize(counterBtnArray[4], WordToTranslateButton, skinCreationAnswer);
        //I need 3 DIFFERENT other number who will not be the same as x
        int wrongAnswerA = getRandomWithExclusion(listOfLists.size(), x);
        int wrongAnswerB = getRandomWithExclusion(listOfLists.size(), x,wrongAnswerA);
        int wrongAnswerC = getRandomWithExclusion(listOfLists.size(), x,wrongAnswerA,wrongAnswerB);
        //Then I use a switch, the first one put the good answer in one of the other button
        //listOfRandomisedOrderForButton is where it come in the game, if it's equal to [2,1,0,3]
        // ths firs button to be change will be the number 2, then the 0, then the 0,...
        mySwitchButton(x, 0);
        mySwitchButton(wrongAnswerA, 1);
        mySwitchButton(wrongAnswerB, 2);
        mySwitchButton(wrongAnswerC, 3);

    }//Commented
    private int getRandomWithExclusion(int end, int... exclude) {
        /*I need a method to get a random number with an exclusion
        I got in the parameter : "end", it's the range and I got a list of number I don't want
        as my random number, this method help me to choose the wrong answers*/
        int random = rand.nextInt(end);
        for (int i = 0; i < exclude.length; i++){
            if (random == exclude[i]){
                //if my random number is in the exclude, so I start again the loop a the begining
                //and choose a new random number
                i = 0;
                random = rand.nextInt(end);
            }
        }
        return random;
    } //Commented
    private void mySwitchButton(int x, int y){
        /* This method help me to put the wright and wrong answers on my buttons randomly
        * I got a list of 4 random number between 0 and 3. So if the first number is 2, then the
        * the good answer will go on the third button. I also put the order of the answer in
        * a new List. The first number of this list will always be the good answer
        * this will help me to be sure the user had the good answer or not.
        * The while loop and the layout help me to calculate the width of the string
        * but I can't change it, so I put it in  sizeCounter and I compare it to the width of the button
        * While sizeCounter is bigger than the size of the button I decrease the font scale.
        * I have a counter for each button who tell me how many time I decreased the fontscale
        * Then I use this counter to increase the font again. Like that the text never go out of
        * the button
        * */

        switch (listOfRandomisedOrderForButton.get(y)){
            case 0: textButtons.get(0).setText(String.valueOf(listOfLists.get(x).get(2)));
                counterBtnArray[0] = adaptTextSize(counterBtnArray[0], textButtons.get(0), skinCreation0);
                listOfId.add(0);
                break;
            case 1: textButtons.get(1).setText(String.valueOf(listOfLists.get(x).get(2)));
                counterBtnArray[1] = adaptTextSize(counterBtnArray[1], textButtons.get(1), skinCreation1);
                listOfId.add(1);
                break;
            case 2: textButtons.get(2).setText(String.valueOf(listOfLists.get(x).get(2)));
                counterBtnArray[2] =  adaptTextSize(counterBtnArray[2], textButtons.get(2), skinCreation2);
                listOfId.add(2);
                break;
            case 3: textButtons.get(3).setText(String.valueOf(listOfLists.get(x).get(2)));
                counterBtnArray[3] =  adaptTextSize(counterBtnArray[3], textButtons.get(3), skinCreation3);
                listOfId.add(3);
                break;
        }

    }//Commented
    private int adaptTextSize(int counterBtn, TextButton textButton, SkinCreation sk){
        //the first time the method runs, it will alway be 0 si the while will not run
        //the next time the method runs, I augment thesize of the scale again if the size was
        //reduced before
        while (counterBtn > 0){
            textButton.getLabel().setFontScale((float) (textButton.
                    getLabel().getFontScaleX()+0.2), (float) (textButton.
                    getLabel().getFontScaleY()+0.2));
            counterBtn--;
        }
        //first I get the text of the button
        sk.setTextLayout((textButton.getText()));
        //Then with the getLayout method I get the Width of the text
        float sizeCounter = sk.getLayout().width;
        //while the text is bigger than the buttons
        while (sizeCounter > Gdx.graphics.getWidth()*0.8){
            //with the counterBtn i Count the number of times I reduced the text size
            counterBtn++;
            //I reduced the size of the font with the scale
            textButton.getLabel().setFontScale((float) (textButton.
                    getLabel().getFontScaleX()-0.2), (float) (textButton.
                    getLabel().getFontScaleY()-0.2));
            sizeCounter = (float) (sizeCounter - (sizeCounter *0.1));
        }
        //I return the number of time I reduced the font, It will be useful for the first while of
        //the method
        return counterBtn;
    }// Commented
    private void objectCollide(){

        /*for the speed, I need a ration, if I just put 50 for example, the enemy will be
        //slower on a big screen, the ration here is for when the enemy charged when the
        //user is wrong*/
        int speedRatioEnemyCharged = (int) (Gdx.graphics.getWidth()/0.2);
        //if I get a collision with the spaceship
        if(enemy.collides(spaceShip.getBounds())){
            canIclick = false;
            /*if the user is wrong the enemy will accelarate very fast and collide with the
            spaceship, so I need to put back again de movement of the enemy at a normal state
            I put the good answer in green, I consider the user answered to do the code
            "(if answered)..."in the update method and put the enemy far away of screen to give
            the user the time to see the good answer. Like everything is reinitialized
            as soon as the enemy come back in the screen, if I don't put the enemy far away
            the user doesn't have the time to see the good answer

            if the enemy collide with the spaceship, the user didn't click on any button,
            so I need a method to find the good button, whit this "if" I can know if the enemy
            collide with the spaceship at normal speed (the user didn't answer or superspeed,
            the user choose a wrong answer*/
            System.out.println(enemy.getMovement() + " + " + speedRatioEnemyCharged);
            if (answered == false){
                correctionAnswerInGreen();
            }
            if (enemy.getMovement() == -speedRatioEnemyCharged){
                enemy.setMovement(lastSpeedOfEnnemi);
            }
            //if the enemy collide with the spaceship I know the answer is false
            isYourAnswerGood = false;
            //I consider the user answered and I need this boolean for continue the code in the
            //update method
            answered = true;
            //The user is wrong so the enemy go far away
            enemyOutOfScreen = Gdx.graphics.getWidth()/2;
            //this boolean will trigger the explosion in the render method
            triggerExplosionAnimation = true;
            progressBar.setValue(progressBar.getValue()-1);
            everythingReadyForNextQuestion();
        }
        //if the enemy collide with the bullet I need to make the enemy ultra fast if the answer is
        // wrong, Like that it will collide with the spaceship and do the code just upthere
        //If the answer is good I prepare for the next question
        else if(enemy.collides(bullet.getBounds())){
            if (isYourAnswerGood){
                triggerExplosionAnimation = true;
                everythingReadyForNextQuestion();
            }else {
                enemy.setMovement(-speedRatioEnemyCharged);
                //then the enemy collide with the spaceship and the if upthere will be trigered
            }
        }
    } //Commented
    private void everythingReadyForNextQuestion(){
        /*I need the enemy position at the moment the enemy is touched by the bullet
        like that I know where to put the explosion */
        posEnnemiXAtCollide = enemy.getPosition().x;
        posEnnemiYatCollide = enemy.getPosition().y;


        /*Since the enemy is touched, I can put it back outside the screen for the next question
        the enemyOutOfScreen is how far the enemy is gone off screen, 10% of the width
        if the answer is good and 50% if the answer is bad to let the user see the good answer*/
        enemy.reposition(cam.position.x + cam.viewportWidth/2 + enemyOutOfScreen,
                ((Gdx.graphics.getHeight()) - ((Consts.bkgSize)/2)
                        - (enemy.getSpriteDesiredHeight()/2)));

        //reinitialise enemyOutOfScreen for the next question
        enemyOutOfScreen = Consts.p10w;

        //We put the bullet behind the ship and hide it
        bullet.reposition(spaceShip.getPosition().x +(Consts.p15h/2),
                spaceShip.getPosition().y + (spaceShip.getSpriteDesiredHeight()/2)
                        - bullet.getSpriteDesiredHeight()/2);
        bullet.hideBullet();

        shooted = false;

        //the counterForListOfLists increases and take the next number in listOfRandomisedOrder
        counterForListOfLists++;

        //if the user answer once for all the word we reinitialize the counter and start again with
        //a new randomized order
        if (counterForListOfLists == listOfLists.size()){
            counterForListOfLists = 0;
            Collections.shuffle(listOfLists);
        }
        //if the answer is good the enemy speed is going up, the counter of good answer goes up too
        if (isYourAnswerGood && answered){
            counterOfGoodAnswer++;
            enemy.setMovement(enemy.getMovement() - Consts.p01w);
            intGoodAnswer.setText(String.valueOf(counterOfGoodAnswer));

        }



    } //Commented
    private void correctionAnswerInGreen(){
        /*The first number in the listOfRandomisedOrderForButton point always on the good answer,
        * So I have a switch to put button this number represent in green when the user give a bad
        * answer or when the user doesn't give any answer
        * */
        switch (listOfRandomisedOrderForButton.get(0)) {
            case 0:
                textButtons.get(0).setStyle(skinCreation0.getTextButtonStyleGreen());
                break;
            case 1:
                textButtons.get(1).setStyle(skinCreation1.getTextButtonStyleGreen());
                break;
            case 2:
                textButtons.get(2).setStyle(skinCreation2.getTextButtonStyleGreen());
                break;
            case 3:
                textButtons.get(3).setStyle(skinCreation3.getTextButtonStyleGreen());
                break;
        }

    } //Commented
    protected String getRes(){
        String path = "res";
        if (Consts.screenWidth <= 480) path = "res";
        else if (Consts.screenWidth <= 720 ) path = "resx";
        else if (Consts.screenWidth <= 1080 ) path = "resxx";
        else if (Consts.screenWidth > 1080 ) path = "resxxx";
        return path;
    }


}
