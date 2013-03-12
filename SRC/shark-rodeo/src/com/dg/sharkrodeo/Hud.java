package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dg.sharkrodeo.HudUtilityButton.UtilityButtonType;

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
		
		TextureAtlas atlas = new TextureAtlas( SharkRodeoConstants.getHudPack() );
		_livesIcon = atlas.findRegion( "lives_icon" );
		_scoreIcon = atlas.findRegion( "score_label" );
		_sharksLeftIcon = atlas.findRegion( "sharks_left_icon" );
		_2xIcon = atlas.findRegion( "powerup_2x" );
		_4xIcon = atlas.findRegion( "powerup_4x" );
		_speedUpIcon = atlas.findRegion( "powerup_speed_up" );
		_enduranceUpIcon = atlas.findRegion( "powerup_endurance_up" );
		_redExclamationIcon = atlas.findRegion( "red_exclamation" );
		_yellowExclamationIcon = atlas.findRegion( "yellow_exclamation" );
		
		TextureRegion sprintActiveIcon = atlas.findRegion( "sprint_icon_active" );
		TextureRegion sprintInactiveIcon = atlas.findRegion( "sprint_icon_inactive" );
		
		_utilityButtons = new HudUtilityButton[ 1 ];
		for( int i = 0; i < _utilityButtons.length; ++i )
			_utilityButtons[ i ] = null;
		
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
	
	public void dispose() {
//		_hudFont;
//		_livesIcon;
//		_sharksLeftIcon;
//		_scoreIcon;
//		_2xIcon;
//		_4xIcon;
//		_speedUpIcon;
//		_enduranceUpIcon;
//		_redExclamationIcon;
//		_yellowExclamationIcon.dispose();
	}
	
}
