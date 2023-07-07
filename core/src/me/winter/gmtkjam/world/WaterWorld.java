package me.winter.gmtkjam.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class WaterWorld
{
	private final Array<Entity> entities = new Array<>();
	private final Array<Entity> toRemove = new Array<>();
	private final ObjectMap<ZIndex, Array<Entity>> entitiesByZIndex = new ObjectMap<>();
	private final World b2world;

	public WaterWorld()
	{
		b2world = new World(Vector2.Zero, true);
		createBorders();

		addEntity(new Water(this));
		addEntity(new Boat(this,
				new Vector2(8.0f, 4.5f),
				new Vector2(1.0f, 0.0f)));
		addEntity(new Log(this,
				new Vector2(12.0f, 4.5f),
				90.0f,
				new Vector2(-1.0f, 0.0f)));
		addEntity(new Rock(this, new Vector2(3.0f, 3.0f), 80.0f));
		addEntity(new Rock(this, new Vector2(10.0f, 3.0f), 0.0f));
		addEntity(new Rock(this, new Vector2(15.0f, 3.0f), 45.0f));
		addEntity(new Rock(this, new Vector2(14.0f, 8.0f), 135.0f));
		addEntity(new Rock(this, new Vector2(13.0f, 8.0f), 30.0f));

		addEntity(new Dock(this, new Vector2(0.0f, 8.0f), 0.0f));
		addEntity(new Dock(this, new Vector2(16.0f, 6.0f), 180.0f));
	}

	private void createBorders()
	{
		{
			BodyDef leftBorderDef = new BodyDef();
			leftBorderDef.position.set(0.0f, 0.0f);

			Body leftBorder = b2world.createBody(leftBorderDef);
			EdgeShape leftEdge = new EdgeShape();
			leftEdge.set(0.0f, 0.0f, 0.0f, 9.0f);

			leftBorder.createFixture(leftEdge, 0.0f);
			leftEdge.dispose();
		}

		{
			BodyDef rightBorderDef = new BodyDef();
			rightBorderDef.position.set(0.0f, 0.0f);

			Body rightBorder = b2world.createBody(rightBorderDef);
			EdgeShape rightEdge = new EdgeShape();
			rightEdge.set(16.0f, 0.0f, 16.0f, 9.0f);

			rightBorder.createFixture(rightEdge, 0.0f);
			rightEdge.dispose();
		}

		{
			BodyDef topBorderDef = new BodyDef();
			topBorderDef.position.set(0.0f, 0.0f);

			Body topBorder = b2world.createBody(topBorderDef);
			EdgeShape topEdge = new EdgeShape();
			topEdge.set(0.0f, 9.0f, 16.0f, 9.0f);

			topBorder.createFixture(topEdge, 0.0f);
			topEdge.dispose();
		}

		{
			BodyDef bottomBorderDef = new BodyDef();
			bottomBorderDef.position.set(0.0f, 0.0f);

			Body bottomBorder = b2world.createBody(bottomBorderDef);
			EdgeShape bottomEdge = new EdgeShape();
			bottomEdge.set(0.0f, 0.0f, 16.0f, 0.0f);

			bottomBorder.createFixture(bottomEdge, 0.0f);
			bottomEdge.dispose();
		}
	}

	public void addEntity(Entity entity)
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

		for(int i = 0; i < toRemove.size; i++)
		{
			Entity entity = toRemove.get(i);

			entities.removeValue(entity, true);
			entitiesByZIndex.get(entity.getZIndex()).removeValue(entity, true);
		}

		b2world.step(delta, 6, 2);
	}

	public void render(GameScreen screen)
	{
		for(ZIndex zIndex : ZIndex.values)
		{
			if(!entitiesByZIndex.containsKey(zIndex))
				continue;
			for(int i = 0; i < entitiesByZIndex.get(zIndex).size; i++)
				entitiesByZIndex.get(zIndex).get(i).render(screen);
		}
	}

	public World getB2world()
	{
		return b2world;
	}

	public Array<Entity> getEntities()
	{
		return entities;
	}

	public void removeEntity(Entity entity)
	{
		toRemove.add(entity);
	}
}
