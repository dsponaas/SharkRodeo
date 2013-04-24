package com.dg.sharkrodeo.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.dg.sharkrodeo.GameBoard;
import com.dg.sharkrodeo.GameState;
import com.dg.sharkrodeo.ResourceManager;
import com.dg.sharkrodeo.SharkRodeo;

public class MenuScreen implements Screen {

	private SharkRodeo _game;
	private Stage _stage;
	private TextureAtlas _atlas;
	private Skin _skin;
	private SpriteBatch _batch;
	private ImageButton _startButton;
//	private TextButton _sizeButton;
	private ImageButton _scoreButton;
	private ImageButton _creditsButton;
//	private Label _label;
	
	public MenuScreen(SharkRodeo game) {
		GameState.menuReset();
		GameBoard.getInstance().startMenu(_game);
		Music menuMusic = ResourceManager.getInstance().getMenuMusic();
		if( !menuMusic.isPlaying() ) {
			menuMusic.play();
		}
		_game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		GameBoard.getInstance().update(delta);
		GameBoard.getInstance().render(delta, false);
		GameBoard.getInstance().unpause();
		
		_stage.act(delta);
		_batch.begin();
		//_whiteFont.draw(_batch, "SHARK RODEO, BITCHES!", (Gdx.graphics.getWidth() / 2) - 15 , (Gdx.graphics.getHeight() / 2) - 100);
		_stage.draw();
		_batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if(null == _stage)
			_stage = new Stage(width, height, true);
		_stage.clear();
		
		Gdx.input.setInputProcessor(_stage);
		
		int BUFFER = 12;
		
		TextureRegion startButtonTex = ResourceManager.getInstance().getMenuTexture( "startButton" );
		_startButton = new ImageButton( new TextureRegionDrawable( startButtonTex ) );
		_startButton.setX((Gdx.graphics.getWidth() / 2) - (_startButton.getWidth() / 2));
		_startButton.setY(((Gdx.graphics.getHeight() / 3) * 2) - (_startButton.getHeight()));
		
		TextureRegion scoreButtonTex = ResourceManager.getInstance().getMenuTexture( "scoreButton" );
		_scoreButton = new ImageButton( new TextureRegionDrawable( scoreButtonTex ) );
		_scoreButton.setX((Gdx.graphics.getWidth() / 2) - (_scoreButton.getWidth() / 2));
		_scoreButton.setY(((Gdx.graphics.getHeight() / 3) * 2) - (2 * _scoreButton.getHeight()) - (2 * BUFFER));
		
		TextureRegion creditsButtonTex = ResourceManager.getInstance().getMenuTexture( "creditsButton" );
		_creditsButton = new ImageButton( new TextureRegionDrawable( creditsButtonTex ) );
		_creditsButton.setX((Gdx.graphics.getWidth() / 2) - (_creditsButton.getWidth() / 2));
		_creditsButton.setY(((Gdx.graphics.getHeight() / 3) * 2) - (3 * _creditsButton.getHeight()) - (3 * BUFFER));
		
		_startButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				GameState.reset();
//				GameState.initLevel();
//				GameBoard.getInstance().startGame( _game );
				_game.setScreen(new GameScreen(_game));
			}
		});
		
		_scoreButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				_game.showScoreloop();
			}
		});
		
		_creditsButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				return;
			}
		});
		
		TextureRegion bannerTex = ResourceManager.getInstance().getMenuTexture( "banner" );
		Image banner = new Image( bannerTex );
		banner.setX( ( Gdx.graphics.getWidth() / 2 ) - ( bannerTex.getRegionWidth() / 2 ));
		banner.setY( Gdx.graphics.getHeight() - bannerTex.getRegionHeight() - 50 );//todo: magic number

		_stage.addActor(banner);
		_stage.addActor(_startButton);
		_stage.addActor(_scoreButton);
		_stage.addActor(_creditsButton);
	}

	@Override
	public void show() {
		_batch = new SpriteBatch();
		_atlas = new TextureAtlas("data/button.pack");
		_skin = new Skin();
		_skin.addRegions(_atlas);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		_batch.dispose();
		_skin.dispose();
		_atlas.dispose();
		_stage.dispose();
	}
}
