package me.winter.gmtkjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-08.
 *
 * @author Alexander Winter
 */
public class TitleScreen extends InputAdapter implements Screen {

    private final Stage stage;

    private final Skin skin;

    private final Color bgColor = new Color(0x6084ffFF);

    private final GMTKJam game;

    private final Sound click;

    public TitleScreen(GMTKJam game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(1600f, 900f));
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        click = Gdx.audio.newSound(Gdx.files.internal("click.ogg"));

        Image logo = new Image(new Texture("ui/logo.png"));
        logo.setSize(1600f, 711.0f);
        logo.setScaling(Scaling.fit);
        logo.setPosition(0.0f,
                900.0f - 711.0f);

        stage.addActor(logo);

        Label start = new Label("Click to start", skin, "title");
        start.pack();
        start.setPosition(stage.getWidth() / 2.0f - start.getWidth() / 2.0f, 100.0f);

        stage.addActor(start);

        Label credits = new Label("Game made by Alexander Winter for the GMTK 2023 game jam\nFonts and UI graphics taken from https://github.com/czyzby/gdx-skins", skin, "big");
        stage.addActor(credits);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {

        stage.act(delta);

        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        click.play(0.5f);
        game.setScreen(new GameScreen());
        return true;
    }
}
