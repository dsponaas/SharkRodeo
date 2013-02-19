package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.Player.PlayerState;

public class HudUtilityButton {
	
	public enum UtilityButtonType { SPRINT };
	private UtilityButtonType _buttonType;

	private Rectangle _bounds;
	private TextureRegion _activeTexture;
	private TextureRegion _inactiveTexture;
	private boolean _active;
	
	public HudUtilityButton( int position, TextureRegion activeTexture, TextureRegion inactiveTexture, UtilityButtonType type ) {
		_activeTexture = activeTexture;
		_inactiveTexture = inactiveTexture;
		_bounds = new Rectangle();
		_active = false;
		_buttonType = type;
		setBounds( position );
	}
	
	private void setBounds( int position ) {
		float BORDER = 8f; //TODO: magic number
		_bounds.x = BORDER;
		_bounds.y = ( float )Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() - BORDER - ( float )_activeTexture.getRegionHeight();
		_bounds.height = ( float )_activeTexture.getRegionHeight();
		_bounds.width = ( float )_activeTexture.getRegionWidth();
	}
	
	public boolean touchDown( float x, float y ) {
		float adjustedY = ( float )Gdx.graphics.getHeight() - y;
		if( _bounds.contains( x, adjustedY ) ) {
			if( !_active )
				activate();
		}
		else {
			if( _active )
				deactivate();
		}
		
		return false;
	}
	
	public boolean touchUp( float x, float y ) {
		float adjustedY = ( float )Gdx.graphics.getHeight() - y;
		if( _bounds.contains( x, adjustedY ) ) {
			if( _active )
				deactivate();
		}
		
		return false;
	}
	
	public boolean touchDragged( float x, float y ) {
		float adjustedY = ( float )Gdx.graphics.getHeight() - y;
		if( _bounds.contains( x, adjustedY ) ) {
			if( !_active )
				activate();
		}
		else {
			if( _active )
				deactivate();
		}
		
		return false;
	}
	
	public void activate() {
		PlayerState playerState = GameBoard.getInstance().getPlayerState();
		if( ( playerState == PlayerState.IDLE ) || ( playerState == PlayerState.MOVING ) ) {
			if( ( _buttonType == UtilityButtonType.SPRINT ) && ( GameState.getSprintRemainingPercent() > 0f ) ) {
				_active = true;
				GameBoard.getInstance().getPlayer().setSprint( true );
			}
		}
	}
	
	public void deactivate() {
		_active = false;

		if( _buttonType == UtilityButtonType.SPRINT ) {
			GameBoard.getInstance().getPlayer().setSprint( false );
		}
	}
	
	public TextureRegion getTexture() {
		if( _active )
			return _activeTexture;
		return _inactiveTexture;
	}

	public UtilityButtonType getType()				{ return _buttonType; }
	public Vector2 getPosition()					{ return new Vector2(_bounds.x, _bounds.y ); }

}
