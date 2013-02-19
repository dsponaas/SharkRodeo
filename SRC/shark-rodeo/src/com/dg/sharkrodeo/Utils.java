package com.dg.sharkrodeo;

import java.util.Random;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Utils
{
	private static Random _rand = null;
	
	public static float getRandomFloatInRange( float start, float end ) {
		if( _rand == null )
			_rand = new Random();
		
		float difference = end - start;
		return start + ( _rand.nextFloat() * difference );
	}
	
	public static boolean coinFlip() {
		if( _rand == null )
			_rand = new Random();
		
		return _rand.nextBoolean();
	}
	
	public static boolean circlesOverlap( Circle c1, Circle c2 ) {
		float distX = c1.x - c2.x;
		float distY = c1.y - c2.y;
		float lenSquared = ( distX * distX ) + ( distY * distY );
		float radSquared = ( c1.radius + c2.radius ) * ( c1.radius + c2.radius );
		if( lenSquared < radSquared )
			return true;
		return false;
	}
	
	public static void collideCircularGameObjects( Circle c1, Circle c2, GameObject g1, GameObject g2 ) {
		Vector2 unitNormal = new Vector2( c2.x - c1.x, c2.y - c1.y );
		unitNormal.div( unitNormal.len() );
		
		Vector2 unitTangent = new Vector2( -1f * unitNormal.y, unitNormal.x );
		
		Vector2 g1Velocity = new Vector2( g1.getVelocity() );
		Vector2 g2Velocity = new Vector2( g2.getVelocity() );
		
		float v1Normal = unitNormal.dot( g1Velocity );
		float v1Tangent = unitTangent.dot( g1Velocity );

		float v2Normal = unitNormal.dot( g2Velocity );
		float v2Tangent = unitTangent.dot( g2Velocity );
		
		float g1Mass = g1.getMass();
		float g2Mass = g2.getMass();
		
		float newV1Normal = ( v1Normal * ( g1Mass - g2Mass ) ) + ( 2f * g2Mass * v2Normal );
		newV1Normal /= g1Mass + g2Mass;
		float newV2Normal = ( v2Normal * ( g2Mass - g1Mass ) ) + ( 2f * g1Mass * v1Normal );
		newV2Normal /= g1Mass + g2Mass;
		
		float g1NewVelX = ( v1Tangent * unitTangent.x ) + ( newV1Normal * unitNormal.x );
		float g1NewVelY = ( v1Tangent * unitTangent.y ) + ( newV1Normal * unitNormal.y );
		
		float g2NewVelX = ( v2Tangent * unitTangent.x ) + ( newV2Normal * unitNormal.x );
		float g2NewVelY = ( v2Tangent * unitTangent.y ) + ( newV2Normal * unitNormal.y );
		
		g2.setVelocity( g2NewVelX, g2NewVelY );
		g1.setVelocity( g1NewVelX, g1NewVelY );
	}
	
}
