package com.mygdx.game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.impl.Player.Ninja;
import com.mygdx.game.view.MainMenuScreen;


public class MyGdxGame extends Game {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 720;
	public SpriteBatch batch;
	public static Ninja ninja;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
		ninja = new Ninja();
	}

	@Override
	public void render () {
		super.render();
	}

}
