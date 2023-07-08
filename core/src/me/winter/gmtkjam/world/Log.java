package me.winter.gmtkjam.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class Log extends Entity implements Floating
{
	public static final float TRAIL_DESPAWN_DELAY = 0.25f;
	private final TextureRegion log, log_waterreflect;

	private final Body body;

	private final Vector2 tmpVec2 = new Vector2();

	private final Array<TrailPiece> trail = new Array<>();

	private final Vector2[] borderPixels = new Vector2[] {
			new Vector2(4.5f, 0.5f),
			new Vector2(5.5f, 0.5f),
			new Vector2(6.5f, 0.5f),
			new Vector2(7.5f, 1.5f),
			new Vector2(8.5f, 1.5f),
			new Vector2(9.5f, 0.5f),
			new Vector2(10.5f, 0.5f),

			new Vector2(2.5f, 9.5f),
			new Vector2(3.5f, 10.5f),
			new Vector2(4.5f, 11.5f),

			new Vector2(5.5f, 14.5f),
			new Vector2(6.5f, 14.5f),
			new Vector2(7.5f, 14.5f),
			new Vector2(8.5f, 15.5f),
			new Vector2(9.5f, 15.5f),
			new Vector2(10.5f, 15.5f),
	};

	public Log(WaterWorld world, Vector2 location, float angle, Vector2 startVelocity)
	{
		super(world);
		log = new TextureRegion(new Texture("log.png"));
		log_waterreflect = new TextureRegion(new Texture("log_waterreflection.png"));

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;

		bodyDef.position.set(location);
		bodyDef.angle = angle * MathUtils.degreesToRadians;
		body = world.getB2world().createBody(bodyDef);

		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(0.2f * 16.0f, 0.5f * 16.0f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygon;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		Fixture fixture = body.createFixture(fixtureDef);

		polygon.dispose();

		body.setLinearVelocity(startVelocity);
		body.setUserData(this);
	}

	@Override
	public void render(GameScreen screen, ZIndex zIndex)
	{
		if(zIndex == ZIndex.WAVE)
			screen.getBatch().draw(log_waterreflect,
					body.getPosition().x - 12f, body.getPosition().y - 12f,
					12f, 12f,
					24f, 24f,
					1.0f, 1.0f,
					MathUtils.radiansToDegrees * body.getAngle());
		else
			screen.getBatch().draw(log,
					body.getPosition().x - 8.0f, body.getPosition().y - 8.0f,
					8f, 8f,
					16f, 16f,
					1.0f, 1.0f,
					MathUtils.radiansToDegrees * body.getAngle());
	}

	@Override
	public void tick(float delta)
	{
		// set tmpVec2 to direction of the boat (unit vector)
		tmpVec2.set(0.0f, 1.0f);
		tmpVec2.rotateRad(body.getAngle());

		// scale that direction by the dot product with lin vel
		tmpVec2.scl(tmpVec2.dot(body.getLinearVelocity()));

		// substract lin vel to get lin vel component perpendicular to boat direction
		tmpVec2.sub(body.getLinearVelocity());

		// scale for some smoothing factor
		tmpVec2.scl(0.1f);

		body.applyForceToCenter(tmpVec2, true);

		for(Vector2 borderPixel : borderPixels)
		{
			tmpVec2.set(borderPixel.x - 8.0f, 16.0f - borderPixel.y - 8.0f);
			tmpVec2.rotateRad(body.getAngle());

			TrailPiece current = new TrailPiece();

			current.x = getBody().getPosition().x + tmpVec2.x;
			current.y = getBody().getPosition().y + tmpVec2.y;
			current.despawnTime = getWorld().getTime() + TRAIL_DESPAWN_DELAY;

			trail.add(current);
		}

		for(int i = 0; i < trail.size; i++) {
			if(trail.get(i).despawnTime  < getWorld().getTime())
			{
				trail.removeIndex(i);
				i--;
			}
		}

		for(TrailPiece piece : trail) {
			getWorld().getWater().addWaterForce(tmpVec2.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
					piece.x, piece.y);
		}
	}

	@Override
	public Body getBody()
	{
		return body;
	}


	@Override
	public ZIndex[] getZIndices()
	{
		return new ZIndex[] { ZIndex.LOG };
	}
}
