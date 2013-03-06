package com.dg.sharkrodeo;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.Player.PlayerState;
import com.dg.sharkrodeo.Powerup.PowerupType;
import com.dg.sharkrodeo.Shark.SharkState;
import com.dg.sharkrodeo.Wave.Direction;
import com.dg.sharkrodeo.Dialogs.GameOverDialog;
import com.dg.sharkrodeo.Dialogs.PlayerDeathDialog;
import com.dg.sharkrodeo.Dialogs.SharkRodeoDialog;
import com.dg.sharkrodeo.Dialogs.StartLevelDialog;
import com.dg.sharkrodeo.Factories.AnimationFactory;
import com.dg.sharkrodeo.HudUtilityButton.UtilityButtonType;

public class GameBoard {
	
	private static GameBoard _instance = null;
	
	private Player _player;
	private Shark[] _sharks;
	private Wave[] _waves;
	private Whirlpool[] _whirlpools;
	private Powerup[] _powerups;
	
	private Rectangle _bounds;

	private GameRenderer _renderer;
	
	private boolean _ridingFlag;
	private float _ridingTime;
	private boolean _paused;
	
	private Texture _backgroundTexture;
	
	private Texture _boundsVertTexture;
	private Texture _boundsHorTexture;
	private Texture _boundsBLTexture;
	private Texture _boundsBRTexture;
	private Texture _boundsTLTexture;
	private Texture _boundsTRTexture;
	
	private Texture _mountedTouchTexture;

	private OceanLayer[] _oceanLayers;
	private InputHandler _inputHandler;
	
	private SharkRodeoDialog _dialog;
	private float _dialogTimer;
	
	private boolean _gameActive;
	
	private GameBoard() {
		_bounds = new Rectangle();
		_bounds.x = 0f;
		_bounds.y = 0f;
		_bounds.width = SharkRodeoConstants.getGameBoardWidth();
		_bounds.height = SharkRodeoConstants.getGameBoardHeight();
		
		_renderer = new GameRenderer();
		
		Texture layerTex = null;
		switch( SharkRodeoConstants.getScale() ) {
		case _128:
			_backgroundTexture = new Texture( "data/water_tex_128_blur.png" );
			_boundsVertTexture = new Texture( "data/board_bounds_vert.png" );
			_boundsHorTexture = new Texture( "data/board_bounds_hor.png" );
			_boundsBLTexture = new Texture( "data/board_bounds_bl.png" );
			_boundsBRTexture = new Texture( "data/board_bounds_br.png" );
			_boundsTRTexture = new Texture( "data/board_bounds_tr.png" );
			_boundsTLTexture = new Texture( "data/board_bounds_tl.png" );
			_mountedTouchTexture = new Texture( "data/touchpos.png" );
			layerTex = new Texture( "data/ocean_anim_1_sparse.png" );
			break;
		case _256:
			_backgroundTexture = new Texture( "data/water_tex_256_blur.png" );
			_boundsVertTexture = new Texture( "data/board_bounds_vert_256.png" );
			_boundsHorTexture = new Texture( "data/board_bounds_hor_256.png" );
			_boundsBLTexture = new Texture( "data/board_bounds_bl_256.png" );
			_boundsBRTexture = new Texture( "data/board_bounds_br_256.png" );
			_boundsTRTexture = new Texture( "data/board_bounds_tr_256.png" );
			_boundsTLTexture = new Texture( "data/board_bounds_tl_256.png" );
			_mountedTouchTexture = new Texture( "data/touchpos_256.png" );
			layerTex = new Texture( "data/ocean_anim_256_1_sparse.png" );
			break;
		}
		
		_backgroundTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		_boundsVertTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		_boundsHorTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		_boundsBLTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		_boundsBRTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		_boundsTRTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		_boundsTLTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		_mountedTouchTexture.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		layerTex.setFilter( TextureFilter.Linear, TextureFilter.Linear );
		
		_oceanLayers = new OceanLayer[ 4 ];
		_oceanLayers[ 0 ] = new OceanLayer( AnimationFactory.createAnimation( .3f, 2, 4, layerTex ), 0, 0, 0.2f );
		_oceanLayers[ 1 ] = new OceanLayer( AnimationFactory.createAnimation( .34f, 2, 4, layerTex ), -96, -16f, 0.62f ); //TODO: these all eventually need to be fixed up to accomodate scale or what have you.. do they?
		_oceanLayers[ 2 ] = new OceanLayer( AnimationFactory.createAnimation( .38f, 2, 4, layerTex ), -32, -32f, 0.45f );
		_oceanLayers[ 3 ] = new OceanLayer( AnimationFactory.createAnimation( .42f, 2, 4, layerTex ), -64, -48f, 0.89f );
		
		_dialogTimer = -1f;
		//**** THIS MUST HAPPEN BEFORE CREATION OF INPUTHANDLER ***
//		_hud = new Hud(this);
		Hud.getInstance().initialize();
		ResourceManager.getInstance().initialize();

		_inputHandler = new InputHandler( this );

		_ridingFlag = false;
		
		_powerups = new Powerup[ 3 ];
		_whirlpools = new Whirlpool[ 8 ];

		_gameActive = true;
		
//		spawnPlayer();
	}
	
