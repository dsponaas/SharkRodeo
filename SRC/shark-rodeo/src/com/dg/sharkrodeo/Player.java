package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.HudUtilityButton.UtilityButtonType;
import com.dg.sharkrodeo.PlayerModifier.PlayerModifierType;

public class Player extends GameObject
{
	
	public enum PlayerState { DEAD, IDLE, MOVING, MOUNTING, MOUNTED };
	private PlayerState _playerState;
	private Shark _ridingShark;
	private float _ridingScoreTimer;
	private boolean _sprinting;
	private PlayerModifier[] _modifiers;
	
	private final int MAX_MODIFIERS = 3;
//	private boolean _alive;
	
	public Player(GameBoard board)
	{
		super(board, 36f); //TODO: magic number
		
		setAccelerationRate(SharkRodeoConstants.getPlayerAcceleration());
		setMaxSpeed(SharkRodeoConstants.getPlayerMaxSpeed());
		
		initBoundsList(1);
		Circle bounds = new Circle(this.getPosition().x, this.getPosition().y, SharkRodeoConstants.getPlayerHitboxRadius());
		setBounds(0, bounds, new Vector2(0, 0));
		
		this.setXPosition(100f);
		this.setYPosition(100f);

		_playerState = PlayerState.DEAD;
		
		TextureAtlas atlas = new TextureAtlas(SharkRodeoConstants.getPlayerPack());
		Animation swimLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("swim_left_4frames"));
		Animation swimRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("swim_right_4frames"));
		Animation swimDownAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("swim_down_4frames"));
		Animation swimUpAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("swim_up_4frames"));
		Animation idleLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("idle_left_4frames"));
		Animation idleRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("idle_right_4frames"));

		AnimationSequence swimLeftSequence = new AnimationSequence(swimLeftAnim);
		AnimationSequence swimRightSequence = new AnimationSequence(swimRightAnim);
		AnimationSequence swimDownSequence = new AnimationSequence(swimDownAnim);
		AnimationSequence swimUpSequence = new AnimationSequence(swimUpAnim);
		AnimationSequence idleLeftSequence = new AnimationSequence(idleLeftAnim);
		AnimationSequence idleRightSequence = new AnimationSequence(idleRightAnim);
		AnimationSequence mountUpSequence = new AnimationSequence(idleRightAnim, false);
		
		addAnimation("move_left", swimLeftSequence);
		addAnimation("move_right", swimRightSequence);
		addAnimation("move_down", swimDownSequence);
		addAnimation("move_up", swimUpSequence);
		addAnimation("idle_left", idleLeftSequence);
		addAnimation("idle_right", idleRightSequence);
		addAnimation("mount_up", mountUpSequence);

		setAnimState("idle_right");
		
		_ridingShark = null;
		_ridingScoreTimer = 0f;
		
		_sprinting = false;
		
		_modifiers = new PlayerModifier[MAX_MODIFIERS];
		//_alive = false;
	}
	
	public void update(float delta)
	{
//		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***********: collisionbounds: x:" + _collisionBounds.x + " y:" + _collisionBounds.y + " r:" + _collisionBounds.radius);
		
		if(_playerState == PlayerState.MOUNTING)
		{
			Vector2 sharkPos = _ridingShark.getPosition();
			this.setPosition(sharkPos.x, sharkPos.y);
		}
		else if(_playerState == PlayerState.MOUNTED)
		{
			Vector2 sharkPos = _ridingShark.getPosition();
			this.setPosition(sharkPos.x, sharkPos.y);
			
			_ridingScoreTimer -= delta;
			float deltaDelta = 0f - _ridingScoreTimer;
			if(deltaDelta > 0f)
			{
				_ridingScoreTimer = SharkRodeoConstants.RIDING_SCORE_TIME - deltaDelta;
				GameState.addToScore(10);
			}
			
			if(_ridingShark.getHealth() < 0f)
			{
				tamingShark();
			}
		}
		
		for(int i = 0; i < _modifiers.length; ++i)
		{
			if(_modifiers[i] != null)
			{
				if(!_modifiers[i].update(delta))
					_modifiers[i] = null;
				
			}
		}
		
		super.update(delta);
	}
	
	public TextureRegion getTexture()
	{
		if(_playerState == PlayerState.MOUNTED)
			return null;
		
		TextureRegion tex = _view.getCurrentAnimationFrame();
		if(tex != null)
			return tex;
		if(_playerState == PlayerState.MOUNTING)
		{
			_playerState = PlayerState.MOUNTED;
			_ridingShark.mountShark();
			this.mountShark();
		}
		
		return null;
	}
	
	public void mountingShark(Shark shark)
	{
		this.setDirection(shark.getDirection());
		_playerState = PlayerState.MOUNTING;
		
		switch(this.getDirection())
		{
		case UP:
			this.setAnimState("mount_up");
			break;
		case DOWN:
			this.setAnimState("mount_up");
			break;
		case LEFT:
			this.setAnimState("mount_up");
			break;
		case RIGHT:
			this.setAnimState("mount_up");
			break;
		case UP_LEFT:
			this.setAnimState("mount_up");
			break;
		case UP_RIGHT:
			this.setAnimState("mount_up");
			break;
		case DOWN_LEFT:
			this.setAnimState("mount_up");
			break;
		case DOWN_RIGHT:
			this.setAnimState("mount_up");
			break;
		}

		_ridingShark = shark;
		_ridingShark.mountingShark();
		
		GameBoard.getInstance().getHud().deactivateButton(UtilityButtonType.SPRINT);
		
		this.setUpatePosition(false);
	}
	
	public void mountShark()
	{
		_ridingScoreTimer = SharkRodeoConstants.RIDING_SCORE_TIME;
	}
	
	public void dismountingShark()
	{
		this.setPosition(100f, 100);
		_playerState = PlayerState.IDLE;
		this.killAcceleration();
		this.killVelocity();
		this.setUpatePosition(true);
		
		this.setAnimState("idle_left");
		GameBoard.getInstance().resetCamera();
		
		_ridingShark.dismount();
	}
	
	public void tamingShark()
	{
		_playerState = PlayerState.IDLE;
		this.killAcceleration();
		this.killVelocity();
		this.setUpatePosition(true);
		
		GameBoard.getInstance().tameShark(_ridingShark);
	}
	
	public void moveTo(Vector2 dest)
	{
		Direction oldDirection = this.getDirection();
		Vector2 delta = dest.sub(this.getPosition());
		this.accelerateInDirection(delta);
		Direction newDirection = this.getDirection();

		float distance = 18f;
		float diagDist = (float)Math.sqrt((distance * distance) / 2f);
		
		if((newDirection != oldDirection) || (_playerState != PlayerState.MOVING))
		{
			switch(newDirection)
			{
			case UP:
				this.setAnimState("move_up");
				_boundsOffsets[0].set(0f, distance);
				break;
			case DOWN:
				this.setAnimState("move_down");
				_boundsOffsets[0].set(0f, -1f * distance);
				break;
			case LEFT:
				this.setAnimState("move_left");
				_boundsOffsets[0].set(-1f * distance, 0f);
				break;
			case RIGHT:
				this.setAnimState("move_right");
				_boundsOffsets[0].set(distance, 0f);
				break;
			case UP_LEFT:
				this.setAnimState("move_up");
				_boundsOffsets[0].set(-1f * diagDist, diagDist);
				break;
			case UP_RIGHT:
				this.setAnimState("move_up");
				_boundsOffsets[0].set(diagDist, diagDist);
				break;
			case DOWN_LEFT:
				this.setAnimState("move_down");
				_boundsOffsets[0].set(-1f* diagDist, -1f* diagDist);
				break;
			case DOWN_RIGHT:
				this.setAnimState("move_down");
				_boundsOffsets[0].set(diagDist, -1f* diagDist);
				break;
			}
			_playerState = PlayerState.MOVING;
		}
	}
	
	public void stopMoving()
	{
		_boundsOffsets[0].set(0f, 0f);
		switch(this.getDirection())
		{
		case UP:
			this.transitionAnimState("idle_right");
			break;
		case DOWN:
			this.transitionAnimState("idle_left");
			break;
		case LEFT:
			this.transitionAnimState("idle_left");
			break;
		case RIGHT:
			this.transitionAnimState("idle_right");
			break;
		case UP_LEFT:
			this.transitionAnimState("idle_left");
			break;
		case UP_RIGHT:
			this.transitionAnimState("idle_right");
			break;
		case DOWN_LEFT:
			this.transitionAnimState("idle_left");
			break;
		case DOWN_RIGHT:
			this.transitionAnimState("idle_right");
			break;
		}

		this.killAcceleration();
		_playerState = PlayerState.IDLE;
	}
	
	public PlayerState getPlayerState()			{ return _playerState; }
	
	public boolean isValidRidingPosition(Vector2 pos)
	{
		Vector2 dist = new Vector2(this.getPosition());
		if(dist.sub(pos).len() > SharkRodeoConstants.getRidingDistanceTolerance())
			return false;
		return true;
	}
	
	public boolean isAlive()
	{
		if(_playerState == PlayerState.DEAD)
			return false;
		return true;
	}
	
	public void spawn()
	{
		_playerState = PlayerState.IDLE;
	}
	
	public void kill()
	{
		_playerState = PlayerState.DEAD;
		
		for(PlayerModifier cur : _modifiers)
		{
			if(cur != null)
				cur.deactivate();
		}
		_modifiers = new PlayerModifier[MAX_MODIFIERS];
		
		GameBoard.getInstance().getHud().deactivateButton(UtilityButtonType.SPRINT);
	}
	
	public void setSprint(boolean val)
	{
		_sprinting = val;
//		if(_sprinting)
//		{
//			float FACTOR = 1.3f; //TODO: magic number
			
//			float accelRate =  SharkRodeoConstants.getPlayerAcceleration() * FACTOR;
//			float maxSpeed = SharkRodeoConstants.getPlayerMaxSpeed() * FACTOR;
			
//			setAccelerationRate(accelRate);
//			setMaxSpeed(maxSpeed);
//		}
//		else
//		{
//			float accelRate =  SharkRodeoConstants.getPlayerAcceleration();
//			float maxSpeed = SharkRodeoConstants.getPlayerMaxSpeed();
			
//			setAccelerationRate(accelRate);
//			setMaxSpeed(maxSpeed);
//		}
	}
	
	public void addModifier(PlayerModifierType type)
	{
		boolean found = false;
		for(PlayerModifier cur : _modifiers)
		{
			if((cur != null) && (cur.getType() == type))
			{
				found = true;
				cur.activate();
			}
		}
		if(!found)
		{
			for(int i = 0; i < _modifiers.length; ++i)
			{
				if(_modifiers[i] == null)
				{
					PlayerModifier modifier = new PlayerModifier(type, this);
					_modifiers[i] = modifier;
					modifier.activate();
					break;
				}
			}
		}
	}
	
	public boolean isSprinting()			{ return _sprinting; }
	public PlayerModifier[] getModifiers()	{ return _modifiers; }
	
	public float getMaxSpeed() 					{ return _sprinting ? _maxSpeed * SharkRodeoConstants.SPRINT_FACTOR : _maxSpeed;	} // TODO: magic number
	public float getAccelerationRate()			{ return _sprinting ? _accelerationRate * SharkRodeoConstants.SPRINT_FACTOR : _accelerationRate;	} // TODO: magic number

	
}
