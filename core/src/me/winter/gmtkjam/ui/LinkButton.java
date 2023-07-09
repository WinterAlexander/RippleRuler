package me.winter.gmtkjam.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * {@link TextButton} that appears like a text link with a bar under its text
 * <p>
 * Created on 2019-06-06.
 *
 * @author Alexander Winter
 */
public class LinkButton extends TextButton
{
	private final LinkButtonStyle style;

	private final Color tmpColor = new Color();

	public LinkButton(String text, Skin skin)
	{
		this(text, skin, "default");
		setSkin(skin);
	}

	public LinkButton(String text, Skin skin, String styleName)
	{
		this(text, skin.get(styleName, LinkButtonStyle.class));
		setSkin(skin);
	}

	public LinkButton(String text, LinkButtonStyle style)
	{
		super(text, style);

		this.style = style;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		if(style.lineWidth <= 0f)
			return;

		tmpColor.set(getFontColor());
		tmpColor.a *= parentAlpha;
		batch.setColor(tmpColor);

		style.line.draw(batch, getX(), getY(), getWidth(), style.lineWidth);
	}
}
