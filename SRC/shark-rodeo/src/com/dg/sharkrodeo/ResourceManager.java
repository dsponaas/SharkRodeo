package com.dg.sharkrodeo;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager {

	private static ResourceManager _instance = null;
//	private TextureAtlas _powerupAtlas;
	private TextureAtlas _dialogAtlas;
	private TextureAtlas _waveAtlas;
	private TextureAtlas _whirlpoolAtlas;
	private TextureAtlas _sharkAtlas;
	private TextureAtlas _playerAtlas;
//	private TextureAtlas _hudAtlas;
	private TextureAtlas _backgroundAtlas;
	private TextureAtlas _menuAtlas;
	private TextureAtlas _splashAtlas;
	private HashMap<String, AnimationSequence> _sharkAnims;
	private HashMap<String, AnimationSequence> _playerAnims;
	private Sound _deathNoise;
	private Sound _ridingNoise;
	private Sound _dismountNoise;
	private Sound _waveSound;
	private Sound _whirlpoolSound;
	private Music _gameMusic;
	private Music _menuMusic;

	
	private ResourceManager() {
	}
	
	public void initialize() {
		_backgroundAtlas = new TextureAtlas( "data/background.pack" );//TODO: make this variable
//		_powerupAtlas = new TextureAtlas( SharkRodeoConstants.getPowerupPack() );
		_dialogAtlas = new TextureAtlas( SharkRodeoConstants.getDialogPack() );
		_waveAtlas = new TextureAtlas( "data/wave.pack" );//TODO: make this variable
		_whirlpoolAtlas = new TextureAtlas( "data/whirlpool.pack" );//TODO: make this variable
		_menuAtlas = new TextureAtlas( "data/menu.pack" );//TODO: make this variable
		_splashAtlas = new TextureAtlas( "data/splash.pack" );//TODO: make this variable
//		_hudAtlas = new TextureAtlas( SharkRodeoConstants.getHudPack() );
		initPlayerAnims();
		initSharkAnims();
		_deathNoise = Gdx.audio.newSound( Gdx.files.internal( "data/death.ogg" ) );
		_ridingNoise = Gdx.audio.newSound( Gdx.files.internal( "data/yahoo.ogg" ) );
		_dismountNoise = Gdx.audio.newSound( Gdx.files.internal( "data/dismount.ogg" ) );
		_waveSound = Gdx.audio.newSound( Gdx.files.internal( "data/wave.ogg" ) );
		_whirlpoolSound = Gdx.audio.newSound( Gdx.files.internal( "data/whirlpool.ogg" ) );
		_gameMusic = Gdx.audio.newMusic( Gdx.files.internal( "data/test4_looping.ogg" ) );
		_gameMusic.setLooping( true );
		_menuMusic = Gdx.audio.newMusic( Gdx.files.internal( "data/menu.ogg" ) );
		_menuMusic.setLooping( true );

	}
	
	private void initPlayerAnims() {
		_playerAtlas = new TextureAtlas( SharkRodeoConstants.getPlayerPack() );
		Animation swimLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "swim_left" ) );
		Animation swimRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "swim_right" ) );
		Animation swimDownAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "swim_down" ) );
		Animation swimUpAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "swim_up" ) );
		Animation swimDownLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "swim_downleft" ) );
		Animation swimDownRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "swim_downright" ) );
		Animation swimUpLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "swim_upleft" ) );
		Animation swimUpRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "swim_upright" ) );
		Animation idleLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "idle_left" ) );
		Animation idleRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "idle_right" ) );
		Animation dismountLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.08f, 2, 4, _playerAtlas.findRegion( "dismount_left" ) );
