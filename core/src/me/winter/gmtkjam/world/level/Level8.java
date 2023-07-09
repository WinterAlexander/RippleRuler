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
public class Level8 implements Level
{
    @Override
    public void load(WaterWorld world) {

        world.addEntity(new Beach(world, Beach.BeachPosition.TOP));
        world.addEntity(new Beach(world, Beach.BeachPosition.BOTTOM));
        world.addEntity(new Boat(world,
                new Vector2(16.0f, 4.5f * 16.0f),
                -90.0f,
                new Vector2(0.0f, 0.0f)));

        world.addEntity(new Rock(world, new Vector2(124.4f, 122.4f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(99.6f, 114.4f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(79.2f, 98.2f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(152.8f, 115.399994f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(172.8f, 96.799995f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(123.2f, 7.800003f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(144.6f, 18.0f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(167.6f, 35.6f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(102.2f, 19.0f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(79.8f, 40.399998f), 0.0f));

        world.addEntity(new Rock(world, new Vector2(125.2f, 67.6f), 0.0f));

        world.addEntity(new Log(world, new Vector2(103.0f, 43.800003f), -45.0f, new Vector2()));
        world.addEntity(new Log(world, new Vector2(114.6f, 55.0f), -45.0f, new Vector2()));

        world.addEntity(new Log(world, new Vector2(139.6f, 56.4f), 45.0f, new Vector2()));
        world.addEntity(new Log(world, new Vector2(154.8f, 45.6f), 45.0f, new Vector2()));

        world.addEntity(new Log(world, new Vector2(138.0f, 77.2f), -45.0f, new Vector2()));
        world.addEntity(new Log(world, new Vector2(153.2f, 87.799995f), -45.0f, new Vector2()));

        world.addEntity(new Log(world, new Vector2(111.6f, 80.4f), 45.0f, new Vector2()));
        world.addEntity(new Log(world, new Vector2(94.8f, 95.2f), 45.0f, new Vector2()));

        world.addEntity(new Dock(world, new Vector2(16.0f * 16.0f, 3.0f * 16.0f), 180f));
    }
}
