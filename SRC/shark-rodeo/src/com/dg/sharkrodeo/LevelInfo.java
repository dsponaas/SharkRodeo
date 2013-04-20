package com.dg.sharkrodeo;

public class LevelInfo {

	public static int getMaximumSharks( int level ) {
		switch( level ) {
		case 1: return 2;
		case 2: return 3;
		case 3: return 3;
		case 4: return 4;
		case 5: return 4;
		case 6: return 5;
		case 7: return 5;
		case 8: return 6;
		case 9: return 7;
		case 10: return 8;
		case 11: return 9;
		case 12: return 10;
		}
		return 12;
	}
	
	public static int getMaximumWaves( int level ) {
		switch( level ) {
//		case 1: return 0;
		case 1: return 8;
		case 2: return 1;
		case 3: return 2;
		case 4: return 4;
		case 5: return 4;
		case 6: return 5;
		case 7: return 5;
		case 8: return 6;
		}
		return 8;
	}
	
	public static int getMaximumWhirlpools( int level ) {
		switch( level ) {
		case 1: return 0;
		case 2: return 0;
		case 3: return 1;
		case 4: return 2;
		case 5: return 2;
		case 6: return 3;
		case 7: return 3;
		case 8: return 4;
		}
		return 5;
	}
	
	public static int getInitialNumSharks( int level ) {
		switch( level ) {
		case 1: return 1;
		case 2: return 1;
		case 3: return 1;
		case 4: return 2;
		case 5: return 2;
		case 6: return 2;
		case 7: return 3;
		case 8: return 3;
		case 9: return 4;
		case 10: return 4;
		case 11: return 4;
		case 12: return 4;
		}
		return 5;
	}
	
	public static int getDelayedNumSharks( int level ) {
		switch( level ) {
		case 1: return 1;
		case 2: return 2;
		case 3: return 3;
		case 4: return 3;
		case 5: return 5;
		case 6: return 7;
		case 7: return 10;
		case 8: return 13;
		}
		return level + 5;
	}
	
	public static float getSharkSpawnTime( int level ) {
		switch( level ) {
		case 1: return 20.05f;
		case 2: return 18.05f;
		case 3: return 16.05f;
		case 4: return 14.05f;
		case 5: return 12.05f;
		case 6: return 10.05f;
		case 7: return 8.05f;
		case 8: return 6.05f;
		}
		return 5.05f;
	}
	
	public static float getWaveSpawnTime( int level ) {
		switch( level ) {
//		case 1: return 10.05f;
		case 1: return 4.05f;
		case 2: return 10.05f;
		case 3: return 8.05f;
		case 4: return 6.05f;
		case 5: return 4.05f;
		case 6: return 2.05f;
		}
		return 1.05f;
	}
	
	public static float getWhirlpoolSpawnTime( int level ) {
		switch( level ) {
		case 1: return 10.05f;
		case 2: return 10.05f;
		case 3: return 10.05f;
		case 4: return 8.05f;
		case 5: return 6.05f;
		case 6: return 4.05f;
		}
		return 1.05f;
	}
	
	public static float getPowerupSpawnTime( int level ) {
		switch( level ) {
		case 1: return Utils.getRandomFloatInRange( 3f, 10f );
		case 2: return Utils.getRandomFloatInRange( 5f, 18f );
		case 3: return Utils.getRandomFloatInRange( 7f, 20f );
		case 4: return Utils.getRandomFloatInRange( 9f, 22f );
		case 5: return Utils.getRandomFloatInRange( 11f, 24f );
		case 6: return Utils.getRandomFloatInRange( 13f, 26f );
		}
		return Utils.getRandomFloatInRange( 15f, 30f );
	}
	
}
