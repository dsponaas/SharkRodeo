package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.HudUtilityButton.UtilityButtonType;
import com.dg.sharkrodeo.Player.PlayerState;
import com.dg.sharkrodeo.Shark.SharkState;
import com.dg.sharkrodeo.Dialogs.SharkRodeoDialog;

public class GameRenderer {
	
	private OrthographicCamera _camera;
	private SpriteBatch _batch;
	private ShapeRenderer _shapeRenderer;
	private ShapeRenderer _hudBackgroundRenderer;
	private SpriteBatch _hudBatch;
	private Vector2 _cameraDest;
	private Vector2 _cameraTempDest;
	private boolean _cameraShake;
	
	private Rectangle _screenBounds;
	
	private float _topLeftAngle;
	private float _topRightAngle;
	private float _bottomLeftAngle;
	private float _bottomRightAngle;
	
	private static final float CAMERA_Z = 0f; //if i leave this final, move to constants
	
	public GameRenderer() {
		float camWidth = Gdx.graphics.getWidth();
		float camHeight = Gdx.graphics.getHeight();
		
		_camera = new OrthographicCamera();
		_camera.setToOrtho( false, camWidth, camHeight );
		_camera.update();
		
		_batch = new SpriteBatch();
		_hudBatch = new SpriteBatch();
		_batch.setProjectionMatrix( _camera.combined );
		
		_shapeRenderer = new ShapeRenderer();
		_hudBackgroundRenderer = new ShapeRenderer();
		
		_cameraDest = new Vector2();
		_cameraTempDest = new Vector2();
		_cameraShake = false;
		
		Vector2 center = new Vector2( Gdx.graphics.getWidth() / 2f, ( Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() ) / 2f );
		Vector2 topLeft = new Vector2( 0, Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() ).sub( center );
		Vector2 topRight = new Vector2( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() ).sub( center );
		Vector2 bottomLeft = new Vector2( 0, 0 ).sub( center );
		Vector2 bottomRight = new Vector2( Gdx.graphics.getWidth(), 0 ).sub( center );
		_topLeftAngle = topLeft.angle();
		_topRightAngle = topRight.angle();
		_bottomLeftAngle = bottomLeft.angle();
		_bottomRightAngle = bottomRight.angle();
		
		_screenBounds = new Rectangle();
		_screenBounds.setWidth( camWidth );
		_screenBounds.setHeight( camHeight - SharkRodeoConstants.getStatusBarWidth() );

	} // public GameRenderer()

	public void setCameraDest( float x, float y ) {
		_cameraDest.set( x, y );
	}
	
	public void update( float delta ) {
		Vector2 dest = new Vector2();
		float speed = SharkRodeoConstants.getCameraSpeed();
		if( _cameraShake ) {
			speed *= SharkRodeoConstants.CAMERA_SHAKE_MULTIPLIER;
			dest.x = _cameraTempDest.x;
			dest.y = _cameraTempDest.y;
		}
		else {
			dest.x = _cameraDest.x;
			dest.y = _cameraDest.y;
		}

		if( GameBoard.getInstance().getPlayerState() != PlayerState.MOUNTED ) {
			setCameraPosition(dest.x, dest.y );
		}
		else {
			Vector2 deltaDest = new Vector2(dest);
			deltaDest.sub(_camera.position.x, _camera.position.y);
			float length = deltaDest.len();
			if( length > 10f ) {// TODO: Magic number
				deltaDest.mul( ( 1f / length ) * speed * delta );
				setCameraPosition( _camera.position.x + deltaDest.x, _camera.position.y + deltaDest.y );
			}
			else {
				setCameraPosition( dest.x, dest.y );
				if( _cameraShake ) {
					resetCameraShakePos();
				}
			}
		} // else
		
		_screenBounds.setX( _camera.position.x - ( Gdx.graphics.getWidth() / 2f ) );
		_screenBounds.setY( _camera.position.y - ( ( Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() ) / 2f ) );

		_camera.update();
	} // public void update( float delta )
	
	public void resetCameraPosition( float x, float y ) {
		_cameraDest.set( x, y );
		setCameraPosition( x, y );
	}
	
