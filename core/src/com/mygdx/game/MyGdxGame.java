package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;


public class MyGdxGame extends Game{

	private MyGdxGame g;
	private Preferences pref=new Preferences();
	Player p=new Player();
	LibgdxNetzwerkHandler nh;
	Music music;


	public MyGdxGame(){
		g=this;
		LibgdxNetzwerkHandler nh=LibgdxNetzwerkHandler.getInstance(g);
	}
	@Override
	public void create() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		music = Gdx.audio.newMusic(Gdx.files.internal("Sound/loop1.mp3"));
		music.setVolume(pref.getMusic()/100);                 // sets the volume to half the maximum volume
		music.setLooping(true);                // will repeat playback until music.stop() is called
		music.play();
		g.setScreen(new MainMenueScreen(g));
	}

	public Preferences getPref() {
		return pref;
	}

	public void setPref(Preferences pref) {
		this.pref = pref;
	}

	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public Music getMusic() {
		return music;
	}
}