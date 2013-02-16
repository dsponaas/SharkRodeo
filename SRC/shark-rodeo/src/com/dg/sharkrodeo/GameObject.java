package com.dg.sharkrodeo;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {

//	private GameBoard GameBoard.getInstance();
	
	private boolean _updatePosition;
	private Vector2 _position;
	private Vector2 _velocity;
	private Vector2 _acceleration;
	
//	private Rectangle _bounds;
	private Circle[] _boundsList;			// TODO: NOTE! this no longer applies to physics collision
	protected Vector2[] _boundsOffsets;
	
	protected Circle _collisionBounds;
	
	protected float _maxSpeed;
	protected float _accelerationRate;
	
//	private Texture _texture;
	protected GameObjectView _view;
	
	public enum Direction { UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT };
	private Direction _direction;
	private boolean _clipping;
	
	public GameObject(GameBoard board, float objRadius)
	{
		_position = new Vector2(0f, 0f);
		_velocity = new Vector2(0f, 0f);
		_acceleration = new Vector2(0f, 0f);
		_maxSpeed = 200f;
		_accelerationRate = 20f;
//		_bounds = new Rectangle();
		_view = new GameObjectView();
//		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***********: _velocity: x:" + _velocity.x + " y:" + _velocity.y);
		_direction = Direction.UP;
		_updatePosition = true;
		_clipping = true;
		_collisionBounds = new Circle(_position.x, _position.y, objRadius);
	}
	
	public boolean isOnScreen()
	{
		return true;
	}
	
	public void update(float delta)
	{
		if(_updatePosition)
		{
			Vector2 unitVelocity = (new Vector2(_velocity));
			float speed = unitVelocity.len();
			if(speed != 0f)
				unitVelocity.mul(1.0f / speed);	
	
			Vector2 playerAccelDelta = (new Vector2(_acceleration)).mul(delta);
//			float excessSpeed = _maxSpeed -_velocity.len(); 
//			if(excessSpeed < 0)
//			{
//				Vector2 excessVelocity = (new Vector2(unitVelocity)).mul(excessSpeed);
//				velocityDelta.add(excessVelocity);
//			}
			
			Vector2 newVelocity = new Vector2(_velocity);
			newVelocity.add(playerAccelDelta);
			
			Vector2 friction = new Vector2(unitVelocity);
			friction.mul(-1 * SharkRodeoConstants.getFriction(speed) * delta);
			
			if(friction.len() > newVelocity.len())
				_velocity.set(0f, 0f);
			else
			{
				_velocity.add(friction);
				if(newVelocity.len() < getMaxSpeed())
					_velocity.add(playerAccelDelta);
			}
			
			//update position
			Vector2 positionDelta = (new Vector2(_velocity)).mul(delta);
			_position.add(positionDelta);
			_collisionBounds.x = _position.x;
			_collisionBounds.y = _position.y;
			for(int i = 0; i < _boundsList.length; ++i)
			{
				_boundsList[i].x = _position.x + _boundsOffsets[i].x;
				_boundsList[i].y = _position.y + _boundsOffsets[i].y;
			}
			
			if(_clipping)
				adjustPositionForBounds();
		}
		
	}
	
	public void updateView(float delta)
	{
		_view.update(delta);
	}

	private void adjustPositionForBounds()
	{
		Rectangle bounds = GameBoard.getInstance().getBounds();
		Vector2 center = new Vector2(bounds.x + (bounds.width / 2f), bounds.y + (bounds.height / 2f));
//		for(Circle cur : _boundsList)

//		Circle cur = _boundsList[0];
		float xPos, yPos;
		if(_collisionBounds.x > center.x)
		{
			if(_collisionBounds.y > center.y)
			{
				xPos = _collisionBounds.x + _collisionBounds.radius;
				yPos = _collisionBounds.y + _collisionBounds.radius;
				if(!bounds.contains(xPos, _collisionBounds.y))
				{
					setXPosition(GameBoard.getInstance().getWidth() - (_collisionBounds.radius * 1.1f));
					_velocity.x *= -1f;
				}
				if(!bounds.contains(_collisionBounds.x, yPos))
				{
					setYPosition(GameBoard.getInstance().getHeight() - (_collisionBounds.radius * 1.1f));
					_velocity.y *= -1f;
				}
			}
			else
			{
				xPos = _collisionBounds.x + _collisionBounds.radius;
				yPos = _collisionBounds.y - _collisionBounds.radius;
				if(!bounds.contains(xPos, _collisionBounds.y))
				{
					setXPosition(GameBoard.getInstance().getWidth() - (_collisionBounds.radius * 1.1f));
					_velocity.x *= -1f;
				}
				if(!bounds.contains(_collisionBounds.x, yPos))
				{
					setYPosition(0f + (_collisionBounds.radius * 1.1f));
					_velocity.y *= -1f;
				}
			}
		}
		else
		{
			if(_collisionBounds.y > center.y)
			{
				xPos = _collisionBounds.x - _collisionBounds.radius;
				yPos = _collisionBounds.y + _collisionBounds.radius;
				if(!bounds.contains(xPos, _collisionBounds.y))
				{
					setXPosition(0f + (_collisionBounds.radius * 1.1f));
					_velocity.x *= -1f;
				}
				if(!bounds.contains(_collisionBounds.x, yPos))
				{
					setYPosition(GameBoard.getInstance().getHeight() - (_collisionBounds.radius * 1.1f));
					_velocity.y *= -1f;
				}
			}
			else
			{
				xPos = _collisionBounds.x - _collisionBounds.radius;
				yPos = _collisionBounds.y - _collisionBounds.radius;
				if(!bounds.contains(xPos, _collisionBounds.y))
				{
					setXPosition(0f + (_collisionBounds.radius * 1.1f));
					_velocity.x *= -1f;
				}
				if(!bounds.contains(_collisionBounds.x, yPos))
				{
					setYPosition(0f + (_collisionBounds.radius * 1.1f));
					_velocity.y *= -1f;
				}
			}
		}
	}
	
	protected boolean isInBounds()
	{
		Rectangle bounds = GameBoard.getInstance().getBounds();
		Vector2 center = new Vector2(bounds.x + (bounds.width / 2f), bounds.y + (bounds.height / 2f));

		float xPos, yPos;
		if(_collisionBounds.x > center.x)
		{
			if(_collisionBounds.y > center.y)
			{
				xPos = _collisionBounds.x + _collisionBounds.radius;
				yPos = _collisionBounds.y + _collisionBounds.radius;
				if(!bounds.contains(xPos, _collisionBounds.y))
				{
					return false;
				}
				if(!bounds.contains(_collisionBounds.x, yPos))
				{
					return false;
				}
			}
			else
			{
				xPos = _collisionBounds.x + _collisionBounds.radius;
				yPos = _collisionBounds.y - _collisionBounds.radius;
				if(!bounds.contains(xPos, _collisionBounds.y))
				{
					return false;
				}
				if(!bounds.contains(_collisionBounds.x, yPos))
				{
					return false;
				}
			}
		}
		else
		{
			if(_collisionBounds.y > center.y)
			{
				xPos = _collisionBounds.x - _collisionBounds.radius;
				yPos = _collisionBounds.y + _collisionBounds.radius;
				if(!bounds.contains(xPos, _collisionBounds.y))
				{
					return false;
				}
				if(!bounds.contains(_collisionBounds.x, yPos))
				{
					return false;
				}
			}
			else
			{
				xPos = _collisionBounds.x - _collisionBounds.radius;
				yPos = _collisionBounds.y - _collisionBounds.radius;
				if(!bounds.contains(xPos, _collisionBounds.y))
				{
					return false;
				}
				if(!bounds.contains(_collisionBounds.x, yPos))
				{
					return false;
				}
			}
		}

		return true;
	}
	
	public void accelerateInDirection(Vector2 dir)
	{
		Vector2 unitDir = dir.mul(1f / dir.len());
		
		
		
		float angle = unitDir.angle();
		
		if(angle < 22.5f)
			_direction = Direction.RIGHT;
		else if(angle < 67.5f)
			_direction = Direction.UP_RIGHT;
		else if (angle < 112.5)
			_direction = Direction.UP;
		else if (angle < 157.5)
			_direction = Direction.UP_LEFT;
		else if (angle < 202.5)
			_direction = Direction.LEFT;
		else if (angle < 247.5)
			_direction = Direction.DOWN_LEFT;
		else if(angle < 292.5)
			_direction = Direction.DOWN;
		else if (angle < 337.5)
			_direction = Direction.DOWN_RIGHT;
		else
			_direction = Direction.RIGHT;
		//0 Right
		//45 upright
		//90 Up
		//135 upleft
		//180 Left
		//225 downleft
		//270 down
		//315 downright
		
		_acceleration.set(unitDir.mul(getAccelerationRate()));
	}
	
	public void killAcceleration()
	{
		_acceleration.set(new Vector2(0f, 0f));
	}
	
	public Vector2 getPosition() {	return _position; }
	public void setPosition(float x, float y)
	{
		_position.set(x, y);
		_collisionBounds.x = x;
		_collisionBounds.y = y;
		for(int i = 0; i < _boundsList.length; ++i)
		{
			_boundsList[i].x = _position.x + _boundsOffsets[i].x;
			_boundsList[i].y = _position.y + _boundsOffsets[i].y;
		}
//		_bounds.setX(position.x);
//		_bounds.setY(position.y);
	}
	protected void setXPosition(float x)
	{
		_position.x = x;
		_collisionBounds.x = x;
		for(int i = 0; i < _boundsList.length; ++i)
		{
			_boundsList[i].x = _position.x + _boundsOffsets[i].x;
		}
	}
	
	protected void setYPosition(float y)
	{
		_position.y = y;
		_collisionBounds.y = y;
		for(int i = 0; i < _boundsList.length; ++i)
		{
			_boundsList[i].y = _position.y + _boundsOffsets[i].y;
		}
	}
	
//	public float getWidth() 									{ return _bounds.width; }
//	protected void setWidth(float width) 						{ _bounds.width = width; }

//	public float getHeight() 									{ return _bounds.height; }
//	protected void setHeight(float height) 						{ _bounds.height = height; }

	protected void initBoundsList(int count)
	{
		_boundsList = new Circle[count];
		_boundsOffsets = new Vector2[count];
	}
	protected void setBounds(int idx, Circle bounds, Vector2 offset)
	{
		_boundsList[idx] = bounds;
		_boundsOffsets[idx] = offset;
	}
	public Circle[] getBounds()
	{
		return _boundsList;
	}
	
	protected Vector2 getVelocity() 					{ return _velocity; }
	public void setVelocity(float x, float y) 			{ _velocity.set(x, y); }

//	private void setXVelocity(float val)						{ _velocity.x = val; }
//	private void setYVelocity(float val)						{ _velocity.y = val; }
	protected Vector2 getAcceleration() 				{ return _acceleration; }
//	private void setAcceleration(Vector2 acceleration) { _acceleration = acceleration; }

	public float getMaxSpeed() 					{ return _maxSpeed;	}
	public void setMaxSpeed(float maxSpeed) 					{	_maxSpeed = maxSpeed;	}

	public float getAccelerationRate() { return _accelerationRate;	}
	public void setAccelerationRate(float accelerationRate) 	{ _accelerationRate = accelerationRate; }
	
	public TextureRegion getTexture()
	{
		TextureRegion tex = _view.getCurrentAnimationFrame();
		return tex;
	}
	
	public List<ParticleEffect> getEmitters()					{ return _view.getEmitters(); }
	
	public Direction getDirection()								{ return _direction; }
	protected void setDirection(Direction newDirection)			{ _direction = newDirection; }
	
	public void addAnimation(String name, AnimationSequence anim)
	{
		_view.addAnimation(name, anim);
	}
	
	public void setAnimState(String newState)
	{
		_view.setAnimState(newState);
	}
	public void transitionAnimState(String newState)
	{
		_view.transitionAnimState(newState);
	}

	public void setUpatePosition(boolean newVal)
	{
		_updatePosition = newVal;
	}
	
	public void killVelocity()
	{
		_velocity.set(0f, 0f);
	}
	
	public void setClipping(boolean val)						{ _clipping = val; }
	public boolean getClipping()								{ return _clipping; }
	public Circle getCollisionBounds()							{ return _collisionBounds; }
	
	protected float getMass()									{ return 1.0f; }
}
