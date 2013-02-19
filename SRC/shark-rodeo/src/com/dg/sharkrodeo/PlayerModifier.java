package com.dg.sharkrodeo;

public class PlayerModifier {
	
	public enum PlayerModifierType { SPEED_UP, ENDURANCE_UP };
	private PlayerModifierType _type;
	private Player _player;
	
	private float _timeLeft;
	
	public PlayerModifier( PlayerModifierType type, Player player ) {
		_type = type;
		_player = player;
		_timeLeft = 0f;
	}
	
	public boolean update( float delta ) {
		_timeLeft -= delta;
		if( _timeLeft < 0f ) {
			deactivate();
			return false;
		}
		return true;
	}
	
	public void activate() {
		switch( _type ) {
		case SPEED_UP:
			_player.setAccelerationRate( SharkRodeoConstants.POWERUP_SPEED_FACTOR * SharkRodeoConstants.getPlayerAcceleration() );
			_player.setMaxSpeed( SharkRodeoConstants.POWERUP_SPEED_FACTOR * SharkRodeoConstants.getPlayerMaxSpeed() );
			break;
		case ENDURANCE_UP:
			GameState.setSprintRechargeRate( SharkRodeoConstants.POWERUP_ENDURANCE_FACTOR * SharkRodeoConstants.SPRINT_RECHARGE_RATE );
			break;
		}
		
		_timeLeft += SharkRodeoConstants.POWERUP_LENGTH;
	}

	public void deactivate() {
		switch( _type ) {
		case SPEED_UP:
			_player.setAccelerationRate( SharkRodeoConstants.getPlayerAcceleration() );
			_player.setMaxSpeed( SharkRodeoConstants.getPlayerMaxSpeed() );
			break;
		case ENDURANCE_UP:
			GameState.setSprintRechargeRate( SharkRodeoConstants.SPRINT_RECHARGE_RATE );
			break;
		}
	}
	
	public PlayerModifierType getType()		{ return _type; }

}
