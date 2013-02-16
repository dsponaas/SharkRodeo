package com.dg.sharkrodeo.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.GameBoard;
import com.dg.sharkrodeo.SharkRodeoConstants;

public class SharkRodeoDialog {

	private TextureRegion _texture;
	private Vector2 _renderPos;
	private Vector2 _touchPos;
	private float _ignoreInputTime;
	protected GameBoard _board;
	
	public SharkRodeoDialog(TextureRegion tex, GameBoard board)
	{
		_texture = tex;
		
		float deltaX = (float)Gdx.graphics.getWidth();
		deltaX -= (float)_texture.getRegionWidth();
		deltaX /= 2f;

		float deltaY = (float)Gdx.graphics.getHeight() - SharkRodeoConstants.getStatusBarWidth();
		deltaY -= (float)_texture.getRegionHeight();
		deltaY /= 2f;
		
		_renderPos = new Vector2(deltaX, deltaY);
		_touchPos = new Vector2(_renderPos);
		
		_ignoreInputTime = SharkRodeoConstants.DIALOG_IGNORE_INPUT_TIME;
		
		_board = board;
	}
	
	public TextureRegion getTexture()
	{
		return _texture;
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(_renderPos);
	}
	
	public void update(float delta)
	{
		_ignoreInputTime -= delta;
	}
	
	public boolean checkTouch(int x, int yOrig)
	{
		if(_ignoreInputTime > 0f)
			return false;
		int y = Gdx.graphics.getHeight() - yOrig;
		float xExtent = _touchPos.x + (float)_texture.getRegionWidth();
		float yExtent = _touchPos.y + (float)_texture.getRegionHeight();
		if(((float)x > _touchPos.x) && ((float)y > _touchPos.y) && ((float)x < xExtent) && ((float)y < yExtent))
			return true;
		return false;
	}
	
	public void onComplete()
	{
		
	}
}
