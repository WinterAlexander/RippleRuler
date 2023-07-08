package me.winter.gmtkjam.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class Rock extends Entity
{
	private final TextureRegion rock;

	private final Body body;

	public Rock(WaterWorld world, Vector2 location, float angle)
	{
		super(world);

		rock = new TextureRegion(new Texture("rock.png"));

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;

		bodyDef.position.set(location);
		bodyDef.angle = angle * MathUtils.degreesToRadians;
		body = world.getB2world().createBody(bodyDef);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(0.5f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circleShape;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		Fixture fixture = body.createFixture(fixtureDef);

		circleShape.dispose();
		body.setUserData(this);
	}

	@Override
	public void render(GameScreen screen)
	{
		screen.getBatch().draw(rock,
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
	public ZIndex getZIndex()
	{
		return ZIndex.ROCK;
	}
}