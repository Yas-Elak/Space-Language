package com.yaselak.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yaselak.game.states.GameStateManager;
import com.yaselak.game.states.MenuState;
import com.yaselak.game.states.ThemeState;

import java.awt.Menu;

public class SpaceLanguage extends ApplicationAdapter {

	//Constant for the size of the screen
	public static int WIDTH = 480;
	public static int HEIGHT = 800;

	public static final String TITLE = "Space Dutch";

	private GameStateManager gsm;
	private SpriteBatch batch;

	@Override
	public void create () {
		//WIDTH = Gdx.graphics.getWidth();
		//HEIGHT = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		MenuState menuState = new MenuState(gsm);
		gsm.push(menuState);
		menuState.init();

	}
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);

	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}

