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
public class LevelFailedUI extends Window {
    public LevelFailedUI(Skin skin, Runnable retryAction) {
        super("", skin);

        setModal(true);
        setMovable(false);
        pad(20f);

        add(new Label("You lost", skin, "big")).padBottom(20f).row();

        TextButton retryButton = new TextButton("Retry", skin);
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                retryAction.run();
                remove();
            }
        });

        add(retryButton);
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
