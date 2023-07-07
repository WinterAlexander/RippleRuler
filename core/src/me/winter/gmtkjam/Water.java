package me.winter.gmtkjam;

import com.badlogic.gdx.graphics.Texture;
import me.winter.gdx.utils.math.VectorUtil;

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

	public Water(World world)
	{
		super(world);
		water = new Texture("water.png");
	}

	@Override
	public void render(GameScreen screen)
	{
		screen.getBatch().draw(water,
				0.0f, 0.0f,
				16.0f, 9.0f);
	}

	@Override
	public void tick(float delta)
	{

	}

	@Override
	public ZIndex getZIndex()
	{
		return ZIndex.WATER;
	}
}
