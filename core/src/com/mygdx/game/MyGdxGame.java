package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;


public class MyGdxGame extends Game{
        parent of 4a0a037... Revert "test1"
	private Game g;

	public MyGdxGame(){
		g=this;
	}
	@Override
	public void create() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		g.setScreen(new MainMenueScreen(g));
	}
}