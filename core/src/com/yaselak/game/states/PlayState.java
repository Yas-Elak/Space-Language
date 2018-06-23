package com.yaselak.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
import com.yaselak.game.SpaceLanguage;
import com.yaselak.game.sprites.Ennemi;
import com.yaselak.game.sprites.Sprites;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Created by Yassine on 03-03-18.
 */

public class PlayState extends State{

    private Stage myStage = new Stage(new ScreenViewport());

    //The different skin for my buttons and label and co
    private Skin quantumHorizonSkin = new Skin(Gdx.files.internal("skin-quantum-horizon/quantum-horizon-ui.json"));
    private Skin cleanCrispySkin = new Skin(Gdx.files.internal("clean-crispy/clean-crispy-ui.json"));

    //my label who goes at top left of screen to count the good answer and his counter
    private Label intGoodAnswer = new Label("0", quantumHorizonSkin);
    private int counterOfGoodAnswer = 0;

    private boolean dialogEnding = false;
    private boolean startMenuState = false;
    private boolean triggerExplosionAnimation = false;

    //my sprite object
    private Sprites spaceShip, explosion, bullet;
    private Ennemi enemy;
    private Texture background, metalTiles, atmoshpere;
    private Vector2 atmoPos1,atmoPos2;
    //I will put the words in these arrays, the list are used to take the words of the array randomly
    private String[] arrayLangOnButton, arrayLangTranslate;
    private List<Integer> listOfRandomisedOrder, listOfRandomisedOrderForButton;
    private Random rand = new Random();

    private ProgressBar progressBar;
    private TextButton WordToTranslateButton; // should not be a button
    private List<TextButton> textButtons = new ArrayList<TextButton>();

    private Table table;

    private int counterForlistOfRandomisedOrder ,lastSpeedOfEnnemi;
    private float posEnnemiXAtCollide, posEnnemiYatCollide;
    private boolean isYourAnswerGood, answered, canIclick, shooted;


    private Dialog  endDialog;