	public static GameBoard getInstance() {
		if( _instance == null )
			_instance = new GameBoard();
		return _instance;
	}
	
	public void startGame() {
		Hud.getInstance().initialize();
		ResourceManager.getInstance().initialize();

		_gameActive = true;
		
		_sharks = new Shark[ 0 ];
		for( int i = 0; i < _sharks.length; ++i )
			_sharks[ i ] = null; //dont think this is necessary in java - investigate
		
		_waves = new Wave[ 0 ]; //TODO:magic number
		for( int i = 0; i < _waves.length; ++i )
			_waves[ i ] = null; //dont think this is necessary in java - investigate
		
		_player = new Player( this );
		_player.setPosition( getWidth() / 2f, getHeight() / 2f );
		
		Gdx.input.setInputProcessor( _inputHandler );
		
		_dialog = new StartLevelDialog( ResourceManager.getInstance().getDialogTexture( "dialog_begin" ), this );
		pause();
		
		resetCamera();
	}
	
	private void pause() {
		_paused = true;
		_inputHandler.pauseGameInput();	
	}
	
	private void unpause() {
		_paused = false;
		_inputHandler.resumeGameInput();	
	}
	
	public boolean dialogPress( int x, int y ) {
		if( _dialog == null )
			return false;
		if( _dialogTimer > 0f )
			return false;
		
		if( _dialog.checkTouch( x, y ) ) {
			_dialog.onComplete();
			_dialog = null;
			return true;
		}

		return false;
	}
	
