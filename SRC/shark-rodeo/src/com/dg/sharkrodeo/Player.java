package com.dg.sharkrodeo;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.HudUtilityButton.UtilityButtonType;
import com.dg.sharkrodeo.PlayerModifier.PlayerModifierType;

public class Player extends GameObject {
	
	public enum PlayerState { DEAD, IDLE, MOVING, MOUNTING, MOUNTED, DISMOUNTING };
	private PlayerState _playerState;
	private Shark _ridingShark;
	private float _ridingScoreTimer;
	private boolean _sprinting;
	private PlayerModifier[] _modifiers;
	
	private float _dismountTargetY;
	private Vector2 _dismountVelocity;
	
	private static long _soundId = -1;
	
	private final int MAX_MODIFIERS = 3;
//	private boolean _alive;
	
	public Player( GameBoard board ) {
		super( board, 36f ); //TODO: magic number
		
		setAccelerationRate( SharkRodeoConstants.getPlayerAcceleration() );
		setMaxSpeed( SharkRodeoConstants.getPlayerMaxSpeed() );
		
		initBoundsList( 1 );
		Circle bounds = new Circle( this.getPosition().x, this.getPosition().y, SharkRodeoConstants.getPlayerHitboxRadius() );
		setBounds( 0, bounds, new Vector2(0, 0 ) );
		
		this.setXPosition( 100f );
		this.setYPosition( 100f );

		_playerState = PlayerState.DEAD;
		
		addAnimation( "move_left", ResourceManager.getInstance().getPlayerAnim( "move_left" ) );
		addAnimation( "move_right", ResourceManager.getInstance().getPlayerAnim( "move_right" ) );
		addAnimation( "move_down", ResourceManager.getInstance().getPlayerAnim( "move_down" ) );
		addAnimation( "move_up", ResourceManager.getInstance().getPlayerAnim( "move_up" ) );
		addAnimation( "move_downleft", ResourceManager.getInstance().getPlayerAnim( "move_downleft" ) );
		addAnimation( "move_downright", ResourceManager.getInstance().getPlayerAnim( "move_downright" ) );
		addAnimation( "move_upleft", ResourceManager.getInstance().getPlayerAnim( "move_upleft" ) );
		addAnimation( "move_upright", ResourceManager.getInstance().getPlayerAnim( "move_upright" ) );
		addAnimation( "idle_left", ResourceManager.getInstance().getPlayerAnim( "idle_left" ) );
		addAnimation( "idle_right", ResourceManager.getInstance().getPlayerAnim( "idle_right" ) );
		addAnimation( "dismount_left", ResourceManager.getInstance().getPlayerAnim( "dismount_left" ) );
		addAnimation( "dismount_right", ResourceManager.getInstance().getPlayerAnim( "dismount_right" ) );
		addAnimation( "mount_up", ResourceManager.getInstance().getPlayerAnim( "mount_up" ) );
		addAnimation( "dead", ResourceManager.getInstance().getPlayerAnim( "dead" ) );

		setAnimState( "idle_left" );
		
		_ridingShark = null;
		_ridingScoreTimer = 0f;
		
		_sprinting = false;
		
		_modifiers = new PlayerModifier[ MAX_MODIFIERS ];
		//_alive = false;
	}
	
	public void update( float delta ) {
//		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***********: collisionbounds: x:" + _collisionBounds.x + " y:" + _collisionBounds.y + " r:" + _collisionBounds.radius);
		
		if( _playerState == PlayerState.MOUNTING ) {
			Vector2 sharkPos = _ridingShark.getPosition();
			this.setPosition( sharkPos.x, sharkPos.y );
		}
		else if( _playerState == PlayerState.MOUNTED ) {
			Vector2 sharkPos = _ridingShark.getPosition();
			this.setPosition( sharkPos.x, sharkPos.y );
			
			_ridingScoreTimer -= delta;
			float deltaDelta = 0f - _ridingScoreTimer;
			if( deltaDelta > 0f ) {
				_ridingScoreTimer = SharkRodeoConstants.RIDING_SCORE_TIME - deltaDelta;
				GameState.addToScore( 10 ); // TODO: magic number
			}
			
			if( _ridingShark.getHealth() < 0f )
				tamingShark();
		}
		else if( _playerState == PlayerState.DISMOUNTING ) {
			if( getPosition().y < _dismountTargetY ) {
				dismountComplete();
			}
			else {
				Vector2 currentPosition = getPosition();
				float newVelocityY = _dismountVelocity.y - ( delta * 250f ); //TODO: magic number
				_dismountVelocity.set( _dismountVelocity.x, newVelocityY );
				Vector2 positionDelta = new Vector2( _dismountVelocity ).mul( delta );
				setPosition( currentPosition.x + positionDelta.x , currentPosition.y + positionDelta.y );
			}
		}
		
		for( int i = 0; i < _modifiers.length; ++i ) {
			if( _modifiers[ i ] != null ) {
				if( !_modifiers[ i ].update( delta ) )
					_modifiers[ i ] = null;
			}
		}
		
		super.update( delta );
	}
	