    PlayState(GameStateManager gsm, String langOnButton, String LangTranslate){
        super(gsm);
        //the camera who zoom and focus on half my area
        cam.setToOrtho(false, SpaceLanguage.WIDTH / 2, SpaceLanguage.HEIGHT/2);

        arrayLangTranslate = (readTheFile(LangTranslate)); //need one list with word to translate
        arrayLangOnButton = (readTheFile(langOnButton)); // need one list with answer word
        randomisedArray();// need array with numbers for choosing the word and buttons
        initializeSprites();
        initializeTextButtons();
        initializeProgressBar();
        generateTable();

        Gdx.input.setInputProcessor(myStage);
        myStage.addActor(intGoodAnswer);
        myStage.addActor(table);

        dialogEndPlayState();


    }
    @Override
    protected void handleInput() {
        if (startMenuState) {
            gsm.set(new MenuState(gsm));
        }
    }
    // the update method do all the calculation for the render method
    @Override
    public void update(float dt) {
        //the update methode is called in the render of the main class, as the render of this class, so both
        //are on a infinite loop, I'll use the update method to position the different sprites
        // and make the follow the camera. I'll check too if the user answered to the question
        handleInput();
        if (!dialogEnding) {
            if (progressBar.getValue() > 0) {
                // I make sure the atmosphere scroll and I update the position of the sprites
                updateAtmosphere();
                spaceShip.updateMovement(dt, 0);
                bullet.updateMovement(dt, 0, shooted, 200);
                enemy.update(dt);
                //Here I'll change the buttons color if it's a good or bad answer, and the color will last until the enemy is back on the screen
                // if the user shoot or if the enemy is of screen, the user can't click on the buttons
                if (shooted || enemy.getPosEnnemi().x > spaceShip.getPosition().x + 240) {
                    canIclick = false;
                } else {
                    canIclick = true;
                    //as soon as he can click I'll reinitialisze the color of the buttons and the buttons
                    if (answered) {
                        answered = false;
                        for (TextButton tb: textButtons){
                            tb.setColor(0.219f, 0.552f, 0.960f, 1);
                        }
                        randomisedTheButtonText(listOfRandomisedOrder.get(counterForlistOfRandomisedOrder));
                    }
                }
                //what happens if the bullet collide with the enemy or if the spaceship collide with the enemy
                objectCollide();
            } else if (progressBar.getValue() == 0) {
                dialogEnding = true;
                endDialog.show(myStage);
            }
            //I set the cam on my ship, Like that the cam follow it and it doesns't seem to move
            cam.position.x = spaceShip.getPosition().x + 110;
            cam.update();
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);

        //if my explosion animation is finished, I need to hide the explosion
        //for this the frame "0" is blank and since the statetime is at 0, the sprite is hidden
        if (explosion.getSpriteAnimation().isAnimationFinished(explosion.getStateTime())){
            explosion.setStateTime(0);
            triggerExplosionAnimation = false;
        }
        // the boolean triggerExplosionAnimation is false, this method will do nothing
        explosion.AnimeMe(triggerExplosionAnimation);
        spaceShip.AnimeMe(true);
        bullet.AnimeMe(true);

        sb.begin();
        sb.draw(metalTiles, (cam.position.x - cam.viewportWidth/2), (0), SpaceLanguage.WIDTH/2, SpaceLanguage.HEIGHT/2);
        sb.draw(background, (cam.position.x - cam.viewportWidth/2), (SpaceLanguage.HEIGHT/2 - background.getHeight()));
        sb.draw(bullet.getCurrentFrame(),bullet.getPosition().x, bullet.getPosition().y);
        sb.draw(spaceShip.getCurrentFrame(), spaceShip.getPosition().x, spaceShip.getPosition().y);
        if (triggerExplosionAnimation) {
            //I can't put the x and y in a good way :(
            sb.draw(explosion.getCurrentFrame(), posEnnemiXAtCollide - (enemy.getEnnemiWidth() / 2) + (((explosion.getSpriteWidth() - enemy.getEnnemiWidth()) / 2)),
                    posEnnemiYatCollide - (enemy.getEnnemiHeight() / 2) + (explosion.getSpriteHeight() / 2));
        }
        sb.draw(enemy.getEnnemi(), enemy.getPosEnnemi().x , enemy.getPosEnnemi().y);
        sb.draw(atmoshpere, atmoPos1.x, atmoPos1.y);
        sb.draw(atmoshpere, atmoPos2.x, atmoPos2.y);
        sb.end();
        myStage.act();
        myStage.draw();
    }
    @Override
    public void dispose() {
    }