	public boolean update( float delta ) {
		if( _dialogTimer > 0f )
			_dialogTimer -= delta;
		
		_renderer.setCameraDest( _player.getPosition().x, _player.getPosition().y + ( SharkRodeoConstants.getStatusBarWidth() / 2f ) );
		_renderer.update( delta );
		for( OceanLayer layer : _oceanLayers )
			layer.update( delta );
		if( ( _dialog != null ) && ( _dialogTimer < 0f ) )
			_dialog.update( delta );

		PlayerState playerState = _player.getPlayerState();
		for( Shark curShark : _sharks ) {
			if( curShark != null ) {
				curShark.update( delta );
				if( ( playerState == PlayerState.IDLE ) || ( playerState == PlayerState.MOVING ) ) {
					Vector2 playerPos = _player.getPosition();
					if( Utils.circlesOverlap( curShark.getBounds()[ 0 ], _player.getBounds()[ 0 ] ) ) {
						beginPlayerDeath();
					}
					if( Utils.circlesOverlap( curShark.getBounds()[ 1 ], _player.getBounds()[ 0 ] ) ) {
						beginMount( curShark );
					}

					if( curShark.isPointInLineOfSight( playerPos.x, playerPos.y ) ) {
						curShark.lungeAtPlayer();
					}
					else {
//						curShark.setAccelerationRate( SharkRodeoConstants.getSharkAcceleration() );
//						curShark.setMaxSpeed( SharkRodeoConstants.getSharkMaxSpeed() );
					}
				}
				else if( !curShark.isBeingRidden() ) {
					curShark.endLunge();
//					curShark.setAccelerationRate( SharkRodeoConstants.getSharkAcceleration() );
//					curShark.setMaxSpeed( SharkRodeoConstants.getSharkMaxSpeed() );
				}
				
				Vector2 curSharkPos = curShark.getPosition();
				Vector2 curInnerSharkPos;
				for( Shark curInnerShark : _sharks ) {
					if( ( curInnerShark != null ) && ( curInnerShark != curShark ) ) {
						curInnerSharkPos = curInnerShark.getPosition();
						float distX = curInnerSharkPos.x - curSharkPos.x;
						float distY = curInnerSharkPos.y - curSharkPos.y;
						float lenSquared = ( distX * distX ) + ( distY * distY );
						Circle sharkBounds = curShark.getCollisionBounds();
						Circle innerSharkBounds = curInnerShark.getCollisionBounds();
						
						float radSquared = ( 2f * sharkBounds.radius ) * ( 2f * innerSharkBounds.radius );
						if( lenSquared < radSquared ) {
							float overlap = ( float )( Math.sqrt( radSquared ) - Math.sqrt( lenSquared ) );
							Vector2 curToInner = new Vector2( distX, distY );
							curToInner.div( curToInner.len() );
							curToInner.mul( overlap / 2f );
							
							curShark.setPosition( curSharkPos.x - curToInner.x, curSharkPos.y - curToInner.y );
							curInnerShark.setPosition( curInnerSharkPos.x + curToInner.x, curInnerSharkPos.y + curToInner.y );
							
							Circle curSharkCircle = new Circle( curSharkPos.x, curSharkPos.y, sharkBounds.radius );
							Circle curInnerSharkCircle = new Circle( curInnerSharkPos.x, curInnerSharkPos.y, innerSharkBounds.radius );
							Utils.collideCircularGameObjects( curSharkCircle, curInnerSharkCircle, curShark, curInnerShark );
						}
					}
				} // for( Shark curInnerShark : _sharks )
			} // if ( curShark != null )
		}
		
		for( int i = 0; i < _waves.length; ++i ) {
			if( _waves[ i ] == null )
				continue;
			
			for( Shark curShark : _sharks ) {
				if( curShark == null )
					continue;
				if( ( curShark.getSharkState() == SharkState.MOUNTED ) || ( curShark.getSharkState() == SharkState.THRASHING ) )
					continue;
				_waves[ i ].pushGameObj( curShark, delta );
			}
			
			_waves[ i ].pushGameObj( _player, delta );

			if( !_waves[ i ].update( delta ) ) {
				_waves[ i ] = null;
			}
		}
		
		for( int i = 0; i < _whirlpools.length; ++i ) {
			Whirlpool curWhirl = _whirlpools[ i ];
			if( curWhirl != null ) {
				if( !curWhirl.update( delta ) ) {
					_whirlpools[ i ] = null;
					continue;
				}
				
				Vector2 pos = _player.getPosition();
				if( curWhirl.isInKillZone( pos.x, pos.y ) && ( playerState != PlayerState.MOUNTING ) && ( playerState != PlayerState.MOUNTED ) ) {
					beginPlayerDeath();
				}
				
				if( curWhirl.isBeingPulled( pos.x, pos.y ) ) {
					Vector2 accel = curWhirl.getAcceleration( pos.x, pos.y ).mul( delta );
					Vector2 vel = _player.getVelocity();
					_player.setVelocity( vel.x + accel.x, vel.y + accel.y );
				}
			}
		}
		
		for( int i = 0; i < _powerups.length; ++i ) {
			Powerup curPowerup = _powerups[ i ];
			if( curPowerup != null ) {
				if( !curPowerup.update( delta ) )
					_powerups[ i ] = null;
				else if( ( playerState == PlayerState.IDLE ) || ( playerState == PlayerState.MOVING ) )
					curPowerup.checkForPickup( _player.getCollisionBounds() );
			}
		}
		
		if(_paused)
			return _gameActive;
		
		GameState.update( delta, _player.isSprinting() );
		if( GameState.isSprintDepleted() ) {
			Hud.getInstance().deactivateButton( UtilityButtonType.SPRINT );
		}
		
		if( this.canSpawnShark() ) {
			if( GameState.checkForSharkSpawn() )
				this.spawnShark();
		}
		
		if( this.canSpawnWave() ) {
			if( GameState.checkForWaveSpawn() )
				this.spawnWave();
		}
		
		if( this.canSpawnWhirlpool() ) {
			if( GameState.checkForWhirpoolSpawn() )
				this.spawnWhirlpool();
		}
		
		if( this.canSpawnPowerup() ) {
			if( GameState.checkForPowerupSpawn() )
				this.spawnPowerup();
		}
		
		if( _player.isAlive() )
			_player.update( delta );
		
		if( ( playerState == PlayerState.MOUNTED ) && ( _ridingFlag == false ) ) {
			_ridingTime -= delta;
			if( _ridingTime < 0f )
				_player.dismountingShark();
		}
		
		if( this.isLevelComplete() ) {
			this.incrementLevel();
		}
		
		return _gameActive;
	}
	