	private void resetCameraShakePos() {
		float deltaX = Utils.getRandomFloatInRange( SharkRodeoConstants.getCameraShakeMin(), SharkRodeoConstants.getCameraShakeMax() );
		float deltaY = Utils.getRandomFloatInRange( SharkRodeoConstants.getCameraShakeMin(), SharkRodeoConstants.getCameraShakeMax() );
		if( Utils.coinFlip() ) {
			deltaX *= -1f;
		}
		if( Utils.coinFlip() ) {
			deltaY *= -1f;
		}
		_cameraTempDest.x = _cameraDest.x + deltaX;
		_cameraTempDest.y = _cameraDest.y + deltaY;
	} // private void resetCameraShakePos()
	
	public void startCameraShake() {
		resetCameraShakePos();
		_cameraShake = true;
	}
	
	public void endCameraShake() {
		_cameraShake = false;
	}
	
	private void setCameraPosition( float x, float y ) {
		Vector2 delta = new Vector2( _cameraDest.x - x, _cameraDest.y - y );
		float frameWidthX = ( float )Gdx.graphics.getWidth() / 2f * 0.65f;// TODO: magic number
		float frameWidthY = ( float )Gdx.graphics.getHeight() / 2f * 0.65f;// TODO: magic number
		
		float xSpillover = Math.abs( delta.x ) - frameWidthX;
		float ySpillover = Math.abs( delta.y ) - frameWidthY;

		float xAdjustment = 0f;
		float yAdjustment = 0f;
		
		if( xSpillover > 0f ) {
			xAdjustment = ( delta.x > 0f ) ? xSpillover : xSpillover * -1f;
		}
		if( ySpillover > 0f ) {
			yAdjustment = ( delta.y > 0f ) ? ySpillover : ySpillover * -1f;
		}
		
		_camera.position.set( x + xAdjustment, y + yAdjustment, CAMERA_Z );
	} // private void setCameraPosition( float x, float y )
	
	public void beginRender()
	{// i could do some checking of states here or whatever but im not going to
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		_batch.setProjectionMatrix(_camera.combined);
		_batch.begin();
	}
	
	public void finishRender() {
		_batch.end();
	}
	
	public void renderGameObjectParticles( GameObject obj, float delta ) {
		Vector2 position = obj.getPosition();
		for( ParticleEffect emitter : obj.getEmitters() ) {
			if( ! obj.isInTheWater() ) {
				emitter.setPosition( -1000, -1000 ); //TODO: hackish asa motherfucker. find a better solution
			}
			else {
				emitter.setPosition( position.x, position.y );
			}
			emitter.draw( _batch, delta );
		}
	} // public void renderGameObjectParticles( GameObject obj, float delta )
	
	public void renderGameObject( GameObject obj, float delta ) {//float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation
		Vector2 position = obj.getPosition();
		obj.updateView( delta );
		
		TextureRegion texture = obj.getTexture();
		if( texture != null ) {
			_batch.draw( texture, position.x - ( ( float )( texture.getRegionWidth() / 2 ) ), position.y - ( ( float )( texture.getRegionHeight() / 2 ) ) );
		}
	} // public void renderGameObject( GameObject obj, float delta )

	public void renderShark( Shark obj, float delta ) {
		Vector2 position = obj.getPosition();
		obj.updateView( delta );
		
		TextureRegion texture = obj.getTexture();
		if( texture != null )
			_batch.draw( texture, position.x - ( ( float )( texture.getRegionWidth() / 2 ) ), position.y - ( ( float )( texture.getRegionHeight() / 2 ) ) );
		
		if( obj.getSharkState() == SharkState.LUNGING ) {    //obj.isPointInLineOfSight( playerPos.x, playerPos.y ) ) {
			TextureRegion exclamation = Hud.getInstance().getRedExclamationIcon();
			_batch.draw( exclamation, position.x - ( ( float )( exclamation.getRegionWidth() / 2 ) ), position.y + ( ( float )( exclamation.getRegionHeight() / 2 ) ) );
		}
		else if( obj.isAboutToTurn() ) {
			TextureRegion exclamation = Hud.getInstance().getYellowExclamationIcon();
			_batch.draw( exclamation, position.x - ( ( float )( exclamation.getRegionWidth() / 2 ) ), position.y + ( ( float )( exclamation.getRegionHeight() / 2 ) ) );
		}
	} // public void renderShark( Shark obj, float delta )
	
