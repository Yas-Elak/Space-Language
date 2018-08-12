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

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;

public class PlayState extends State{

    protected Stage myStage = new Stage(new ScreenViewport());

    //Mostl used in updtate, I prefere protected because I don't want to have the same
    //var in all my game when One is enough
    protected int counterOfGoodAnswer = 0;
    //counterBtnArray to count the number of time I resized the font (see the method adaptTextSize)
    private int[] counterBtnArray = {0,0,0,0,0};

    protected List<TextButton> textButtons = new ArrayList<TextButton>();
    private Random rand = new Random();

    //The different skin for my buttons and label and co
    private Skin textButtonSkin0, textButtonSkin1,textButtonSkin2,textButtonSkin3,textButtonSkinScore;
    private Skin textAnswerButtonSkin;
    //I need several skincreation because if I had only one I could't change the color of only
    //one button, so it is a skin creation by object
    protected SkinCreation skinCreation0;
    private SkinCreation skinCreation1, skinCreation2, skinCreation3, skinCreationAnswer;

    //my label who goes at top left of screen to count the good answer and his counter
    protected Label intGoodAnswer;

    private TextButton WordToTranslateButton;
    protected TextButton scoreFinal, quit, startAgain;
    protected ProgressBar progressBar;

    //I will put the words in these arrays,the list are used to take the words of the array randomly
    protected List<List<String>> listOfLists;
    protected List<Integer> listOfRandomisedOrderForButton, listOfId;

    protected Table table, tableEnd;
    protected Group group;

    //langueA is the langage we need to translate, langueB is the langagein the answers
    protected int counterForListOfLists;
    private int langueA;
    private int langueB;

    private String vocabularyPath;

    //to kown if the answer is good, if the user answered, if the buttons are clickable and if
    //the user shooted
    protected boolean isYourAnswerGood;
    protected boolean answered;
    protected boolean canIclick;
    protected boolean setFinalScore;

    protected Preferences prefsScore;
    protected Preferences prefsUrl;

    Texture tableBackground = new Texture("spritespaceshipgame/bkg/tablebackground.png");

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

        // initialise everything except the script
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
        initializeTextButtons();
        progressBar = skinCreation0.progressBar();
        generateTable();

        Gdx.input.setInputProcessor(myStage);
        myStage.addActor(intGoodAnswer);
        System.out.println("prrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        myStage.addActor(group);

    }
    // the update method do all the calculation for the render method
    @Override
    public void update(float dt) {
    //just need to call it because it's abstract in state
    //We play a lot with the sprite so all happen in the game class
    }
    @Override
    public void render(SpriteBatch sb) {

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
    protected void initializeTextButtons(){
        //Create the button and put the skin on it;
        WordToTranslateButton = new TextButton("Answer Button", textAnswerButtonSkin, "white");
        textButtons.add(new TextButton("Text Button", textButtonSkin0,"default"));
        textButtons.add(new TextButton("Text Button", textButtonSkin1,"default"));
        textButtons.add(new TextButton("Text Button", textButtonSkin2,"default"));
        textButtons.add(new TextButton("Text Button", textButtonSkin3,"default"));

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
                (Gdx.graphics.getHeight()-((Consts.bkgSize+Consts.atmoSize))));
        group.setWidth(Gdx.graphics.getWidth());
        group.setHeight(Gdx.graphics.getHeight());

        //I start my table and put all the button and label I need in it, nothin complicate
        //then I add the table to the group, I do the second table who will containt the score
        //and a word for when the player has finished to play
        table = new Table();
        table.setSize(Gdx.graphics.getWidth(),
                (Gdx.graphics.getHeight()-((Consts.bkgSize+Consts.atmoSize))));
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
        tableEnd.setSize(Gdx.graphics.getWidth(), (Gdx.graphics.getHeight()-
                ((Consts.bkgSize+Consts.atmoSize))));
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
                dispose();
                ThemeState themeState =  new ThemeState(gsm, false);
                gsm.set(themeState);
                themeState.init();
                return true;
            }
        });

    } //Commented
    protected void randomisedTheButtonText(int x){
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
    protected void correctionAnswerInGreen(){
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