    //--------------------------------------THE METHODS/FUNCTIONS-----------------------------------
    private String[] readTheFile(String filePath)  {
        FileHandle file = Gdx.files.internal(filePath);
        String text = file.readString();
        return text.split("\\r?\\n");
    }
    private void randomisedArray(){
        // need list as long as the one with the words with randomized number to pick a word in first list
        //need a list of length 4 to choose the button who will contains the answers randomly
        listOfRandomisedOrder = new ArrayList<Integer>();
        listOfRandomisedOrderForButton = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));

        //I'll use a list because of the method shuffle for make the list random
        for (int i = 0; i < arrayLangTranslate.length; i++) {
            listOfRandomisedOrder.add(i);
        }
        Collections.shuffle(listOfRandomisedOrder);
        Collections.shuffle(listOfRandomisedOrderForButton);
        //then I need a counterForlistOfRandomisedOrder from 0 to arrayLangue.length, I'll use it for taking the number of listOfRandomisedOrder one by one
        counterForlistOfRandomisedOrder = 0;
    }
    private void initializeSprites(){
        // the sprite who doesn't seem to move on the screen
        background = new Texture("background2.jpg");
        metalTiles = new Texture("metaltiles.png");
        spaceShip = new Sprites(5, (SpaceLanguage.HEIGHT/2) - (background.getHeight()/2) - Consts.spaceShip1Height/2, 25, "spaceship1.png", 2,1, 0.09f);
        intGoodAnswer.setPosition(10,SpaceLanguage.HEIGHT-intGoodAnswer.getHeight()-10);

        //the sprites who seem to move on the screen
        bullet = new Sprites(spaceShip.getPosition().x + (spaceShip.getSpriteWidth()/2), spaceShip.getPosition().y + (spaceShip.getSpriteHeight())-5, 25, "bullet1.png",1,1,1 );
        enemy = new Ennemi(cam.position.x + cam.viewportWidth/2 +25,spaceShip.getPosition().y + (spaceShip.getSpriteHeight()/2));
        explosion = new Sprites("fx2.png", 7,1,0.09f);
        atmoshpere = new Texture("atmo1.png");

        //I'll need two atmosphere, as soon as the first one go out of the screen, it goes back behind the second one and is ready one more time to pass in front of the camera
        atmoPos1 = new Vector2((cam.position.x - cam.viewportWidth/2) -50, (cam.viewportHeight - background.getHeight()- atmoshpere.getHeight()));
        atmoPos2 = new Vector2((cam.position.x - cam.viewportWidth/2) -50 + atmoshpere.getWidth(),cam.viewportHeight - background.getHeight()- atmoshpere.getHeight());

    }
    private void initializeTextButtons(){
        WordToTranslateButton = new TextButton("test", cleanCrispySkin, "default");
        textButtons.add(new TextButton("Text Button", quantumHorizonSkin,"default"));
        textButtons.add(new TextButton("Text Button", quantumHorizonSkin,"default"));
        textButtons.add(new TextButton("Text Button", quantumHorizonSkin,"default"));
        textButtons.add(new TextButton("Text Button", quantumHorizonSkin,"default"));
        for (TextButton tb : textButtons){
            tb.setColor(0.219f, 0.552f, 0.960f, 1);
            listenerButton(tb);
            tb.getLabel().setFontScale(2,2);
        }
        randomisedTheButtonText(listOfRandomisedOrder.get(counterForlistOfRandomisedOrder));

    }
    private void initializeProgressBar(){
        progressBar = new ProgressBar(0, 5, 1, false, cleanCrispySkin);
        progressBar.setValue(5);
        progressBar.setAnimateDuration(2);
    }
    private void generateTable(){
        //I start my table and put all the button and label I need in it
        //tableRoot = new Table();
        //tableRoot.debug();
        //tableRoot.setFillParent(true);
        //tableRoot.padTop(SpaceDutch.HEIGHT/2 - background.getHeight())-atmoshpere.getHeight()));
        table = new Table();
        float ratioScreen = ((float)Gdx.graphics.getHeight()/(float)(SpaceLanguage.HEIGHT/2));
        table.setSize(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight()-((background.getHeight()+atmoshpere.getHeight())*ratioScreen)));
        table.setPosition(0,0);
        //table.padTop((background.getHeight()*(Gdx.graphics.getHeight() / (SpaceDutch.HEIGHT/2))) + (atmoshpere.getHeight()*(Gdx.graphics.getHeight() / (SpaceDutch.HEIGHT/2))));
        table.defaults().fillX().padBottom(10);
        table.top();

        table.add(progressBar).expandX().fill().padBottom(5);
        table.row();
        table.add(WordToTranslateButton).expand().fill();
        WordToTranslateButton.getLabel().setFontScale(3, 3);
        table.row();
        for (TextButton tb : textButtons) {
            table.add(tb).expand().fill();
            table.row();
        }
        table.debug();

    }
    private void dialogEndPlayState(){
        endDialog = new Dialog("Fin", cleanCrispySkin)
        {
            protected void result(Object object)
            {
                // todo Je dois mettre le score ici
                dialogEnding = false;
                startMenuState = true;
            }
        };
        endDialog.button("Quitter", 1);
    }
    private void updateAtmosphere(){
        if(cam.position.x - (cam.viewportWidth/2) > atmoPos1.x + atmoshpere.getWidth()){
            atmoPos1.add(atmoshpere.getWidth() *2 ,0);
        }
        if(cam.position.x - (cam.viewportWidth/2) > atmoPos2.x + atmoshpere.getWidth()){
            atmoPos2.add(atmoshpere.getWidth() *2 ,0);
        }
    }
    private void listenerButton(final TextButton myButton){
        myButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //here I need to check at which position is the label in the arrayLangTranslate and compare it
                //to the position of the word who is on the button I clicked in arrayLangOnButton
                //but I know the word on the label is at the position I choose on listOfRandomisedOrder
                // with answered = true, I'll know the user clicked and like that in the update,
                // i'll change the color of the button clicked and the color of the button with the good answer
                // I need this boolean because I have 2 buttons to change colors, if only one I could do it
                //in the listener
                //at the moment I click on the button I get the last speed of the enemy,
                //if the answer si wrong, the speed of the enemy will jump a lot but with this var
                //it will be possible to put it back at a normal state
                lastSpeedOfEnnemi = enemy.getMovement();
                if (canIclick) {
                    answered = true;
                    // i can't compare the word to know if the answer is good, I need compare the number I'm using in the list of randomisedOrder
                    // with the position of the word in is list
                    if (listOfRandomisedOrder.get(counterForlistOfRandomisedOrder) == getArrayIndex(arrayLangOnButton, String.valueOf(myButton.getText()))) {
                        isYourAnswerGood = true;
                        myButton.setColor(0.290f, 0.827f, 0.305f, 1);
                        Collections.shuffle(listOfRandomisedOrderForButton);
                    } else {
                        isYourAnswerGood = false;
                        myButton.setColor(0.960f, 0.219f, 0.219f, 1);
                        correctionAnswerInGreen();
                    }
                    shooted = true;
                    Collections.shuffle(listOfRandomisedOrderForButton);
                }
                    return true;
                }
        });

    }
    private void randomisedTheButtonText(int x){
        //I need to initialise the text on my button, so I need a method who will
        //set the text of the word to find (in the label) and put the good answer on one button randomly
        // and 3 different wrong answers on the other buttons, for that I need the first number I'll find
        //in the list of randomised integers and after three other number of the listOfRandomisedOrder for the wrong answer button
        //I set the label with the word the user need to translate
        WordToTranslateButton.setText(arrayLangTranslate[x]);
        //I need 3 DIFFERENT other number who will not be the same as x
        int wrongAnswerA = getRandomWithExclusion(arrayLangOnButton.length, x);
        int wrongAnswerB = getRandomWithExclusion(arrayLangOnButton.length, x,wrongAnswerA);
        int wrongAnswerC = getRandomWithExclusion(arrayLangOnButton.length, x,wrongAnswerA,wrongAnswerB);
        //Then I use a switch, the first one put the good answer in one of the other button
        //listOfRandomisedOrderForButton is where it come in the game, if it's equal to [2,1,0,3]
        // ths firs button to be change will be the number 2, then the 0, then the 0,...
        mySwitchButton(x, 0);
        mySwitchButton(wrongAnswerA, 1);
        mySwitchButton(wrongAnswerB, 2);
        mySwitchButton(wrongAnswerC, 3);
    }
    private int getRandomWithExclusion(int end, int... exclude) {
        int random = rand.nextInt(end);
        for (int i = 0; i < exclude.length; i++){
            if (random == exclude[i]){
                i = 0;
                random = rand.nextInt(end);
            }
        }
        return random;
    }
    private void mySwitchButton(int x, int y){
        switch (listOfRandomisedOrderForButton.get(y)){
            case 0: textButtons.get(0).setText(arrayLangOnButton[x]);break;
            case 1: textButtons.get(1).setText(arrayLangOnButton[x]);break;
            case 2: textButtons.get(2).setText(arrayLangOnButton[x]);break;
            case 3: textButtons.get(3).setText(arrayLangOnButton[x]);break;
        }
    }
    private int getArrayIndex(String[] arr, String value) {
        //To know the position of a string in an array string by comparing the string with the other string in the array
        int k=0;
        for(int i=0;i<arr.length;i++){
            if(arr[i].equals(value)){
                k=i;
                break;
            }
        }
        return k;
    }
    private void objectCollide(){
        final int MAXIMUM_ENNEMI_SPEED = -1000;
        //if I get a collision...         //... and this collision is with the spaceship
        if(enemy.collides(spaceShip.getBounds())){
            //if the user is wrong the enemy will accelarate very fast and collide with the spaceship,
            //so I need to put back again de movement of the enemy at a normal state
            //I put the good answer in green, considered the user answered to do the code (if answered)
            //in the update methode and put the enemy far away of screen to give the user the time
            //to see the good answer. Like everithing is reinitialized as soon as the enemy come back
            //in the screen, if I don't put the enemy far away the user doesn't have the time
            //if the enemy collide with the spaceship, the user didn't click on any button,
            //so I need a method to find the good button
            if (enemy.getMovement() > MAXIMUM_ENNEMI_SPEED){
                correctionAnswerInGreen();
            }
            if (enemy.getMovement() == MAXIMUM_ENNEMI_SPEED){
                enemy.setMovement(lastSpeedOfEnnemi);
            }
            isYourAnswerGood = false;
            answered = true;
            Consts.enemyOutOfScreen = 150;
            //this boolean will trigger the explosion in the render method
            triggerExplosionAnimation = true;
            progressBar.setValue(progressBar.getValue()-1);
            everythingReadyForNextQuestion();
        }
        if(enemy.collides(bullet.getBounds())){
            if (isYourAnswerGood){
                triggerExplosionAnimation = true;
                everythingReadyForNextQuestion();
            }else {
                enemy.setMovement(
                        MAXIMUM_ENNEMI_SPEED);
                //TODO initialise an animation of shield on the enemy
            }
        }
    }
    private void everythingReadyForNextQuestion(){
        // I need the enemy position at the moment the enemy is touched by the bullet
        //like that I know where to put the explosion
        posEnnemiXAtCollide = enemy.getPosEnnemi().x;
        posEnnemiYatCollide = enemy.getPosEnnemi().y;
        //Since the enemy is touched, I can put it back outside the screen for the next question, as the bullet
        //the ennemConsts.enemyOutOfScreeniOutOfScreen is how far the enemy is gone off screen
        enemy.reposition(cam.position.x + cam.viewportWidth/2 +Consts.enemyOutOfScreen,spaceShip.getPosition().y + (spaceShip.getSpriteHeight()/2));
        //reinitialise th Consts.enemyOutOfScreen for the next question
        Consts.enemyOutOfScreen = 50;
        bullet.reposition(spaceShip.getPosition().x + (spaceShip.getSpriteWidth()/2), spaceShip.getPosition().y + (spaceShip.getSpriteHeight())-5);
        shooted = false;
        //the counterForlistOfRandomisedOrder increases and take the next number in listOfRandomisedOrder
        counterForlistOfRandomisedOrder++;
        //TODO
        //if the bullet collide with the enemy BUT the answer is good, the enemy must not be destroyed
        // but we need a animation of shiel who block the bullet
        //TODO***********************************************************
        if (counterForlistOfRandomisedOrder == arrayLangOnButton.length){
            counterForlistOfRandomisedOrder = 0;
            Collections.shuffle(listOfRandomisedOrder);
        }
        //******************************************
        //TODO*************************************************
        if (isYourAnswerGood && answered){
            counterOfGoodAnswer++;
            enemy.setMovement(enemy.getMovement() - 3);
            intGoodAnswer.setText(String.valueOf(counterOfGoodAnswer));
        }
    }
    private void correctionAnswerInGreen(){
        switch (listOfRandomisedOrderForButton.get(0)) {
            case 0:
                textButtons.get(0).setColor(0.290f, 0.827f, 0.305f, 1);
                break;
            case 1:
                textButtons.get(1).setColor(0.290f, 0.827f, 0.305f, 1);
                break;
            case 2:
                textButtons.get(2).setColor(0.290f, 0.827f, 0.305f, 1);
                break;
            case 3:
                textButtons.get(3).setColor(0.290f, 0.827f, 0.305f, 1);
                break;
        }

    }

}
