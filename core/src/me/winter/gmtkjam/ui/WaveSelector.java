package me.winter.gmtkjam.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import me.winter.gmtkjam.GameScreen;
import me.winter.gmtkjam.WaveType;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-08.
 *
 * @author Alexander Winter
 */
public class WaveSelector extends Table
{
    public WaveSelector(GameScreen screen)
    {
        TextureRegionDrawable shockUp = new TextureRegionDrawable(new Texture("ui/shock_gray.png"));
        ImageButton shock = new ImageButton(shockUp,
                shockUp.tint(new Color(0.5f, 0.5f, 0.5f, 1.0f)),
                new TextureRegionDrawable(new Texture("ui/shock.png")));
        shock.getStyle().imageOver = shockUp.tint(new Color(0.75f, 0.75f, 0.75f, 1.0f));
        shock.setStyle(shock.getStyle());
        shock.getCell(shock.getImage()).size(100, 100);
        shock.setChecked(true);

        TextureRegionDrawable spiralUp = new TextureRegionDrawable(new Texture("ui/spiral_gray.png"));
        ImageButton spiral = new ImageButton(spiralUp,
                spiralUp.tint(new Color(0.5f, 0.5f, 0.5f, 1.0f)),
                new TextureRegionDrawable(new Texture("ui/spiral.png")));
        spiral.getStyle().imageOver = spiralUp.tint(new Color(0.75f, 0.75f, 0.75f, 1.0f));
        spiral.setStyle(spiral.getStyle());
        spiral.getCell(spiral.getImage()).size(100, 100);

        TextureRegion flippedSpiralGray = new TextureRegion(new Texture("ui/spiral_gray.png"));
        flippedSpiralGray.flip(true, false);

        TextureRegion flippedSpiral = new TextureRegion(new Texture("ui/spiral.png"));
        flippedSpiral.flip(true, false);

        TextureRegionDrawable spiralUpRev = new TextureRegionDrawable(flippedSpiralGray);
        ImageButton spiralRev = new ImageButton(spiralUpRev,
                spiralUpRev.tint(new Color(0.5f, 0.5f, 0.5f, 1.0f)),
                new TextureRegionDrawable(flippedSpiral));
        spiralRev.getStyle().imageOver = spiralUpRev.tint(new Color(0.75f, 0.75f, 0.75f, 1.0f));
        spiralRev.setStyle(spiralRev.getStyle());
        spiralRev.getCell(spiralRev.getImage()).size(100, 100);

        add(shock).size(100.0f, 100.0f).fill().padRight(20f);
        add(spiral).size(100.0f, 100.0f).fill().padRight(20f);
        add(spiralRev).size(100.0f, 100.0f).fill();

        shock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spiral.setChecked(false);
                spiralRev.setChecked(false);
                screen.setWaveType(WaveType.SHOCK);
            }
        });

        spiral.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                shock.setChecked(false);
                spiralRev.setChecked(false);
                screen.setWaveType(WaveType.SPIRAL);
            }
        });

        spiralRev.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spiral.setChecked(false);
                shock.setChecked(false);
                screen.setWaveType(WaveType.SPIRAL_REVERSE);
            }
        });

        pack();
    }

    @Override
    protected void setStage(Stage stage)
    {
        super.setStage(stage);

        if(getStage() != null)
            setPosition(getStage().getWidth() - getWidth() - 20.0f, getStage().getHeight() - getHeight() - 20.0f);
    }
}
