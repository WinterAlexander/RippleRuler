package me.winter.gmtkjam.world.level;

import com.badlogic.gdx.math.Vector2;
import me.winter.gmtkjam.world.Beach;
import me.winter.gmtkjam.world.Boat;
import me.winter.gmtkjam.world.Dock;
import me.winter.gmtkjam.world.WaterWorld;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class Level1 implements Level
{
	@Override
	public void load(WaterWorld world)
	{
		world.addEntity(new Beach(world));
		world.addEntity(new Boat(world,
				new Vector2(1.0f * 16.0f, 4.5f * 16.0f),
				-90.0f,
				new Vector2(8f, 0.0f)));

		world.addEntity(new Dock(world, new Vector2(16.0f * 16.0f, 6.0f * 16.0f), 180.0f));
	}
}