	public void renderSharkPositionIndicator( Shark shark ) {
		//i can get the delta between camera pos and shark
		
		Vector2 center = new Vector2( Gdx.graphics.getWidth() / 2f, ( Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() ) / 2f );
		Vector2 deltaVector = ( new Vector2( shark.getPosition() ) ).sub( _camera.position.x, _camera.position.y ).nor();
		float deltaVectorAngle = deltaVector.angle();
//		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "angles br:" + _bottomRightAngle + " ... tr:" + _topRightAngle + " ... tl:" + _topLeftAngle + " ... bl:" + _topLeftAngle );
		
		TextureRegion tex = Hud.getInstance().getSharkPositionIcon( shark.getDirection() );
		if( null == tex ) {
			Gdx.app.log(SharkRodeoConstants.LOG_TAG, "null texture in renderer.getsharkpositionindicator" );
			return;
		}
		
		if( ( deltaVectorAngle > _bottomRightAngle ) || ( deltaVectorAngle < _topRightAngle ) ) { // right
			float distToBorder = Gdx.graphics.getWidth() / 2f;
			float factor = distToBorder / deltaVector.x;
			deltaVector.mul( factor );
			center.add( deltaVector );
			center.set( renderSharkPositionIndicatorHelper( center, tex.getRegionWidth(), tex.getRegionHeight() ) );
//			center.set( center.x - tex.getRegionWidth(), center.y );
		}
		else if( deltaVectorAngle < _topLeftAngle ) { // up
			float distToBorder = ( Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() ) / 2f;
			float factor = distToBorder / deltaVector.y;
			deltaVector.mul( factor );
			center.add( deltaVector );
			center.set( renderSharkPositionIndicatorHelper( center, tex.getRegionWidth(), tex.getRegionHeight() ) );
//			center.set( center.x, center.y - tex.getRegionHeight() );
		}
		else if( deltaVectorAngle < _bottomLeftAngle ) { // left
			float distToBorder = Gdx.graphics.getWidth() / -2f;
			float factor = distToBorder / deltaVector.x;
			deltaVector.mul( factor );
			center.add( deltaVector );
			center.set( renderSharkPositionIndicatorHelper( center, tex.getRegionWidth(), tex.getRegionHeight() ) );
		}
		else { // down
			float distToBorder = ( Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() ) / -2f;
			float factor = distToBorder / deltaVector.y;
			deltaVector.mul( factor );
			center.add( deltaVector );
			center.set( renderSharkPositionIndicatorHelper( center, tex.getRegionWidth(), tex.getRegionHeight() ) );
		}
		
		_hudBatch.draw( tex, center.x, center.y );
	} // public void renderSharkPositionIndicator( Shark shark )
	
	private Vector2 renderSharkPositionIndicatorHelper( Vector2 pos, int texWidth, int texHeight ) {
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth();
//		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "prePos x:" + pos.x + " y:" + pos.y );
		if( pos.x < 0f ) {
			pos.set( 0f, pos.y );
		}
		else if( pos.x > ( width - texWidth ) ) {
			pos.set( width - texWidth, pos.y );
		}
		
		if( pos.y < 0f ) {
			pos.set( pos.x, 0f );
		}
		else if( pos.y > ( height - texHeight ) ) {
			pos.set( pos.x, height - texHeight );
		}
//		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "postPos x:" + pos.x + " y:" + pos.y );
		return pos;
	} // private Vector2 renderSharkPositionIndicatorHelper( Vector2 pos )
	
