package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class SharkRodeoConstants {
	public static final String LOG_TAG = "Shark-Rodeo";
	
	public static final int MAX_ENEMIES = 20;
	public static final float RIDING_TIME = 5f;
	public static final boolean RENDER_HITBOXES = false;
	public static final float CAMERA_SHAKE_MULTIPLIER = 3f;
	public static final Color HUD_BLANK_COLOR = Color.BLACK;
	public static final float MULTIPLIER_TIME = 20f;
	public static final float RIDING_SCORE_TIME = .97f; //1 second but we'd prefer to cheat to the lower end
	public static final float DEFAULT_SHARK_HEALTH = 15f;
	public static final double SHARK_REGULAR_TURN_SPEED = Math.PI / 75.0;
	public static final double SHARK_LUNGE_TURN_SPEED = Math.PI / 40.0;
	public static final float SHARK_LUNGE_MULTIPLIER = 1.8f;
	public static final float DIALOG_IGNORE_INPUT_TIME = 1f;
	public static final float MUSIC_FADE_TIME = 4f;
	public static final float POWERUP_FADE_TIMER = 2f;
	public static final float POWERUP_TIMER = 8f;
	public static final float SPRINT_DURATION = 5f;
	public static final float SPRINT_RECHARGE_RATE = .2f;
	public static final float SPRINT_FACTOR = 1.4f;
	public static final float POWERUP_SPEED_FACTOR = 1.4f;
	public static final float POWERUP_ENDURANCE_FACTOR = 3f;
	public static final float POWERUP_LENGTH = 15f;
	public static final float DEFAULT_RENDER_TOUCHPOS_ALPHA = 0.8f;
	
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	/*********** SCALING CONSTANTS.... this is a stupid fucking way of solving this problem but here we goooooooo! ************/
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	/**************************************************************************************************************************/
	private static final String PLAYER_PACK_128 = "data/swimmer.pack";
	private static final String PLAYER_PACK_256 = "data/swimmer.pack";//***********************************************************************INCORRECT!
	/**************************************************************************************************************************/
	private static final String SHARK_PACK_128 = "data/shark.pack";
	private static final String SHARK_PACK_256 = "data/shark.pack";//**************************************************************************INCORRECT!
	/**************************************************************************************************************************/
	private static final String HUD_PACK_128 = "data/hud.pack";
	private static final String HUD_PACK_256 = "data/hud.pack";//**************************************************************************INCORRECT!
	/**************************************************************************************************************************/
	private static final String DIALOG_PACK_128 = "data/dialog.pack";
	private static final String DIALOG_PACK_256 = "data/dialog.pack";//**************************************************************************INCORRECT!
	/**************************************************************************************************************************/
	private static final String POWERUP_PACK_128 = "data/powerup.pack";
	private static final String POWERUP_PACK_256 = "data/powerup.pack";//**************************************************************************INCORRECT!
	/**************************************************************************************************************************/
	private static final float STATUS_BAR_WIDTH_128 = 64f;
	private static final float STATUS_BAR_WIDTH_256 = 128f;
	/**************************************************************************************************************************/
	private static final float UTILITY_BAR_WIDTH_128 = 128f;
	private static final float UTILITY_BAR_WIDTH_256 = 256f;
	/**************************************************************************************************************************/
	private static final float GAMEBOARD_WIDTH_128 = 1760f;//(128 * 12) + (112 * 2)
	private static final float GAMEBOARD_WIDTH_256 = 3520f;//3072 + 448 = 
	/**************************************************************************************************************************/
	private static final float GAMEBOARD_HEIGHT_128 = 1760f;
	private static final float GAMEBOARD_HEIGHT_256 = 3520f;
	/**************************************************************************************************************************/
	private static final float GAMEBOARD_CONST_FRICTION_128 = 200f;
	private static final float GAMEBOARD_CONST_FRICTION_256 = 400f;
	/**************************************************************************************************************************/
	private static final float GAMEBOARD_VARIABLE_FRICTION_128 = 800f;
	private static final float GAMEBOARD_VARIABLE_FRICTION_256 = 1600f;
	/**************************************************************************************************************************/
	private static final float GAMEBOARD_VARIABLE_FRICTION_MIN_SPD_128 = 300f;
	private static final float GAMEBOARD_VARIABLE_FRICTION_MIN_SPD_256 = 600f;
	/**************************************************************************************************************************/
	private static final float GAMEBOARD_VARIABLE_FRICTION_MAX_SPD_128 = 800f;
	private static final float GAMEBOARD_VARIABLE_FRICTION_MAX_SPD_256 = 1600f;
	/**************************************************************************************************************************/
	private static final float GAMEBOARD_VARIABLE_FRICTION_A_TERM_128 = 0.0032f; // a = mag / rangedelta^2
	private static final float GAMEBOARD_VARIABLE_FRICTION_A_TERM_256 = 0.0016f;
	/**************************************************************************************************************************/
	private static final float RIDING_DISTANCE_TOLERANCE_128 = 100f;
	private static final float RIDING_DISTANCE_TOLERANCE_256 = 200f;
	/**************************************************************************************************************************/
	private static final float CAMERA_SPEED_128 = 260f;
	private static final float CAMERA_SPEED_256 = 520f;
	/**************************************************************************************************************************/
	private static final float CAMERA_SHAKE_MIN_128 = 40f;
	private static final float CAMERA_SHAKE_MIN_256 = 80f;
	/**************************************************************************************************************************/
	private static final float CAMERA_SHAKE_MAX_128 = 100f;
	private static final float CAMERA_SHAKE_MAX_256 = 200f;
	/**************************************************************************************************************************/
	private static final float PLAYER_ACCELERATION_128 = 600f;
	private static final float PLAYER_ACCELERATION_256 = 1200f;
	/**************************************************************************************************************************/
	private static final float PLAYER_MAX_SPEED_128 = 260f;
	private static final float PLAYER_MAX_SPEED_256 = 460f;
	/**************************************************************************************************************************/
	private static final float SHARK_ACCELERATION_128 = 500f;
	private static final float SHARK_ACCELERATION_256 = 1000f;
	/**************************************************************************************************************************/
	private static final float SHARK_MAX_SPEED_128 = 200f;
	private static final float SHARK_MAX_SPEED_256 = 400f;
	/**************************************************************************************************************************/
	private static final float MOUNTED_SHARK_ACCELERATION_128 = 1400f;
	private static final float MOUNTED_SHARK_ACCELERATION_256 = 2800f;
	/**************************************************************************************************************************/
	private static final float MOUNTED_SHARK_MAX_SPEED_128 = 900f;
	private static final float MOUNTED_SHARK_MAX_SPEED_256 = 1800f;
	/**************************************************************************************************************************/
	private static final float SHARK_MOUTH_HITBOX_RADIUS_128 = 16f;
	private static final float SHARK_MOUTH_HITBOX_RADIUS_256 = 32f;
	/**************************************************************************************************************************/
	private static final float SHARK_BACK_HITBOX_RADIUS_128 = 12f;
	private static final float SHARK_BACK_HITBOX_RADIUS_256 = 24f;
	/**************************************************************************************************************************/
	private static final float SHARK_HITBOX_OFFSET_128 = 40f;
	private static final float SHARK_HITBOX_OFFSET_256 = 80f;
	/**************************************************************************************************************************/
	private static final float SHARK_DEST_TURN_DISTANCE_SQUARED_128 = 2500f;
	private static final float SHARK_DEST_TURN_DISTANCE_SQUARED_256 = 10000f;
	/**************************************************************************************************************************/
	private static final float SHARK_DEST_ALERT_DISTANCE_SQUARED_128 = 40000f;
	private static final float SHARK_DEST_ALERT_DISTANCE_SQUARED_256 = 160000f;
	/**************************************************************************************************************************/
	private static final float PLAYER_HITBOX_RADIUS_128 = 20f;
	private static final float PLAYER_HITBOX_RADIUS_256 = 40f;
	/**************************************************************************************************************************/
	private static final float WAVE_SPEED_128 = 200f;
	private static final float WAVE_SPEED_256 = 400f;
	/**************************************************************************************************************************/
	private static final float WAVE_FRICTION_128 = 800f;
	private static final float WAVE_FRICTION_256 = 1600f;
	/**************************************************************************************************************************/
	private static final float POWERUP_COLLISION_RADIUS_128 = 32f;
	private static final float POWERUP_COLLISION_RADIUS_256 = 64f;
	/**************************************************************************************************************************/
	public enum GameBoardScale { _128, _256 };
	private static GameBoardScale _scale = GameBoardScale._128;
	public static void setScale(GameBoardScale scale)	{ _scale = scale; }
	public static GameBoardScale getScale()				{ return _scale; }
	
	public static String getPlayerPack()
	{
		switch(_scale)
		{
		case _128: return PLAYER_PACK_128;
		case _256: return PLAYER_PACK_256;
		}
		
		//warngin
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getPlayerPack() returning default value!");
		return "FFFFFFFFFFFFUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU";
	}
	
	public static String getSharkPack()
	{
		switch(_scale)
		{
		case _128: return SHARK_PACK_128;
		case _256: return SHARK_PACK_256;
		}
		
		//warngin
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getSharkPack() returning default value!");
		return "FFFFFFFFFFFFUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU";
	}
	
	public static String getHudPack()
	{
		switch(_scale)
		{
		case _128: return HUD_PACK_128;
		case _256: return HUD_PACK_256;
		}
		
		//warngin
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getHudPack() returning default value!");
		return "FFFFFFFFFFFFUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU";
	}
	
	public static String getDialogPack()
	{
		switch(_scale)
		{
		case _128: return DIALOG_PACK_128;
		case _256: return DIALOG_PACK_256;
		}
		
		//warngin
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getDialogPack() returning default value!");
		return "FFFFFFFFFFFFUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU";
	}
	
	public static String getPowerupPack()
	{
		switch(_scale)
		{
		case _128: return POWERUP_PACK_128;
		case _256: return POWERUP_PACK_256;
		}
		
		//warngin
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getPowerupPack() returning default value!");
		return "FFFFFFFFFFFFUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU";
	}
	
	public static float getStatusBarWidth()
	{
		switch(_scale)
		{
		case _128: return STATUS_BAR_WIDTH_128;
		case _256: return STATUS_BAR_WIDTH_256;
		}
		
		//warngin
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getStatusBarWidth() returning default value!");
		return 0f;
	}
	
	public static float getUtilityBarWidth()
	{
		switch(_scale)
		{
		case _128: return UTILITY_BAR_WIDTH_128;
		case _256: return UTILITY_BAR_WIDTH_256;
		}
		
		//warngin
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getUtilityBarWidth() returning default value!");
		return 0f;
	}
	
	public static float getGameBoardWidth()
	{
		switch(_scale)
		{
		case _128: return GAMEBOARD_WIDTH_128;
		case _256: return GAMEBOARD_WIDTH_256;
		}
		
		//warngin
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getGameBoardWidth() returning default value!");
		return 0f;
	}
	
	public static float getGameBoardHeight()
	{
		switch(_scale)
		{
		case _128: return GAMEBOARD_HEIGHT_128;
		case _256: return GAMEBOARD_HEIGHT_256;
		}
		
		//warngin
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getGameBoardHeight() returning default value!");
		return 0f;
	}

	public static float getFriction(float speed)
	{
		float delta;
		float retval;
		switch(_scale)
		{
		case _128:
			if(speed < GAMEBOARD_VARIABLE_FRICTION_MIN_SPD_128)
				return GAMEBOARD_CONST_FRICTION_128;
			else if(speed > GAMEBOARD_VARIABLE_FRICTION_MAX_SPD_128)
				return GAMEBOARD_CONST_FRICTION_128 + GAMEBOARD_VARIABLE_FRICTION_128;
			delta = speed - GAMEBOARD_VARIABLE_FRICTION_MIN_SPD_128;
			retval = (delta * delta * GAMEBOARD_VARIABLE_FRICTION_A_TERM_128) + GAMEBOARD_CONST_FRICTION_128;
//			Gdx.app.log(SharkRodeoConstants.LOG_TAG, "FRICTION spd:" + speed + " fric:" + retval);
			return retval;
		case _256:
			if(speed < GAMEBOARD_VARIABLE_FRICTION_MIN_SPD_256)
				return GAMEBOARD_CONST_FRICTION_256;
			else if(speed > GAMEBOARD_VARIABLE_FRICTION_MAX_SPD_256)
				return GAMEBOARD_CONST_FRICTION_256 + GAMEBOARD_VARIABLE_FRICTION_256;
			delta = speed - GAMEBOARD_VARIABLE_FRICTION_MIN_SPD_256;
			retval = (delta * delta * GAMEBOARD_VARIABLE_FRICTION_A_TERM_256) + GAMEBOARD_CONST_FRICTION_256;
			return retval;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getFriction() returning default value!");
		return 0f;
	}
	
	public static float getRidingDistanceTolerance()
	{
		switch(_scale)
		{
		case _128: return RIDING_DISTANCE_TOLERANCE_128;
		case _256: return RIDING_DISTANCE_TOLERANCE_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getRidingDistanceTolerance() returning default value!");
		return 0f;
	}
	
	public static float getCameraSpeed()
	{
		switch(_scale)
		{
		case _128: return CAMERA_SPEED_128;
		case _256: return CAMERA_SPEED_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getCameraSpeed() returning default value!");
		return 0f;
	}
	
	public static float getCameraShakeMin()
	{
		switch(_scale)
		{
		case _128: return CAMERA_SHAKE_MIN_128;
		case _256: return CAMERA_SHAKE_MIN_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getCameraShakeMin() returning default value!");
		return 0f;
	}
	
	public static float getCameraShakeMax()
	{
		switch(_scale)
		{
		case _128: return CAMERA_SHAKE_MAX_128;
		case _256: return CAMERA_SHAKE_MAX_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getCameraShakeMax() returning default value!");
		return 0f;
	}
	
	public static float getPlayerAcceleration()
	{
		switch(_scale)
		{
		case _128: return PLAYER_ACCELERATION_128;
		case _256: return PLAYER_ACCELERATION_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getPlayerAcceleration() returning default value!");
		return 0f;
	}
	
	public static float getPlayerMaxSpeed()
	{
		switch(_scale)
		{
		case _128: return PLAYER_MAX_SPEED_128;
		case _256: return PLAYER_MAX_SPEED_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getPlayerMaxSpeed() returning default value!");
		return 0f;
	}
	
	public static float getSharkAcceleration()
	{
		switch(_scale)
		{
		case _128: return SHARK_ACCELERATION_128;
		case _256: return SHARK_ACCELERATION_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getSharkAcceleration() returning default value!");
		return 0f;
	}
	
	public static float getSharkMaxSpeed()
	{
		switch(_scale)
		{
		case _128: return SHARK_MAX_SPEED_128;
		case _256: return SHARK_MAX_SPEED_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getSharkMaxSpeed() returning default value!");
		return 0f;
	}
	
	public static float getMountedSharkAcceleration()
	{
		switch(_scale)
		{
		case _128: return MOUNTED_SHARK_ACCELERATION_128;
		case _256: return MOUNTED_SHARK_ACCELERATION_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getMountedSharkAcceleration() returning default value!");
		return 0f;
	}
	
	public static float getMountedSharkMaxSpeed()
	{
		switch(_scale)
		{
		case _128: return MOUNTED_SHARK_MAX_SPEED_128;
		case _256: return MOUNTED_SHARK_MAX_SPEED_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getMountedSharkMaxSpeed() returning default value!");
		return 0f;
	}
	
	public static float getSharkMouthHitboxRadius()
	{
		switch(_scale)
		{
		case _128: return SHARK_MOUTH_HITBOX_RADIUS_128;
		case _256: return SHARK_MOUTH_HITBOX_RADIUS_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getSharkMouthHitboxRadius() returning default value!");
		return 0f;
	}
	
	public static float getSharkBackHitboxRadius()
	{
		switch(_scale)
		{
		case _128: return SHARK_BACK_HITBOX_RADIUS_128;
		case _256: return SHARK_BACK_HITBOX_RADIUS_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getSharkBackHitboxRadius() returning default value!");
		return 0f;
	}
	
	public static float getSharkDestTurnDistanceSquared() {
		switch( _scale ) {
		case _128: return SHARK_DEST_TURN_DISTANCE_SQUARED_128;
		case _256: return SHARK_DEST_TURN_DISTANCE_SQUARED_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getSharkDestAlertDistanceSquared() returning default value!");
		return 0f;
	}
	
	public static float getSharkDestAlertDistanceSquared() {
		switch( _scale ) {
		case _128: return SHARK_DEST_ALERT_DISTANCE_SQUARED_128;
		case _256: return SHARK_DEST_ALERT_DISTANCE_SQUARED_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getSharkDestAlertDistanceSquared() returning default value!");
		return 0f;
	}
	
	public static float getSharkHitboxOffset()
	{
		switch(_scale)
		{
		case _128: return SHARK_HITBOX_OFFSET_128;
		case _256: return SHARK_HITBOX_OFFSET_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getSharkHitboxOffset() returning default value!");
		return 0f;
	}
	
	public static float getPlayerHitboxRadius()
	{
		switch(_scale)
		{
		case _128: return PLAYER_HITBOX_RADIUS_128;
		case _256: return PLAYER_HITBOX_RADIUS_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getPlayerHitboxRadius() returning default value!");
		return 0f;
	}
	
	public static float getWaveSpeed()
	{
		switch(_scale)
		{
		case _128: return WAVE_SPEED_128;
		case _256: return WAVE_SPEED_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getWaveSpeed() returning default value!");
		return 0f;
	}
	
	public static float getWaveFriction()
	{
		switch(_scale)
		{
		case _128: return WAVE_FRICTION_128;
		case _256: return WAVE_FRICTION_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getWaveFriction() returning default value!");
		return 0f;
	}
	
	public static float getPowerupCollisionRadius()
	{
		switch(_scale)
		{
		case _128: return POWERUP_COLLISION_RADIUS_128;
		case _256: return POWERUP_COLLISION_RADIUS_256;
		}
		
		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***ERROR*** SharkRodeoConstants.getPowerupCollisionRadius() returning default value!");
		return 0f;
	}
	
}
