package com.dg.sharkrodeo;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {

	private boolean _updatePosition;
	private Vector2 _position;
	private Vector2 _lastPosition;
	private Vector2 _velocity;
	private Vector2 _acceleration;
	
	private Circle[] _boundsList;
	protected Vector2[] _boundsOffsets;
	
	protected Circle _collisionBounds;
	
	protected float _maxSpeed;
	protected float _accelerationRate;
	
	protected GameObjectView _view;
	
	public enum Direction { UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT };
	private Direction _direction;
	private boolean _clipping;
	
	public GameObject( GameBoard board, float objRadius ) {
		_position = new Vector2( 0f, 0f );
		_lastPosition = new Vector2( _position );
		_velocity = new Vector2( 0f, 0f );
		_acceleration = new Vector2( 0f, 0f );
		_maxSpeed = 200f;
		_accelerationRate = 20f;
		_view = new GameObjectView();
		_direction = Direction.UP;
		_updatePosition = true;
		_clipping = true;
		_collisionBounds = new Circle( _position.x, _position.y, objRadius );
	}
	
	public boolean isOnScreen() {
		return true;
	}
	
	public void update( float delta ) {
		if( _updatePosition ) {
			Vector2 unitVelocity = ( new Vector2( _velocity ) );
			float speed = unitVelocity.len();
			if( speed != 0f )
				unitVelocity.mul( 1.0f / speed );	
	
			Vector2 playerAccelDelta = ( new Vector2( _acceleration ) ).mul( delta );
			
			Vector2 newVelocity = new Vector2( _velocity );
			newVelocity.add( playerAccelDelta );
			
			Vector2 friction = new Vector2( unitVelocity );
			friction.mul( -1.0f * SharkRodeoConstants.getFriction( speed ) * delta );
			
			if( friction.len() > newVelocity.len() )
				_velocity.set( 0.0f, 0.0f );
			else {
				_velocity.add( friction );
				if( newVelocity.len() < getMaxSpeed() )
					_velocity.add( playerAccelDelta );
			}
			
			Vector2 positionDelta = ( new Vector2( _velocity ) ).mul( delta );
			_lastPosition.set( _position );
			_position.add( positionDelta );
			_collisionBounds.x = _position.x;
			_collisionBounds.y = _position.y;
			for( int i = 0; i < _boundsList.length; ++i ) {
				_boundsList[ i ].x = _position.x + _boundsOffsets[ i ].x;
				_boundsList[ i ].y = _position.y + _boundsOffsets[ i ].y;
			}
			
			if( _clipping )
				adjustPositionForBounds();
		}
		
	}
	
	public void updateView( float delta ) {
		_view.update( delta );
	}

	private void adjustPositionForBounds() {
		Rectangle bounds = GameBoard.getInstance().getBounds();
		Vector2 center = new Vector2( bounds.x + ( bounds.width / 2f ), bounds.y + ( bounds.height / 2f ) );

		float xPos, yPos;
		if( _collisionBounds.x > center.x ) {
			if( _collisionBounds.y > center.y ) {
				xPos = _collisionBounds.x + _collisionBounds.radius;
				yPos = _collisionBounds.y + _collisionBounds.radius;
				if( !bounds.contains( xPos, _collisionBounds.y ) ) {
					setXPosition( GameBoard.getInstance().getWidth() - ( _collisionBounds.radius * 1.1f ) );
					_velocity.x *= -1f;
				}
				if( !bounds.contains( _collisionBounds.x, yPos ) ) {
					setYPosition( GameBoard.getInstance().getHeight() - ( _collisionBounds.radius * 1.1f ) );
					_velocity.y *= -1f;
				}
			}
			else {
				xPos = _collisionBounds.x + _collisionBounds.radius;
				yPos = _collisionBounds.y - _collisionBounds.radius;
				if( !bounds.contains( xPos, _collisionBounds.y ) ) {
					setXPosition( GameBoard.getInstance().getWidth() - ( _collisionBounds.radius * 1.1f ) );
					_velocity.x *= -1f;
				}
				if( !bounds.contains( _collisionBounds.x, yPos ) ) {
					setYPosition( 0f + ( _collisionBounds.radius * 1.1f ) );
					_velocity.y *= -1f;
				}
			}
		} // if ( collisionBounds.x ? center.x )
		else {
			if( _collisionBounds.y > center.y ) {
				xPos = _collisionBounds.x - _collisionBounds.radius;
				yPos = _collisionBounds.y + _collisionBounds.radius;
				if( !bounds.contains( xPos, _collisionBounds.y ) ) {
					setXPosition( 0f + ( _collisionBounds.radius * 1.1f ) );
					_velocity.x *= -1f;
				}
				if( !bounds.contains( _collisionBounds.x, yPos ) ) {
					setYPosition( GameBoard.getInstance().getHeight() - ( _collisionBounds.radius * 1.1f ) );
					_velocity.y *= -1f;
				}
			}
			else {
				xPos = _collisionBounds.x - _collisionBounds.radius;
				yPos = _collisionBounds.y - _collisionBounds.radius;
				if( !bounds.contains( xPos, _collisionBounds.y ) ) {
					setXPosition( 0f + ( _collisionBounds.radius * 1.1f ) );
					_velocity.x *= -1f;
				}
				if( !bounds.contains( _collisionBounds.x, yPos ) ) {
					setYPosition( 0f + ( _collisionBounds.radius * 1.1f ) );
					_velocity.y *= -1f;
				}
			}
		} // else
	} // private void adjustPositionForBounds()
	
	protected boolean isInBounds() {
		Rectangle bounds = GameBoard.getInstance().getBounds();
		Vector2 center = new Vector2( bounds.x + ( bounds.width / 2f ), bounds.y + ( bounds.height / 2f ) );

		float xPos, yPos;
		if( _collisionBounds.x > center.x ) {
			if( _collisionBounds.y > center.y ) {
				xPos = _collisionBounds.x + _collisionBounds.radius;
				yPos = _collisionBounds.y + _collisionBounds.radius;
				if( !bounds.contains( xPos, _collisionBounds.y ) ) {
					return false;
				}
				if( !bounds.contains( _collisionBounds.x, yPos ) ) {
					return false;
				}
			}
			else {
				xPos = _collisionBounds.x + _collisionBounds.radius;
				yPos = _collisionBounds.y - _collisionBounds.radius;
				if( !bounds.contains( xPos, _collisionBounds.y ) ) {
					return false;
				}
				if( !bounds.contains( _collisionBounds.x, yPos ) ) {
					return false;
				}
			}
		}
		else {
			if( _collisionBounds.y > center.y ) {
				xPos = _collisionBounds.x - _collisionBounds.radius;
				yPos = _collisionBounds.y + _collisionBounds.radius;
				if( !bounds.contains( xPos, _collisionBounds.y ) ) {
					return false;
				}
				if( !bounds.contains( _collisionBounds.x, yPos ) ) {
					return false;
				}
			}
			else {
				xPos = _collisionBounds.x - _collisionBounds.radius;
				yPos = _collisionBounds.y - _collisionBounds.radius;
				if( !bounds.contains( xPos, _collisionBounds.y ) ) {
					return false;
				}
				if( !bounds.contains( _collisionBounds.x, yPos ) ) {
					return false;
				}
			}
		}

		return true;
	}
	
	public void accelerateInDirection( Vector2 dir, int fuckflag ) {
		Vector2 unitDir = dir.mul( 1f / dir.len() );
		float angle = unitDir.angle();
		
		if( fuckflag == 0) {
			Gdx.app.log( SharkRodeoConstants.LOG_TAG, "FUCK YOU, DALE!" );				
		}
		else if (fuckflag==1){
//			Gdx.app.log( SharkRodeoConstants.LOG_TAG, "  " );				
		}
		else if (fuckflag==2){
		}
		
		if( angle < 22.5f )
			_direction = Direction.RIGHT;
		else if( angle < 67.5f )
			_direction = Direction.UP_RIGHT;
		else if( angle < 112.5 )
			_direction = Direction.UP;
		else if( angle < 157.5 )
			_direction = Direction.UP_LEFT;
		else if( angle < 202.5 )
			_direction = Direction.LEFT;
		else if( angle < 247.5 )
			_direction = Direction.DOWN_LEFT;
		else if( angle < 292.5 )
			_direction = Direction.DOWN;
		else if( angle < 337.5 )
			_direction = Direction.DOWN_RIGHT;
		else
			_direction = Direction.RIGHT;
		
		_acceleration.set( unitDir.mul( getAccelerationRate() ) );
	}
	
	public void killAcceleration() {
		_acceleration.set( new Vector2( 0f, 0f ) );
	}
	
	public void setPosition( float x, float y ) {
		_lastPosition.set( _position );
		_position.set( x, y );
		_collisionBounds.x = x;
		_collisionBounds.y = y;
		for( int i = 0; i < _boundsList.length; ++i ) {
			_boundsList[ i ].x = _position.x + _boundsOffsets[ i ].x;
			_boundsList[ i ].y = _position.y + _boundsOffsets[ i ].y;
		}
	}

	protected void setXPosition( float x ) {
		_lastPosition.set( _position );
		_position.x = x;
		_collisionBounds.x = x;
		for( int i = 0; i < _boundsList.length; ++i ) {
			_boundsList[ i ].x = _position.x + _boundsOffsets[ i ].x;
		}
	}
	
	protected void setYPosition( float y ) {
		_lastPosition.set( _position );
		_position.y = y;
		_collisionBounds.y = y;
		for( int i = 0; i < _boundsList.length; ++i ) {
			_boundsList[ i ].y = _position.y + _boundsOffsets[ i ].y;
		}
	}
	
	protected void initBoundsList( int count ) {
		_boundsList = new Circle[ count ];
		_boundsOffsets = new Vector2[ count ];
	}
	protected void setBounds( int idx, Circle bounds, Vector2 offset ) {
		_boundsList[ idx ] = bounds;
		_boundsOffsets[ idx ] = offset;
	}
	
	public Circle[] getBounds() {
		return _boundsList;
	}
	
	protected Vector2 getVelocity() 								{ return _velocity; }
	public void setVelocity( float x, float y ) 					{ _velocity.set(x, y); }

	protected Vector2 getAcceleration() 							{ return _acceleration; }

	public float getMaxSpeed() 										{ return _maxSpeed;	}
	public void setMaxSpeed( float maxSpeed ) 						{	_maxSpeed = maxSpeed;	}

	public float getAccelerationRate() 								{ return _accelerationRate;	}
	public void setAccelerationRate(float accelerationRate) {
		_accelerationRate = accelerationRate; }
	
	public TextureRegion getTexture() {
		TextureRegion tex = _view.getCurrentAnimationFrame();
		return tex;
	}
	
	public List<ParticleEffect> getEmitters()						{ return _view.getEmitters(); }
	
	public Direction getDirection()									{ return _direction; }
	protected void setDirection( Direction newDirection )			{ _direction = newDirection; }
	
	public void addAnimation( String name, AnimationSequence anim ) {
		_view.addAnimation( name, anim );
	}
	
	public void setAnimState( String newState ) {
		_view.setAnimState( newState );
	}
	
	public void transitionAnimState( String newState ) {
		_view.transitionAnimState( newState );
	}

	public void setUpatePosition( boolean newVal ) {
		_updatePosition = newVal;
	}
	
	public void killVelocity() {
		_velocity.set( 0f, 0f );
	}
	
	public boolean isLastPositionCloserToPoint( float x, float y ) {
		Vector2 curDelta = ( new Vector2( getPosition() ) ).sub( x, y );
		Vector2 lastDelta = ( new Vector2( getLastPosition() ) ).sub( x, y );
		if( lastDelta.len2() < curDelta.len2() )
			return true;
		return false;
	}
	
	public Vector2 getPosition()									{ return _position; }
	public Vector2 getLastPosition()								{ return _lastPosition; }
	public void setClipping( boolean val )							{ _clipping = val; }
	public boolean getClipping()									{ return _clipping; }
	public Circle getCollisionBounds()								{ return _collisionBounds; }
	
	protected float getMass()										{ return 1.0f; }
}
