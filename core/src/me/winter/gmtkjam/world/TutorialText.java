package me.winter.gmtkjam.world;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import me.winter.gmtkjam.GameScreen;

/**
 * Undocumented :(
 * <p>
 * Created on 2023-07-09.
 *
 * @author Alexander Winter
 */
public class TutorialText extends Entity {
    private final Vector2 location;
    private final String text;

    private final BitmapFont font;
    private final GlyphLayout glyphLayout;

    public TutorialText(WaterWorld world, Vector2 location, String text) {
        super(world);
        this.location = location;
        this.text = text;
        this.font = world.getScreen().getSkin().getFont("title");
        font.getData().setScale(0.2f);
        this.glyphLayout = new GlyphLayout(font, text);
        font.getData().setScale(1.0f);
    }

    @Override
    public void render(GameScreen screen, ZIndex zIndex) {
        font.getData().setScale(0.2f);
        font.draw(screen.getBatch(), glyphLayout, location.x, location.y);
        font.getData().setScale(1.0f);
    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public ZIndex[] getZIndices() {
        return new ZIndex[] { ZIndex.TUTORIAL_TEXT };
    }
}