//		Animation dismountRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "dismount_right" ) );
		Animation deadAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _playerAtlas.findRegion( "dead" ) );
		Animation deadIntroAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.25f, 1, 4, _playerAtlas.findRegion( "dead_intro" ) );

		AnimationSequence swimLeftSequence = new AnimationSequence( swimLeftAnim );
		AnimationSequence swimRightSequence = new AnimationSequence( swimRightAnim );
		AnimationSequence swimDownSequence = new AnimationSequence( swimDownAnim );
		AnimationSequence swimUpSequence = new AnimationSequence( swimUpAnim );
		AnimationSequence swimDownLeftSequence = new AnimationSequence( swimDownLeftAnim );
		AnimationSequence swimDownRightSequence = new AnimationSequence( swimDownRightAnim );
		AnimationSequence swimUpLeftSequence = new AnimationSequence( swimUpLeftAnim );
		AnimationSequence swimUpRightSequence = new AnimationSequence( swimUpRightAnim );
		AnimationSequence idleLeftSequence = new AnimationSequence( idleLeftAnim );
		AnimationSequence idleRightSequence = new AnimationSequence( idleRightAnim );
		AnimationSequence dismountLeftSequence = new AnimationSequence( dismountLeftAnim );
//		AnimationSequence dismountRightSequence = new AnimationSequence( dismountRightAnim );
		AnimationSequence mountUpSequence = new AnimationSequence( idleRightAnim, false );
		AnimationSequence deadSequence = new AnimationSequence( deadAnim );
		deadSequence.setIntro( deadIntroAnim );
		
		_playerAnims = new HashMap<String, AnimationSequence>();
		_playerAnims.put( "move_left", swimLeftSequence );
		_playerAnims.put( "move_right", swimRightSequence );
		_playerAnims.put( "move_down", swimDownSequence );
		_playerAnims.put( "move_up", swimUpSequence );
		_playerAnims.put( "move_downleft", swimDownLeftSequence );
		_playerAnims.put( "move_downright", swimDownRightSequence );
		_playerAnims.put( "move_upleft", swimUpLeftSequence );
		_playerAnims.put( "move_upright", swimUpRightSequence );
		_playerAnims.put( "idle_left", idleLeftSequence );
		_playerAnims.put( "idle_right", idleRightSequence );
		_playerAnims.put( "dismount_left", dismountLeftSequence );
//		_playerAnims.put( "dismount_right", dismountRightSequence );
		_playerAnims.put( "mount_up", mountUpSequence );
		_playerAnims.put( "dead", deadSequence );
	}
	
	private void initSharkAnims() {
		_sharkAtlas = new TextureAtlas( SharkRodeoConstants.getSharkPack() );
		Animation swimLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_swim_left" ) );
		Animation swimRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_swim_right" ) );
		Animation swimDownAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_swim_down" ) );
		Animation swimUpAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_swim_up" ) );
		Animation swimDownLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_swim_downleft" ) );
		Animation swimDownRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_swim_downright" ) );
		Animation swimUpLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_swim_upleft" ) );
		Animation swimUpRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_swim_upright" ) );
		Animation ridingLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_riding_left" ) );
		Animation ridingRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_riding_right" ) );
		Animation ridingDownAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_riding_down" ) );
		Animation ridingUpAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_riding_up" ) );
		Animation ridingUpLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_riding_upleft" ) );
		Animation ridingUpRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_riding_upright" ) );
		Animation ridingDownLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_riding_downleft" ) );
		Animation ridingDownRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation( 0.15f, 1, 4, _sharkAtlas.findRegion( "shark_riding_downright" ) );
		
		AnimationSequence swimLeftSequence = new AnimationSequence( swimLeftAnim );
		AnimationSequence swimRightSequence = new AnimationSequence( swimRightAnim );
		AnimationSequence swimDownSequence = new AnimationSequence( swimDownAnim );
		AnimationSequence swimUpSequence = new AnimationSequence( swimUpAnim );
		AnimationSequence swimDownLeftSequence = new AnimationSequence( swimDownLeftAnim );
		AnimationSequence swimDownRightSequence = new AnimationSequence( swimDownRightAnim );
		AnimationSequence swimUpLeftSequence = new AnimationSequence( swimUpLeftAnim );
		AnimationSequence swimUpRightSequence = new AnimationSequence( swimUpRightAnim );
		AnimationSequence ridingLeftSequence = new AnimationSequence( ridingLeftAnim );
		AnimationSequence ridingRightSequence = new AnimationSequence( ridingRightAnim );
		AnimationSequence ridingDownSequence = new AnimationSequence( ridingDownAnim );
		AnimationSequence ridingUpSequence = new AnimationSequence( ridingUpAnim );
		AnimationSequence ridingUpLeftSequence = new AnimationSequence( ridingUpLeftAnim );
		AnimationSequence ridingUpRightSequence = new AnimationSequence( ridingUpRightAnim );
		AnimationSequence ridingDownLeftSequence = new AnimationSequence( ridingDownLeftAnim );
		AnimationSequence ridingDownRightSequence = new AnimationSequence( ridingDownRightAnim );
		
		_sharkAnims = new HashMap<String, AnimationSequence>();
		_sharkAnims.put( "move_left", swimLeftSequence );
		_sharkAnims.put( "move_right", swimRightSequence );
		_sharkAnims.put( "move_down", swimDownSequence );
		_sharkAnims.put( "move_up", swimUpSequence );
		_sharkAnims.put( "move_downleft", swimDownLeftSequence );
		_sharkAnims.put( "move_downright", swimDownRightSequence );
		_sharkAnims.put( "move_upleft", swimUpLeftSequence );
		_sharkAnims.put( "move_upright", swimUpRightSequence );
		_sharkAnims.put( "riding_left", ridingLeftSequence );
		_sharkAnims.put( "riding_right", ridingRightSequence );
		_sharkAnims.put( "riding_down", ridingDownSequence );
		_sharkAnims.put( "riding_up", ridingUpSequence );
		_sharkAnims.put( "riding_upleft", ridingUpLeftSequence );
		_sharkAnims.put( "riding_upright", ridingUpRightSequence );
		_sharkAnims.put( "riding_downleft", ridingDownLeftSequence );
		_sharkAnims.put( "riding_downright", ridingDownRightSequence );
	}
	
	public static ResourceManager getInstance() {
		if( _instance == null )
			_instance = new ResourceManager();
		return _instance;
	}
	
