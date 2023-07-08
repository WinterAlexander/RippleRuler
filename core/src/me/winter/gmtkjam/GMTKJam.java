package me.winter.gmtkjam;

import com.badlogic.gdx.Game;

public class GMTKJam extends Game
{
	public GMTKJam()
	{

	}

	@Override
	public void create()
	{
		setScreen(new TitleScreen(this));
	}
}
