package me.winter.gmtkjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GMTKJam extends Game
{
	private Preferences preferences;
	
	public GMTKJam()
	{

	}

	@Override
	public void create()
	{
		preferences = Gdx.app.getPreferences("ripple-ruler");
		setScreen(new TitleScreen(this));
	}

	public Preferences getPreferences() {
		return preferences;
	}
}