	public TextureRegion getTexture() {
		if( _playerState == PlayerState.MOUNTED )
			return null;
		
		TextureRegion tex = _view.getCurrentAnimationFrame();
		if( tex != null )
			return tex;
		if( _playerState == PlayerState.MOUNTING ) {
			_playerState = PlayerState.MOUNTED;
			_ridingShark.mountShark();
			this.mountShark();
		}
		
		return null;
	}
	
	public void mountingShark( Shark shark ) {
		this.setDirection( shark.getDirection() );
		_playerState = PlayerState.MOUNTING;
		
		switch( this.getDirection() ) {
		case UP:
			this.setAnimState( null );
			break;
		case DOWN:
			this.setAnimState( null );
			break;
		case LEFT:
			this.setAnimState( null );
			break;
		case RIGHT:
			this.setAnimState( null );
			break;
		case UP_LEFT:
			this.setAnimState( null );
			break;
		case UP_RIGHT:
			this.setAnimState( null );
			break;
		case DOWN_LEFT:
			this.setAnimState( null );
			break;
		case DOWN_RIGHT:
			this.setAnimState( null );
			break;
		}

		_ridingShark = shark;
		_ridingShark.mountingShark();
		
		Hud.getInstance().deactivateButton( UtilityButtonType.SPRINT );
		
		this.setUpatePosition( false );
	}
	
	public void mountShark() {
		_ridingScoreTimer = SharkRodeoConstants.RIDING_SCORE_TIME;
		Sound ridingNoise = ResourceManager.getInstance().getRidingNoise();
		_soundId = -1;
		while( _soundId < 0 ) {
			_soundId = ridingNoise.loop( 0.5f );
		}
	}
	
	public void dismountingShark() {
//		this.setPosition( 100f, 100 );
		_playerState = PlayerState.DISMOUNTING;
		_ridingShark.dismount();

		this.setAnimState( "dismount_left" );
		this.setInTheWater( false );

		_dismountTargetY = getPosition().y;
		_dismountVelocity = new Vector2( -100f, 300f );
		if( this.getPosition().x < ( GameBoard.getInstance().getWidth() / 2f ) ) {
			_dismountVelocity.set( _dismountVelocity.x * -1f, _dismountVelocity.y );
		}
		
		ResourceManager.getInstance().getRidingNoise().stop( _soundId );
		ResourceManager.getInstance().getDismountNoise().play( 0.5f );
	}
	
	private void dismountComplete() {
		this.killAcceleration();
		this.killVelocity();
		this.setUpatePosition( true );
		
		this.setInTheWater( true );
		_playerState = PlayerState.IDLE;
		this.setAnimState( "idle_left" );
	}
	
	public void tamingShark() {
		_playerState = PlayerState.IDLE;
		this.setAnimState( "idle_left" );
		this.killAcceleration();
		this.killVelocity();
		this.setUpatePosition( true );
		
		ResourceManager.getInstance().getRidingNoise().stop( _soundId );

		GameBoard.getInstance().tameShark( _ridingShark );
	}
	
	public void accelerateInDirection( Vector2 dir ) {
//		float oldAngle = ( new Vector2( getAcceleration() ) ).angle();
		Vector2 unitDir = dir.nor();
//		float angle = unitDir.angle();

		setDirection( getDirection( unitDir) );
		setAcceleration( unitDir.mul( getAccelerationRate() ) );
	} // public void accelerateInDirection( Vector2 dir )
	
