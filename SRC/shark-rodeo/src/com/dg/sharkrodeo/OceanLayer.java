package com.dg.sharkrodeo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class OceanLayer
{
	private Animation _animation;
	private Vector2 _position;
	private float _curAlpha;
	private float _alphaDeltaPerSecond;
	private float _elapsedTime;
	private float _textureWidth;
	private float _textureHeight;
//	private static float TIME_OFFSET_CRAPTAR = 0f;
	private static Random _rand = new Random();
	
	public OceanLayer( Animation animation, float x, float y )
	{
		this( animation, x, y, ( float )_rand.nextDouble() );
	}

	public OceanLayer( Animation animation, float x, float y, float alpha )
	{
		_animation = animation;
		_position = new Vector2( x, y );
		_curAlpha = alpha;
		_alphaDeltaPerSecond = .2f;
		_elapsedTime = 0f;
//		_elapsedTime = TIME_OFFSET_CRAPTAR;
//		TIME_OFFSET_CRAPTAR += 0.18f;
		
		TextureRegion tex = animation.getKeyFrame( 0, true );
		_textureWidth = tex.getRegionWidth();
		_textureHeight = tex.getRegionHeight();
	}
	
	public void update( float delta )
	{
		_elapsedTime += delta;
		
		float alphaDelta = _alphaDeltaPerSecond * delta;
		float alphaTemp = _curAlpha + alphaDelta;
		if( ( alphaTemp < 0f ) || ( alphaTemp > 1f ) )
		{
			alphaDelta *= -1f;
			_alphaDeltaPerSecond *= -1f;
		}
		_curAlpha += alphaDelta;
	}
	
	public List<Vector2> getTexturePositions( float originX, float originY )
	{
		float frameWidth = Gdx.graphics.getWidth();
		float frameHeight = Gdx.graphics.getHeight();
		
		//This isnt the most efficient way of doing this but for the numbers involved it should be fine, yes?
		float startX = ( frameWidth * -1f ) + _position.x;
		float startY = ( frameHeight * -1f ) + _position.y;
		while( ( startX + _textureWidth ) < originX )
			startX += _textureWidth;
		while( ( startY + _textureHeight ) < originY )
			startY += _textureHeight;
		
		List<Vector2> retval = new ArrayList<Vector2>();
		
		for( float curX = startX; curX < ( originX + frameWidth + _textureWidth ); curX += _textureWidth )
		{
			for( float curY = startY; curY < ( originY + frameHeight + _textureHeight ); curY += _textureHeight )
			{
				retval.add( new Vector2( curX, curY ) );
			}			
		}
		
		return retval;
	}
	
	public float getAlpha()
	{
		float alpha = _curAlpha - .0f;
		return ( alpha < 0f ) ? 0f : alpha;
	}
	
	public TextureRegion getTexture()	{ return _animation.getKeyFrame( _elapsedTime, true ); }

}	
