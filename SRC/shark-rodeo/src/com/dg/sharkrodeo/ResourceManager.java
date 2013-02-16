package com.dg.sharkrodeo;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager
{

	private static ResourceManager _instance = null;
	private TextureAtlas _powerupAtlas;
	private TextureAtlas _dialogAtlas;
	private TextureAtlas _waveAtlas;
	private TextureAtlas _whirlpoolAtlas;
	
	private ResourceManager()
	{
		_powerupAtlas = new TextureAtlas(SharkRodeoConstants.getPowerupPack());
		_dialogAtlas = new TextureAtlas(SharkRodeoConstants.getDialogPack());
		_waveAtlas = new TextureAtlas("data/wave.pack");//TODO: make this variable
		_whirlpoolAtlas = new TextureAtlas("data/whirlpool.pack");//TODO: make this variable
	}
	
	public static ResourceManager getInstance()
	{
		if(_instance == null)
			_instance = new ResourceManager();
		return _instance;
	}
	
	public TextureRegion getPowerupTexture(String name)
	{
		return _powerupAtlas.findRegion(name);
	}
	
	public TextureRegion getDialogTexture(String name)
	{
		return _dialogAtlas.findRegion(name);
	}
	
	public TextureRegion getWaveTexture(String name)
	{
		return _waveAtlas.findRegion(name);
	}
	
	public TextureRegion getWhirlpoolTexture(String name)
	{
		return _whirlpoolAtlas.findRegion(name);
	}
}