	public void moveTo( Vector2 dest ) {
		Direction oldDirection = this.getDirection();
		Vector2 delta = dest.sub( this.getPosition() );
		this.accelerateInDirection( delta );
		Direction newDirection = this.getDirection();

//		float distance = 8f; // TODO: magic number
//		float diagDist = ( float )Math.sqrt( ( distance * distance ) / 2f ); // TODO: magic number
		
		if( ( newDirection != oldDirection ) || ( _playerState != PlayerState.MOVING ) ) {
			switch( newDirection ) {
			case UP:
				this.setAnimState( "move_up" );
//				_boundsOffsets[ 0 ].set( 0f, distance );
				break;
			case DOWN:
				this.setAnimState( "move_down" );
//				_boundsOffsets[ 0 ].set( 0f, -1f * distance );
				break;
			case LEFT:
				this.setAnimState( "move_left" );
//				_boundsOffsets[ 0 ].set( -1f * distance, 0f );
				break;
			case RIGHT:
				this.setAnimState( "move_right" );
//				_boundsOffsets[ 0 ].set( distance, 0f );
				break;
			case UP_LEFT:
				this.setAnimState( "move_upleft" );
//				_boundsOffsets[ 0 ].set( -1f * diagDist, diagDist );
				break;
			case UP_RIGHT:
				this.setAnimState( "move_upright" );
//				_boundsOffsets[ 0 ].set( diagDist, diagDist );
				break;
			case DOWN_LEFT:
				this.setAnimState( "move_downleft" );
//				_boundsOffsets[ 0 ].set( -1f* diagDist, -1f * diagDist );
				break;
			case DOWN_RIGHT:
				this.setAnimState( "move_downright" );
//				_boundsOffsets[ 0 ].set( diagDist, -1f * diagDist );
				break;
			}
			_playerState = PlayerState.MOVING;
		}
	}
	
	public void stopMoving() {
		if( _playerState != PlayerState.MOVING ) {
			return;
		}
		
		_boundsOffsets[ 0 ].set( 0f, 0f );
		if( this.getAcceleration().x > 0f )
			this.transitionAnimState( "idle_right" );
		else
			this.transitionAnimState( "idle_left" );

		this.killAcceleration();
		_playerState = PlayerState.IDLE;
	}
	
	public boolean isValidRidingPosition( Vector2 pos ) {
		Vector2 dist = new Vector2( this.getPosition() );
		float tolerance = SharkRodeoConstants.getRidingDistanceTolerance();
		if( dist.sub( pos ).len2() > ( tolerance * tolerance ) )
			return false;
		return true;
	}
	
	public boolean isAlive() {
		if( _playerState == PlayerState.DEAD )
			return false;
		return true;
	}
	
	public void spawn() {
		_playerState = PlayerState.IDLE;
		setAnimState( "idle_left" );
	}
	
	public void kill( boolean whirlpoolDeath ) {
		_playerState = PlayerState.DEAD;
		
		for( PlayerModifier cur : _modifiers ) {
			if( cur != null )
				cur.deactivate();
		}
		_modifiers = new PlayerModifier[ MAX_MODIFIERS ];
		
		Hud.getInstance().deactivateButton( UtilityButtonType.SPRINT );
		if( !whirlpoolDeath ) {
			setAnimState( "dead" );
		}
		else {
			setAnimState( null );
		}
	}
	
	public void setSprint( boolean val ) {
		_sprinting = val;
	}
	
	public void addModifier( PlayerModifierType type ) {
		boolean found = false;
		for( PlayerModifier cur : _modifiers ) {
			if( ( cur != null ) && ( cur.getType() == type ) ) {
				found = true;
				cur.activate();
			}
		}
		if( !found ) {
			for( int i = 0; i < _modifiers.length; ++i ) {
				if( _modifiers[ i ] == null ) {
					PlayerModifier modifier = new PlayerModifier( type, this );
					_modifiers[ i ] = modifier;
					modifier.activate();
					break;
				}
			}
		}
	}
	
	public PlayerState getPlayerState()			{ return _playerState; }
	
	public boolean isSprinting()				{ return _sprinting; }
	public PlayerModifier[] getModifiers()		{ return _modifiers; }
	
	public float getMaxSpeed() 					{ return _sprinting ? _maxSpeed * SharkRodeoConstants.SPRINT_FACTOR : _maxSpeed; }
	public float getAccelerationRate()			{ return _sprinting ? _accelerationRate * SharkRodeoConstants.SPRINT_FACTOR : _accelerationRate; }

	
}
