package me.winter.gmtkjam.world;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public interface Floating
{
	Body getBody();

	Array<ForceApplicationPoint> getForceApplicationPoints();

	public static class ForceApplicationPoint
	{
		float x, y, weight;
	}
}
