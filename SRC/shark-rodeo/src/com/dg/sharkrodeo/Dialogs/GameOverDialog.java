package com.dg.sharkrodeo.Dialogs;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dg.sharkrodeo.GameBoard;

public class GameOverDialog extends SharkRodeoDialog
{
	
	public GameOverDialog(TextureRegion tex, GameBoard board)
	{
		super(tex, board);
	}

	public void onComplete()
	{
		_board.gameOver();
	}
	
}
