package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.Factories.AnimationFactory;

public class Whirlpool {

	public enum WhirlpoolState { FADE_IN, ACTIVE, FADE_OUT };
	private WhirlpoolState _whirlpoolState;
	private float _stateTime;
	
	private Vector2 _position;
	private Circle _killZone;
	private Circle _outerGravZone;
	private Circle _innerGravZone;
	private float _grav;
	private Animation _anim;
	private float _elapsed;
	private float _accelerationATerm;
	
	private static RecurringSoundPlayer _sound;
	private static int _whirlpoolId;
	
	private final float EVENT_HORIZON_FACTOR = 3f; //TODO: MAGIC NUMBER
	private final float FADE_TIME = 3f; //TODO: MAGIC NUMBER
	private final float ACTIVE_TIME = 15f; //TODO: MAGIC NUMBER
	
	public Whirlpool( float x, float y ) {
		_position = new Vector2( x, y );
		_killZone = new Circle( x, y, 40f ); //TODO: MAGIC NUMBER
		_outerGravZone = new Circle( x, y, 500f ); //TODO: MAGIC NUMBER
		_innerGravZone = new Circle( x, y, 220f ); //TODO: MAGIC NUMBER
		_grav = 300f;
		
		TextureRegion tex = ResourceManager.getInstance().getWhirlpoolTexture( "whirlpool" );
		_anim = AnimationFactory.createAnimation( 0.1f, 2, 4, tex );
		
		float delta = _outerGravZone.radius - _innerGravZone.radius;
		_accelerationATerm = _grav / ( delta * delta );
		
		_whirlpoolState = WhirlpoolState.FADE_IN;
		_stateTime = FADE_TIME;
		
		if ( _sound == null ) {
			_sound = new RecurringSoundPlayer( ResourceManager.getInstance().getWhirlpoolSound() );
		}
		
		_whirlpoolId = -1;
	}
	
	public boolean update( float delta ) {
		_stateTime -= delta;
		
		if( _stateTime < 0f ) {
			switch( _whirlpoolState ) {
			case FADE_IN:
				_whirlpoolState = WhirlpoolState.ACTIVE;
				_stateTime = ACTIVE_TIME;
				break;
			case ACTIVE:
				_whirlpoolState = WhirlpoolState.FADE_OUT;
				_stateTime = FADE_TIME;
				break;
			case FADE_OUT:
				return false;
			}
		}
		
		Vector2 playerDelta = new Vector2( GameBoard.getInstance().getPlayerPos() );
		playerDelta.sub( _position );
		float dist = playerDelta.len();
		final float maxDist = ( ( float )Gdx.graphics.getWidth() / 2f ); //TODO: MOVE THIS TO CONSTANTS 
		if( dist > maxDist ) {
			_sound.setVolume( _whirlpoolId, -1f );
		}
		else {
			float factor = ( maxDist - dist ) / maxDist;
			_sound.setVolume( _whirlpoolId, factor );
		}
	
		return true;
	}
	
	public boolean isBeingPulled( float x, float y ) {
		if( _whirlpoolState != WhirlpoolState.ACTIVE )
			return false;
		return _outerGravZone.contains( x, y );
	}
	
	public boolean isInKillZone( float x, float y ) {
		if( _whirlpoolState != WhirlpoolState.ACTIVE )
			return false;
		return _killZone.contains( x, y );
	}
	
	public Vector2 getAcceleration( float x, float y ) {
		Vector2 delta = new Vector2( _position.x - x, _position.y - y );
		
		float len = delta.len();
		float distFromGravZone = _outerGravZone.radius - len;
		float factor = _accelerationATerm * distFromGravZone * distFromGravZone;
		if( len > _outerGravZone.radius )
			factor = 0f;
		else if( len < _innerGravZone.radius )
			factor = _grav * EVENT_HORIZON_FACTOR;
		
		return delta.nor().mul( factor );
	}
	
	public void updateView( float delta ) {
		_elapsed += delta;
	}
	
	public float getAlpha() {
		if( _whirlpoolState == WhirlpoolState.ACTIVE )
			return 1f;
		else if( _whirlpoolState == WhirlpoolState.FADE_IN )
			return ( FADE_TIME - _stateTime ) / FADE_TIME;
		return _stateTime / FADE_TIME;
	}
	
	public void kill() {
		_whirlpoolState = WhirlpoolState.ACTIVE;
		_stateTime = -1f;
		_sound.setVolume( _whirlpoolId, -1f );
	}
	
	public void setId( int id ) {
		_whirlpoolId = id;
	}

//	public int getNumLayers()						{ return _anims.length; }
	public TextureRegion getTexture()				{ return _anim.getKeyFrame( _elapsed, true ); }
	public Vector2 getPosition()					{ return _position; }
	
}
;