package com.yaselak.game.addtogdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SkinCreation {


    private TextButton.TextButtonStyle textButtonStyle, textButtonStyleGreen,textButtonStyleRed,
            textAnswerWhite, textButtonStyleGrey;
    private Label.LabelStyle labelStyle;
    private ProgressBar progressBar;
    private BitmapFont bitmapFont;
    private int size;

    private GlyphLayout layout = new GlyphLayout();




    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator generator;
    public Skin CreateSkin(String urlFont, boolean colorBlack, int size) {
        this.size = size;
        //I want to use 9 patch for my button, if I just put the 9patch image on the style without
        //using a ninepatch object  I will get black lines at my buttons
        TextureAtlas buttonAtlas = new TextureAtlas("buttons/btnrectangle.pack");
        NinePatch buttonNinePatchNormalBlue = buttonAtlas.createPatch("btnnormalblue");
        NinePatch buttonNinePatchPushedBlue = buttonAtlas.createPatch("btnpushedblue");
        NinePatch buttonNinePatchNormalRed = buttonAtlas.createPatch("btnnormalred");
        NinePatch buttonNinePatchPushedRed = buttonAtlas.createPatch("btnpushedred");
        NinePatch buttonNinePatchNormalGreen = buttonAtlas.createPatch("btnnormalgreen");
        NinePatch buttonNinePatchPushedGreen = buttonAtlas.createPatch("btnpushedgreen");
        NinePatch buttonNinePatchNormalGrey = buttonAtlas.createPatch("btnnormalgrey");
        NinePatch buttonNinePatchPushedGrey = buttonAtlas.createPatch("btnpushedgrey");

        NinePatchDrawable ninePatchDrawableNormalBlue = new NinePatchDrawable(buttonNinePatchNormalBlue);
        NinePatchDrawable ninePatchDrawablePushedBlue = new NinePatchDrawable(buttonNinePatchPushedBlue);
        NinePatchDrawable ninePatchDrawableNormalGreen = new NinePatchDrawable(buttonNinePatchNormalGreen);
        NinePatchDrawable ninePatchDrawablePushedGreen = new NinePatchDrawable(buttonNinePatchPushedGreen);
        NinePatchDrawable ninePatchDrawableNormalRed = new NinePatchDrawable(buttonNinePatchNormalRed);
        NinePatchDrawable ninePatchDrawablePushedRed = new NinePatchDrawable(buttonNinePatchPushedRed);
        NinePatchDrawable ninePatchDrawableNormalGrey = new NinePatchDrawable(buttonNinePatchNormalGrey);
        NinePatchDrawable ninePatchDrawablePushedGrey= new NinePatchDrawable(buttonNinePatchPushedGrey);

        /* Next I want a ttf font but there is none in the skins you can download. You need to do it
        in the code. I do a empty skin en add the ttf font in it, I ad the textbuttonstyle too
        */
        generator = new FreeTypeFontGenerator(Gdx.files.internal(urlFont));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        if (colorBlack) parameter.color = Color.BLACK;
        bitmapFont = generator.generateFont(parameter);
        bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        // create a new style for the text button
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = ninePatchDrawableNormalBlue;
        textButtonStyle.down = ninePatchDrawablePushedBlue;
        textButtonStyle.font =bitmapFont;

        textButtonStyleGreen = new TextButton.TextButtonStyle();
        textButtonStyleGreen.up = ninePatchDrawableNormalGreen;
        textButtonStyleGreen.down = ninePatchDrawablePushedGreen;
        textButtonStyleGreen.font =bitmapFont;

        textButtonStyleRed = new TextButton.TextButtonStyle();
        textButtonStyleRed.up = ninePatchDrawableNormalRed;
        textButtonStyleRed.down = ninePatchDrawablePushedRed;
        textButtonStyleRed.font =bitmapFont;

        textButtonStyleGrey = new TextButton.TextButtonStyle();
        textButtonStyleGrey.up = ninePatchDrawableNormalGrey;
        textButtonStyleGrey.down = ninePatchDrawablePushedGrey;
        textButtonStyleGrey.font = bitmapFont;

        labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;

    //I generate the empty skin and add the elements (patch and style of the buttons
        Skin textButtonSkin = new Skin();
        textButtonSkin.add("default",textButtonStyle);
        textButtonSkin.add("green",textButtonStyleGreen);
        textButtonSkin.add("red",textButtonStyleRed);
        textButtonSkin.add("grey",textButtonStyleGrey);
        textButtonSkin.add("label", labelStyle);

        Texture textureBkgAnswer = new Texture("lowpolylabel.png");
        textButtonSkin.add("answer", new TextureRegion(textureBkgAnswer, 0,0, ((int) Math.round(Gdx.graphics.getWidth()*0.9)), ((int) Math.round(Gdx.graphics.getWidth()*0.15))));
        textAnswerWhite = new TextButton.TextButtonStyle();
        textAnswerWhite.up = textButtonSkin.newDrawable("answer");
        textAnswerWhite.down = textButtonSkin.newDrawable("answer");
        textAnswerWhite.font = bitmapFont;
        textButtonSkin.add("white", textAnswerWhite);

        return textButtonSkin;

    }

    public ProgressBar progressBar(){
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();

        TextureRegionDrawable drawable;
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture("progressbar/progressbarempty.png")));
        progressBarStyle.background = drawable;
        Pixmap pixmap = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(com.badlogic.gdx.graphics.Color.GREEN);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        progressBarStyle.knob = drawable;
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture("progressbar/progressbarfull.png")));
        progressBarStyle.knobBefore = drawable;


        progressBar = new ProgressBar(0, 5, 1, false, progressBarStyle);
        progressBar.setValue(5);
        progressBar.setAnimateDuration(2);

        return progressBar;
    }

    public TextButton.TextButtonStyle getTextButtonStyle() {
        parameter.color = Color.WHITE;

        return textButtonStyle;
    }

    public TextButton.TextButtonStyle getTextButtonStyleGreen() {
        parameter.color = Color.WHITE;
        return textButtonStyleGreen;
    }

    public TextButton.TextButtonStyle getTextButtonStyleRed() {
        return textButtonStyleRed;
    }

    public TextButton.TextButtonStyle getTextAnswerWhite() {
        return textAnswerWhite;
    }

    public TextButton.TextButtonStyle getTextButtonStyleGrey() {
        return textButtonStyleGrey;
    }
    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }
    public void setTextLayout(CharSequence layoutString) {
        this.layout.setText(getBitmapFont(), layoutString);
    }

    public GlyphLayout getLayout() {
        return layout;
    }
    public FreeTypeFontGenerator.FreeTypeFontParameter getParameter() {
        return parameter;
    }

    public int getSize() {
        return size;
    }

    public void setParameterSize(int a){
        parameter.size = a;
        bitmapFont = generator.generateFont(parameter);
        bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textButtonStyle.font = bitmapFont;
        textButtonStyleGreen.font = bitmapFont;
        textButtonStyleRed.font = bitmapFont;


    }

}
