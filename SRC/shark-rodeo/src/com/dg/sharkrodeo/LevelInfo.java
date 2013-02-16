package com.dg.sharkrodeo;

public class LevelInfo
{

	public static int getMaximumSharks(int level)
	{
		switch(level)
		{
		case 1: return 2;
		case 2: return 3;
		case 3: return 3;
		case 4: return 4;
		}
		return 8;
	}
	
	public static int getMaximumWaves(int level)
	{
		switch(level)
		{
		case 1: return 0;
		case 2: return 1;
		case 3: return 2;
		case 4: return 4;
		}
		return 6;
	}
	
	public static int getMaximumWhirlpools(int level)
	{
		switch(level)
		{
		case 1: return 0;
		case 2: return 0;
		case 3: return 1;
		case 4: return 2;
		}
		return 4;
	}
	
	public static int getInitialNumSharks(int level)
	{
		switch(level)
		{
		case 1: return 1;
		case 2: return 1;
		case 3: return 1;
		case 4: return 2;
		}
		return 3;
	}
	
	public static int getDelayedNumSharks(int level)
	{
		switch(level)
		{
		case 1: return 1;
		case 2: return 2;
		case 3: return 3;
		case 4: return 3;
		}
		return 4;
	}
	
	public static float getSharkSpawnTime(int level)
	{
		switch(level)
		{
		case 1: return 10.05f;
		case 2: return 9.05f;
		case 3: return 8.05f;
		case 4: return 8.05f;
		}
		return 5.05f;
	}
	
	public static float getWaveSpawnTime(int level)
	{
		switch(level)
		{
		case 1: return 8.05f;
		case 2: return 6.05f;
		}
		return 5.05f;
	}
	
	public static float getWhirlpoolSpawnTime(int level)
	{
		switch(level)
		{
		case 1: return 8.05f;
		case 2: return 6.05f;
		}
		return 5.05f;
	}
	
	public static float getPowerupSpawnTime(int level)
	{
		switch(level)
		{
		case 1: return Utils.getRandomFloatInRange(3f, 20f);
		case 2: return Utils.getRandomFloatInRange(5f, 20f);
		}
		return Utils.getRandomFloatInRange(10f, 20f);
	}
	
}
