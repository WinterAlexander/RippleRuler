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
public class Dock extends Entity
{
	private final TextureRegion dock, dock_waterreflection;

	private final Body body;

	public Dock(WaterWorld world, Vector2 location, float angle)
	{
		super(world);

		dock = new TextureRegion(new Texture("dock.png"));
		dock_waterreflection = new TextureRegion(new Texture("dock_waterreflection.png"));

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;

		bodyDef.position.set(location);
		bodyDef.angle = angle * MathUtils.degreesToRadians;
		body = world.getB2world().createBody(bodyDef);

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(30.0f / 32.0f / 2.0f * 2.0f,
							  16.0f / 32.0f / 2.0f * 2.0f,
							  new Vector2(30.0f / 32.0f / 2.0f * 2.0f, 0.0f),
							  0.0f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		Fixture fixture = body.createFixture(fixtureDef);

		polygonShape.dispose();
		body.setUserData(this);
	}

	@Override
	public void render(GameScreen screen, ZIndex zIndex)
	{
		float aspectRatio = 3.0f / 4.0f;

		if(zIndex == ZIndex.WAVE)
			screen.getBatch().draw(dock_waterreflection,
					body.getPosition().x, body.getPosition().y - aspectRatio,
					0.0f, aspectRatio,
					2.0f, 2.0f * aspectRatio,
					1.0f, 1.0f,
					MathUtils.radiansToDegrees * body.getAngle());
		else
			screen.getBatch().draw(dock,
					body.getPosition().x, body.getPosition().y - aspectRatio,
					0.0f, aspectRatio,
					2.0f, 2.0f * aspectRatio,
					1.0f, 1.0f,
					MathUtils.radiansToDegrees * body.getAngle());
	}

	@Override
	public void tick(float delta)
	{

	}

	@Override
	public ZIndex[] getZIndices()
	{
		return new ZIndex[] { ZIndex.DOCK, ZIndex.WAVE };
	}
}
