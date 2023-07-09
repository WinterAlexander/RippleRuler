package me.winter.gmtkjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import me.winter.gmtkjam.ui.LinkButton;
import me.winter.gmtkjam.ui.LinkButtonStyle;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-09.
 *
 * @author Alexander Winter
 */
public class VictoryScreen implements Screen {

    private final Stage stage;

    private final Skin skin;

    private final Color bgColor = new Color(0x6084ffFF);

    private final GMTKJam game;

    public VictoryScreen(GMTKJam game)
    {
        this.game = game;
        stage = new Stage(new FitViewport(1600f, 900f));
        skin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        Image logo = new Image(new Texture("ui/victory.png"));
        logo.setSize(1600f, 900.0f);
        logo.setScaling(Scaling.fit);
        logo.setPosition(0.0f,
                0.0f);

        stage.addActor(logo);

        Label start = new Label("Congratulations for beating the game!", skin, "title");
        start.pack();
        start.setPosition(stage.getWidth() / 2.0f - start.getWidth() / 2.0f, 200.0f);
        stage.addActor(start);

        Label ifYouLiked = new Label("If you enjoyed this game feel free to try out my other games.\nI am the creator of MakerKing, a free online multiplayer platformer\nwhere you can create levels and share them with others.", skin, "big");
        ifYouLiked.pack();
        ifYouLiked.setPosition(stage.getWidth() / 2.0f - ifYouLiked.getWidth() / 2.0f, 100.0f);
        stage.addActor(ifYouLiked);

        LinkButtonStyle style = new LinkButtonStyle();
        style.font = skin.getFont("button");
        style.line = new TextureRegionDrawable(new Texture("pixel.png"));
        style.fontColor = Color.WHITE;
        style.downFontColor = Color.GRAY;
        style.overFontColor = Color.LIGHT_GRAY;

        LinkButton url = new LinkButton("makerkinggame.com", style);
        url.pack();
        url.setPosition(stage.getWidth() / 2.0f - url.getWidth() / 2.0f, 50.0f);
        url.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://makerkinggame.com");
            }
        });
        stage.addActor(url);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);
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
}
