package me.winter.gmtkjam.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class SpiralWave extends Entity
{
	private final TextureRegion wave;

	private final Vector2 location;
	private float radius = 0.0f;
	private final float maxRadius = 5.0f * 16.0f;
	private float angle = 0.0f;

	private final Vector2 tmpVec2 = new Vector2();

	private final float peakWaveMagnitude = 3.0f * 16.0f * 16.0f;
	private final float rangeOfEffect = 0.5f * 16.0f;

	public SpiralWave(WaterWorld world, Vector2 location)
	{
		super(world);
		this.location = location;

		wave = new TextureRegion(new Texture("spiral_wave.png"));
	}

	@Override
	public void render(GameScreen screen, ZIndex zIndex)
	{
		screen.getBatch().setColor(1.0f, 1.0f, 1.0f, (maxRadius - radius) / maxRadius);
		screen.getBatch().draw(wave,
				location.x - radius,
				location.y - radius,
				radius, radius,
				radius * 2.0f, radius * 2.0f,
				1.0f, 1.0f,
				angle);
		screen.getBatch().setColor(Color.WHITE);
	}

	@Override
	public void tick(float delta)
	{
		radius += delta * 2.0f * 16.0f;
		angle += delta * 0.25f * 360.0f;

		if(radius >= maxRadius)
		{
			getWorld().removeEntity(this);
			return;
		}

		for(int i = 0; i < getWorld().getEntities().size; i++) {
			Entity entity = getWorld().getEntities().get(i);
			if(!(entity instanceof Floating))
				continue;

			Floating floating = (Floating)entity;

			tmpVec2.set(floating.getBody().getPosition()).sub(location);

			float dstToCenter = tmpVec2.len();

			if(dstToCenter == 0f)
				return;

			tmpVec2.scl(1.0f / dstToCenter);

			tmpVec2.scl(-0.5f);
			tmpVec2.add(tmpVec2.y * 0.5f, -tmpVec2.x * 0.5f);

			float dstToWave = dstToCenter - radius;

			dstToWave *= 2.0f / rangeOfEffect;

			tmpVec2.scl((float)Math.exp(-(dstToWave * dstToWave)) * peakWaveMagnitude);

			floating.getBody().applyForceToCenter(tmpVec2, true);


			float angle = floating.getBody().getAngle() * MathUtils.radiansToDegrees;
			float waveAngle = tmpVec2.angleDeg();

			float deltaAngle = waveAngle - angle;

			while(deltaAngle > 180.0f)
				deltaAngle -= 360.0f;

			while(deltaAngle < -180.0f)
				deltaAngle += 360.0f;

			if(Math.abs(deltaAngle) > 5.0f)
				deltaAngle = 5.0f * Math.signum(deltaAngle);

			floating.getBody().applyAngularImpulse(deltaAngle * delta * MathUtils.degreesToRadians, true);
		}
	}

	@Override
	public ZIndex[] getZIndices()
	{
		return new ZIndex[] { ZIndex.WAVE };
	}
}
