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
	private final TextureRegion log;

	private final Body body;

	public Log(WaterWorld world, Vector2 location, float angle, Vector2 startVelocity)
	{
		super(world);
		log = new TextureRegion(new Texture("log.png"));

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;

		bodyDef.position.set(location);
		bodyDef.angle = angle * MathUtils.degreesToRadians;
		body = world.getB2world().createBody(bodyDef);

		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(0.2f, 0.5f);

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
	public void render(GameScreen screen)
	{
		screen.getBatch().draw(log,
				body.getPosition().x - 0.5f, body.getPosition().y - 0.5f,
				0.5f, 0.5f,
				1.0f, 1.0f,
				1.0f, 1.0f,
				MathUtils.radiansToDegrees * body.getAngle());
	}

	@Override
	public void tick(float delta)
	{

	}

	@Override
	public Body getBody()
	{
		return body;
	}

	@Override
	public ZIndex getZIndex()
	{
		return ZIndex.LOG;
	}
}
