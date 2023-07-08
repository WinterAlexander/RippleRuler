package me.winter.gmtkjam.world.level;

import com.badlogic.gdx.math.Vector2;
import me.winter.gmtkjam.world.*;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class Level2 implements Level
{
	public Level2()
	{

	}

	@Override
	public void load(WaterWorld world)
	{
		world.addEntity(new Beach(world));
		world.addEntity(new Boat(world,
				new Vector2(16.0f, 4.5f * 16.0f),
				0.0f,
				new Vector2(8f, 0.0f)));
		world.addEntity(new Log(world,
				new Vector2(12.0f * 16.0f, 4.5f * 16.0f),
				90.0f,
				new Vector2(-1.0f * 16.0f, 0.0f)));
		world.addEntity(new Rock(world, new Vector2(3.0f * 16.0f, 3.0f * 16.0f), 90.0f));
		world.addEntity(new Rock(world, new Vector2(10.0f * 16.0f, 3.0f * 16.0f), 0.0f));
		world.addEntity(new Rock(world, new Vector2(15.0f * 16.0f, 3.0f * 16.0f), 90.0f));
		world.addEntity(new Rock(world, new Vector2(14.0f * 16.0f, 8.0f * 16.0f), 180.0f));
		world.addEntity(new Rock(world, new Vector2(13.0f * 16.0f, 8.0f * 16.0f), -90.0f));

		world.addEntity(new Dock(world, new Vector2(16.0f * 16.0f, 6.0f * 16.0f), 180f));
	}
}