	public void renderWaveLayer( Wave wave, float delta, boolean top ) {
		wave.updateView( delta );
		Vector2 position = wave.getPosition();
		TextureRegion texture;
		float alpha;
		
		if( top ) {
			texture = wave.getTopTexture();
			alpha = 0.9f;
		}
		else {
			texture = wave.getBottomTexture();
			alpha = 0.5f;
		}
		
		_batch.setColor( 1f, 1f, 1f, alpha );
		if( texture != null ) {
			_batch.draw( texture, position.x - ( ( float )( texture.getRegionWidth() / 2 ) ), position.y - ( ( float )( texture.getRegionHeight() / 2 ) ) );
		}
		_batch.setColor( 1f, 1f, 1f, 1f );
	} // public void renderWaveLayer( Wave wave, float delta, boolean top )
	
	public void renderWhirlpool( Whirlpool pool, float delta ) {
		pool.updateView( delta );
		for( int i = 0; i < pool.getNumLayers(); ++i ) {
			Vector2 pos = pool.getPosition();
			TextureRegion tex = pool.getTexture( i );
			_batch.setColor( 1, 1, 1, pool.getAlpha() );
			_batch.draw( tex, pos.x - ( ( float )tex.getRegionWidth() / 2f ), pos.y - ( ( float )tex.getRegionHeight() / 2f ) );
			_batch.setColor( 1, 1, 1, 1 );
		} // for( int i = 0; i < pool.getNumLayers(); ++i )
	} // public void renderWhirlpool( Whirlpool pool, float delta )
	
	public void renderGameBoard() {
		float frameWidth = Gdx.graphics.getWidth();
		float frameHeight = Gdx.graphics.getHeight();
		
		float originX = _camera.position.x - ( frameWidth / 2f );
		float originY = _camera.position.y - ( frameHeight / 2f );
		
		TextureRegion backgroundTexture = GameBoard.getInstance().getBackgroundTexture();
		float backgroundTexWidth = ( ( float )backgroundTexture.getRegionWidth() );
		float backgroundTexHeight = ( ( float )backgroundTexture.getRegionHeight() );
		float backgroundStartX = 0f - frameWidth;
		float backgroundStartY = 0f - frameHeight;
		while( backgroundStartX < ( originX - backgroundTexWidth ) )
			backgroundStartX += backgroundTexWidth;
		while( backgroundStartY < ( originY - backgroundTexHeight ) )
			backgroundStartY += backgroundTexHeight;
		for( float curX = backgroundStartX; curX < ( originX + frameWidth + backgroundTexWidth ); curX += backgroundTexWidth ) {
			for( float curY = backgroundStartY; curY < ( originY + frameHeight + backgroundTexHeight ); curY += backgroundTexHeight ) {
				_batch.draw( backgroundTexture, curX, curY );
			}
		}
		
		for( OceanLayer layer : GameBoard.getInstance().getOceanLayers() ) {
			TextureRegion texture = layer.getTexture();
			for( Vector2 pos : layer.getTexturePositions( originX, originY ) ) {
				_batch.setColor( 1, 1, 1, layer.getAlpha() );
				_batch.draw( texture, pos.x, pos.y );
				_batch.setColor( 1, 1, 1, 1 );
			}
		} // for( OceanLayer layer : GameBoard.getInstance().getOceanLayers() )
	} // public void renderGameBoard()
	
	public void renderBorder() {
		GameBoard board = GameBoard.getInstance();
		
		TextureRegion vertBoundsTex = board.getVertBoundsTexture();
		float borderWidth = ( ( float )vertBoundsTex.getRegionWidth() ) / 2f;
		float borderLength = ( float )vertBoundsTex.getRegionHeight();

		float leftBoundsX = -1f * borderWidth;
		float rightBoundsX = board.getWidth() - borderWidth;
		float leftBoundsY = borderLength - borderWidth;
		for( ; ( leftBoundsY + borderLength ) < board.getHeight(); leftBoundsY += borderLength ) {
			_batch.draw( vertBoundsTex, leftBoundsX, leftBoundsY );
			_batch.draw( vertBoundsTex, rightBoundsX, leftBoundsY );
		}
		
		TextureRegion horBoundsTex = board.getHorBoundsTexture();
		leftBoundsY = -1f * borderWidth;
		float rightBoundsY = board.getHeight() - borderWidth;
		for( leftBoundsX = borderLength - borderWidth; ( leftBoundsX + borderLength ) < board.getWidth(); leftBoundsX += borderLength ) {
			_batch.draw( horBoundsTex, leftBoundsX, leftBoundsY );
			_batch.draw( horBoundsTex, leftBoundsX, rightBoundsY );
		}

		_batch.draw( board.getBoundsBLTexture(), -1f * borderWidth, -1f * borderWidth );
		_batch.draw( board.getBoundsBRTexture(), board.getWidth() + borderWidth - board.getBoundsTLTexture().getRegionWidth(), -1f * borderWidth );
		_batch.draw( board.getBoundsTRTexture(), board.getWidth() + borderWidth - board.getBoundsTLTexture().getRegionWidth(), board.getHeight() + borderWidth - board.getBoundsTLTexture().getRegionHeight() );
		_batch.draw( board.getBoundsTLTexture(), -1f * borderWidth, board.getHeight() + borderWidth - board.getBoundsTLTexture().getRegionHeight() );
	} // public void renderBorder()
	
