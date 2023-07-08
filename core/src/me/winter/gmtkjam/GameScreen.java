package me.winter.gmtkjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.winter.gmtkjam.ui.LevelFailedUI;
import me.winter.gmtkjam.ui.LevelCompleteUI;
import me.winter.gmtkjam.world.ShockWave;
import me.winter.gmtkjam.world.SpiralWave;
import me.winter.gmtkjam.world.WaterWorld;
import me.winter.gmtkjam.world.level.Level1;
import me.winter.gmtkjam.world.level.Level2;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class GameScreen extends InputAdapter implements Screen
{
	private final SpriteBatch batch;
	private final WaterWorld world;

	private final OrthographicCamera camera;
	private final Stage stage;

	private final Vector3 tmpVec3 = new Vector3();

	private boolean debug = false;
	private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	private final Skin skin;

	public GameScreen()
	{
		batch = new SpriteBatch();
		world = new WaterWorld(this);

		camera = new OrthographicCamera(256.0f, 144.0f);
		camera.position.set(128f, 72f, 0.0f);
		camera.update();
		stage = new Stage(new FitViewport(1600f, 900f));

		skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
	}

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));
	}

	@Override
	public void render(float delta)
	{
		world.tick(delta);
		stage.act(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		world.render(this);

		batch.end();

		stage.draw();

		if(debug)
			debugRenderer.render(world.getB2world(), camera.combined);
	}

	@Override
	public void resize(int width, int height)
	{
		getStage().getViewport().update(width, height, true);
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void hide()
	{

	}

	@Override
	public boolean keyDown(int keycode)
	{
		if(keycode >= Keys.NUM_1 && keycode <= Keys.NUM_9)
		{
			world.loadLevel((keycode - Keys.NUM_1) % world.levels.length);
			world.setPaused(false);
		}
		else if(keycode == Keys.F12)
			debug = !debug;
		else
			return false;
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if(screenX < stage.getViewport().getScreenX()
		|| screenY < stage.getViewport().getScreenY()
		|| screenX > stage.getViewport().getScreenX() + stage.getViewport().getScreenWidth()
		|| screenY > stage.getViewport().getScreenY() + stage.getViewport().getScreenHeight())
			return false;

		tmpVec3.set(screenX, screenY, 0.0f);


		camera.unproject(tmpVec3,
				stage.getViewport().getScreenX(),
				stage.getViewport().getScreenY(),
				stage.getViewport().getScreenWidth(),
				stage.getViewport().getScreenHeight());

		if(button == Buttons.LEFT)
			getWorld().addEntity(new ShockWave(world,
					new Vector2(tmpVec3.x, tmpVec3.y)));
		else
			getWorld().addEntity(new SpiralWave(world,
					new Vector2(tmpVec3.x, tmpVec3.y),
					Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)));

		return true;
	}

	@Override
	public boolean scrolled(float amountX, float amountY)
	{
		if(!debug)
			return false;

		if(amountY > 0.0f)
			camera.zoom *= 2.0f;
		else
			camera.zoom /= 2.0f;
		camera.update();

		return true;
	}

	public void showLevelCompleteUI(float time, float score, Runnable nextLevelCallback, Runnable retryCallback)
	{
		getStage().addActor(new LevelCompleteUI(skin, time, score, retryCallback, nextLevelCallback));
	}

	public void showLevelFailedUI(Runnable retryCallback)
	{
		getStage().addActor(new LevelFailedUI(skin, retryCallback));
	}

	public void showGameCompleteUI()
	{

	}

	@Override
	public void dispose()
	{
		batch.dispose();
	}

	public SpriteBatch getBatch()
	{
		return batch;
	}

	public WaterWorld getWorld()
	{
		return world;
	}

	public OrthographicCamera getCamera()
	{
		return camera;
	}

	public Stage getStage()
	{
		return stage;
	}
}
