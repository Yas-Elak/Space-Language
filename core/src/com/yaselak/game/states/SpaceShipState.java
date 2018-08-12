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

public class SpaceShipState extends PlayState {

    // need time passed for my only animation who is constantly running, the fire
    private float timePassed = 0;
    //this one is different for timePassed because it's animate only certain time
    private float counterExplosionDeltaTime = 0;

    //the sped must be different for every screen, If I dot It by pixel, the speed will be faster
    //on small screen, so i do a speed with a ration of the screen.
    private float speedRatioBullet = Consts.p12w;
    private float speedRationBulletShooted = Consts.p50w;
    private float speedRatioSpaceship = Consts.p12w;
    //the space between the left side of the screen and the ship
    private float spaceBetweenShipNScreen = Consts.p12w;
    private float enemyOutOfScreen = Consts.p10w;

    private boolean triggerExplosionAnimation = false;

    //my sprite object and textures
    private Sprites spaceShip, bkg1, bkg2, shipFire, bullet, explosion, enemy;
    private Vector2 atmoPos1, atmoPos2;

    //The position of the enemy always change, so I need to variable to get the enemy position
    // when it collide and give it to the explosion, If I don't do that, hte explosion will
    //follow the enemy out of screen with te reposition
    private float lastSpeedOfEnnemi, posEnnemiXAtCollide, posEnnemiYatCollide;

    public boolean shooted;

    Texture atmoshpere = new Texture("spritespaceshipgame/atmosphere.png");


    SpaceShipState(GameStateManager gsm, String vocabularyPath, int langueA, int langueB) {
        super(gsm, vocabularyPath, langueA, langueB);
    }

    @Override
    public void init() {
        super.init();
        //call init from PLayState then Initialise the sprite of this particular game
        initializeSprites();
    }