	public void render(float delta) { //need delta for particle effects, yes?
		_renderer.beginRender();
		
		_renderer.renderGameBoard();

		List<Shark> onScreenSharks = new ArrayList<Shark>();
		for( Shark curShark : _sharks ) {
			if( ( curShark != null ) && ( curShark.isOnScreen() ) ) {
				onScreenSharks.add( curShark );
			}
		}
		
		for( Shark curShark : onScreenSharks ) {
			_renderer.renderGameObjectParticles( curShark, delta );
		}
		if( _player.isAlive() )
			_renderer.renderGameObjectParticles( _player, delta );
		
		_renderer.renderBorder();
		
		for( Powerup curPowerup : _powerups ) {
			if( curPowerup != null )
				_renderer.renderPowerup( curPowerup, delta );
		}

		for( Whirlpool curWhirl : _whirlpools ) {
			if( curWhirl != null )
				_renderer.renderWhirlpool( curWhirl, delta );
		}
		
		for( Wave curWave : _waves ) {
			if( curWave != null )
				_renderer.renderWaveLayer( curWave, delta, false );
		}
		
		if( _player.getPlayerState() == PlayerState.MOUNTED ) {
			if( _ridingFlag )
				_renderer.renderMountedTouchPos( true, getRidingTimePercent() );
			else
				_renderer.renderMountedTouchPos( false, getRidingTimePercent() );
		}
		
		for( Shark curShark : onScreenSharks ) {
			_renderer.renderShark( curShark, delta );
		}
		
		if( _player.isAlive() )
			_renderer.renderGameObject( _player, delta );
		
		for( Wave curWave : _waves ) {
			if( curWave != null )
				_renderer.renderWaveLayer( curWave, delta, true );
		}
		_renderer.finishRender();

		_renderer.beginShapeRender();

		for( Shark curShark : onScreenSharks ) {
			_renderer.renderHitPoints( curShark );
		}
		
		if( SharkRodeoConstants.RENDER_HITBOXES ) {
			for( Shark curShark : _sharks ) {
				if( curShark == null )
					continue;
				_renderer.renderHitBoxes( curShark );
			}
			_renderer.renderHitBoxes( _player );
		}
		
		for( Shark curShark : onScreenSharks ) {
			_renderer.renderSharkDest( curShark );
		}
		
		_renderer.endShapeRender();
		
		_renderer.renderHud();
		if( ( _dialog != null ) && ( _dialogTimer < 0f ) )
			_renderer.renderDialog( _dialog );
		_renderer.endHudBatch();
	}

	public void dispose()
	{		
	}

	//****************************************************************************************************************************************************************************************************	
	//****************************************************************************************************************************************************************************************************	
	//**********make these dependent on level******************************************************************************************************************************************************************************************	
	//****************************************************************************************************************************************************************************************************	
	//****************************************************************************************************************************************************************************************************	
	public void initLevel() {
		int level = GameState.getLevel();
		_sharks = new Shark[ LevelInfo.getMaximumSharks( level ) ];
		_waves = new Wave[ LevelInfo.getMaximumWaves( level ) ];
		_whirlpools = new Whirlpool[ LevelInfo.getMaximumWhirlpools( level ) ];
		for( int i = 0; i < LevelInfo.getInitialNumSharks( level ); ++i ) {
			spawnShark();
		}
	}
	
	public boolean isLevelComplete() {
		if( GameState.getSharksRemaining() == 0 ) {
			for( Shark shark : _sharks ) {
				if( shark != null )
					return false;
			}
			return true;
		}
		return false;
	}
	
	public void incrementLevel() {
		pause();
		GameState.incrementLevel();
		_dialog = new StartLevelDialog( ResourceManager.getInstance().getDialogTexture( "dialog_begin" ), this );
	}
	
