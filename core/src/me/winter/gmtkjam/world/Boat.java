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
import com.badlogic.gdx.utils.Array;
import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class Boat extends Entity implements Floating
{
	public static final float TRAIL_DESPAWN_DELAY = 0.25f;

	private final TextureRegion boat;

	private final Body body;

	private final Vector2 tmpVec2 = new Vector2();

	private final Array<TrailPiece> trail = new Array<>();

	private final Vector2[] borderPixels = new Vector2[] {
			new Vector2(7.5f, 0.5f),

			new Vector2(6.5f, 1.5f),
			new Vector2(8.5f, 1.5f),

			new Vector2(5.5f, 2.5f),
			new Vector2(9.5f, 2.5f),

			new Vector2(4.5f, 3.5f),
			new Vector2(10.5f, 3.5f),

			new Vector2(4.5f, 4.5f),
			new Vector2(10.5f, 4.5f),

			new Vector2(3.5f, 5.5f),
			new Vector2(11.5f, 5.5f),

			new Vector2(3.5f, 11.5f),
			new Vector2(11.5f, 11.5f),

			new Vector2(4.5f, 14.5f),
			new Vector2(10.5f, 14.5f),

			new Vector2(5.5f, 11.5f),
			new Vector2(9.5f, 11.5f),
	};

	public Boat(WaterWorld world, Vector2 location, float angle, Vector2 initialVelocity)
	{
		super(world);
		this.boat = new TextureRegion(new Texture("boat.png"));

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;

		bodyDef.position.set(location);
		bodyDef.angle = angle * MathUtils.degreesToRadians;
		body = world.getB2world().createBody(bodyDef);

		PolygonShape polygon1 = new PolygonShape();
		PolygonShape polygon2 = new PolygonShape();

		float[] arr = new float[] {
				7.5f, (16.0f -  0.5f),
				10.5f, (16.0f -  3.5f),
				11.5f, (16.0f -  5.5f),
				3.5f, (16.0f -  5.5f),
				4.5f, (16.0f -  3.5f),
		};

		float[] arr2 = new float[] {
				11.5f, (16.0f -  5.5f),
				11.5f, (16.0f - 11.5f),
				10.5f, (16.0f - 14.5f),
				9.5f, (16.0f - 15.5f),
				5.5f, (16.0f - 15.5f),
				4.5f, (16.0f - 14.5f),
				3.5f, (16.0f - 11.5f),
				3.5f, (16.0f -  5.5f),
		};

		float[] rev = new float[arr.length];

		for(int i = 0; i < arr.length; i += 2) {
			rev[i] = arr[arr.length - i - 2] - 7.5f;
			rev[i + 1] = arr[arr.length - i - 1] - 8f;
		}

		float[] rev2 = new float[arr2.length];

		for(int i = 0; i < arr2.length; i += 2) {
			rev2[i] = arr2[arr2.length - i - 2] - 7.5f;
			rev2[i + 1] = arr2[arr2.length - i - 1] - 8f;
		}

		polygon1.set(rev);
		polygon2.set(rev2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygon1;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		Fixture fixture = body.createFixture(fixtureDef);

		polygon1.dispose();

		FixtureDef fixtureDef2 = new FixtureDef();
		fixtureDef2.shape = polygon2;
		fixtureDef2.density = 0.5f;
		fixtureDef2.friction = 1.0f;
		fixtureDef2.restitution = 0.0f;

		Fixture fixture2 = body.createFixture(fixtureDef2);

		polygon2.dispose();

		body.setLinearVelocity(initialVelocity);
		body.setUserData(this);
		body.setAngularDamping(1.0f);
	}

	@Override
	public void render(GameScreen screen, ZIndex zIndex)
	{
		screen.getBatch().draw(boat,
				body.getPosition().x - 7.5f, body.getPosition().y - 8f,
				7.5f, 8f,
				15f, 16f,
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
		tmpVec2.scl(50.0f);

		body.applyForceToCenter(tmpVec2, true);

		for(Vector2 borderPixel : borderPixels)
		{
			tmpVec2.set(borderPixel.x - 7.5f, 16.0f - borderPixel.y - 8.0f);
			tmpVec2.rotateRad(body.getAngle());

			TrailPiece current = new TrailPiece();

			current.x = getBody().getPosition().x + tmpVec2.x;
			current.y = getBody().getPosition().y + tmpVec2.y;
			current.despawnTime = getWorld().getTime() + TRAIL_DESPAWN_DELAY;

			trail.add(current);
		}

		for(int i = 0; i < trail.size; i++) {
			if(trail.get(i).despawnTime < getWorld().getTime())
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

	public Vector2 getLocation()
	{
		return body.getPosition();
	}

	@Override
	public Body getBody()
	{
		return body;
	}

	@Override
	public ZIndex[] getZIndices()
	{
		return new ZIndex[] { ZIndex.BOAT };
	}

}
