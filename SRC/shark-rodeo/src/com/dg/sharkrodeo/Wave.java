package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.Factories.AnimationFactory;

public class Wave {
	
	public enum Direction { UP, DOWN, LEFT, RIGHT };
	
	private Direction _direction;
	private Rectangle _bounds;
	private Animation _anim;
	private Vector2 _position;
	private float _speed;
	private float _friction;
	private float _waveTime;
	
	public Wave( Direction direction, Vector2 position, TextureRegion tex ) {
		float frameTime = .2f; //TODO: MAGIC NUMBER
		_direction = direction;
		float waveWidth = 0f;
		float waveHeight = 0f;
		float waveSizeFactorDepth = 0.75f;
		float waveSizeFactorLength = 0.9f;
		
		TextureRegion temp;
		switch( direction ) {
		case UP:
			_anim = AnimationFactory.createAnimation( frameTime, 4, 1, tex );
			temp = _anim.getKeyFrame( 0f, true );
			waveWidth = ( float )temp.getRegionWidth() * waveSizeFactorLength;
			waveHeight = ( float )temp.getRegionHeight() * waveSizeFactorDepth;
			break;
		case DOWN:
			_anim = AnimationFactory.createAnimation( frameTime, 4, 1, tex );
			temp = _anim.getKeyFrame( 0f, true );
			waveWidth = ( float )temp.getRegionWidth() * waveSizeFactorLength;
			waveHeight = ( float )temp.getRegionHeight() * waveSizeFactorDepth;
			break;
		case LEFT:
			_anim = AnimationFactory.createAnimation( frameTime, 1, 4, tex );
			temp = _anim.getKeyFrame( 0f, true );
			waveWidth = ( float )temp.getRegionWidth() * waveSizeFactorDepth;
			waveHeight = ( float )temp.getRegionHeight() * waveSizeFactorLength;
			break;
		case RIGHT:
			_anim = AnimationFactory.createAnimation( frameTime, 1, 4, tex );
			temp = _anim.getKeyFrame( 0f, true );
			waveWidth = ( float )temp.getRegionWidth() * waveSizeFactorDepth;
			waveHeight = ( float )temp.getRegionHeight() * waveSizeFactorLength;
			break;
		}
		_bounds = new Rectangle( position.x - ( waveWidth / 2f ), position.y - ( waveHeight / 2f ), waveWidth, waveHeight );
		_position = new Vector2( position.x, position.y );
		_waveTime = 0f;
		_speed = SharkRodeoConstants.getWaveSpeed();
		_friction = SharkRodeoConstants.getWaveFriction();
	}
	
	public boolean update( float delta ) {
		boolean stillAliveFlag = true;
		Vector2 velocity = new Vector2( 0f, 0f );
		switch( _direction ) {
		case UP:
			velocity.set( 0f, _speed );
			if( _position.y > ( SharkRodeoConstants.getGameBoardHeight() + ( float )( Gdx.graphics.getHeight() / 2 ) ) ) {
				stillAliveFlag = false;
			}
			break;
		case DOWN:
			velocity.set( 0f, -1f * _speed );
			if( _position.y < ( float )( Gdx.graphics.getHeight() / -2 ) ) {
				stillAliveFlag = false;
			}
			break;
		case LEFT:
			velocity.set( -1f * _speed, 0f );
			if( _position.x < ( float )( Gdx.graphics.getWidth() / -2 ) ) {
				stillAliveFlag = false;
			}
			break;
		case RIGHT:
			velocity.set( _speed, 0f );
			if( _position.x > ( SharkRodeoConstants.getGameBoardWidth() + ( float )( Gdx.graphics.getWidth() / 2 ) ) ) {
				stillAliveFlag = false;
			}
			break;
		}
		velocity.mul( delta );
		_position.set( _position.x + velocity.x, _position.y + velocity.y );
		_bounds.x = _position.x - ( _bounds.width / 2f );
		_bounds.y = _position.y - ( _bounds.height / 2f );
		
//		if( _waveTime > 60f ) // TODO: THIS IS INCORRECT. FIX IT
//			return false;
		return stillAliveFlag;
	}
	
	public void updateView( float delta ) {
		_waveTime += delta;
	}
	
	public void pushGameObj( GameObject obj, float delta ) {
		Vector2 pos = obj.getPosition();
		if( _bounds.contains( pos.x, pos.y) ) {
			Vector2 vel = obj.getVelocity();
			switch( _direction ) {
			case UP:
				if( vel.y < _speed )
					obj.setVelocity( vel.x, vel.y + ( delta * _friction ) );
				break;
			case DOWN:
				if( vel.y > ( _speed * -1f ) )
					obj.setVelocity( vel.x, vel.y + ( -1f * delta * _friction ) );
				break;
			case LEFT:
				if( vel.x > ( _speed * -1f ) )
					obj.setVelocity( vel.x +  + ( -1f * delta * _friction ), vel.y );
				break;
			case RIGHT:
				if( vel.x < _speed )
					obj.setVelocity( vel.x + ( delta * _friction ), vel.y );
				break;
			}
		}
	}
	
	public Vector2 getPosition()					{ return _position; }
	public TextureRegion getTexture()			{ return _anim.getKeyFrame( _waveTime, true ); }
	
	

}
