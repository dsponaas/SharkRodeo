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
import com.dg.sharkrodeo.ResourceManager;
import com.dg.sharkrodeo.SharkRodeo;
import com.dg.sharkrodeo.Tweens.FadeTween;

public class SplashScreen implements Screen {

	Sprite _backgroundSprite;
	Sprite _presentsSprite;
	Sprite _titleSprite;
	SpriteBatch _batch;
	SharkRodeo _game;
	TweenManager _tweenManager;
	TweenCallback presentsShowing;
	TweenCallback titleShowing;
	TweenCallback allHidden;
	
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
		_backgroundSprite.draw(_batch);
		_presentsSprite.draw(_batch);
		_titleSprite.draw(_batch);
		_batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

		TextureRegion backgroundTex = ResourceManager.getInstance().getSplashTexture( "background" );
		_backgroundSprite = new Sprite( backgroundTex );
		_backgroundSprite.setColor(1, 1, 1, 0);
		_backgroundSprite.setOrigin( _backgroundSprite.getWidth() / 2, _backgroundSprite.getHeight() / 2 );
		_backgroundSprite.setPosition( ( Gdx.graphics.getWidth() / 2 ) - ( _backgroundSprite.getWidth() / 2 ), ( Gdx.graphics.getHeight() / 2 ) - ( _backgroundSprite.getHeight() / 2 ) );
		
		TextureRegion presentsTex = ResourceManager.getInstance().getSplashTexture( "presents" );
		_presentsSprite = new Sprite( presentsTex );
		_presentsSprite.setColor(1, 1, 1, 0);
		_presentsSprite.setOrigin( _presentsSprite.getWidth() / 2, _presentsSprite.getHeight() / 2 );
		_presentsSprite.setPosition( ( Gdx.graphics.getWidth() / 2 ) - ( _presentsSprite.getWidth() / 2 ), ( Gdx.graphics.getHeight() / 2 ) - ( _presentsSprite.getHeight() / 2 ) );
		
		TextureRegion titleTex = ResourceManager.getInstance().getSplashTexture( "title" );
		_titleSprite = new Sprite( titleTex );
		_titleSprite.setColor(1, 1, 1, 0);
		_titleSprite.setOrigin( _titleSprite.getWidth() / 2, _titleSprite.getHeight() / 2 );
		_titleSprite.setPosition( ( Gdx.graphics.getWidth() / 2 ) - ( _titleSprite.getWidth() / 2 ), ( Gdx.graphics.getHeight() / 2 ) - ( _titleSprite.getHeight() / 2 ) );
		
		_batch = new SpriteBatch();
		
		Tween.registerAccessor(Sprite.class, new FadeTween());
		_tweenManager = new TweenManager();
		TweenCallback backgroundShowing = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				showPresents();
			}
		};
		presentsShowing = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				showTitle();
			}
		};
		titleShowing = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				hideAll();
			}
		};
		allHidden = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				splashCompleted();
			}
		};
		Tween.to( _backgroundSprite, FadeTween.ALPHA, 3f ).target( 1 ).ease( TweenEquations.easeInQuad ).setCallback( backgroundShowing ).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
	}
	
	private void showPresents() {
		Tween.to( _presentsSprite, FadeTween.ALPHA, 2f ).target( 1 ).ease( TweenEquations.easeInQuad ).setCallback( presentsShowing ).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
	}

	private void showTitle() {
		Tween.to( _titleSprite, FadeTween.ALPHA, 2f ).target( 1 ).ease( TweenEquations.easeInQuad ).setCallback( titleShowing ).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
	}

	private void hideAll() {
		Tween.to( _backgroundSprite, FadeTween.ALPHA, 1f ).target( 0 ).ease( TweenEquations.easeInQuad ).delay( 2f ).start(_tweenManager);
		Tween.to( _presentsSprite, FadeTween.ALPHA, 1f ).target( 0 ).ease( TweenEquations.easeInQuad ).delay( 2f ).start(_tweenManager);
		Tween.to( _titleSprite, FadeTween.ALPHA, 1f ).target( 0 ).ease( TweenEquations.easeInQuad ).delay( 2f ).setCallback( allHidden ).setCallbackTriggers(TweenCallback.COMPLETE).start(_tweenManager);
	}

	private void splashCompleted() {
		_game.setScreen(new MenuScreen(_game, true));
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
