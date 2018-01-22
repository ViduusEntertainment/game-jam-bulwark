/**
 * Copyright 2017-2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Jan 21, 2018 by Ethan Toney
 */
package org.viduus.charon.gamejam.graphics.ui;

import org.apache.commons.lang3.tuple.Pair;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.opengl.OpenGLGraphics;
import org.viduus.charon.global.graphics.opengl.font.OpenGLFont;
import org.viduus.charon.global.graphics.util.Size;

/**
 * @author Ethan Toney
 *
 */
public class UIString {
	
	private static final int ANIMATION_BORDER = 1;
	private static final float INITIAL_SIZE = -1;

	private class UIStringSegment {
		
		final Animation<?> left_animation;
		final String right_text;
		
		UIStringSegment(Animation<?> animation, String text) {
			left_animation = animation;
			right_text = text;
		}
		
		float getWidth() {
			float animation_width = ANIMATION_BORDER;
			if (left_animation != null) {
				animation_width += left_animation.getCurrentFrame().getBoundingSize().width + ANIMATION_BORDER;
			}
			return OpenGLFont.getStringWidth(right_text) + animation_width + 1;
		}
		
		float getHeight() {
			float animation_height = 0;
			if (left_animation != null) {
				animation_height = left_animation.getCurrentFrame().getBoundingSize().height;
			}
			return Math.max(animation_height + 2 * ANIMATION_BORDER, OpenGLFont.getLineHeight());
		}
		
	}
	
	private final UIStringSegment[] segments;
	private float width = -1, height = -1;
	
	@SafeVarargs
	public UIString(Pair<Animation<?>,String> ... string_segments) {
		segments = new UIStringSegment[string_segments.length];
		int i=0;
		for (Pair<Animation<?>,String> string_segment : string_segments) {
			UIStringSegment segment = new UIStringSegment(string_segment.getLeft(), string_segment.getRight());
			segments[i++] = segment;
		}
	}

	public UIString(Animation<?> animation, String string) {
		this(Pair.of(animation, string));
	}
	
	public UIString(String string) {
		this(Pair.of(null, string));
	}
	
	public float getWidth() {
		if (width == INITIAL_SIZE)
			updateWidth();
		return width;
	}
	
	public float getHeight() {
		if (height == INITIAL_SIZE)
			updateHeight();
		return height;
	}
	
	public void render(OpenGLGraphics graphics, float d_sec, int x, int y) {
		float
			left = x,
			height = getHeight(),
			top = y - height,
			d_str = height - (height - OpenGLFont.getLineHeight()) / 2 - 2;
		
		for (UIStringSegment segment : segments) {
			// draw animation
			if (segment.left_animation != null) {
				Size ani_size = segment.left_animation.getCurrentFrame().getBoundingSize();
				float d_ani = (height - ani_size.height) / 2;
				segment.left_animation.renderAnimation(graphics, d_sec, (int) left, (int) (top + d_ani), 1);
				left += ani_size.width + ANIMATION_BORDER;
			}
			// draw text
			if (segment.right_text != null) {
				OpenGLFont.drawString2D(graphics, segment.right_text, (int) left, (int) (top + d_str));
				left += OpenGLFont.getStringWidth(segment.right_text) + ANIMATION_BORDER + 1;
			}
		}
		
		updateWidth();
		updateHeight();
	}
	
	private void updateWidth() {
		float width = 0;
		for (UIStringSegment segment : segments) {
			width += segment.getWidth();
		}
		this.width = width;
	}
	
	private void updateHeight() {
		float height = 0;
		for (UIStringSegment segment : segments) {
			height = Math.max(height, segment.getHeight());
		}
		this.height = height;
	}
	
}
