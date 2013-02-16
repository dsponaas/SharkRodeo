package com.dg.sharkrodeo;

import com.badlogic.gdx.Game;
import com.dg.sharkrodeo.Screens.SplashScreen;

//FILES TO REPLACE:
///*button.pack
///*button.png
///*font_0.tga
///*font.font
///*whitefont_0.tga
///*whitefont.font
///*



public class SharkRodeo extends Game {
//	private OrthographicCamera camera;
//	private SpriteBatch batch;
//	private Texture texture;
//	private Sprite sprite;
	
	@Override
	public void create() {
		setScreen(new SplashScreen(this));
/*
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
*/
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