	public void renderMountedTouchPos( boolean touchFlag, float percent ) {
		float alpha = percent * SharkRodeoConstants.DEFAULT_RENDER_TOUCHPOS_ALPHA;
		if(touchFlag)
			_batch.setColor( 0, 1, 0, alpha );
		else
			_batch.setColor( 1, 0, 0, alpha );
		
		TextureRegion tex = GameBoard.getInstance().getMountedTouchTexture();
		Vector2 pos = GameBoard.getInstance().getPlayerPos();
		_batch.draw( tex, pos.x - ( tex.getRegionWidth() / 2f ), pos.y - ( tex.getRegionHeight() / 2f ) );
		_batch.setColor( 1, 1, 1, 1 );
	} // public void renderMountedTouchPos( boolean touchFlag, float percent )
	
	public void beginShapeRender() {
		_shapeRenderer.setProjectionMatrix( _camera.combined );
	}
	
	public void endShapeRender() {
		_shapeRenderer.end();
	}
	
	public void renderHitBoxes( GameObject obj ) {
		for( Circle cur : obj.getBounds() ) {
			_shapeRenderer.begin( ShapeType.Circle );
			_shapeRenderer.setColor( Color.RED );
			_shapeRenderer.circle( cur.x, cur.y, cur.radius );
			_shapeRenderer.end();
		}
	} // public void renderHitBoxes( GameObject obj )
	
	public void renderSharkDest( Shark shark ) {
		Vector2 dest = shark.getSharkDest();
		_shapeRenderer.begin( ShapeType.FilledCircle );
		_shapeRenderer.setColor( Color.RED );
		_shapeRenderer.filledCircle( dest.x, dest.y, 3 );
		_shapeRenderer.end();
	} // public void renderSharkDest( Shark shark )
	
	public void renderHitPoints( Shark shark ) {
		float health = shark.getHealthPercent();
		if(health > .98f) //TODO: Magic number
			return;
		
		float width = 120f; //TODO:Magic number
		float height = 8f; //TODO:Magic number
		float yOffset = 60f; //TODO:Magic number
		float buffer = 1f; //TODO:Magic number
		Vector2 sharkPos = shark.getPosition();
		
		_shapeRenderer.begin( ShapeType.FilledRectangle );
		_shapeRenderer.setColor( new Color( 0f, 0f, 0f, 0.3f ) );
		_shapeRenderer.filledRect( sharkPos.x - ( width / 2f ), sharkPos.y + yOffset, width, height );
		_shapeRenderer.end();
		
		_shapeRenderer.begin( ShapeType.FilledRectangle );
		_shapeRenderer.setColor( new Color( 1f, 0f, 0f, 0.5f ) );
		_shapeRenderer.filledRect( sharkPos.x - ( width / 2f ) + buffer, sharkPos.y + yOffset + buffer, width - ( 2f * buffer ), height - ( 2f * buffer ) );
		_shapeRenderer.end();
		
		_shapeRenderer.begin( ShapeType.FilledRectangle );
		_shapeRenderer.setColor( new Color( 0f, 1f, 0f, 0.8f ) );
		_shapeRenderer.filledRect( sharkPos.x - ( width / 2f ) + buffer, sharkPos.y + yOffset + buffer, health * ( width - ( 2f * buffer ) ), height - ( 2f * buffer ) );
		_shapeRenderer.end();
	} // public void renderHitPoints( Shark shark )
	
