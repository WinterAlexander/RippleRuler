package me.winter.gmtkjam.world;

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
	private final Vector2 location;
	private float radius = 0.0f;
	private final float maxRadius = 4.0f * 16.0f;
	private float angle = 0.0f;

	private final Vector2 tmpVec2 = new Vector2();

	private final float peakWaveMagnitude = 3.0f * 16.0f;

	private final boolean clockwise;

	public SpiralWave(WaterWorld world, Vector2 location, boolean clockwise)
	{
		super(world);
		this.location = location;
		this.clockwise = clockwise;
	}

	@Override
	public void render(GameScreen screen, ZIndex zIndex) {}

	@Override
	public void tick(float delta)
	{
		radius += delta * 2.0f * 16.0f;
		angle += delta * 0.25f * 360.0f * (clockwise ? -1.0f : 1.0f);

		if(radius >= maxRadius)
		{
			getWorld().removeEntity(this);
			return;
		}
		float strength = Math.min(4.0f * (maxRadius - radius) / maxRadius, 1.0f);

		for(int i = 0; i < getWorld().getEntities().size; i++) {
			Entity entity = getWorld().getEntities().get(i);
			if(!(entity instanceof Floating))
				continue;

			Floating floating = (Floating)entity;

			for(Floating.ForceApplicationPoint point : floating.getForceApplicationPoints())
			{
				tmpVec2.set(point.x, point.y).sub(location);

				float dstToCenter = tmpVec2.len();

				if(dstToCenter == 0f)
					return;

				tmpVec2.scl(1.0f / dstToCenter).scl(-1.0f);

				tmpVec2.set(tmpVec2.y * (clockwise ? -1.0f : 1.0f),
						tmpVec2.x * (clockwise ? 1.0f : -1.0f));


				float dstToWave = Math.min(10.0f * (radius - dstToCenter) / radius, 1.0f);

				if(dstToWave <= 0.0f)
					dstToWave = 0.0f;

				tmpVec2.scl(peakWaveMagnitude * strength * dstToWave * point.weight);

				floating.getBody().applyLinearImpulse(tmpVec2.x, tmpVec2.y, point.x, point.y, true);
			}
		}

		for(int x = 0; x < getWorld().getWater().getWaterTileXCount(); x++)
		{
			for(int y = 0; y < getWorld().getWater().getWaterTileYCount(); y++)
			{
				tmpVec2.set(x + 0.5f, y + 0.5f).sub(location);

				float dstToCenter = tmpVec2.len();
				float angle = tmpVec2.angleRad() + this.angle;

				if(dstToCenter == 0f)
					continue;
				tmpVec2.scl(1.0f / dstToCenter);
				tmpVec2.scl(-0.5f);
				tmpVec2.add(tmpVec2.y * 0.5f * (clockwise ? -1.0f : 1.0f),
						tmpVec2.x * 0.5f * (clockwise ? 1.0f : -1.0f));

				float dstToWave = dstToCenter - radius;

				if(dstToWave <= 0.0f)
					dstToWave = 0.0f;

				dstToWave += 1.0f;

				tmpVec2.scl(peakWaveMagnitude * strength * 16.0f / dstToWave);

				float visualAngleFactor = (float)Math.max(0.0f, Math.sin(angle * 8.0f + dstToCenter * (clockwise ? -1.0f : 1.0f)));
				tmpVec2.scl(visualAngleFactor).scl(2.0f);

				getWorld().getWater().addWaterForce(tmpVec2, x, y);
			}
		}
	}

	@Override
	public ZIndex[] getZIndices()
	{
		return new ZIndex[] { ZIndex.WAVE };
	}
}
