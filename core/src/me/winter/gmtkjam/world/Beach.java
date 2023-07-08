package me.winter.gmtkjam.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
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

    private final BeachPosition position;
    private final Vector2 direction;

    public Beach(WaterWorld world, BeachPosition position) {
        super(world);
        this.position = position;

        beach = new Texture("beach.png");
        beachReflection = new Texture("beach_waterreflection.png");

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        switch (position)
        {
            case TOP:
                this.direction = new Vector2(0.0f, -1.0f);
                bodyDef.position.set(128.0f, 144.0f);
                break;
            case BOTTOM:
                this.direction = new Vector2(0.0f, 1.0f);
                bodyDef.position.set(128.0f, 0.0f);
                break;
            default:
                throw new IllegalArgumentException("bad enum value: " + position);
        }
        body = world.getB2world().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(128.0f, 16.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 10.0f;
        fixtureDef.restitution = 0.0f;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();
        body.setUserData(this);
    }

    @Override
    public void render(GameScreen screen, ZIndex zIndex) {
        if(zIndex == ZIndex.BEACH)
        {
            switch (position)
            {
                case TOP:
                    screen.getBatch().draw(beach, 0.0f, 144.0f - 32.0f, 256.0f, 32.0f);
                    break;
                case BOTTOM:
                    screen.getBatch().draw(beach,
                            0.0f, 0.0f,  // position
                            128.0f, 16.0f, // origin
                            256.0f, 32.0f, // size
                            1.0f, 1.0f, // scale
                            0.0f, // rotation
                            0, 0,
                            256, 32,
                            false, true);
                    break;
            }
        }
        else
        {
            switch (position)
            {
                case TOP:
                    screen.getBatch().draw(beachReflection, 0.0f, 144.0f - 32.0f, 256.0f, 32.0f);
                    break;
                case BOTTOM:
                    screen.getBatch().draw(beachReflection,
                            0.0f, 0.0f,  // position
                            128.0f, 16.0f, // origin
                            256.0f, 32.0f, // size
                            1.0f, 1.0f, // scale
                            0.0f, // rotation
                            0, 0,
                            256, 32,
                            false, true);
                    break;
            }
        }
    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public ZIndex[] getZIndices() {
        return new ZIndex[] { ZIndex.BEACH, ZIndex.WAVE };
    }

    public Vector2 getDirection() {
        return direction;
    }

    public static enum BeachPosition
    {
        TOP,
        BOTTOM
    }
}

