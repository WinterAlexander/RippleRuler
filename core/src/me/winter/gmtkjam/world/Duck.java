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
public class Duck extends Entity implements Floating
{
	public static final float TRAIL_DESPAWN_DELAY = 0.25f;

	private final TextureRegion duck;

	private final Body body;

	private final Vector2 tmpVec2 = new Vector2();

	private final Array<TrailPiece> trail = new Array<>();

	private final Array<ForceApplicationPoint> points = new Array<>();

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

	private final Vector2 pointA, pointB;
	boolean goingToB = true;

	public Duck(WaterWorld world, Vector2 location, Vector2 patrolPoint)
	{
		super(world);
		this.duck = new TextureRegion(new Texture("duck.png"));

		this.pointA = location;
		this.pointB = patrolPoint;

		points.add(new ForceApplicationPoint());
		points.add(new ForceApplicationPoint());
		points.add(new ForceApplicationPoint());

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;

		bodyDef.position.set(location);
		bodyDef.angle = tmpVec2.set(pointA).sub(pointB).angleRad();
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

		body.setLinearVelocity(new Vector2());
		body.setUserData(this);
		body.setAngularDamping(4.0f);
		body.setLinearDamping(2.0f);
	}

	@Override
	public void render(GameScreen screen, ZIndex zIndex)
	{
		screen.getBatch().draw(duck,
				body.getPosition().x - 4f, body.getPosition().y - 8f,
				4f, 8f,
				8f, 16f,
				1.0f, 1.0f,
				MathUtils.radiansToDegrees * body.getAngle());
	}

	@Override
	public void tick(float delta)
	{
		getForceApplicationPoints();
		tmpVec2.set(goingToB ? pointB : pointA).sub(points.get(0).x, points.get(0).y);

		float dst = tmpVec2.len();

		if(dst <= 1.0f) {
			goingToB = !goingToB;
		} else {
			tmpVec2.scl(1.0f / dst);


			float deltaAngle = body.getAngle() - tmpVec2.angleRad();

			//float ratio = body.getLinearVelocity().len() == 0.0f ? 0.0f : tmpVec2.dot(body.getLinearVelocity()) / body.getLinearVelocity().len();
			//ratio = MathUtils.clamp(ratio, 0.0f, 1.0f);

			tmpVec2.scl(delta * 256.0f * 256.0f * 2.50f * (1.0f - 0.0f));
			body.applyForce(tmpVec2.x, tmpVec2.y, points.get(0).x, points.get(0).y, true);

			if(deltaAngle < -180.0f)
				deltaAngle += 360.0f;

			if(deltaAngle > 180.0f)
				deltaAngle -= 360.0f;

			body.applyTorque(deltaAngle * delta * 10000f, true);
		}

		// set tmpVec2 to direction of the boat (unit vector)
		tmpVec2.set(0.0f, 1.0f);
		tmpVec2.rotateRad(body.getAngle());

		// scale that direction by the dot product with lin vel
		tmpVec2.scl(tmpVec2.dot(body.getLinearVelocity()));

		// substract lin vel to get lin vel component perpendicular to duck direction
		tmpVec2.sub(body.getLinearVelocity());

		// scale for some smoothing factor
		tmpVec2.scl(50.0f);

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

	@Override
	public Array<ForceApplicationPoint> getForceApplicationPoints() {

		tmpVec2.set(0.0f, 8.0f);
		tmpVec2.rotateRad(body.getAngle());
		tmpVec2.add(body.getPosition());

		points.get(0).x = tmpVec2.x;
		points.get(0).y = tmpVec2.y;
		points.get(0).weight = 0.5f;

		tmpVec2.set(0.0f, 0.0f);
		tmpVec2.rotateRad(body.getAngle());
		tmpVec2.add(body.getPosition());

		points.get(2).x = tmpVec2.x;
		points.get(2).y = tmpVec2.y;
		points.get(2).weight = 0.3f;

		tmpVec2.set(0.0f, -8.0f);
		tmpVec2.rotateRad(body.getAngle());
		tmpVec2.add(body.getPosition());

		points.get(1).x = tmpVec2.x;
		points.get(1).y = tmpVec2.y;
		points.get(1).weight = 0.2f;

		return points;
	}
}
