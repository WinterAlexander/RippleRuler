package me.winter.gmtkjam.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import me.winter.gmtkjam.WaveType;
import me.winter.gmtkjam.world.level.*;

import java.util.Random;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class WaterWorld implements ContactListener
{
	private static final float SPIRAL_SPAWNING_DELAY = 0.5f;
	private static final float WAVE_SPAWNING_DELAY = 0.2f;
	public static final float WORLD_WIDTH = 256.0f;
	public static final float WORLD_HEIGHT = 144.0f;

	private final Array<Entity> entities = new Array<>();
	private final Array<Entity> toRemove = new Array<>();
	private final ObjectMap<ZIndex, Array<Entity>> entitiesByZIndex = new ObjectMap<>();
	private World b2world = null;

	private Water water = null;

	private boolean paused = false;

	private final GameScreen screen;

	public float energyUsed = 0.0f;

	public final Level[] levels = new Level[] {
			new Level1(),
			new Level2(),
			new Level3(),
			new Level4(),
			new Level5(),
			new Level6(),
			new Level7(),
			new Level8(),
			new Level9()
	};
	public int currentLevelIndex = 0;

	private final Random randomGenerator = new Random();

	private float time = 0.0f, lastSpawnedWave = 0.0f;

	private final Vector2 tmpVec2 = new Vector2();

	private final Sound[] splashes;
	private final Sound win, loss;

	private final Music[] tracks;
	private Music currentTrack = null;

	public WaterWorld(GameScreen screen)
	{
		this.screen = screen;

		splashes = new Sound[] {
				Gdx.audio.newSound(Gdx.files.internal("splash1.ogg")),
				Gdx.audio.newSound(Gdx.files.internal("splash2.ogg")),
				Gdx.audio.newSound(Gdx.files.internal("splash3.ogg")),
				Gdx.audio.newSound(Gdx.files.internal("splash4.ogg"))
		};

		tracks = new Music[] {
				Gdx.audio.newMusic(Gdx.files.internal("music/track0.ogg")),
				Gdx.audio.newMusic(Gdx.files.internal("music/track1.ogg")),
				Gdx.audio.newMusic(Gdx.files.internal("music/track2.ogg")),
				Gdx.audio.newMusic(Gdx.files.internal("music/track3.ogg")),
		};

		win = Gdx.audio.newSound(Gdx.files.internal("win.ogg"));
		loss = Gdx.audio.newSound(Gdx.files.internal("loss.ogg"));

		loadLevel(new Level1());
	}

	public void loadLevel(int index)
	{
		currentLevelIndex = index;
		loadLevel(levels[index]);
	}

	public void loadLevel(Level level)
	{
		time = 0.0f;
		lastSpawnedWave = 0.0f;
		energyUsed = 0.0f;
		entities.clear();
		entitiesByZIndex.clear();
		toRemove.clear();
		b2world = new World(Vector2.Zero, true);
		b2world.setContactListener(this);
		createBorders();
		addEntity(water = new Water(this));
		level.load(this);
		if(currentTrack != null)
			currentTrack.stop();
		currentTrack = tracks[currentLevelIndex % tracks.length];
		currentTrack.setLooping(true);
		currentTrack.setVolume(0.05f);
		currentTrack.play();
	}

	private void createBorders()
	{
		{
			BodyDef leftBorderDef = new BodyDef();
			leftBorderDef.position.set(0.0f, 0.0f);

			Body leftBorder = b2world.createBody(leftBorderDef);
			EdgeShape leftEdge = new EdgeShape();
			leftEdge.set(0.0f, 0.0f, 0.0f, WORLD_HEIGHT);

			leftBorder.createFixture(leftEdge, 0.0f);
			leftEdge.dispose();
		}

		{
			BodyDef rightBorderDef = new BodyDef();
			rightBorderDef.position.set(0.0f, 0.0f);

			Body rightBorder = b2world.createBody(rightBorderDef);
			EdgeShape rightEdge = new EdgeShape();
			rightEdge.set(WORLD_WIDTH, 0.0f, WORLD_WIDTH, WORLD_HEIGHT);

			rightBorder.createFixture(rightEdge, 0.0f);
			rightEdge.dispose();
		}

		{
			BodyDef topBorderDef = new BodyDef();
			topBorderDef.position.set(0.0f, 0.0f);

			Body topBorder = b2world.createBody(topBorderDef);
			EdgeShape topEdge = new EdgeShape();
			topEdge.set(0.0f, WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);

			topBorder.createFixture(topEdge, 0.0f);
			topEdge.dispose();
		}

		{
			BodyDef bottomBorderDef = new BodyDef();
			bottomBorderDef.position.set(0.0f, 0.0f);

			Body bottomBorder = b2world.createBody(bottomBorderDef);
			EdgeShape bottomEdge = new EdgeShape();
			bottomEdge.set(0.0f, 0.0f, WORLD_WIDTH, 0.0f);

			bottomBorder.createFixture(bottomEdge, 0.0f);
			bottomEdge.dispose();
		}
	}

	public void addEntity(Entity entity)
	{
		entities.add(entity);
		for(ZIndex zIndex : entity.getZIndices())
		{
			if(!entitiesByZIndex.containsKey(zIndex))
				entitiesByZIndex.put(zIndex, new Array<>());
			entitiesByZIndex.get(zIndex).add(entity);
		}
	}

	public void tick(float delta)
	{
		if(paused)
			return;

		time += delta;

		for(int i = 0; i < entities.size; i++)
			entities.get(i).tick(delta);

		for(int i = 0; i < toRemove.size; i++)
		{
			Entity entity = toRemove.get(i);

			entities.removeValue(entity, true);
			for(ZIndex zIndex : entity.getZIndices())
				entitiesByZIndex.get(zIndex).removeValue(entity, true);
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
				entitiesByZIndex.get(zIndex).get(i).render(screen, zIndex);
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
		if(paused)
			return;

		Object objA = contact.getFixtureA().getBody().getUserData();
		Object objB = contact.getFixtureB().getBody().getUserData();

		if((objA instanceof Beach || objB instanceof Beach)
				&& (objA instanceof Floating || objB instanceof Floating))
		{
			Floating boat = objA instanceof Floating ? (Floating)objA : (Floating)objB;

			Beach beach = objA instanceof Beach ? (Beach)objA : (Beach)objB;

			tmpVec2.set(boat.getBody().getLinearVelocity());

			float dot = tmpVec2.dot(beach.getDirection());
			if(dot > 0.0f)
				return;

			tmpVec2.mulAdd(beach.getDirection(), -dot);

			boat.getBody().setLinearVelocity(tmpVec2.x, tmpVec2.y);
			boat.getBody().setAngularVelocity(0.0f);
		}
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
			if(currentTrack != null)
				currentTrack.stop();
			win.play(0.5f);

			if(currentLevelIndex + 1 < levels.length)
				screen.showLevelCompleteUI(currentLevelIndex, getTime(), Math.max(0.0f, 100.0f - energyUsed), this::nextLevel, this::retryLevel);
			else
				screen.showGameCompleteUI(currentLevelIndex, getTime(), Math.max(0.0f, 100.0f - energyUsed), this::retryLevel);
			return;
		}

		if(objA instanceof Log || objB instanceof Log
		|| objA instanceof Rock || objB instanceof Rock
		|| objA instanceof Duck || objB instanceof Duck)
		{
			paused = true;
			if(currentTrack != null)
				currentTrack.stop();
			loss.play(0.5f);
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

	public Water getWater()
	{
		return water;
	}

	public Random getRandomGenerator() {
		return randomGenerator;
	}

	public float getTime() {
		return time;
	}

	public void spawnWave(WaveType waveType, float x, float y) {
		if(lastSpawnedWave + (waveType == WaveType.SHOCK ? WAVE_SPAWNING_DELAY : SPIRAL_SPAWNING_DELAY) > time)
			return;


		splashes[randomGenerator.nextInt(splashes.length)].play();
		if(waveType == WaveType.SHOCK) {
			addEntity(new ShockWave(this, new Vector2(x, y)));
			energyUsed += 1.0f;
		}
		else {
			addEntity(new SpiralWave(this, new Vector2(x, y),
					waveType == WaveType.SPIRAL_REVERSE));
			energyUsed += 1.0f;
		}
		lastSpawnedWave = time;
	}

	public GameScreen getScreen() {
		return screen;
	}
}
