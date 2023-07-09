package me.winter.gmtkjam.world.level;

import com.badlogic.gdx.math.Vector2;
import me.winter.gmtkjam.world.*;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-09.
 *
 * @author Alexander Winter
 */
public class Level7 implements Level {
    @Override
    public void load(WaterWorld world) {

        world.addEntity(new Beach(world, Beach.BeachPosition.TOP));
        world.addEntity(new Beach(world, Beach.BeachPosition.BOTTOM));
        world.addEntity(new Boat(world,
                new Vector2(16.0f, 4.5f * 16.0f),
                -90.0f,
                new Vector2(0.0f, 0.0f)));

        world.addEntity(new Duck(world, new Vector2(6.0f * 16.0f, 2.0f * 16.0f), new Vector2(6f * 16f, 7f * 16.0f)));
        world.addEntity(new Duck(world, new Vector2(10.0f * 16.0f, 7.0f * 16.0f), new Vector2(10f * 16f, 2f * 16.0f)));

        world.addEntity(new Dock(world, new Vector2(16.0f * 16.0f, 3.0f * 16.0f), 180f));
    }
}

