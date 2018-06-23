package com.yaselak.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yaselak.game.SpaceLanguage;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SpaceLanguage.WIDTH;
		config.height = SpaceLanguage.HEIGHT;
		config.title = SpaceLanguage.TITLE;
		new LwjglApplication(new SpaceLanguage(), config);
	}
}
