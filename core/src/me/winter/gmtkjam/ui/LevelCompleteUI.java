package me.winter.gmtkjam.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-08.
 *
 * @author Alexander Winter
 */
public class LevelCompleteUI extends Window {
    public LevelCompleteUI(Skin skin, float time, float score, Runnable retryAction, Runnable nextLevelAction) {
        super("", skin);

        setModal(true);
        setMovable(false);
        pad(20f);

        add(new Label("Level complete!", skin, "title")).padBottom(20f).row();
        add(new Label("Time: " + time + " seconds", skin, "big")).padBottom(20f).row();
        add(new Label("Score: " + score, skin, "big")).padBottom(20f).row();

        Table buttonRow = new Table();

        TextButton retryButton = new TextButton("Retry", skin);
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                retryAction.run();
                remove();
            }
        });

        buttonRow.add(retryButton).padRight(20f);

        TextButton nextButton = new TextButton("Next level", skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextLevelAction.run();
                remove();
            }
        });

        buttonRow.add(nextButton);

        add(buttonRow);
        pack();
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if(getStage() != null) {
            setPosition(getStage().getWidth() / 2.0f - getWidth() / 2.0f,
                    getStage().getHeight() / 2.0f - getHeight() / 2.0f);
        }
    }
}
