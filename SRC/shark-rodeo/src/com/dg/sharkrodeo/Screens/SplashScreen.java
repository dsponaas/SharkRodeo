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
import com.dg.sharkrodeo.ResourceManager;
import com.dg.sharkrodeo.SharkRodeo;
import com.dg.sharkrodeo.SharkRodeoConstants;
import com.dg.sharkrodeo.Tweens.FadeTween;

public class SplashScreen implements Screen {

	Texture _splashTexture;
	Sprite _splashSprite;
	SpriteBatch _batch;
	SharkRodeo _game;
	TweenManager _tweenManager;
	
	public SplashScreen(SharkRodeo game) {
		ResourceManager.getInstance().initialize();
		_game = game;
		ResourceManager.getInstance().getMenuMusic().play();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		_tweenManager.update(delta);
		
		_batch.begin();
		_splashSprite.draw(_batch);
		_batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		_splashTexture = new Texture("data/splash_screen_08.png");
		_splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		_splashSprite = new Sprite(_splashTexture);
		_splashSprite.setColor(1, 1, 1, 0);
		_splashSprite.setOrigin(_splashSprite.getWidth() / 2, _splashSprite.getHeight() / 2);
		_splashSprite.setPosition((Gdx.graphics.getWidth() / 2) - (_splashSprite.getWidth() / 2), (Gdx.graphics.getHeight() / 2) - (_splashSprite.getHeight() / 2));
		
		_batch = new SpriteBatch();
		
		Tween.registerAccessor(Sprite.class, new FadeTween());
		_tweenManager = new TweenManager();
		TweenCallback cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				tweenCompleted();
			}
		};
		Tween.to(_splashSprite, FadeTween.ALPHA, 2f).target(1).ease(TweenEquations.easeInQuad).repeatYoyo(1, 2f).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
	}
	
	private void tweenCompleted() {
		//Gdx.app.log(MyGdxGameConstants.LOG_TAG, "craptartastic");
		_game.setScreen(new MenuScreen(_game));
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
