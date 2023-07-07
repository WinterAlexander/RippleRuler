package me.winter.gmtkjam.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
	private final Texture wave;

	private final Vector2 location;
	private float radius = 0.0f;
	private final float maxRadius = 10.0f;

	private final Vector2 tmpVec2 = new Vector2();

	private final float peakWaveMagnitude = 3.0f;
	private final float rangeOfEffect = 0.5f;

	public ShockWave(WaterWorld world, Vector2 location)
	{
		super(world);
		this.location = location;

		wave = new Texture("wave.png");
	}

	@Override
	public void render(GameScreen screen)
	{
		screen.getBatch().setColor(1.0f, 1.0f, 1.0f, (maxRadius - radius) / maxRadius);
		screen.getBatch().draw(wave,
				location.x - radius,
				location.y - radius,
				radius * 2.0f, radius * 2.0f);
		screen.getBatch().setColor(Color.WHITE);
	}

	@Override
	public void tick(float delta)
	{
		radius += delta * 2.0f;

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


			float dstToWave = dstToCenter - radius;

			dstToWave *= 2.0f / rangeOfEffect;

			tmpVec2.scl((float)Math.exp(-(dstToWave * dstToWave)) * peakWaveMagnitude);

			floating.getBody().applyForce(tmpVec2, floating.getBody().getPosition(), true);
		}
	}

	@Override
	public ZIndex getZIndex()
	{
		return ZIndex.WAVE;
	}
}
