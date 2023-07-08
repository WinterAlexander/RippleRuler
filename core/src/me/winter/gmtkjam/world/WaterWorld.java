package me.winter.gmtkjam.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import me.winter.gmtkjam.GameScreen;
import me.winter.gmtkjam.world.level.Level;
import me.winter.gmtkjam.world.level.Level1;
import me.winter.gmtkjam.world.level.Level2;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class WaterWorld implements ContactListener
{
	private final Array<Entity> entities = new Array<>();
	private final Array<Entity> toRemove = new Array<>();
	private final ObjectMap<ZIndex, Array<Entity>> entitiesByZIndex = new ObjectMap<>();
	private World b2world = null;

	private boolean paused = false;

	private final GameScreen screen;

	private final Level[] levels = new Level[] {
		new Level1(),
		new Level2()
	};
	private int currentLevelIndex = 0;

	public WaterWorld(GameScreen screen)
	{
		this.screen = screen;

		loadLevel(new Level1());
	}

	public void loadLevel(Level level)
	{
		entities.clear();
		entitiesByZIndex.clear();
		toRemove.clear();
		b2world = new World(Vector2.Zero, true);
		b2world.setContactListener(this);
		createBorders();
		addEntity(new Water(this));
		level.load(this);
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
		if(paused)
			return;

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

	@Override
	public void beginContact(Contact contact)
	{

	}

	@Override
	public void endContact(Contact contact)
	{

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		if(paused)
			return;

		Object objA = contact.getFixtureA().getBody().getUserData();
		Object objB = contact.getFixtureB().getBody().getUserData();
		if(!(objA instanceof Boat) && !(objB instanceof Boat))
			return;

		if(objA instanceof Dock || objB instanceof Dock)
		{
			paused = true;
			screen.showLevelCompleteUI(this::nextLevel, this::retryLevel);
			return;
		}

		if(objA instanceof Log || objB instanceof Log
				|| objA instanceof Rock || objB instanceof Rock)
		{
			paused = true;
			screen.showLevelFailedUI(this::retryLevel);
			return;
		}
	}

	public void retryLevel()
	{
		loadLevel(levels[currentLevelIndex]);
		paused = false;
	}

	public void nextLevel()
	{
		currentLevelIndex++;
		currentLevelIndex %= levels.length;
		loadLevel(levels[currentLevelIndex]);
		paused = false;
	}

	public boolean isPaused()
	{
		return paused;
	}

	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}
}
