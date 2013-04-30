package com.dg.sharkrodeo.Screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dg.sharkrodeo.GameBoard;
import com.dg.sharkrodeo.ResourceManager;
import com.dg.sharkrodeo.SharkRodeo;
import com.dg.sharkrodeo.Tweens.FadeTween;

public class CreditsScreen implements Screen {

	Sprite _creditsSprite1;
	Sprite _creditsSprite2;
	Sprite _creditsSprite3;
	SpriteBatch _batch;
	SharkRodeo _game;
	TweenManager _tweenManager;
	TweenCallback _credits2Callback;
	TweenCallback _credits3Callback;
	
	public CreditsScreen(SharkRodeo game) {
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
		
		_batch.begin();
		_creditsSprite1.draw(_batch);
		_creditsSprite2.draw(_batch);
		_creditsSprite3.draw(_batch);
		_batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(null);
		
		TextureRegion credits1Tex = ResourceManager.getInstance().getMenuTexture( "credits1" );
		_creditsSprite1 = new Sprite(credits1Tex);
		_creditsSprite1.setColor(1, 1, 1, 0);
		_creditsSprite1.setOrigin(_creditsSprite1.getWidth() / 2, _creditsSprite1.getHeight() / 2);
		_creditsSprite1.setPosition((Gdx.graphics.getWidth() / 2) - (_creditsSprite1.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (_creditsSprite1.getHeight() / 2));
		
		TextureRegion credits2Tex = ResourceManager.getInstance().getMenuTexture( "credits2" );
		_creditsSprite2 = new Sprite(credits2Tex);
		_creditsSprite2.setColor(1, 1, 1, 0);
		_creditsSprite2.setOrigin(_creditsSprite2.getWidth() / 2, _creditsSprite2.getHeight() / 2);
		_creditsSprite2.setPosition((Gdx.graphics.getWidth() / 2) - (_creditsSprite2.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (_creditsSprite2.getHeight() / 2));
		
		TextureRegion credits3Tex = ResourceManager.getInstance().getMenuTexture( "credits3" );
		_creditsSprite3 = new Sprite(credits3Tex);
		_creditsSprite3.setColor(1, 1, 1, 0);
		_creditsSprite3.setOrigin(_creditsSprite3.getWidth() / 2, _creditsSprite3.getHeight() / 2);
		_creditsSprite3.setPosition((Gdx.graphics.getWidth() / 2) - (_creditsSprite3.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (_creditsSprite3.getHeight() / 2));
		
		_batch = new SpriteBatch();
		
		Tween.registerAccessor(Sprite.class, new FadeTween());
		_tweenManager = new TweenManager();
		TweenCallback credits1Callback = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				credits1Completed();
			}
		};
		_credits2Callback = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				credits2Completed();
			}
		};
		_credits3Callback = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				credits3Completed();
			}
		};
		Tween.to(_creditsSprite1, FadeTween.ALPHA, 1f).target(1).ease(TweenEquations.easeInQuad).repeatYoyo(1, 5f).setCallback(credits1Callback).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
	}
	
	private void credits1Completed() {
		Tween.to(_creditsSprite2, FadeTween.ALPHA, 1f).target(1).ease(TweenEquations.easeInQuad).repeatYoyo(1, 8f).setCallback(_credits2Callback).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
	}

	private void credits2Completed() {
		Tween.to(_creditsSprite3, FadeTween.ALPHA, 1f).target(1).ease(TweenEquations.easeInQuad).repeatYoyo(1, 5f).setCallback(_credits3Callback).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
	}

	private void credits3Completed() {
		_game.setScreen(new MenuScreen(_game, false));
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
