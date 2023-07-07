package me.winter.gmtkjam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class Boat extends Entity
{
	private final TextureRegion boat;

	private final Vector2 location = new Vector2();
	private final Vector2 direction;

	public Boat(World world, Vector2 location)
	{
		super(world);
		this.boat = new TextureRegion(new Texture("boat.png"));
		this.location.set(location);
		this.direction = new Vector2(1.0f, 0.0f);
	}

	@Override
	public void render(GameScreen screen)
	{
		screen.getBatch().draw(boat,
				location.x, location.y,
				0.5f, 0.5f,
				1.0f, 1.0f,
				1.0f, 1.0f,
				direction.angleDeg(new Vector2(0.0f, 1.0f)));
	}

	@Override
	public void tick(float delta)
	{

	}

	@Override
	public ZIndex getZIndex()
	{
		return ZIndex.BOAT;
	}
}
