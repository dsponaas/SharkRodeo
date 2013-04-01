package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dg.sharkrodeo.GameObject.Direction;
import com.dg.sharkrodeo.HudUtilityButton.UtilityButtonType;
import com.dg.sharkrodeo.Shark.SharkState;

public class Hud {
	
	private BitmapFont _hudFont;
	private TextureRegion _livesIcon;
	private TextureRegion _sharksLeftIcon;
	private TextureRegion _scoreIcon;
	private TextureRegion _2xIcon;
	private TextureRegion _4xIcon;
	private TextureRegion _speedUpIcon;
	private TextureRegion _enduranceUpIcon;
	private TextureRegion _redExclamationIcon;
	private TextureRegion _yellowExclamationIcon;
	
	private TextureRegion _sharkPosIconUp;
	private TextureRegion _sharkPosIconDown;
	private TextureRegion _sharkPosIconLeft;
	private TextureRegion _sharkPosIconRight;
	private TextureRegion _sharkPosIconUpLeft;
	private TextureRegion _sharkPosIconUpRight;
	private TextureRegion _sharkPosIconDownLeft;
	private TextureRegion _sharkPosIconDownRight;
	
	private HudUtilityButton[] _utilityButtons;
	
	private static Hud _instance = null;
	
	private Hud() {
	}
	
	public static Hud getInstance() {
		if( _instance == null )
			_instance = new Hud();
		return _instance;
	}
	
	public void initialize() {
		_hudFont = new BitmapFont( Gdx.files.internal( "data/small_fonts_82_white_bold.fnt" ), false );
		
		_livesIcon = ResourceManager.getInstance().getBackgroundTexture( "lives_icon" );
		_scoreIcon = ResourceManager.getInstance().getBackgroundTexture( "score_label" );
		_sharksLeftIcon = ResourceManager.getInstance().getBackgroundTexture( "sharks_left_icon" );
		_2xIcon = ResourceManager.getInstance().getBackgroundTexture( "powerup_2x" );
		_4xIcon = ResourceManager.getInstance().getBackgroundTexture( "powerup_4x" );
		_speedUpIcon = ResourceManager.getInstance().getBackgroundTexture( "powerup_speed_up" );
		_enduranceUpIcon = ResourceManager.getInstance().getBackgroundTexture( "powerup_endurance_up" );
		_redExclamationIcon = ResourceManager.getInstance().getBackgroundTexture( "red_exclamation" );
		_yellowExclamationIcon = ResourceManager.getInstance().getBackgroundTexture( "yellow_exclamation" );

		_sharkPosIconUp = ResourceManager.getInstance().getBackgroundTexture( "shark_icon_up" );
		_sharkPosIconDown = ResourceManager.getInstance().getBackgroundTexture( "shark_icon_down" );
		_sharkPosIconLeft = ResourceManager.getInstance().getBackgroundTexture( "shark_icon_left" );
		_sharkPosIconRight = ResourceManager.getInstance().getBackgroundTexture( "shark_icon_right" );
		_sharkPosIconUpLeft = ResourceManager.getInstance().getBackgroundTexture( "shark_icon_upleft" );
		_sharkPosIconUpRight = ResourceManager.getInstance().getBackgroundTexture( "shark_icon_upright" );
		_sharkPosIconDownLeft = ResourceManager.getInstance().getBackgroundTexture( "shark_icon_downleft" );
		_sharkPosIconDownRight = ResourceManager.getInstance().getBackgroundTexture( "shark_icon_downright" );
		
		TextureRegion sprintActiveIcon = ResourceManager.getInstance().getBackgroundTexture( "sprint_icon_active" );
		TextureRegion sprintInactiveIcon = ResourceManager.getInstance().getBackgroundTexture( "sprint_icon_inactive" );
		
		_utilityButtons = new HudUtilityButton[ 1 ];
		for( int i = 0; i < _utilityButtons.length; ++i ) {
			_utilityButtons[ i ] = null;
		}
		
		_utilityButtons[ 0 ] = new HudUtilityButton( 0, sprintActiveIcon, sprintInactiveIcon, UtilityButtonType.SPRINT );
	}
	
	private boolean isTouchInHud( float x, float y ) {
		if( ( x < SharkRodeoConstants.getUtilityBarWidth() ) || ( y < SharkRodeoConstants.getStatusBarWidth() ) ) {
			return true;
		}
		return false;
	}
	
	public BitmapFont getFont() {
		return _hudFont;
	}
	
	public boolean touchDown( float x, float y) {
		if( isTouchInHud( x, y ) ) {
			for( HudUtilityButton curButton : _utilityButtons ) {
				if( curButton != null ) {
					curButton.touchDown( x, y );
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean touchUp( float x, float y ) {
		if( isTouchInHud( x, y ) ) {
			for( HudUtilityButton curButton : _utilityButtons ) {
				if( curButton != null ) {
					curButton.touchUp( x, y );
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean touchDragged( float x, float y ) {
		if( isTouchInHud( x, y ) ) {
			for( HudUtilityButton curButton : _utilityButtons ) {
				if( curButton != null ) {
					curButton.touchDragged( x, y );
				}
			}
			return true;
		}
		return false;
	}
	
	public void deactivateButton( UtilityButtonType buttonType ) {
		for( HudUtilityButton cur : _utilityButtons ) {
			if( ( cur != null ) && ( cur.getType() == buttonType ) ) {
				cur.deactivate();
			}
		}
	}
	
	public TextureRegion getSharkPositionIcon( Direction direction ) {
		switch( direction ) {
		case UP:
			return _sharkPosIconUp;
		case DOWN:
			return _sharkPosIconDown;
		case LEFT:
			return _sharkPosIconLeft;
		case RIGHT:
			return _sharkPosIconRight;
		case UP_LEFT:
			return _sharkPosIconUpLeft;
		case UP_RIGHT:
			return _sharkPosIconUpRight;
		case DOWN_LEFT:
			return _sharkPosIconDownLeft;
		case DOWN_RIGHT:
			return _sharkPosIconDownRight;
		} // switch( newDirection )
		return null;
	}
	
	public int getNumButtons()									{ return _utilityButtons.length; }
	public HudUtilityButton getButton(int position)				{ return _utilityButtons[position]; }
	public TextureRegion getLivesIcon()							{ return _livesIcon; }
	public TextureRegion getScoreIcon()							{ return _scoreIcon; }
	public TextureRegion getSharksLeftIcon()					{ return _sharksLeftIcon; }
	public TextureRegion get2xIcon()							{ return _2xIcon; }
	public TextureRegion get4xIcon()							{ return _4xIcon; }
	public TextureRegion getSpeedUpIcon()						{ return _speedUpIcon; }
	public TextureRegion getEnduranceUpIcon()					{ return _enduranceUpIcon; }
	public TextureRegion getRedExclamationIcon()				{ return _redExclamationIcon; }
	public TextureRegion getYellowExclamationIcon()				{ return _yellowExclamationIcon; }
	
}
