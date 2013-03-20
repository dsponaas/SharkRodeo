package com.dg.sharkrodeo;


public class GameState {
	
	private static int _lives = 2;
	private static int _score = 0;
	private static int _multiplier;
	private static float _multiplierTime;
	private static float _gameTime;
	private static float _timeUntilSharkSpawn;
	private static float _timeUntilWaveSpawn;
	private static float _timeUntilWhirlpoolSpawn;
	private static float _timeUntilPowerupSpawn;
	
	private static int _curLevel;
	private static int _sharksRemaining;
	
	private static float _sprintDuration;
	private static float _maxSprintDuration;
	private static float _sprintRechargeRate;
//	private static float _
	
	public static void reset() {
		_score = 0;
		_lives = 2;
		_multiplier = 1;
		_multiplierTime = 0f;
		_gameTime = 0f;
		_curLevel = 1;
		_timeUntilSharkSpawn = LevelInfo.getSharkSpawnTime( _curLevel );
		_timeUntilWaveSpawn = LevelInfo.getWaveSpawnTime( _curLevel );
		_timeUntilWhirlpoolSpawn = LevelInfo.getWhirlpoolSpawnTime( _curLevel );
		
		_maxSprintDuration = SharkRodeoConstants.SPRINT_DURATION;
		_sprintRechargeRate = SharkRodeoConstants.SPRINT_RECHARGE_RATE;
		_sprintDuration = _maxSprintDuration;
	}
	
	public static void initLevel() {
		_sharksRemaining = LevelInfo.getDelayedNumSharks( _curLevel );
		_gameTime = 0f;
		_timeUntilPowerupSpawn = LevelInfo.getPowerupSpawnTime( _curLevel );
	}
	
	public static void update( float delta, boolean playerSprintingFlag ) {
		if( _multiplier > 1 ) {
			_multiplierTime -= delta;
			if( _multiplierTime < 0f ) {
				_multiplier = 1;
				_multiplierTime = 0f;
			}
		}
		
		_gameTime += delta;
		_timeUntilSharkSpawn -= delta;
		_timeUntilWaveSpawn -= delta;
		_timeUntilWhirlpoolSpawn -= delta;
		_timeUntilPowerupSpawn -= delta;
		
		if( playerSprintingFlag )
			_sprintDuration -= delta;
		
		_sprintDuration += ( _sprintRechargeRate * delta );
		if( _sprintDuration > _maxSprintDuration )
			_sprintDuration = _maxSprintDuration;
	}
	
	public static void setMultiplier( int multiplier ) {
		if( multiplier > _multiplier )
			_multiplier = multiplier;
		_multiplierTime = SharkRodeoConstants.MULTIPLIER_TIME;
	}
	
	public static boolean takeLife() {
		--_lives;
		_multiplier = 1;
		_multiplierTime = 0f;
		if( _lives < 0 )
			return true;
		return false;
	}
	
	public static boolean checkForSharkSpawn() {
		if( ( _timeUntilSharkSpawn < 0f ) && ( _sharksRemaining > 0 ) ) {
			_timeUntilSharkSpawn = LevelInfo.getSharkSpawnTime( _curLevel );
			--_sharksRemaining;
			return true;
		}
		return false;
	}
	
	public static boolean checkForWaveSpawn() {
		if( _timeUntilWaveSpawn < 0f ) {
			_timeUntilWaveSpawn = LevelInfo.getWaveSpawnTime( _curLevel );
			return true;
		}
		return false;
	}
	
	public static boolean checkForWhirpoolSpawn() {
		if( _timeUntilWhirlpoolSpawn < 0f ) {
			_timeUntilWhirlpoolSpawn = LevelInfo.getWhirlpoolSpawnTime( _curLevel );
			return true;
		}
		return false;
	}
	
	public static boolean checkForPowerupSpawn() {
		if( _timeUntilPowerupSpawn < 0f ) {
			_timeUntilPowerupSpawn = LevelInfo.getPowerupSpawnTime( _curLevel );
			return true;
		}
		return false;
	}
	
	public static void addLife()								{ ++_lives; }
	public static void addToScore( int val )					{ _score += ( val * _multiplier ); }
	public static void incrementLevel()							{ ++_curLevel; }
	
	public static int getScore()								{ return _score; }
	public static int getLives()								{ return _lives; }
	public static int getMulitplier()							{ return _multiplier; }
	public static int getGameTime()								{ return ( int )_gameTime; }
	public static int getLevel()								{ return _curLevel; }
	public static int getSharksRemaining()						{ return _sharksRemaining; }
	
	public static boolean isSprintDepleted()					{ return ( _sprintDuration < 0f ) ? true : false; }
	public static float getSprintRemainingPercent()		 		{ return _sprintDuration / _maxSprintDuration;	}
	
	public static void setSprintRechargeRate( float rate )		{ _sprintRechargeRate = rate; }
	
}
