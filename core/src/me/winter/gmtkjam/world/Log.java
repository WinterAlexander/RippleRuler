package me.winter.gmtkjam.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
	private final TextureRegion log, log_waterreflect;

	private final Body body;

	private final Vector2 tmpVec2 = new Vector2();

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
	}

	@Override
	public Body getBody()
	{
		return body;
	}


	@Override
	public ZIndex[] getZIndices()
	{
		return new ZIndex[] { ZIndex.LOG, ZIndex.WAVE };
	}
}