//	public TextureRegion getPowerupTexture( String name ) {
//		return _powerupAtlas.findRegion( name );
//	}
	
	public TextureRegion getDialogTexture( String name ) {
		return _dialogAtlas.findRegion( name );
	}
	
	public TextureRegion getWaveTexture( String name ) {
		return _waveAtlas.findRegion( name );
	}
	
	public TextureRegion getWhirlpoolTexture( String name ) {
		return _whirlpoolAtlas.findRegion( name );
	}
	
//	public TextureRegion getHudTexture( String name ) {
//		return _hudAtlas.findRegion( name );
//	}
	
	public TextureRegion getBackgroundTexture( String name ) {
		return _backgroundAtlas.findRegion( name );
	}
	
	public TextureRegion getMenuTexture( String name ) {
		return _menuAtlas.findRegion( name );
	}
	
	public TextureRegion getSplashTexture( String name ) {
		return _splashAtlas.findRegion( name );
	}
	
	public AnimationSequence getSkarkAnim( String anim ) {
		return ( AnimationSequence )_sharkAnims.get( anim );
	}
	
	public AnimationSequence getPlayerAnim( String anim ) {
		return ( AnimationSequence )_playerAnims.get( anim );
	}
	
	public Sound getDeathNoise() {
		return _deathNoise;
	}
	
	public Sound getRidingNoise() {
		return _ridingNoise;
	}
	
	public Sound getDismountNoise() {
		return _dismountNoise;
	}
	
	public Sound getWaveSound() {
		return _waveSound;
	}
	
	public Sound getWhirlpoolSound() {
		return _whirlpoolSound;
	}
	
	public Music getGameMusic() {
		return _gameMusic;
	}
	
	public Music getMenuMusic() {
		return _menuMusic;
	}
	
	public void dispose() {
//		_powerupAtlas.dispose();
		_dialogAtlas.dispose();
		_waveAtlas.dispose();
		_whirlpoolAtlas.dispose();
		_sharkAtlas.dispose();
		_playerAtlas.dispose();
//		_hudAtlas.dispose();
		_backgroundAtlas.dispose();
		_menuAtlas.dispose();
		_splashAtlas.dispose();
		_deathNoise.dispose();
		_ridingNoise.dispose();
		_waveSound.dispose();
		_whirlpoolSound.dispose();
		_dismountNoise.dispose();
		_gameMusic.dispose();
		_menuMusic.dispose();
	}
	
}
