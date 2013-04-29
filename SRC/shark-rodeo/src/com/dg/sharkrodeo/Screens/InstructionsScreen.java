package com.dg.sharkrodeo.Screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.dg.sharkrodeo.GameBoard;
import com.dg.sharkrodeo.ResourceManager;
import com.dg.sharkrodeo.SharkRodeo;
import com.dg.sharkrodeo.Tweens.FadeTween;

public class InstructionsScreen implements Screen {

	private Stage _stage;
	private Sprite _instructionsSprite1;
	private Sprite _instructionsSprite2;
	private SpriteBatch _batch;
	private SharkRodeo _game;
	private TweenManager _tweenManager;
	private ImageButton _nextButton1;
	private ImageButton _nextButton2;
	private TweenCallback instructions2Showing;
	
	public InstructionsScreen(SharkRodeo game) {
		_game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		GameBoard.getInstance().update(delta);
		GameBoard.getInstance().render(delta, false);
		GameBoard.getInstance().unpause();
		
		_tweenManager.update(delta);
		
		_stage.act(delta);
		_batch.begin();
		_stage.draw();
		_batch.end();

		_batch.begin();
		_instructionsSprite1.draw(_batch);
		_instructionsSprite2.draw(_batch);
		_batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if(null == _stage)
			_stage = new Stage(width, height, true);
		_stage.clear();
		Gdx.input.setInputProcessor(_stage);

		TextureRegion intructionsTex1 = ResourceManager.getInstance().getMenuTexture( "instructions1" );
		_instructionsSprite1 = new Sprite(intructionsTex1);
		_instructionsSprite1.setColor(1, 1, 1, 0);
		_instructionsSprite1.setOrigin(_instructionsSprite1.getWidth() / 2, _instructionsSprite1.getHeight() / 2);
		_instructionsSprite1.setPosition((Gdx.graphics.getWidth() / 2) - (_instructionsSprite1.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (_instructionsSprite1.getHeight() / 2));
		
		TextureRegion intructionsTex2 = ResourceManager.getInstance().getMenuTexture( "instructions2" );
		_instructionsSprite2 = new Sprite(intructionsTex2);
		_instructionsSprite2.setColor(1, 1, 1, 0);
		_instructionsSprite2.setOrigin(_instructionsSprite2.getWidth() / 2, _instructionsSprite2.getHeight() / 2);
		_instructionsSprite2.setPosition((Gdx.graphics.getWidth() / 2) - (_instructionsSprite2.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (_instructionsSprite2.getHeight() / 2));
		
		_batch = new SpriteBatch();
		
		Tween.registerAccessor(Sprite.class, new FadeTween());
		_tweenManager = new TweenManager();
		TweenCallback instructions1Showing = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				showNextButton1();
			}
		};
		final TweenCallback instructions1Hiding = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				beginInstructions2();
			}
		};
		instructions2Showing = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				showNextButton2();
			}
		};
		final TweenCallback instructions2Hiding = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				tweenCompleted();
			}
		};
		Tween.to(_instructionsSprite1, FadeTween.ALPHA, 1f).target(1).ease(TweenEquations.easeInQuad).setCallback(instructions1Showing).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
		
		TextureRegion nextButtonTex = ResourceManager.getInstance().getMenuTexture( "nextButton" );
		_nextButton1 = new ImageButton( new TextureRegionDrawable( nextButtonTex ) );
		_nextButton1.setX( ( Gdx.graphics.getWidth() / 2 ) - (_nextButton1.getWidth() / 2f));
		_nextButton1.setY(20f); // TODO: magic number
		
		_nextButton1.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				_stage.getRoot().removeActor(_nextButton1);
				Tween.to(_instructionsSprite1, FadeTween.ALPHA, 1f).target(0).ease(TweenEquations.easeInQuad).setCallback(instructions1Hiding).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
//				tweenCompleted();
			}
		});
		
		_nextButton2 = new ImageButton( new TextureRegionDrawable( nextButtonTex ) );
		_nextButton2.setX( ( Gdx.graphics.getWidth() / 2 ) - (_nextButton2.getWidth() / 2f));
		_nextButton2.setY(20f); // TODO: magic number
		
		_nextButton2.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				_stage.getRoot().removeActor(_nextButton2);
				Tween.to(_instructionsSprite2, FadeTween.ALPHA, 1f).target(0).ease(TweenEquations.easeInQuad).setCallback(instructions2Hiding).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
			}
		});
	} // public void resize(int width, int height)

	@Override
	public void show() {
	}
	
	private void beginInstructions2() {
		Tween.to(_instructionsSprite2, FadeTween.ALPHA, 1f).target(1).ease(TweenEquations.easeInQuad).setCallback(instructions2Showing).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
	}

	private void tweenCompleted() {
		_game.setScreen(new MenuScreen(_game, false));
	}

	private void showNextButton1() {
		_stage.addActor(_nextButton1);
	}

	private void showNextButton2() {
		_stage.addActor(_nextButton2);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
