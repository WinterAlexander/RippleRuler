package me.winter.gmtkjam.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-08.
 *
 * @author Alexander Winter
 */
public class Beach extends Entity {

    private final Texture beach, beachReflection;

    private final Body body;

    public Beach(WaterWorld world) {
        super(world);

        beach = new Texture("beach.png");
        beachReflection = new Texture("beach_waterreflection.png");

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(128.0f, 144.0f);
        body = world.getB2world().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(128.0f, 16.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();
        body.setUserData(this);
    }

    @Override
    public void render(GameScreen screen, ZIndex zIndex) {
        if(zIndex == ZIndex.BEACH)
        {
            screen.getBatch().draw(beach, 0.0f, 144.0f - 32.0f, 256.0f, 32.0f);
        }
        else
        {
            screen.getBatch().draw(beachReflection, 0.0f, 144.0f - 32.0f, 256.0f, 32.0f);
        }
    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public ZIndex[] getZIndices() {
        return new ZIndex[] { ZIndex.BEACH, ZIndex.WAVE };
    }
}

