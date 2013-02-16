package com.dg.sharkrodeo.Screens;

import com.badlogic.gdx.Screen;
import com.dg.sharkrodeo.GameBoard;
import com.dg.sharkrodeo.SharkRodeo;

public class GameScreen implements Screen {

	private SharkRodeo _game;
//	private GameBoard _gameBoard;
	
	public GameScreen(SharkRodeo game) {
		_game = game;
		
		//move this to an init() method?
		GameBoard.getInstance().startGame(); // force creation of gameboard
	}
	
	@Override
	public void render(float delta) {
		
		if(!GameBoard.getInstance().update(delta))
		{
			_game.setScreen(new MenuScreen(_game));
		}
		GameBoard.getInstance().render(delta);
		
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
