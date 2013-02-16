package com.dg.sharkrodeo;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.Player.PlayerState;

public class InputHandler implements InputProcessor {

	private GameBoard _board;
	private boolean _pauseGameInput;
	
	public InputHandler(GameBoard board)
	{
		_board = board;
		_pauseGameInput = false;
	}
	
	public void pauseGameInput()
	{
		_pauseGameInput = true;
	}
	
	public void resumeGameInput()
	{
		_pauseGameInput = false;
	}
	
	@Override
	public boolean keyDown(int keycode) { return false; }

	@Override
	public boolean keyUp(int keycode) {	return false; }

	@Override
	public boolean keyTyped(char character) { return false; }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if(_board.dialogPress(screenX, screenY))
			return true;
		if(_pauseGameInput)
			return true;
		if(!_board.getHud().touchDown(screenX, screenY))
		{
			Vector2 worldPos = _board.getWorldPosFromScreenPos(screenX, screenY);
	
			PlayerState playerState =_board.getPlayerState(); 
			if((playerState == PlayerState.IDLE) || (playerState == PlayerState.MOVING))
				_board.movePlayerTo(worldPos);
			if(playerState == PlayerState.MOUNTED)
			{
				_board.reportRidingPosition(worldPos);
			}
		}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if(_pauseGameInput)
			return true;
		if(!_board.getHud().touchUp(screenX, screenY))
		{
			PlayerState playerState =_board.getPlayerState(); 
			if((playerState == PlayerState.IDLE) || (playerState == PlayerState.MOVING))
				_board.killPlayerAcceleration();
			if(playerState == PlayerState.MOUNTED)
			{
				_board.reportRidingPosition(null);
			}
		}
		
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		if(_pauseGameInput)
			return true;
		if(!_board.getHud().touchDragged(screenX, screenY))
		{
			Vector2 worldPos = _board.getWorldPosFromScreenPos(screenX, screenY);
			
			PlayerState playerState =_board.getPlayerState(); 
			if((playerState == PlayerState.IDLE) || (playerState == PlayerState.MOVING))
				_board.movePlayerTo(worldPos);
			if(playerState == PlayerState.MOUNTED)
			{
				_board.reportRidingPosition(worldPos);
			}
		}
		
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) { return false;	}

	@Override
	public boolean scrolled(int amount) { return false;	}

}