	public Vector2 getWorldPosFromScreenPos( int screenX, int screenY ) {
		//NOTE: screen origin is top left
		int screenXOffset = ( ( Gdx.graphics.getWidth() / 2 ) - screenX ) * -1;
		int screenYOffset = ( ( Gdx.graphics.getHeight() / 2 ) - screenY );
		
		Vector2 cameraPos = _renderer.getCameraPos();
		cameraPos.x += ( float )screenXOffset;
		cameraPos.y += ( float )screenYOffset;
		
		return cameraPos;
	}
	
	public boolean isPositionInBounds( Vector2 pos ) {
		float CRAPTAR_BUFFER = 100f; //********************************************************************************** TODO: fix this
		if( ( ( pos.x - CRAPTAR_BUFFER ) > _bounds.x ) && ( ( pos.x + CRAPTAR_BUFFER ) < _bounds.width ) &&
		   ( ( pos.y - CRAPTAR_BUFFER ) > _bounds.y ) && ( ( pos.y + CRAPTAR_BUFFER ) < _bounds.height ) )
			return true;
		return false;
	}
	
	public void movePlayerTo( Vector2 dest ) {
		_player.moveTo( dest );
	}
	
	public void killPlayerAcceleration() {
		_player.stopMoving();
	}
	
	public void reportRidingPosition( Vector2 worldPos ) {
		if( worldPos == null ) {
			_ridingFlag = false;
			return;
		}
		
		if( _player.isValidRidingPosition( worldPos ) ) {
			_ridingFlag = true;
		}
		else {
			_ridingFlag = false;
		}
	}
	
	public void tameShark( Shark shark ) {
		for( int i = 0; i < _sharks.length; ++i ) {
			if( _sharks[ i ] == shark ) {
				endCameraShake();
				_sharks[ i ] = null;
			}
		}
	}
	
	public boolean canSpawnShark() {
		for( Shark shark : _sharks ) {
			if( shark == null )
				return true;
		}
		return false;
	}
	
	public boolean canSpawnWave() {
		for( Wave wave : _waves ) {
			if( wave == null )
				return true;
		}
		return false;
	}
	
	public boolean canSpawnWhirlpool() {
		for( Whirlpool whirlpool : _whirlpools ) {
			if( whirlpool == null )
				return true;
		}
		return false;
	}
	
	public boolean canSpawnPowerup() {
		for( Powerup powerup : _powerups ) {
			if( powerup == null )
				return true;
		}
		return false;
	}
	
	public void spawnShark() {
		final float BUFFER = 50f; // TODO: MAGIC NUMBER
		
		float xPos, yPos;
		if( Utils.coinFlip() ) {
			xPos = Utils.getRandomFloatInRange( 0f + BUFFER, getWidth() - BUFFER );
			if( Utils.coinFlip() ) {
				yPos = getHeight() + ( ( float )Gdx.graphics.getHeight() / 2f );
			}
			else {
				yPos = 0f - ( ( float )Gdx.graphics.getHeight() / 2f );
			}
		}
		else {
			yPos = Utils.getRandomFloatInRange( 0f + BUFFER, getHeight() - BUFFER );
			if( Utils.coinFlip() ) {
				xPos = getWidth() + ( ( float )Gdx.graphics.getWidth() / 2f );
			}
			else {
				xPos = 0f - ( ( float )Gdx.graphics.getWidth() / 2f );
			}
		}
		
		for( int i = 0; i < _sharks.length; ++i ) {
			if( _sharks[ i ] == null ) {
				Shark shark = new Shark( this );
				shark.setPosition( xPos, yPos );
				_sharks[ i ] = shark;
				break;
			}
		}
	}
	