	public void beginHudBatch() {
		_hudBatch.begin();
	}
	
	public void endHudBatch() {
		_hudBatch.end();
	}
	
	public void renderHud() {
		Hud hud = Hud.getInstance();
		
		_hudBackgroundRenderer.begin( ShapeType.FilledRectangle );
		_hudBackgroundRenderer.setColor( SharkRodeoConstants.HUD_BLANK_COLOR );
		_hudBackgroundRenderer.filledRect( 0f, ( ( float )Gdx.graphics.getHeight() ) - SharkRodeoConstants.getStatusBarWidth(), ( float )Gdx.graphics.getWidth(), SharkRodeoConstants.getStatusBarWidth() );
		_hudBackgroundRenderer.end();
		
		BitmapFont hudFont = hud.getFont();
		TextureRegion livesIcon = hud.getLivesIcon();
		TextureRegion sharksLeftIcon = hud.getSharksLeftIcon();

		float livesIconXPos = 0f;
		float livesIconYPos = ( float )Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() + ( ( SharkRodeoConstants.getStatusBarWidth() - ( float )livesIcon.getRegionHeight() ) / 2f );
		_hudBatch.draw(livesIcon, livesIconXPos, livesIconYPos);
		int lives = GameState.getLives();
		hudFont.draw( _hudBatch, Integer.toString( lives < 0 ? 0 : lives ), livesIconXPos + ( float )livesIcon.getRegionWidth(), ( float )Gdx.graphics.getHeight() - 4f );//TODO: magic number
		
		float sharksLeftIconXPos = ( ( float )Gdx.graphics.getWidth() ) / 6f;
		_hudBatch.draw( sharksLeftIcon, sharksLeftIconXPos, livesIconYPos );
		int sharksRemaining = GameBoard.getInstance().getSharksRemaining();
		hudFont.draw( _hudBatch, Integer.toString( sharksRemaining < 0 ? 0 : sharksRemaining ), sharksLeftIconXPos + ( float )sharksLeftIcon.getRegionWidth(), ( float )Gdx.graphics.getHeight() - 4f );//TODO: magic number
		
		float levelXPos = sharksLeftIconXPos * 2f;
		hudFont.draw( _hudBatch, "LEVEL:" + GameState.getLevel(), levelXPos, ( float )Gdx.graphics.getHeight() - 4f );//TODO: magic number
		
		float scoreIconXPos = ((float)Gdx.graphics.getWidth()) / 6f + ((float)Gdx.graphics.getWidth()) / 2f;
		hudFont.draw( _hudBatch, "SCORE:" + GameState.getScore(), scoreIconXPos, ( float )Gdx.graphics.getHeight() - 4f );//TODO: magic number
		
		_hudBatch.setColor( 1, 1, 1, 0.7f );
		TextureRegion multiplierIcon = hud.get2xIcon();
		float powerupIconXPos = ( float )Gdx.graphics.getWidth() - ( float )multiplierIcon.getRegionWidth();
		float powerupIconYPos = ( float )Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth() - ( float )multiplierIcon.getRegionHeight();
		int multiplier = GameState.getMulitplier();
		if( multiplier == 2 ) {
			_hudBatch.draw( hud.get2xIcon(), powerupIconXPos, powerupIconYPos ); // TODO: magic number
			powerupIconYPos -= ( float )multiplierIcon.getRegionHeight();
		}
		else if( multiplier == 4 ) {
			_hudBatch.draw( hud.get4xIcon(), powerupIconXPos, powerupIconYPos ); // TODO: magic number
			powerupIconYPos -= ( float )multiplierIcon.getRegionHeight();
		}
		
		for( PlayerModifier cur : GameBoard.getInstance().getPlayer().getModifiers() ) {
			if( cur != null ) {
				TextureRegion icon = null;
				switch( cur.getType() ) {
				case SPEED_UP:
					icon = hud.getSpeedUpIcon();
					break;
				case ENDURANCE_UP:
					icon = hud.getEnduranceUpIcon();
					break;
				}

				if( icon != null ) {
					_hudBatch.draw( icon, powerupIconXPos, powerupIconYPos );
					powerupIconYPos -= ( float )icon.getRegionHeight();
				}
			} // if( cur != null )
		} // for( PlayerModifier cur : GameBoard.getInstance().getPlayer().getModifiers() )
		
		_hudBatch.setColor( 1, 1, 1, 1 );

		for( int i = 0; i < hud.getNumButtons(); ++i ) {
			HudUtilityButton curButton = hud.getButton( i );
			if( curButton == null ) {
				continue;
			}
			TextureRegion buttonTex = curButton.getTexture();
			Vector2 position = curButton.getPosition();
			_hudBatch.draw( buttonTex, position.x, position.y );
			
			if( curButton.getType() == UtilityButtonType.SPRINT ) {// a bit hackish but this 'should' be my only special case button... maybe
				float remaining = GameState.getSprintRemainingPercent();
				
				if( remaining < .98f ) {
					float width = 8f; //TODO:Magic number
					float height = ( float )buttonTex.getRegionHeight();
					float buffer = 1f; //TODO:Magic number
					
					_hudBackgroundRenderer.begin( ShapeType.FilledRectangle );
					_hudBackgroundRenderer.setColor( Color.BLACK );
					_hudBackgroundRenderer.filledRect( SharkRodeoConstants.getUtilityBarWidth(), position.y, width, height );
					_hudBackgroundRenderer.end();
					
					_hudBackgroundRenderer.begin( ShapeType.FilledRectangle );
					_hudBackgroundRenderer.setColor( Color.RED );
					_hudBackgroundRenderer.filledRect( SharkRodeoConstants.getUtilityBarWidth() + buffer, position.y + buffer, width - ( 2f * buffer ), height - ( 2f * buffer ) );
					_hudBackgroundRenderer.end();
					
					_hudBackgroundRenderer.begin( ShapeType.FilledRectangle );
					_hudBackgroundRenderer.setColor( Color.GREEN );
					_hudBackgroundRenderer.filledRect( SharkRodeoConstants.getUtilityBarWidth() + buffer, position.y + buffer, width - ( 2f * buffer ), remaining * ( height - ( 2f * buffer ) ) );
					_hudBackgroundRenderer.end();
				} // if( remaining < .98f )
			} // if( curButton.getType() == UtilityButtonType.SPRINT )
		} // for( int i = 0; i < hud.getNumButtons(); ++i )
	} // public void renderHud()
	
