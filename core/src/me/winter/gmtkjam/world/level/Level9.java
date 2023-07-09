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
public class Level9 implements Level {
    @Override
    public void load(WaterWorld world) {

        world.addEntity(new Beach(world, Beach.BeachPosition.TOP));
        world.addEntity(new Boat(world,
                new Vector2(16.0f, 4.5f * 16.0f),
                -90.0f,
                new Vector2(0.0f, 0.0f)));

        world.addEntity(new Rock(world, new Vector2(71.4f, 115.2f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(72.6f, 98.0f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(182.4f, 121.600006f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(182.8f, 103.8f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(182.2f, 5.5999985f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(181.6f, 22.399998f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(70.0f, 5.0f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(70.6f, 23.599998f), 0.0f));

        world.addEntity(new Rock(world, new Vector2(199.0f, 30.2f), 0.0f));
        world.addEntity(new Rock(world, new Vector2(221.0f, 27.800003f), 0.0f));


        for(float x = 5.0f; x <= 11.0f; x++) {

            world.addEntity(new Log(world,
                    new Vector2(x * 16.0f, 0.5f * 16.0f),
                    0.0f,
                    new Vector2(0.0f, 0.0f)));
            world.addEntity(new Log(world,
                    new Vector2(x * 16.0f, 1.5f * 16.0f),
                    0.0f,
                    new Vector2(0.0f, 0.0f)));
            world.addEntity(new Log(world,
                    new Vector2(x * 16.0f, 2.5f * 16.0f),
                    0.0f,
                    new Vector2(0.0f, 0.0f)));
            world.addEntity(new Log(world,
                    new Vector2(x * 16.0f, 3.5f * 16.0f),
                    0.0f,
                    new Vector2(0.0f, 0.0f)));
            world.addEntity(new Log(world,
                    new Vector2(x * 16.0f, 4.5f * 16.0f),
                    0.0f,
                    new Vector2(0.0f, 0.0f)));

            world.addEntity(new Log(world,
                    new Vector2(x * 16.0f, 5.5f * 16.0f),
                    0.0f,
                    new Vector2(0.0f, 0.0f)));

            world.addEntity(new Log(world,
                    new Vector2(x * 16.0f, 6.5f * 16.0f),
                    0.0f,
                    new Vector2(0.0f, 0.0f)));
            world.addEntity(new Log(world,
                    new Vector2(x * 16.0f, 7.5f * 16.0f),
                    0.0f,
                    new Vector2(0.0f, 0.0f)));
        }

        world.addEntity(new Dock(world, new Vector2(16.0f * 16.0f, 3.0f * 16.0f), 180f));
    }
}
