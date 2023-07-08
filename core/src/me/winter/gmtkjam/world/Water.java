package me.winter.gmtkjam.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class Water extends Entity
{
	private final Texture water, cloud;
	private final Color waterColor = new Color(0x6084ffFF);
	private final Color waveColor = new Color(0xafc1ffFF);

	private final float[][][] waterForce = new float[256][144][2];

	private final Array<Cloud> clouds = new Array<>();

	public Water(WaterWorld world)
	{
		super(world);
		water = new Texture("pixel.png");
		cloud = new Texture("cloud.png");

		for(int i = 0; i < 8; i++)
		{
			float x = world.getRandomGenerator().nextFloat() * (256.0f + 24.0f);
			float y = world.getRandomGenerator().nextFloat() * 144.0f;

			boolean tooClose = false;

			for(Cloud existingCloud : clouds)
			{
				float dx = existingCloud.x - x;
				float dy = existingCloud.y - y;

				float dst2 = dx * dx + dy * dy;

				if(dst2 < 64.0f * 64.0f)
				{
					tooClose = true;
					break;
				}
			}

			if(tooClose) {
				i--;
				continue;
			}

			clouds.add(new Cloud(x, y, world.getRandomGenerator().nextFloat() + 1.0f));
		}
	}

	@Override
	public void render(GameScreen screen, ZIndex zIndex)
	{
		float prev = screen.getBatch().getPackedColor();

		screen.getBatch().setColor(waterColor);
		screen.getBatch().draw(water,
				0.0f, 0.0f,
				256.0f, 144.0f);

		screen.getBatch().setColor(1.0f, 1.0f, 1.0f, 1.0f);
		for(Cloud current : clouds)
			screen.getBatch().draw(cloud, /*Math.round(*/current.x - 48f/*)*/, /*Math.round(*/current.y - 32f/*)*/, 48.0f, 32.0f);

		for(int x = 0; x < getWaterTileXCount(); x++)
		{
			for(int y = 0; y < getWaterTileYCount(); y++)
			{
				float force = (float)Math.sqrt(waterForce[x][y][0] * waterForce[x][y][0] +
						waterForce[x][y][1] * waterForce[x][y][1]);

				if(force < 400.0f)
					continue;

				screen.getBatch().setColor(waveColor);
				screen.getBatch().draw(water,
						x, y,
						1f, 1f);
			}
		}
		screen.getBatch().setPackedColor(prev);
	}

	@Override
	public void tick(float delta)
	{
		for(int i = 0; i < clouds.size; i++)
		{
			clouds.get(i).x -= 0.1f * delta * clouds.get(i).speed;
		}

		for(int x = 0; x < getWaterTileXCount(); x++)
		{
			for(int y = 0; y < getWaterTileYCount(); y++)
			{
				waterForce[x][y][0] = 0.0f;
				waterForce[x][y][1] = 0.0f;
			}
		}
	}

	@Override
	public ZIndex[] getZIndices()
	{
		return new ZIndex[] { ZIndex.WATER };
	}

	public void addWaterForce(Vector2 waterForce, float x, float y)
	{
		addWaterForce(waterForce, Math.round(x), Math.round(y));
	}

	public void addWaterForce(Vector2 waterForce, int x, int y)
	{
		if(x < 0 || x >= getWaterTileXCount() || y < 0 || y >= getWaterTileYCount())
			return;

		this.waterForce[x][y][0] += waterForce.x;
		this.waterForce[x][y][1] += waterForce.y;
	}

	public int getWaterTileXCount()
	{
		return 256;
	}

	public int getWaterTileYCount()
	{
		return 144;
	}

	private static class Cloud
	{
		float x, y, speed;

		public Cloud(float x, float y, float speed) {
			this.x = x;
			this.y = y;
			this.speed = speed;
		}
	}
}