	public void spawnWave() {
		final float BUFFER = 50f; // TODO: MAGIC NUMBER
		Wave.Direction dir;
		
		float xPos, yPos;
		if( Utils.coinFlip() ) {
			xPos = Utils.getRandomFloatInRange( 0f + BUFFER, getWidth() - BUFFER );
			if( Utils.coinFlip() ) {
				yPos = getHeight() + ( ( float )Gdx.graphics.getHeight() / 2f );
				dir = Direction.DOWN;
			}
			else {
				yPos = 0f - ( ( float )Gdx.graphics.getHeight() / 2f );
				dir = Direction.UP;
			}
		}
		else {
			yPos = Utils.getRandomFloatInRange( 0f + BUFFER, getHeight() - BUFFER );
			if( Utils.coinFlip() ) {
				xPos = getWidth() + ( ( float )Gdx.graphics.getWidth() / 2f );
				dir = Direction.LEFT;
			}
			else {
				xPos = 0f - ( ( float )Gdx.graphics.getWidth() / 2f );
				dir = Direction.RIGHT;
			}
		}
		
		for( int i = 0; i < _waves.length; ++i ) {
			if( _waves[ i ] == null ) {
				TextureRegion bottomTex = null;
				TextureRegion topTex = null;
				switch( dir ) {
				case UP:
					bottomTex = ResourceManager.getInstance().getWaveTexture( "wave_bottom_up" );
					topTex = ResourceManager.getInstance().getWaveTexture( "wave_top_up" );
					break;
				case DOWN:
					bottomTex = ResourceManager.getInstance().getWaveTexture( "wave_bottom_down" );
					topTex = ResourceManager.getInstance().getWaveTexture( "wave_top_down" );
					break;
				case LEFT:
					bottomTex = ResourceManager.getInstance().getWaveTexture( "wave_bottom_left" );
					topTex = ResourceManager.getInstance().getWaveTexture( "wave_top_left" );
					break;
				case RIGHT:
					bottomTex = ResourceManager.getInstance().getWaveTexture( "wave_bottom_right" );
					topTex = ResourceManager.getInstance().getWaveTexture( "wave_top_right" );
					break;
				}

				Wave wave = new Wave( dir, new Vector2( xPos, yPos ), bottomTex, topTex );
				_waves[ i ] = wave;
				break;
			}
		}
		
	}
	
	public void spawnWhirlpool() {
		TextureRegion bottomTex = ResourceManager.getInstance().getWhirlpoolTexture( "whirlpool_bottom" );
		TextureRegion topTex = ResourceManager.getInstance().getWhirlpoolTexture( "whirlpool_top" );
		
		Animation bottomAnim = AnimationFactory.createAnimation( 0.3f, 1, 2, bottomTex );
		Animation topAnim = AnimationFactory.createAnimation( 0.3f, 1, 2, topTex );
		
		Whirlpool newWhirlpool = new Whirlpool( Utils.getRandomFloatInRange( 0f, getWidth() ), Utils.getRandomFloatInRange( 0f, getHeight() ), bottomAnim, topAnim );
		for( int i = 0; i < _whirlpools.length; ++i ) {
			if( _whirlpools[ i ] == null ) {
				_whirlpools[ i ] = newWhirlpool;
				break;
			}
		}
	}
	
	public void spawnPowerup() {
//		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***********: spawnpowerup");
		for( int i = 0; i < _powerups.length; ++i ) {
			if( _powerups[ i ] == null ) {
				Powerup powerup;
				float val = Utils.getRandomFloatInRange( 0f, 100f );
				if( val < 20f )
					powerup = new Powerup( PowerupType.MULTIPLIER_2X, new Vector2( Utils.getRandomFloatInRange( 100f, getWidth() - 100f ), Utils.getRandomFloatInRange( 100f, getHeight() - 100f ) ) ); //TODO: magic numbers
				else if( val < 40f )
					powerup = new Powerup( PowerupType.MULTIPLIER_4X, new Vector2( Utils.getRandomFloatInRange( 100f, getWidth() - 100f ), Utils.getRandomFloatInRange( 100f, getHeight() - 100f ) ) ); //TODO: magic numbers
				else if( val < 60f )
					powerup = new Powerup( PowerupType.SPEED_UP, new Vector2( Utils.getRandomFloatInRange( 100f, getWidth() - 100f ), Utils.getRandomFloatInRange( 100f, getHeight() - 100f ) ) ); //TODO: magic numbers
				else if( val < 80f )
					powerup = new Powerup( PowerupType.ONE_UP, new Vector2( Utils.getRandomFloatInRange( 100f, getWidth() - 100f ), Utils.getRandomFloatInRange( 100f, getHeight() - 100f ) ) ); //TODO: magic numbers
				else
					powerup = new Powerup( PowerupType.ENDURANCE_UP, new Vector2( Utils.getRandomFloatInRange( 100f, getWidth() - 100f ), Utils.getRandomFloatInRange( 100f, getHeight() - 100f ) ) ); //TODO: magic numbers

				_powerups[ i ] = powerup;
				break;
			}
		}
		
	}
	
