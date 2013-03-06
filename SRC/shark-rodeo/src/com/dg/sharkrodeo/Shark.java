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
	
	private static final float STATE_TIME_CRAPTAR = 2f; 
	
	public Shark( GameBoard board ) {
		super( board, 36f );//TODO: magic number

		setAccelerationRate( SharkRodeoConstants.getSharkAcceleration() );
		setMaxSpeed( SharkRodeoConstants.getSharkMaxSpeed() );
		
		initBoundsList( 2 );
		Circle bounds = new Circle( this.getPosition().x, this.getPosition().y, SharkRodeoConstants.getSharkMouthHitboxRadius() );
		Circle bounds2 = new Circle( this.getPosition().x, this.getPosition().y, SharkRodeoConstants.getSharkBackHitboxRadius() );
		setBounds( 0, bounds, new Vector2( 0, 0 ) );
		setBounds( 1, bounds2, new Vector2( 0, 0 ) );
		
		this.setXPosition( 800f );
		this.setYPosition( 800f );

		setSharkState( SharkState.SEARCHING );
		_health = SharkRodeoConstants.DEFAULT_SHARK_HEALTH;

		TextureAtlas atlas = new TextureAtlas( SharkRodeoConstants.getSharkPack() );
		Animation swimLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_swim_left" ) );
		Animation swimRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_swim_right" ) );
		Animation swimDownAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_swim_down" ) );
		Animation swimUpAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_swim_up" ) );
		Animation swimDownLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_swim_downleft" ) );
		Animation swimDownRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_swim_downright" ) );
		Animation swimUpLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_swim_upleft" ) );
		Animation swimUpRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_swim_upright" ) );
		Animation ridingLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_riding_left" ) );
		Animation ridingRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_riding_right" ) );
		Animation ridingDownAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_riding_down" ) );
		Animation ridingUpAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_riding_up" ) );
		Animation ridingUpLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_riding_upleft" ) );
		Animation ridingUpRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_riding_upright" ) );
		Animation ridingDownLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_riding_downleft" ) );
		Animation ridingDownRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, atlas.findRegion( "shark_riding_downright" ) );
		
		AnimationSequence swimLeftSequence = new AnimationSequence( swimLeftAnim );
		AnimationSequence swimRightSequence = new AnimationSequence( swimRightAnim );
		AnimationSequence swimDownSequence = new AnimationSequence( swimDownAnim );
		AnimationSequence swimUpSequence = new AnimationSequence( swimUpAnim );
		AnimationSequence swimDownLeftSequence = new AnimationSequence( swimDownLeftAnim );
		AnimationSequence swimDownRightSequence = new AnimationSequence( swimDownRightAnim );
		AnimationSequence swimUpLeftSequence = new AnimationSequence( swimUpLeftAnim );
		AnimationSequence swimUpRightSequence = new AnimationSequence( swimUpRightAnim );
		AnimationSequence ridingLeftSequence = new AnimationSequence( ridingLeftAnim );
		AnimationSequence ridingRightSequence = new AnimationSequence( ridingRightAnim );
		AnimationSequence ridingDownSequence = new AnimationSequence( ridingDownAnim );
		AnimationSequence ridingUpSequence = new AnimationSequence( ridingUpAnim );
		AnimationSequence ridingUpLeftSequence = new AnimationSequence( ridingUpLeftAnim );
		AnimationSequence ridingUpRightSequence = new AnimationSequence( ridingUpRightAnim );
		AnimationSequence ridingDownLeftSequence = new AnimationSequence( ridingDownLeftAnim );
		AnimationSequence ridingDownRightSequence = new AnimationSequence( ridingDownRightAnim );
		
		addAnimation( "move_left", swimLeftSequence );
		addAnimation( "move_right", swimRightSequence );
		addAnimation( "move_down", swimDownSequence );
		addAnimation( "move_up", swimUpSequence );
		addAnimation( "move_downleft", swimDownLeftSequence );
		addAnimation( "move_downright", swimDownRightSequence );
		addAnimation( "move_upleft", swimUpLeftSequence );
		addAnimation( "move_upright", swimUpRightSequence );
		addAnimation( "riding_left", ridingLeftSequence );
		addAnimation( "riding_right", ridingRightSequence );
		addAnimation( "riding_down", ridingDownSequence );
		addAnimation( "riding_up", ridingUpSequence );
		addAnimation( "riding_upleft", ridingUpLeftSequence );
		addAnimation( "riding_upright", ridingUpRightSequence );
		addAnimation( "riding_downleft", ridingDownLeftSequence );
		addAnimation( "riding_downright", ridingDownRightSequence );
		
		this.setClipping( false );

		this.setDirection( Direction.RIGHT );
		setAnimState( "move_right" );
		searchToDest( getNextSearchDest() );
	}
	
	public void update( float delta ) {
		_stateTime -= delta;
		
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
		
		if( ( _sharkState == SharkState.SEARCHING ) || ( _sharkState == SharkState.MOUNTED ) || ( _sharkState == SharkState.LUNGING ) ) {
			Vector2 positionDelta = ( new Vector2( _searchDest ) ).sub( this.getPosition() );
			this.accelerateInDirection( positionDelta );
			if( isLastPositionCloserToPoint( _searchDest.x, _searchDest.y ) ) {
				searchToDest( getNextSearchDest() );
			}
		}//if( _sharkState == SharkState.SEARCHING )
		
		if( _sharkState == SharkState.LUNGING ) {
			Vector2 playerPos = GameBoard.getInstance().getPlayerPos();
			if( isPointInLineOfSight( playerPos.x, playerPos.y ) ) {
				Vector2 sharkPos = getPosition();
				Vector2 sharkToPlayer = ( new Vector2( playerPos.x - sharkPos.x, playerPos.y - sharkPos.y ) );
				float distanceToPlayer = sharkToPlayer.len();
				
				sharkToPlayer.nor();
				Vector2 curDirection = ( new Vector2( _searchDest ) ).sub( sharkPos ).nor();

				double angle = Math.atan2( sharkToPlayer.y, sharkToPlayer.x ) - Math.atan2( curDirection.y, curDirection.x );
				if( Math.abs( angle ) > SharkRodeoConstants.SHARK_LUNGE_TURN_SPEED ) {
					if( angle > 0f ) {
						angle = SharkRodeoConstants.SHARK_LUNGE_TURN_SPEED;
					}
					else {
						angle = -1f * SharkRodeoConstants.SHARK_LUNGE_TURN_SPEED;
					}
				}
				float angleInDegrees = ( float )( ( 180.0 / Math.PI ) * angle );
				//Gdx.app.log( SharkRodeoConstants.LOG_TAG, "angle radians:" + angle );
				
				curDirection.rotate( angleInDegrees );
				float dist = distanceToPlayer + 150f;//TODO:magic number
				
				curDirection.mul( dist ).add( sharkPos );
				
//				sharkToPlayer.mul( dist ).add( sharkPos );
				
				searchToDest( curDirection );
			} // if( isPointInLineOfSight( playerPos.x, playerPos.y ) )
			else if( isLastPositionCloserToPoint( _searchDest.x, _searchDest.y ) ) {
				endLunge();
				searchToDest( getNextSearchDest() );
			}
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
//		Gdx.app.log( SharkRodeoConstants.LOG_TAG, "***********: dest: x:" + dest.x + " y:" + dest.y );

		_searchDest = new Vector2( dest );
//		Direction oldDirection = this.getDirection();
		Vector2 delta = dest.sub( this.getPosition() );
		this.accelerateInDirection( delta );
		Direction newDirection = this.getDirection();
		
//		if( ( newDirection != oldDirection ) || ( ( _sharkState != SharkState.SEARCHING ) && ( _sharkState != SharkState.MOUNTED ) && ( _sharkState != SharkState.THRASHING ) && ( _sharkState != SharkState.LUNGING ) ) ) {
		changeDirection( newDirection );
		if( _sharkState != SharkState.MOUNTED ) {
			setSharkState( SharkState.SEARCHING );
		}
//		}
	}
	
	public void lungeAtPlayer()	{
		if( _sharkState == SharkState.LUNGING )
			return;

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
		Direction newDirection = this.getDirection();
		
		changeDirection( newDirection );

		setAccelerationRate( SharkRodeoConstants.SHARK_LUNGE_MULTIPLIER * SharkRodeoConstants.getSharkAcceleration() );
		setMaxSpeed( SharkRodeoConstants.SHARK_LUNGE_MULTIPLIER * SharkRodeoConstants.getSharkMaxSpeed() );
	} // public void lungeAtPlayer()
	
	public void endLunge() {
		if( _sharkState != SharkState.LUNGING )
			return;
		
		setSharkState( SharkState.SEARCHING );
	}
	
	private void changeDirection( Direction newDirection ) {
		float distance = SharkRodeoConstants.getSharkHitboxOffset();
		float diagDist = ( float ) Math.sqrt( ( distance * distance ) / 2f );//TODO: calculate this beforehand, store as a constant
		
		switch( newDirection ) {
		case UP:
			if( _sharkState == SharkState.MOUNTED ) {
				this.setAnimState( "riding_up" );
			}
			else {
				this.setAnimState( "move_up" );
			}
			_boundsOffsets[ 0 ].set( 0, distance );
			_boundsOffsets[ 1 ].set( 0, -1f * distance );
			break;
		case DOWN:
			if( _sharkState == SharkState.MOUNTED ) {
				this.setAnimState( "riding_down" );
			}
			else {
				this.setAnimState( "move_down" );
			}
			_boundsOffsets[ 0 ].set( 0, -1f * distance );
			_boundsOffsets[ 1 ].set( 0, distance );
			break;
		case LEFT:
			if( _sharkState == SharkState.MOUNTED ) {
				this.setAnimState( "riding_left" );
			}
			else {
				this.setAnimState( "move_left" );
			}
			_boundsOffsets[ 0 ].set( -1f * distance, 0 );
			_boundsOffsets[ 1 ].set( distance, 0 );
			break;
		case RIGHT:
			if( _sharkState == SharkState.MOUNTED ) {
				this.setAnimState( "riding_right" );
			}
			else {
				this.setAnimState( "move_right" );
			}
			_boundsOffsets[ 0 ].set( distance, 0 );
			_boundsOffsets[ 1 ].set( -1f * distance, 0 );
			break;
		case UP_LEFT:
			if( _sharkState == SharkState.MOUNTED ) {
				this.setAnimState( "riding_upleft" );
			}
			else {
				this.setAnimState( "move_upleft" );
			}
			_boundsOffsets[ 0 ].set( -1f * diagDist, diagDist );
			_boundsOffsets[ 1 ].set( diagDist, -1f * diagDist );
			break;
		case UP_RIGHT:
			if( _sharkState == SharkState.MOUNTED ) {
				this.setAnimState( "riding_upright" );
			}
			else {
				this.setAnimState( "move_upright" );
			}
			_boundsOffsets[ 0 ].set( diagDist, diagDist );
			_boundsOffsets[ 1 ].set( -1f * diagDist, -1f * diagDist );
			break;
		case DOWN_LEFT:
			if( _sharkState == SharkState.MOUNTED ) {
				this.setAnimState( "riding_downleft" );
			}
			else {
				this.setAnimState( "move_downleft" );
			}
			_boundsOffsets[ 0 ].set( -1f * diagDist, -1f * diagDist );
			_boundsOffsets[ 1 ].set( diagDist, diagDist );
			break;
		case DOWN_RIGHT:
			if( _sharkState == SharkState.MOUNTED ) {
				this.setAnimState( "riding_downright" );
			}
			else {
				this.setAnimState( "move_downright" );
			}
			_boundsOffsets[ 0 ].set( diagDist, -1f * diagDist );
			_boundsOffsets[ 1 ].set( -1f * diagDist, diagDist );
			break;
		} // switch( newDirection )
	} // private void changeDirection( Direction newDirection )
	
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
		this.setPosition( 1200f, 1200f ); //TODO: magic number
		setAccelerationRate( SharkRodeoConstants.getSharkAcceleration() );
		setMaxSpeed( SharkRodeoConstants.getSharkMaxSpeed() );
		this.setUpatePosition( true );
		GameBoard.getInstance().endCameraShake();
	}

	private Vector2 getNextSearchDest() {
		float MIN_DIST = 800f;
		float MAX_DIST = 1000f;
		float magnitude = Utils.getRandomFloatInRange( MIN_DIST, MAX_DIST );
		
		Vector2 tempDest = new Vector2();

		if( !this.getClipping() ) {
			tempDest.x = Utils.getRandomFloatInRange( 200f, GameBoard.getInstance().getWidth() - 200f );// TODO: MAGIC NUMBER
			tempDest.y = Utils.getRandomFloatInRange( 200f, GameBoard.getInstance().getHeight() - 200f );// TODO: MAGIC NUMBER
			return tempDest;
		}
		
		do {
			tempDest.set( Utils.getRandomFloatInRange( -1f, 1f ), Utils.getRandomFloatInRange( -1f, 1f ) );
			tempDest.nor();
			tempDest.mul( magnitude );
			tempDest.add( this.getPosition() );
		}
		while( !GameBoard.getInstance().isPositionInBounds( tempDest ) );
		
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
		
		float SIGHT_DISTANCE = 550f; //TODO: magic number
		float SIGHT_DISTANCE_DIAG = 247.487f;//( float )Math.sqrt( ( SIGHT_DISTANCE * SIGHT_DISTANCE ) / 2f );
		float SIGHT_OFFSET = 250f; //TODO: magic number
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
		
		return ( u >= 0f ) && ( v >= 0f ) && ( ( u + v ) < 1f );
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
