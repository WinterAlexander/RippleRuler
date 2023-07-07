package me.winter.gmtkjam;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class World
{
	private final Array<Entity> entities = new Array<>();
	private final ObjectMap<ZIndex, Array<Entity>> entitiesByZIndex = new ObjectMap<>();

	public World()
	{
		addEntity(new Water(this));
		addEntity(new Boat(this, new Vector2(8.0f, 4.5f)));
	}

	private void addEntity(Entity entity)
	{
		entities.add(entity);
		if(!entitiesByZIndex.containsKey(entity.getZIndex()))
			entitiesByZIndex.put(entity.getZIndex(), new Array<>());
		entitiesByZIndex.get(entity.getZIndex()).add(entity);
	}

	public void tick(float delta)
	{
		for(int i = 0; i < entities.size; i++)
			entities.get(i).tick(delta);
	}

	public void render(GameScreen screen)
	{
		for(ZIndex zIndex : ZIndex.values)
			for(int i = 0; i < entitiesByZIndex.get(zIndex).size; i++)
				entitiesByZIndex.get(zIndex).get(i).render(screen);
	}
}
