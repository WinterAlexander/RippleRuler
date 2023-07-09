package me.winter.gmtkjam.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import me.winter.gmtkjam.GameScreen;
import me.winter.gmtkjam.VictoryScreen;
import text.formic.Stringf;

import static java.lang.Float.isNaN;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-08.
 *
 * @author Alexander Winter
 */
public class GameCompleteUI extends Window {
    public GameCompleteUI(GameScreen screen, Skin skin, int levelId, float time, float score, Runnable retryAction) {
        super("", skin);

        setModal(true);
        setMovable(false);
        pad(20f);

        add(new Label("Level complete!", skin, "title")).padBottom(20f).row();

        String timeKey = "level-" + levelId + "-time";
        String scoreKey = "level-" + levelId + "-score";


        float bestTime = screen.getGame().getPreferences().getFloat(timeKey, Float.NaN);
        float bestScore = screen.getGame().getPreferences().getFloat(scoreKey, Float.NaN);

        add(new Label("Time: " + Stringf.format("%.3f", time) + " seconds" + (isNaN(bestTime) ? "" : " (Best: " + Stringf.format("%.3f", bestTime) + " seconds)"), skin, "big")).padBottom(20f).row();
        add(new Label("Score: " + score + (isNaN(bestScore) ? "" : " (Best: " + bestScore + ")"), skin, "big")).padBottom(20f).row();
        add(new Label("Score is higher the less ripples you use to complete the level. Feel free to beat your best time or best score.", skin))
                .padBottom(20f).fill().getActor().setWrap(true);
        row();

        bestTime = isNaN(bestTime) ? time : Math.min(bestTime, time);
        bestScore = isNaN(bestScore) ?  score : Math.max(bestScore, score);

        screen.getGame().getPreferences().putFloat(timeKey, bestTime);
        screen.getGame().getPreferences().putFloat(scoreKey, bestScore);
        screen.getGame().getPreferences().flush();

        Table buttonRow = new Table();

        TextButton retryButton = new TextButton("Retry", skin);
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.click.play(0.5f);
                retryAction.run();
                remove();
            }
        });

        buttonRow.add(retryButton).padRight(20f);

        TextButton nextButton = new TextButton("Next", skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.click.play(0.5f);
                screen.getGame().setScreen(new VictoryScreen(screen.getGame()));
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
