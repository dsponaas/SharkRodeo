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
	
	public static ActionResolver _actionResolver;
	
	public SharkRodeo( ActionResolver actionResolver ) {
		_actionResolver = actionResolver;
//		_this = this;
	}
	
	@Override
	public void create() {
		setScreen(new SplashScreen(this));
		
		if( _actionResolver != null ) {
			_actionResolver.bootstrap();
		}
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
/*	
	public static ActionResolver getActionResolver() {
		return _actionResolver;
	}
*/
	public static void saveScore( int score ) {
		if( _actionResolver != null ) {
			_actionResolver.submitScore( 0, score );
		}
	}
	
	public static void showScoreloop() {
		if( _actionResolver != null ) {
			_actionResolver.showScoreloop();
		}
	}

} // public class SharkRodeo