    // the update method do all the calculation for the render method
    @Override
    public void update(float dt) {
        //the update method is called in the render of the main class, as the render of this class,
        //so both are on a infinite loop, I'll use the update method to position the different sprites
        // and make the follow the camera. I'll check too if the user answered to the question
        if (canIclick) {
            /*I need the speed of the enemy for later (is it normal or superfast ?
                * Then I have a CanIClick boolean who need to be true, like that the user can click
                * on the buttons only when I want to*/
        }
        boolean dialogEnding = false;
        if (!dialogEnding) {
            if (progressBar.getValue() > 0) {
                // I make sure the atmosphere scroll and I update the position of the sprites
                updateAtmosphere();
                spaceShip.updateMovement(dt, 0);
                bkg1.updateMovement(dt, 0);
                bkg2.updateMovement(dt, 0);
                shipFire.updateMovement(dt, 0);
                //update the movement of the background is way more complicated than the
                //movement of the rest, so It has is own method
                updateBkg();

                bullet.updateMovement(dt, 0, shooted, speedRationBulletShooted);
                enemy.updateMovement(dt, 0);

                //Here I'll change the buttons color if it's a good or bad answer, and the color
                // will last until the enemy is back on the screen
                // if the user shoot or if the enemy is of screen, the user can't click
                // on the buttons
                if (shooted || enemy.getPosition().x > spaceShip.getPosition().x + Gdx.graphics.getWidth()) {
                    if (shooted) bullet.unHideBullet();
                    canIclick = false;
                    System.out.println("canIIIIIIIIIIIIIIClccddfmsdfsfdfs");
                } else {
                    canIclick = true;
                    //as soon as he can click I'll reinitialisze the color
                    // of the buttons and the buttons
                    if (answered) {
                        answered = false;
                        for (TextButton tb : textButtons) {
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
                    scoreFinal.setText("Score : " + String.valueOf(counterOfGoodAnswer) + "\n" +
                            "Vous pouvez acheter\ndes skins avec vos scores.\nIl vous en reste : "
                            + Integer.toString(prefsScore.getInteger("highScore")
                            + counterOfGoodAnswer));
                    prefsScore.putInteger("highScore", prefsScore.getInteger("highScore")
                            + counterOfGoodAnswer);
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
            cam.position.x = spaceShip.getPosition().x + cam.viewportWidth / 2 - spaceBetweenShipNScreen;
            cam.update();
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            dispose();
            MenuState menuState = new MenuState(gsm);
            gsm.set(menuState);
            menuState.init();
        }
        sb.begin();
        timePassed += Gdx.graphics.getDeltaTime();
        sb.draw(tableBackground, (cam.position.x - cam.viewportWidth / 2), (0), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        sb.draw(bkg1.getOnlyTexture(), bkg1.getPosition().x, bkg1.getPosition().y, bkg1.getSpriteOriginalWidth(), bkg1.getSpriteOriginalHeight());
        sb.draw(bkg2.getOnlyTexture(), bkg2.getPosition().x, bkg2.getPosition().y, bkg2.getSpriteOriginalWidth(), bkg2.getSpriteOriginalHeight());

        sb.draw(bullet.getOnlyTexture(), bullet.getPosition().x, bullet.getPosition().y, bullet.getSpriteDesiredWidth(), bullet.getSpriteDesiredHeight());

        sb.draw(shipFire.getSpriteAnimation().getKeyFrame(timePassed, true), shipFire.getPosition().x, shipFire.getPosition().y, shipFire.getSpriteDesiredWidth(), shipFire.getSpriteDesiredHeight());

        sb.draw(spaceShip.getOnlyTexture(), spaceShip.getPosition().x, spaceShip.getPosition().y, spaceShip.getSpriteDesiredWidth(), spaceShip.getSpriteDesiredHeight());

        if (explosion.getSpriteAnimation().isAnimationFinished(counterExplosionDeltaTime)) {
            triggerExplosionAnimation = false;
            counterExplosionDeltaTime = 0;
        }
        if (triggerExplosionAnimation) {
            counterExplosionDeltaTime += Gdx.graphics.getDeltaTime();
            sb.draw(explosion.getSpriteAnimation().getKeyFrame(counterExplosionDeltaTime, true), (posEnnemiXAtCollide) + enemy.getSpriteDesiredWidth() / 2 - explosion.getSpriteDesiredWidth() / 2, (posEnnemiYatCollide) + enemy.getSpriteDesiredHeight() / 2 - explosion.getSpriteDesiredHeight() / 2, Consts.p08h, Consts.p08h);
        }

        sb.draw(enemy.getOnlyTexture(), enemy.getPosition().x, enemy.getPosition().y, enemy.getSpriteDesiredWidth(), enemy.getSpriteDesiredHeight());

        sb.draw(atmoshpere, atmoPos1.x, atmoPos1.y, Gdx.graphics.getWidth(), (Gdx.graphics.getWidth() / atmoshpere.getWidth()) * atmoshpere.getHeight());
        sb.draw(atmoshpere, atmoPos2.x, atmoPos2.y, Gdx.graphics.getWidth(), (Gdx.graphics.getWidth() / atmoshpere.getWidth()) * atmoshpere.getHeight());

        sb.end();
        myStage.act();
        myStage.draw();
    }
    @Override
    public void dispose() {
        super.dispose();
        spaceShip.dispose();
        bkg1.dispose();
        bkg2.dispose();
        bullet.dispose();
        explosion.dispose();
        enemy.dispose();
        atmoshpere.dispose();

    }

    private void initializeSprites() {

        bkg1 = new Sprites(cam.position.x - cam.viewportWidth / 2 - spaceBetweenShipNScreen, (Gdx.graphics.getHeight() - (Consts.bkgSize)), Consts.p11w, "spritespaceshipgame/bkg/bkgmauve.jpg");
        bkg1.setSpriteOriginalWidth(Gdx.graphics.getWidth());
        bkg1.setSpriteOriginalHeight((int) Consts.bkgSize);

        bkg2 = new Sprites(bkg1.getPosition().x + bkg1.getSpriteOriginalWidth(), (Gdx.graphics.getHeight() - (Consts.bkgSize)), Consts.p11w, "spritespaceshipgame/bkg/bkgmauve.jpg");
        bkg2.setSpriteOriginalWidth(Gdx.graphics.getWidth());
        bkg2.setSpriteOriginalHeight((int) Consts.bkgSize);

        //Whatever the Y of the spaceship initialization, I will reposition it right after.
        spaceShip = new Sprites(0, 0, speedRatioSpaceship, "spritespaceshipgame/" + getRes() + "/" + prefsUrl.getString("shipUrlActif") + ".png", Consts.p23w);

        //You think I just position the spaceShip, why do I do a reposition right after ?
        //it's because when I initiate the object spaceship, I need the height of the spaceship to
        //position it. But I just have the texture height, not the height it will be on the screen
        //so I do a reposition right after with the good height
        spaceShip.reposition(0, ((Gdx.graphics.getHeight()) - ((Consts.bkgSize) / 2) - (spaceShip.getSpriteDesiredHeight() / 2)));
        spaceShip.setBoundsSize(spaceShip.getPosition().x, ((Gdx.graphics.getHeight()) - ((Consts.bkgSize) / 2) - (spaceShip.getSpriteDesiredHeight() / 2)), spaceShip.getSpriteDesiredWidth(), spaceShip.getSpriteDesiredHeight());

        shipFire = new Sprites(0, 0, speedRatioSpaceship, "spritespaceshipgame/fire/fire.pack", 1 / 30f, Consts.p20w);
        shipFire.reposition(0 - shipFire.getSpriteDesiredWidth(), ((Gdx.graphics.getHeight()) - ((Consts.bkgSize) / 2) - (shipFire.getSpriteDesiredHeight() / 2)));

        intGoodAnswer.setPosition(Consts.p02w, Gdx.graphics.getHeight() - intGoodAnswer.getHeight() - Consts.p02w);

        bullet = new Sprites(0, 0, speedRatioBullet, "spritespaceshipgame/bullet.png", Consts.p02h);
        bullet.reposition(spaceShip.getPosition().x + (Consts.p15h / 2), spaceShip.getPosition().y + (spaceShip.getSpriteDesiredHeight() / 2) - bullet.getSpriteDesiredHeight() / 2);
        bullet.setBoundsSize(bullet.getPosition().x, bullet.getPosition().y, bullet.getSpriteDesiredWidth(), bullet.getSpriteDesiredHeight());
        bullet.hideBullet();

        enemy = new Sprites(0, 0, 0, "spritespaceshipgame/" + getRes() + "/" + prefsUrl.getString("monsterUrlActif") + ".png", Consts.p20w);
        enemy.reposition(cam.position.x + cam.viewportWidth / 2 + enemyOutOfScreen, ((Gdx.graphics.getHeight()) - ((Consts.bkgSize) / 2) - ((enemy.getSpriteDesiredHeight()) / 2)));
        enemy.setBoundsSize(enemy.getPosition().x, enemy.getPosition().y, enemy.getSpriteDesiredWidth(), enemy.getSpriteDesiredHeight());

        explosion = new Sprites(0, 0, 0, "spritespaceshipgame/explosion/explosion.pack", 1 / 20f, Consts.p15w);

        //I'll need two atmosphere, as soon as the first one go out of the screen, it goes back
        //behind the second one and is ready one more time to pass in front of the camera
        atmoPos1 = new Vector2((cam.position.x - cam.viewportWidth / 2) - 50, (Gdx.graphics.getHeight() - (Consts.bkgSize)) - atmoshpere.getHeight());
        atmoPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) - 50 + atmoshpere.getWidth(), (Gdx.graphics.getHeight() - (Consts.bkgSize)) - atmoshpere.getHeight());
    } //Commented
    private void updateAtmosphere() {
        /*I have two atmoshepere right next to each other, they pas the screen from left to right
        * and give the de impression that it's infinite because, when the first one get out
        * of the screen, it teleport at the other side of the screen and is ready to pass again*/
        if (cam.position.x - (cam.viewportWidth / 2) > atmoPos1.x + atmoshpere.getWidth()) {
            atmoPos1.add(atmoshpere.getWidth() * 2, 0);
        }
        if (cam.position.x - (cam.viewportWidth / 2) > atmoPos2.x + atmoshpere.getWidth()) {
            atmoPos2.add(atmoshpere.getWidth() * 2, 0);
        }
    } //Commented
    private void updateBkg() {
        //This method replace the backgroudn who is out of the screen next to the ther background
        //like that the backgrounds pass in front of the screen indefinitely
        if (cam.position.x - (cam.viewportWidth / 2) > bkg1.getPosition().x + bkg1.getSpriteOriginalWidth()) {
            bkg1.reposition(bkg2.getPosition().x + bkg2.getSpriteOriginalWidth(), (Gdx.graphics.getHeight() - (Consts.bkgSize)));
        }
        if (cam.position.x - (cam.viewportWidth / 2) > bkg2.getPosition().x + bkg2.getSpriteOriginalWidth()) {
            bkg2.reposition(bkg1.getPosition().x + bkg1.getSpriteOriginalWidth(), (Gdx.graphics.getHeight() - (Consts.bkgSize)));
        }
    } //commented
    private void objectCollide() {

        /*for the speed, I need a ration, if I just put 50 for example, the enemy will be
        //slower on a big screen, the ration here is for when the enemy charged when the
        //user is wrong*/
        int speedRatioEnemyCharged = (int) (Gdx.graphics.getWidth() / 0.2);
        //if I get a collision with the spaceship
        if (enemy.collides(spaceShip.getBounds())) {
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
            if (answered == false) {
                correctionAnswerInGreen();
            }
            if (enemy.getMovement() == -speedRatioEnemyCharged) {
                enemy.setMovement(lastSpeedOfEnnemi);
            }
            //if the enemy collide with the spaceship I know the answer is false
            isYourAnswerGood = false;
            //I consider the user answered and I need this boolean for continue the code in the
            //update method
            answered = true;
            //The user is wrong so the enemy go far away
            enemyOutOfScreen = Gdx.graphics.getWidth() / 2;
            //this boolean will trigger the explosion in the render method
            triggerExplosionAnimation = true;
            progressBar.setValue(progressBar.getValue() - 1);
            everythingReadyForNextQuestion();
        }
        //if the enemy collide with the bullet I need to make the enemy ultra fast if the answer is
        // wrong, Like that it will collide with the spaceship and do the code just upthere
        //If the answer is good I prepare for the next question
        else if (enemy.collides(bullet.getBounds())) {
            if (isYourAnswerGood) {
                triggerExplosionAnimation = true;
                everythingReadyForNextQuestion();
            } else {
                enemy.setMovement(-speedRatioEnemyCharged);
                //then the enemy collide with the spaceship and the if upthere will be trigered
            }
        }
    } //Commented
    private void everythingReadyForNextQuestion() {
        //I would have keep this method in PlayState but I need to move too much sprite

        /*I need the enemy position at the moment the enemy is touched by the bullet
        like that I know where to put the explosion */
        posEnnemiXAtCollide = enemy.getPosition().x;
        posEnnemiYatCollide = enemy.getPosition().y;

        /*Since the enemy is touched, I can put it back outside the screen for the next question
        the enemyOutOfScreen is how far the enemy is gone off screen, 10% of the width
        if the answer is good and 50% if the answer is bad to let the user see the good answer*/
        enemy.reposition(cam.position.x + cam.viewportWidth / 2 + enemyOutOfScreen, ((Gdx.graphics.getHeight()) - ((Consts.bkgSize) / 2) - (enemy.getSpriteDesiredHeight() / 2)));

        //reinitialise enemyOutOfScreen for the next question
        enemyOutOfScreen = Consts.p10w;

        //We put the bullet behind the ship and hide it
        bullet.reposition(spaceShip.getPosition().x + (Consts.p15h / 2), spaceShip.getPosition().y + (spaceShip.getSpriteDesiredHeight() / 2) - bullet.getSpriteDesiredHeight() / 2);
        bullet.hideBullet();

        shooted = false;

        //the counterForListOfLists increases and take the next number in listOfRandomisedOrder
        counterForListOfLists++;

        //if the user answer once for all the word we reinitialize the counter and start again with
        //a new randomized order
        if (counterForListOfLists == listOfLists.size()) {
            counterForListOfLists = 0;
            Collections.shuffle(listOfLists);
        }
        //if the answer is good the enemy speed is going up, the counter of good answer goes up too
        if (isYourAnswerGood && answered) {
            counterOfGoodAnswer++;
            enemy.setMovement(enemy.getMovement() - Consts.p01w);
            intGoodAnswer.setText(String.valueOf(counterOfGoodAnswer));
        }
    } //Commented
    private void listenerButton(final TextButton myButton) {
        myButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lastSpeedOfEnnemi = enemy.getMovement();
                if (canIclick) {
                    answered = true;
                    /*i can't compare the word to know if the answer is good, I need compare
                     when I click on the button I can know which button it is. Then I compare it
                    with the listOfId because I put the index of the good button in first place
                    with the help of the method mySwitchButton*/
                    if (textButtons.indexOf(myButton) == listOfId.get(0)) {
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


    }
    protected void initializeTextButtons(){
        super.initializeTextButtons();
        for (TextButton tb : textButtons){
            listenerButton(tb);
        }
    } //Commented
}
