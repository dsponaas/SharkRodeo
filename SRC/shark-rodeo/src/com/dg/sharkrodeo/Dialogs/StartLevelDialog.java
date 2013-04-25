package com.dg.sharkrodeo.Dialogs;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dg.sharkrodeo.GameBoard;
import com.dg.sharkrodeo.GameState;

public class StartLevelDialog extends SharkRodeoDialog
{
	
	public StartLevelDialog(TextureRegion tex, GameBoard board)
	{
		super(tex, board);
	}

	public void onComplete()
	{
		GameState.initLevel();
		_board.initLevel();
		_board.spawnPlayer();
		_board.startMusic();
	}

}