	public void beginPlayerDeath() {
		if( !_player.isAlive() )
			return;
		
		boolean gameOverFlag = GameState.takeLife();
		
		_player.kill();
		if( gameOverFlag ) {
			_dialog = new GameOverDialog( ResourceManager.getInstance().getDialogTexture( "dialog_gameover" ), this );
			_dialogTimer = 3f;
		}
		else {
			_dialog = new PlayerDeathDialog( ResourceManager.getInstance().getDialogTexture( "dialog_dead" ), this );
			_dialogTimer = 3f;
		}
		
		for( Shark curShark : _sharks ) {
			if( null != curShark ) {
				curShark.resetSharkState();
			}
		}
		
		endCameraShake(); // just for good measure...
		
		pause();
	}
	
	public void spawnPlayer() {
		Vector2 playerPos = new Vector2( getWidth() / 2f, getHeight() / 2f );
		
		for( int i = 0; i < _whirlpools.length; ++i ) {
			_whirlpools[ i ] = null;
		}
		
		for( int i = 0; i < _waves.length; ++i ) {
			_waves[ i ] = null;
		}
		
		for( int i = 0; i < _sharks.length; ++i ) {
			Shark curShark = _sharks[ i ];
			if( curShark == null )
				continue;
			float minDist = getWidth() / 5f; // TODO: magic number
			Vector2 sharkPos = curShark.getPosition();
			Vector2 delta = new Vector2( sharkPos.x - playerPos.x, sharkPos.y - playerPos.y );
			if( delta.len2() < ( minDist * minDist ) ) {
				delta.nor().mul( minDist );
				curShark.setPosition( playerPos.x + delta.x, playerPos.y + delta.y );
			}
		}
		
		_player.spawn();
		
		_player.killAcceleration();
		_player.killVelocity();
		
		_player.setPosition(playerPos.x, playerPos.y);
		resetCamera();
		
		unpause();
	}
	
	public int getSharksRemaining() {
		int retval = GameState.getSharksRemaining();
		for( Shark curShark : _sharks ) {
			if( curShark != null )
				++retval;
		}
		return retval;
	}
	
	public void beginMount( Shark shark ) {
		GameState.addToScore( 100 );
		_player.mountingShark( shark );
		_ridingTime = SharkRodeoConstants.RIDING_TIME;

	}
	
	public void resetCamera() {
		Vector2 playerPos = _player.getPosition();
		_renderer.resetCameraPosition( playerPos.x, playerPos.y + ( SharkRodeoConstants.getStatusBarWidth() / 2f ) );
	}
	
	public void startCameraShake() {
		_renderer.startCameraShake();
	}
	
	public void endCameraShake() {
		_renderer.endCameraShake();
	}
	
	public float getWidth() 				{ return _bounds.width; }
	public float getHeight() 				{ return _bounds.height; }
	public Rectangle getBounds()			{ return _bounds; }
	public PlayerState getPlayerState()		{ return _player.getPlayerState(); }
	public Vector2 getPlayerPos()			{ return _player.getPosition(); }
	public Player getPlayer()				{ return _player; }
	
	public Texture getBackgroundTexture()	{ return _backgroundTexture; }
	public Texture getVertBoundsTexture()	{ return _boundsVertTexture; }
	public Texture getHorBoundsTexture()	{ return _boundsHorTexture; }
	public Texture getBoundsBLTexture()		{ return _boundsBLTexture; }
	public Texture getBoundsBRTexture()		{ return _boundsBRTexture; }
	public Texture getBoundsTLTexture()		{ return _boundsTLTexture; }
	public Texture getBoundsTRTexture()		{ return _boundsTRTexture; }
	public Texture getMountedTouchTexture()	{ return _mountedTouchTexture; }
	
	public OceanLayer[] getOceanLayers()	{ return _oceanLayers; }
	
	public float getRidingTimePercent()		{ return _ridingTime / SharkRodeoConstants.RIDING_TIME; }
	
	public void gameOver()					{ _gameActive = false; }
}
