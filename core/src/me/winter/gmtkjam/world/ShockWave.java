package me.winter.gmtkjam.world;

import com.badlogic.gdx.math.Vector2;
import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class ShockWave extends Entity
{
	private final Vector2 location;
	private float radius = 0.0f;
	private final float maxRadius = 5.0f * 16.0f;

	private final Vector2 tmpVec2 = new Vector2();

	private final float peakWaveMagnitude = 2000f;
	private final float rangeOfEffect = 0.5f * 16.0f;

	public ShockWave(WaterWorld world, Vector2 location)
	{
		super(world);
		this.location = location;
	}

	@Override
	public void render(GameScreen screen, ZIndex zIndex) {}

	@Override
	public void tick(float delta)
	{
		radius += delta * 2.0f * 16.0f;

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

			tmpVec2.set(floating.getBody().getPosition()).sub(location);

			float dstToCenter = tmpVec2.len();

			if(dstToCenter == 0f)
				continue;

			tmpVec2.scl(1.0f / dstToCenter);

			float dstToWave = dstToCenter - radius;
			dstToWave *= 2.0f / rangeOfEffect;

			tmpVec2.scl((float)Math.exp(-(dstToWave * dstToWave)) * peakWaveMagnitude * strength);

			floating.getBody().applyForceToCenter(tmpVec2, true);
		}

		int minX = Math.max(Math.round(location.x - radius - rangeOfEffect), 0);
		int maxX = Math.min(Math.round(location.x + radius + rangeOfEffect), getWorld().getWater().getWaterTileXCount() - 1);
		int minY = Math.max(Math.round(location.y - radius - rangeOfEffect), 0);
		int maxY = Math.min(Math.round(location.y + radius + rangeOfEffect), getWorld().getWater().getWaterTileYCount() - 1);

		for(int x = minX; x <= maxX; x++)
		{
			for(int y = minY; y <= maxY; y++)
			{
				tmpVec2.set(x + 0.5f, y + 0.5f).sub(location);

				float dstToCenter = tmpVec2.len2();

				if(dstToCenter > (2.0f * rangeOfEffect + radius) * (2.0f * rangeOfEffect + radius))
					continue;

				if(dstToCenter == 0f)
					continue;

				dstToCenter = (float)Math.sqrt(dstToCenter);

				tmpVec2.scl(1.0f / dstToCenter);

				float dstToWave = dstToCenter - radius;
				dstToWave *= 2.0f / rangeOfEffect;

				tmpVec2.scl((float)Math.exp(-(dstToWave * dstToWave)) * peakWaveMagnitude * strength);
				tmpVec2.scl(0.25f);

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
