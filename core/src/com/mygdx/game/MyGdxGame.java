package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class MyGdxGame extends Game {

	private MyGdxGame g;
	public ActionResolver actionResolver;

	public MyGdxGame(ActionResolver actionResolver){
		g=this;
		g.actionResolver = actionResolver;
	}
	@Override
	public void create() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		g.setScreen(new MainMenueScreen(g));
	}
}