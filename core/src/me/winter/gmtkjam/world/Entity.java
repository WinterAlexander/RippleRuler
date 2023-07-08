package me.winter.gmtkjam.world;

import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public abstract class Entity
{
	private final WaterWorld world;

	public Entity(WaterWorld world)
	{
		this.world = world;
	}

	public abstract void render(GameScreen screen, ZIndex zIndex);

	public abstract void tick(float delta);

	public abstract ZIndex[] getZIndices();

	public WaterWorld getWorld()
	{
		return world;
	}
}
