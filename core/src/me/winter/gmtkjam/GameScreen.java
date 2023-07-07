package me.winter.gmtkjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-07.
 *
 * @author Alexander Winter
 */
public class GameScreen implements Screen
{
	private final SpriteBatch batch;
	private final World world;

	protected OrthographicCamera camera;
	protected Stage stage;

	public GameScreen()
	{
		batch = new SpriteBatch();
		world = new World();

		camera = new OrthographicCamera(16.0f, 9.0f);
		camera.position.set(8.0f, 4.5f, 0.0f);
		camera.update();
		stage = new Stage(new FitViewport(1600f, 900f));
	}

	@Override
	public void show()
	{

	}

	@Override
	public void render(float delta)
	{
		world.tick(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		world.render(this);

		batch.end();
	}

	@Override
	public void resize(int width, int height)
	{

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
	public void dispose()
	{
		batch.dispose();
	}

	public SpriteBatch getBatch()
	{
		return batch;
	}
}
