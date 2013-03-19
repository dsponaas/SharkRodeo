package com.dg.sharkrodeo.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.dg.sharkrodeo.GameState;
import com.dg.sharkrodeo.SharkRodeo;
import com.dg.sharkrodeo.SharkRodeoConstants;
import com.dg.sharkrodeo.SharkRodeoConstants.GameBoardScale;

public class MenuScreen implements Screen {

	private SharkRodeo _game;
	private Stage _stage;
	private BitmapFont _blackFont;
	private BitmapFont _vinerFont;
//	private BitmapFont _whiteFont;
	private TextureAtlas _atlas;
	private Skin _skin;
	private SpriteBatch _batch;
	private TextButton _button;
	private TextButton _sizeButton;
	private TextButton _scoreButton;
	private Label _label;
	
	public MenuScreen(SharkRodeo game) {
		GameState.reset();
		_game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	
		_stage.act(delta);
		
		_batch.begin();
		_stage.draw();
		//_whiteFont.draw(_batch, "SHARK RODEO, BITCHES!", (Gdx.graphics.getWidth() / 2) - 15 , (Gdx.graphics.getHeight() / 2) - 100);
		_batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if(null == _stage)
			_stage = new Stage(width, height, true);
		_stage.clear();
		
		Gdx.input.setInputProcessor(_stage);
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = _skin.getDrawable("buttonnormal");
		style.down = _skin.getDrawable("buttonpressed");
		style.font = _blackFont;
		
		int BUFFER = 12;
		
		_button = new TextButton("Start Game", style);
		_button.setWidth(400);
		_button.setHeight(120);
		_button.setX((Gdx.graphics.getWidth() / 2) - (_button.getWidth() / 2));
		_button.setY(((Gdx.graphics.getHeight() / 3) * 2) - (_button.getHeight()));
		
		_sizeButton = new TextButton("Scale:" + SharkRodeoConstants.getScale(), style);
		_sizeButton.setWidth(400);
		_sizeButton.setHeight(120);
		_sizeButton.setX((Gdx.graphics.getWidth() / 2) - (_button.getWidth() / 2));
		_sizeButton.setY(((Gdx.graphics.getHeight() / 3) * 2) - (2 * _button.getHeight()) - BUFFER);
		
		_scoreButton = new TextButton("High Scores", style);
		_scoreButton.setWidth(400);
		_scoreButton.setHeight(120);
		_scoreButton.setX((Gdx.graphics.getWidth() / 2) - (_button.getWidth() / 2));
		_scoreButton.setY(((Gdx.graphics.getHeight() / 3) * 2) - (3 * _button.getHeight()) - (2 * BUFFER));
		
		_button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				_game.setScreen(new GameScreen(_game));
			}
		});
		
		_sizeButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				switch(SharkRodeoConstants.getScale())
				{
				case _128:
					SharkRodeoConstants.setScale(GameBoardScale._256);
					break;
				case _256:
					SharkRodeoConstants.setScale(GameBoardScale._128);
					break;
				}
				_sizeButton.setText("Scale:" + SharkRodeoConstants.getScale());
			}
		});
		
		_scoreButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				SharkRodeo.showScoreloop();
			}
		});
		
		LabelStyle labelStyle = new LabelStyle(_vinerFont, Color.WHITE);
		_label = new Label("SHARK RODEO", labelStyle);
		_label.setX(0);
		_label.setY((Gdx.graphics.getHeight() / 3) * 2 + 100);
		_label.setWidth(Gdx.graphics.getWidth());
		_label.setAlignment(Align.center);
		
		_stage.addActor(_button);
		_stage.addActor(_sizeButton);
		_stage.addActor(_scoreButton);
		_stage.addActor(_label);
	}

	@Override
	public void show() {
		_batch = new SpriteBatch();
		_atlas = new TextureAtlas("data/button.pack");
		_skin = new Skin();
		_skin.addRegions(_atlas);
//		_whiteFont = new BitmapFont(Gdx.files.internal("data/whitefont.fnt"), false);
		_blackFont = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		_vinerFont = new BitmapFont(Gdx.files.internal("data/segoe_92_white_bold.fnt"), false);
		
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
//		_whiteFont.dispose();
		_vinerFont.dispose();
		_blackFont.dispose();
		_stage.dispose();
	}
}
