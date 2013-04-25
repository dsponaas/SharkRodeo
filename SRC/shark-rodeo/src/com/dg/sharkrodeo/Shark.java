package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Shark extends GameObject {
	
	public enum SharkState { SEARCHING, MOUNTING, MOUNTED, THRASHING, LUNGING };
	private SharkState _sharkState;
	private Vector2 _searchDest;
	private float _stateTime;
	private float _health;
	private Vector2 _destAcceleration;
	private float _lungeTimeout;
	
	private static final float STATE_TIME_CRAPTAR = 2f; 
	
	public Shark( GameBoard board ) {
		super( board, 36f );//TODO: magic number

		setAccelerationRate( SharkRodeoConstants.getSharkAcceleration() );
		setMaxSpeed( SharkRodeoConstants.getSharkMaxSpeed() );
		
		initBoundsList( 3 );
		Circle bounds = new Circle( this.getPosition().x, this.getPosition().y, SharkRodeoConstants.getSharkMouthHitboxRadius() );
		Circle bounds2 = new Circle( this.getPosition().x, this.getPosition().y, SharkRodeoConstants.getSharkBackHitboxRadius() );
		Circle bounds3 = new Circle( this.getPosition().x, this.getPosition().y, SharkRodeoConstants.getSharkBackHitboxRadius() );
		setBounds( 0, bounds, new Vector2( 0, 0 ) );
		setBounds( 1, bounds2, new Vector2( 0, 0 ) );
		setBounds( 2, bounds3, new Vector2( 0, 0 ) );
		_boundsOffsets[ 2 ].set( 0, 0 );
		
		this.setXPosition( 800f );
		this.setYPosition( 800f );

		setSharkState( SharkState.SEARCHING );
		_health = SharkRodeoConstants.DEFAULT_SHARK_HEALTH;

		addAnimation( "move_left", ResourceManager.getInstance().getSkarkAnim( "move_left" ) );
		addAnimation( "move_right", ResourceManager.getInstance().getSkarkAnim( "move_right" ) );
		addAnimation( "move_down", ResourceManager.getInstance().getSkarkAnim( "move_down" ) );
		addAnimation( "move_up", ResourceManager.getInstance().getSkarkAnim( "move_up" ) );
		addAnimation( "move_downleft", ResourceManager.getInstance().getSkarkAnim( "move_downleft" ) );
		addAnimation( "move_downright", ResourceManager.getInstance().getSkarkAnim( "move_downright" ) );
		addAnimation( "move_upleft", ResourceManager.getInstance().getSkarkAnim( "move_upleft" ) );
		addAnimation( "move_upright", ResourceManager.getInstance().getSkarkAnim( "move_upright" ) );
		addAnimation( "riding_left", ResourceManager.getInstance().getSkarkAnim( "riding_left" ) );
		addAnimation( "riding_right", ResourceManager.getInstance().getSkarkAnim( "riding_right" ) );
		addAnimation( "riding_down", ResourceManager.getInstance().getSkarkAnim( "riding_down" ) );
		addAnimation( "riding_up", ResourceManager.getInstance().getSkarkAnim( "riding_up" ) );
		addAnimation( "riding_upleft", ResourceManager.getInstance().getSkarkAnim( "riding_upleft" ) );
		addAnimation( "riding_upright", ResourceManager.getInstance().getSkarkAnim( "riding_upright" ) );
		addAnimation( "riding_downleft", ResourceManager.getInstance().getSkarkAnim( "riding_downleft" ) );
		addAnimation( "riding_downright", ResourceManager.getInstance().getSkarkAnim( "riding_downright" ) );
		
		this.setClipping( false );

		this.setDirection( Direction.RIGHT );
		setAnimState( "move_right" );
		searchToDest( getNextSearchDest() );
	}
	
	public void update( float delta ) {
		_stateTime -= delta;
		
		Vector2 positionDelta = ( new Vector2( _searchDest ) ).sub( this.getPosition() );
		this.accelerateInDirection( positionDelta );
		
		if( getAcceleration().len2() > 1f ) {
			Vector2 unitDir = ( new Vector2( _destAcceleration ) ).nor();
			Vector2 curAccelNor = ( new Vector2( getAcceleration() ) ).nor();
	
			double angle = Math.atan2( unitDir.y, unitDir.x ) - Math.atan2( curAccelNor.y, curAccelNor.x );
			
			double turnSpeed = ( ( _sharkState == SharkState.LUNGING ) || isBeingRidden() ) ? SharkRodeoConstants.SHARK_LUNGE_TURN_SPEED : SharkRodeoConstants.SHARK_REGULAR_TURN_SPEED; 
			if( Math.abs( angle ) > turnSpeed ) {
				if( angle > 0f ) {
					angle = turnSpeed;
				}
				else {
					angle = -1f * turnSpeed;
				}
			} // if( Math.abs( angle ) > turnSpeed )
			float angleInDegrees = ( float )( ( 180.0 / Math.PI ) * angle );
			
			curAccelNor.rotate( angleInDegrees );
			setAcceleration( curAccelNor.mul( getAccelerationRate() ) );
		}
		else {
			setAcceleration( _destAcceleration );
		}
		
		Direction oldDirection = getDirection();
		setDirection( getDirection( getAcceleration() ) );
		Direction newDirection = getDirection();
		if( newDirection != oldDirection ) {
			changeDirection( newDirection );
		}
		
		
		if( _sharkState == SharkState.LUNGING ) {
			setAccelerationRate( SharkRodeoConstants.SHARK_LUNGE_MULTIPLIER * SharkRodeoConstants.getSharkAcceleration() );
			setMaxSpeed( SharkRodeoConstants.SHARK_LUNGE_MULTIPLIER * SharkRodeoConstants.getSharkMaxSpeed() );
		}
		else if( !isBeingRidden() ) {
			setAccelerationRate( SharkRodeoConstants.getSharkAcceleration() );
			setMaxSpeed( SharkRodeoConstants.getSharkMaxSpeed() );
		}
		
		if( !this.getClipping() ) {
			if( this.isInBounds() ) {
				this.setClipping( true );
			}
		}
		
		if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.THRASHING ) ) {
			_health -= delta; //TODO: times some bonus multiplier? ( spurs? )
		}
		
		if( ( _sharkState == SharkState.SEARCHING ) || ( _sharkState == SharkState.MOUNTED ) /*|| ( _sharkState == SharkState.LUNGING )*/ ) {
			Vector2 sharkToDest = ( new Vector2( _searchDest ) ).sub( getPosition() );
			if( sharkToDest.len2() < 22500 ) { // TODO: magic number
				searchToDest( getNextSearchDest() );
			}
//			if( isLastPositionCloserToPoint( _searchDest.x, _searchDest.y ) ) {
//				searchToDest( getNextSearchDest() );
//			}
		}//if( _sharkState == SharkState.SEARCHING )
		
		if( _sharkState == SharkState.LUNGING ) {
			_lungeTimeout -= delta;
			Vector2 playerPos = GameBoard.getInstance().getPlayerPos();
			if( isPointInLineOfSight( playerPos.x, playerPos.y ) ) {
				Vector2 sharkPos = getPosition();
				Vector2 sharkToPlayer = ( new Vector2( playerPos.x - sharkPos.x, playerPos.y - sharkPos.y ) );
				float distanceToPlayer = sharkToPlayer.len();
				
				sharkToPlayer.nor().mul( distanceToPlayer + 150f ).add( sharkPos );
				searchToDest( sharkToPlayer );
			} // if( isPointInLineOfSight( playerPos.x, playerPos.y ) )
			else {
				Vector2 sharkToDest = ( new Vector2( _searchDest ) ).sub( getPosition() );
				if( ( _lungeTimeout < 0f ) || ( sharkToDest.len2() < 22500 ) ) { // TODO: magic number
					endLunge();
					searchToDest( getNextSearchDest() );
				}
			}
//			else if( isLastPositionCloserToPoint( _searchDest.x, _searchDest.y ) ) {
//				endLunge();
//				searchToDest( getNextSearchDest() );
//			}
		} // if( _sharkState == SharkState.LUNGING )
		
		if( _stateTime < 0f ) {
			if( _sharkState == SharkState.MOUNTED ) {
				setSharkState( SharkState.THRASHING );
				this.setUpatePosition( false );
				_stateTime = STATE_TIME_CRAPTAR;
				GameBoard.getInstance().startCameraShake();
			}
			else if( _sharkState == SharkState.THRASHING ) {
				setSharkState( SharkState.MOUNTED );
				this.setUpatePosition( true );
				_stateTime = STATE_TIME_CRAPTAR;
				GameBoard.getInstance().endCameraShake();
			}
		} // if( _stateTime < 0f )
		
		super.update( delta );
	} // public void update( float delta )
	
	public void searchToDest( Vector2 dest ) {
		_searchDest = new Vector2( dest );
		Vector2 delta = dest.sub( this.getPosition() );
		this.accelerateInDirection( delta );
		
		if( _sharkState != SharkState.MOUNTED ) {
			setSharkState( SharkState.SEARCHING );
		}
	} // public void searchToDest( Vector2 dest )
	
	public void lungeAtPlayer()	{
		if( _sharkState == SharkState.LUNGING ) {
			return;
		}

		Vector2 sharkPos = getPosition();
		Vector2 playerPos = GameBoard.getInstance().getPlayerPos();
		Vector2 sharkToPlayer = ( new Vector2( playerPos.x - sharkPos.x, playerPos.y - sharkPos.y ) );//      .nor().mul( 150f ); //TODO: magic number
		float dist = sharkToPlayer.len() + 150f;//TODO:magic number
		sharkToPlayer.nor().mul( dist );
		Vector2 sharkDest = ( new Vector2( playerPos ) ).add( sharkToPlayer );		

		setSharkState( SharkState.LUNGING );
		
		_searchDest = new Vector2( sharkDest );
		Vector2 delta = sharkDest.sub( this.getPosition() );
		this.accelerateInDirection( delta );

		setAccelerationRate( SharkRodeoConstants.SHARK_LUNGE_MULTIPLIER * SharkRodeoConstants.getSharkAcceleration() );
		setMaxSpeed( SharkRodeoConstants.SHARK_LUNGE_MULTIPLIER * SharkRodeoConstants.getSharkMaxSpeed() );
	} // public void lungeAtPlayer()
	
	public void endLunge() {
		if( _sharkState != SharkState.LUNGING ) {
			return;
		}
		
		setSharkState( SharkState.SEARCHING );
	}
	
	private void changeDirection( Direction newDirection ) {
		float distance = SharkRodeoConstants.getSharkHitboxOffset();
		float diagDist = ( float ) Math.sqrt( ( distance * distance ) / 2f );//TODO: calculate this beforehand, store as a constant
		
		switch( newDirection ) {
		case UP:
			if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.THRASHING ) ) {
				this.setAnimState( "riding_up" );
			}
			else {
				this.setAnimState( "move_up" );
			}
			_boundsOffsets[ 0 ].set( 0, distance );
			_boundsOffsets[ 1 ].set( 0, -.8f * distance );
			break;
		case DOWN:
			if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.THRASHING ) ) {
				this.setAnimState( "riding_down" );
			}
			else {
				this.setAnimState( "move_down" );
			}
			_boundsOffsets[ 0 ].set( 0, -1f * distance );
			_boundsOffsets[ 1 ].set( 0, 0.8f * distance );
			break;
		case LEFT:
			if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.THRASHING ) ) {
				this.setAnimState( "riding_left" );
			}
			else {
				this.setAnimState( "move_left" );
			}
			_boundsOffsets[ 0 ].set( -1f * distance, 0 );
			_boundsOffsets[ 1 ].set( 0.8f * distance, 0 );
			break;
		case RIGHT:
			if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.THRASHING ) ) {
				this.setAnimState( "riding_right" );
			}
			else {
				this.setAnimState( "move_right" );
			}
			_boundsOffsets[ 0 ].set( distance, 0 );
			_boundsOffsets[ 1 ].set( -0.8f * distance, 0 );
			break;
		case UP_LEFT:
			if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.THRASHING ) ) {
				this.setAnimState( "riding_upleft" );
			}
			else {
				this.setAnimState( "move_upleft" );
			}
			_boundsOffsets[ 0 ].set( -1f * diagDist, diagDist );
			_boundsOffsets[ 1 ].set( 0.8f * diagDist, -0.8f * diagDist );
			break;
		case UP_RIGHT:
			if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.THRASHING ) ) {
				this.setAnimState( "riding_upright" );
			}
			else {
				this.setAnimState( "move_upright" );
			}
			_boundsOffsets[ 0 ].set( diagDist, diagDist );
			_boundsOffsets[ 1 ].set( -0.8f * diagDist, -0.8f * diagDist );
			break;
		case DOWN_LEFT:
			if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.THRASHING ) ) {
				this.setAnimState( "riding_downleft" );
			}
			else {
				this.setAnimState( "move_downleft" );
			}
			_boundsOffsets[ 0 ].set( -1f * diagDist, -1f * diagDist );
			_boundsOffsets[ 1 ].set( 0.8f * diagDist, 0.8f * diagDist );
			break;
		case DOWN_RIGHT:
			if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.THRASHING ) ) {
				this.setAnimState( "riding_downright" );
			}
			else {
				this.setAnimState( "move_downright" );
			}
			_boundsOffsets[ 0 ].set( diagDist, -1f * diagDist );
			_boundsOffsets[ 1 ].set( -0.8f * diagDist, 0.8f * diagDist );
			break;
		} // switch( newDirection )
	} // private void changeDirection( Direction newDirection )
	
	public void accelerateInDirection( Vector2 dir ) {
		_destAcceleration = dir;
	} // public void accelerateInDirection( Vector2 dir )
	
	
	public void mountingShark() {
		setSharkState( SharkState.MOUNTING );
		this.setUpatePosition( true );
	}
	
	public void mountShark() {
		setSharkState( SharkState.MOUNTED );
		setAccelerationRate( SharkRodeoConstants.getMountedSharkAcceleration() );
		setMaxSpeed( SharkRodeoConstants.getMountedSharkMaxSpeed() );
		// TODO: change anim state... maybe do that in changeDirection instead and just call it here? also increase speed
		
		switch( this.getDirection() ) {
		case UP:
			this.setAnimState( "riding_up" );
			break;
		case DOWN:
			this.setAnimState( "riding_down" );
			break;
		case LEFT:
			this.setAnimState( "riding_left" );
			break;
		case RIGHT:
			this.setAnimState( "riding_right" );
			break;
		case UP_LEFT:
			this.setAnimState( "riding_upleft" );
			break;
		case UP_RIGHT:
			this.setAnimState( "riding_upright" );
			break;
		case DOWN_LEFT:
			this.setAnimState( "riding_downleft" );
			break;
		case DOWN_RIGHT:
			this.setAnimState( "riding_downright" );
			break;
		} // switch( this.getDirection() )
		
		this.setUpatePosition( true );
		_stateTime = STATE_TIME_CRAPTAR;
	} // public void mountShark()
	
	public void dismount() {
		setSharkState( SharkState.SEARCHING );
		setAccelerationRate( SharkRodeoConstants.getSharkAcceleration() );
		setMaxSpeed( SharkRodeoConstants.getSharkMaxSpeed() );
		this.setUpatePosition( true );
		GameBoard.getInstance().endCameraShake();
		this.searchToDest( getNextSearchDest() );
		changeDirection( getDirection() );
	}

	private Vector2 getNextSearchDest() {
		Vector2 tempDest = new Vector2();
		tempDest.x = Utils.getRandomFloatInRange( 50f, GameBoard.getInstance().getWidth() - 50f );// TODO: MAGIC NUMBER
		tempDest.y = Utils.getRandomFloatInRange( 50f, GameBoard.getInstance().getHeight() - 50f );// TODO: MAGIC NUMBER
		return tempDest;
	} // private Vector2 getNextSearchDest()
	
	public float getMass() {
		if( isBeingRidden() )
			return 1.4f;//TODO:magic number
		return 1.0f;//TODO:magic number
	}
	
	public boolean isPointInLineOfSight( float x, float y ) {
		if( isBeingRidden() )
			return false;
		
		float SIGHT_DISTANCE = 300f; //TODO: magic number
		float SIGHT_DISTANCE_DIAG = 212.132f;//( float )Math.sqrt( ( SIGHT_DISTANCE * SIGHT_DISTANCE ) / 2f );
		float SIGHT_OFFSET = 150f; //TODO: magic number
		float SIGHT_OFFSET_DIAG = 106.066f;//( float )Math.sqrt( ( SIGHT_OFFSET * SIGHT_OFFSET ) / 2f );
		
		Vector2 dist2 = new Vector2( this.getPosition() );
		dist2.sub( x, y );
		if( dist2.len2() > ( SIGHT_DISTANCE * SIGHT_DISTANCE ) )
			return false;
		
		Vector2 sightExtent = new Vector2( getPosition() );
		Vector2 t1 = new Vector2();
		Vector2 t2 = new Vector2();
		
		switch( getDirection() ) {
		case UP:
			sightExtent.y += SIGHT_DISTANCE;
			t1.set( sightExtent );
			t2.set( sightExtent );
			t1.x += SIGHT_OFFSET;
			t2.x -= SIGHT_OFFSET;
			break;
		case DOWN:
			sightExtent.y -= SIGHT_DISTANCE;
			t1.set( sightExtent );
			t2.set( sightExtent );
			t1.x -= SIGHT_OFFSET;
			t2.x += SIGHT_OFFSET;
			break;
		case LEFT:
			sightExtent.x -= SIGHT_DISTANCE;
			t1.set( sightExtent );
			t2.set( sightExtent );
			t1.y += SIGHT_OFFSET;
			t2.y -= SIGHT_OFFSET;
			break;
		case RIGHT:
			sightExtent.x += SIGHT_DISTANCE;
			t1.set( sightExtent );
			t2.set( sightExtent );
			t1.y -= SIGHT_OFFSET;
			t2.y += SIGHT_OFFSET;
			break;
		case UP_LEFT:
			sightExtent.y += SIGHT_DISTANCE_DIAG;
			sightExtent.x -= SIGHT_DISTANCE_DIAG;
			t1.set( sightExtent );
			t2.set( sightExtent );
			t1.x -= SIGHT_OFFSET_DIAG;
			t1.y -= SIGHT_OFFSET_DIAG;
			t2.x += SIGHT_OFFSET_DIAG;
			t2.y += SIGHT_OFFSET_DIAG;
			break;
		case UP_RIGHT:
			sightExtent.y += SIGHT_DISTANCE_DIAG;
			sightExtent.x += SIGHT_DISTANCE_DIAG;
			t1.set( sightExtent );
			t2.set( sightExtent );
			t1.x -= SIGHT_OFFSET_DIAG;
			t1.y += SIGHT_OFFSET_DIAG;
			t2.x += SIGHT_OFFSET_DIAG;
			t2.y -= SIGHT_OFFSET_DIAG;
			break;
		case DOWN_LEFT:
			sightExtent.y -= SIGHT_DISTANCE_DIAG;
			sightExtent.x -= SIGHT_DISTANCE_DIAG;
			t1.set( sightExtent );
			t2.set( sightExtent );
			t1.x -= SIGHT_OFFSET_DIAG;
			t1.y += SIGHT_OFFSET_DIAG;
			t2.x += SIGHT_OFFSET_DIAG;
			t2.y -= SIGHT_OFFSET_DIAG;
			break;
		case DOWN_RIGHT:
			sightExtent.y -= SIGHT_DISTANCE_DIAG;
			sightExtent.x += SIGHT_DISTANCE_DIAG;
			t1.set( sightExtent );
			t2.set( sightExtent );
			t1.x -= SIGHT_OFFSET_DIAG;
			t1.y -= SIGHT_OFFSET_DIAG;
			t2.x += SIGHT_OFFSET_DIAG;
			t2.y += SIGHT_OFFSET_DIAG;
			break;
		} // switch( getDirection() )
		
		Vector2 v0 = ( new Vector2( t1 ) ).sub( getPosition() );
		Vector2 v1 = ( new Vector2( t2 ) ).sub( getPosition() );
		Vector2 v2 = ( new Vector2( x, y ) ).sub( getPosition() );
		
		float dot00 = v0.dot( v0 );
		float dot01 = v0.dot( v1 );
		float dot02 = v0.dot( v2 );
		float dot11 = v1.dot( v1 );
		float dot12 = v1.dot( v2 );

		float invDenim = 1f / ( ( dot00 * dot11 ) - ( dot01 * dot01 ) );
		float u = ( ( dot11 * dot02 ) - ( dot01 * dot12 ) ) * invDenim;
		float v = ( ( dot00 * dot12 ) - ( dot01 * dot02 ) ) * invDenim;
		
		boolean retval = ( u >= 0f ) && ( v >= 0f ) && ( ( u + v ) < 1f );
		
		if( retval ) {
			_lungeTimeout = 3f; // TODO: magic number
			return true;
		}
		return false;
	} // public boolean isPointInLineOfSight( float x, float y )
	
	public boolean isAboutToTurn() {
		if( isBeingRidden() )
			return false;
		Vector2 delta = ( new Vector2( getPosition() ) ).sub( _searchDest );
		if( delta.len2() < SharkRodeoConstants.getSharkDestAlertDistanceSquared() )
			return true;
		return false;
	}
	
	public boolean isBeingRidden() {
		if( ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.MOUNTING ) || ( _sharkState == SharkState.THRASHING ) )
			return true;
		return false;
	}
	
	public void resetSharkState() {
		setSharkState( SharkState.SEARCHING );
		setAccelerationRate( SharkRodeoConstants.getSharkAcceleration() );
		setMaxSpeed( SharkRodeoConstants.getSharkMaxSpeed() );
	}
	
	private void setSharkState( SharkState newState ) {
		_sharkState = newState;
	}

	public SharkState getSharkState()			{ return _sharkState; }
	public float getHealth()					{ return _health; }
	public float getHealthPercent()				{ return _health / SharkRodeoConstants.DEFAULT_SHARK_HEALTH; }
	public Vector2 getSharkDest()				{ return _searchDest; }

} // public class Shark