	public void renderDialog( SharkRodeoDialog dialog ) {
		TextureRegion dialogTex = dialog.getTexture();
		Vector2 pos = dialog.getPosition();
		if( ( dialogTex != null ) && ( pos != null ) ) { //TODO: take this check out as an optimization?
			_hudBatch.setColor( 1, 1, 1, 0.9f );
			_hudBatch.draw( dialogTex, pos.x, pos.y );
			_hudBatch.setColor( 1, 1, 1, 1 );
		}
	} // public void renderDialog( SharkRodeoDialog dialog )
	
	public void renderPowerup( Powerup powerup, float delta ) {
		TextureRegion tex = powerup.getTexture();
		Vector2 pos = powerup.getPosition();
		ParticleEffect emitter = powerup.getEmitter();

		_batch.setColor( 1, 1, 1, powerup.getAlpha() );
		if( emitter != null ) {
			emitter.setPosition( pos.x, pos.y );
			emitter.draw( _batch, delta );
		}
		_batch.draw( tex, pos.x - ( ( float )( tex.getRegionWidth() / 2 ) ), pos.y - ( ( float )( tex.getRegionHeight() / 2 ) ) );
		_batch.setColor( 1, 1, 1, 1 );
	} // public void renderPowerup( Powerup powerup, float delta )
	
	public Vector2 getCameraPos() {
		return new Vector2( _camera.position.x, _camera.position.y );
	}
	
	public Rectangle getScreenBounds() {
		return _screenBounds;
	}

} // public class GameRenderer
