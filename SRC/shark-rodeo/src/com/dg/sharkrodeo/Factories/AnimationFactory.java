package com.dg.sharkrodeo.Factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationFactory {

	public static Animation createAnimation(float frameTime, int rows, int cols, TextureRegion srcTexture)
	{
//		Texture tempTexture = new Texture(Gdx.files.internal("animation_sheet.png"));
		TextureRegion[][] tempRegionMatrix = srcTexture.split(srcTexture.getRegionWidth() / cols, srcTexture.getRegionHeight() / rows);
		TextureRegion[] frames = new TextureRegion[rows * cols];
		
		int curIdx = 0;
		for (int i = 0; i < rows; i++)
		{
            for (int j = 0; j < cols; j++)
            {
                    frames[curIdx++] = tempRegionMatrix[i][j];
            }
	    }
		
		return new Animation(frameTime, frames);
	}

	public static Animation createAnimation(float frameTime, int rows, int cols, Texture srcTexture)
	{
//		Texture tempTexture = new Texture(Gdx.files.internal("animation_sheet.png"));
		TextureRegion[][] tempRegionMatrix = TextureRegion.split(srcTexture, srcTexture.getWidth() / cols, srcTexture.getHeight() / rows);
		TextureRegion[] frames = new TextureRegion[rows * cols];
		
		int curIdx = 0;
		for (int i = 0; i < rows; i++)
		{
            for (int j = 0; j < cols; j++)
            {
                    frames[curIdx++] = tempRegionMatrix[i][j];
            }
	    }
		
		return new Animation(frameTime, frames);
	}

}
