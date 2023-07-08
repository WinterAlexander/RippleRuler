package me.winter.gmtkjam.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
	private final Texture water;
	private final Color waterColor = new Color(0x4e6bcfFF);
	private final Color tmpColor = new Color();

	private final float[][] waterHeight = new float[160][90];

	public Water(WaterWorld world)
	{
		super(world);
		water = new Texture("pixel.png");
	}

	@Override
	public void render(GameScreen screen)
	{

		float prev = screen.getBatch().getPackedColor();
		for(int x = 0; x < 160; x++)
		{
			for(int y = 0; y < 90; y++)
			{
				float height = waterHeight[x][y];

				float whiteness = Math.min(Math.abs(height), 1.0f);
				tmpColor.set(waterColor);
				tmpColor.mul(1.0f - whiteness);
				tmpColor.add(whiteness, whiteness, whiteness, whiteness);
				screen.getBatch().setColor(tmpColor);
				screen.getBatch().draw(water,
						x * 0.1f, y * 0.1f,
						0.1f, 0.1f);
			}
		}
		screen.getBatch().setPackedColor(prev);
	}

	@Override
	public void tick(float delta)
	{
		for(int x = 0; x < 160; x++)
		{
			for(int y = 0; y < 90; y++)
			{
				waterHeight[x][y] = 0.0f;
			}
		}
	}

	@Override
	public ZIndex getZIndex()
	{
		return ZIndex.WATER;
	}

	public void addWaterHeight(float height, int x, int y)
	{
		waterHeight[x][y] += height;
	}

	public int getWaterTileXCount()
	{
		return 160;
	}

	public int getWaterTileYCount()
	{
		return 90;
	}
}
