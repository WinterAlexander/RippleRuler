package me.winter.gmtkjam;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public abstract class Entity
{
	private final World world;

	public Entity(World world)
	{
		this.world = world;
	}

	public abstract void render(GameScreen screen);

	public abstract void tick(float delta);

	public abstract ZIndex getZIndex();

	public World getWorld()
	{
		return world;
	}
}
