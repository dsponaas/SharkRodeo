package com.dg.sharkrodeo.Screens;

import com.badlogic.gdx.Screen;
import com.dg.sharkrodeo.GameBoard;
import com.dg.sharkrodeo.GameState;
import com.dg.sharkrodeo.ResourceManager;
import com.dg.sharkrodeo.SharkRodeo;

public class GameScreen implements Screen {

	private SharkRodeo _game;
	
	public GameScreen(SharkRodeo game) {
		_game = game;
		
		//move this to an init() method?
		ResourceManager.getInstance().getMenuMusic().stop();
		GameBoard.getInstance().startGame( _game ); // force creation of gameboard
		GameState.initLevel();
		GameState.stupidGameStartSharkNumHack();
	}
	
	@Override
	public void render(float delta) {
		
		if(!GameBoard.getInstance().update(delta))
		{
			_game.setScreen(new MenuScreen(_game));
		}
		GameBoard.getInstance().render(delta, true);
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		GameBoard.getInstance().dispose();
	}

}
