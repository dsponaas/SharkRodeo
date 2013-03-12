package com.dg.sharkrodeo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameObjectView {

	private List<ParticleEffect> _emitters;

	private Map<String, AnimationSequence> _animations;
	private AnimationSequence _activeAnimation;
	private String _transitionState;
	
	public GameObjectView() {
		_animations = new HashMap<String, AnimationSequence>();
		
		_emitters = new ArrayList<ParticleEffect>();
		ParticleEffect particle = new ParticleEffect();
		particle.load( Gdx.files.internal( "data/bubble.p" ), Gdx.files.internal( "data" ) );
		particle.setPosition( 0f, 0f );
		_emitters.add( particle );
		
		_activeAnimation = null;

		_transitionState = null;
	}
	
	public void update( float delta ) {
		if( _activeAnimation == null ) {
			Gdx.app.log( SharkRodeoConstants.LOG_TAG, "*********** _activeAnimation is null in GameObjectView.update(float)***********" );
			return;
		}
		_activeAnimation.update( delta );
	}
	
	public TextureRegion getCurrentAnimationFrame() {
		if( _activeAnimation == null ) {
			Gdx.app.log( SharkRodeoConstants.LOG_TAG, "*********** _activeAnimation is null in GameObjectView.getCurrentAnimationFrame()***********" );
			return null;
		}
			
		TextureRegion tex =_activeAnimation.getCurrentFrame();
		
		if( tex != null ) {
			return tex;
		}
		else {
			if( _transitionState != null ) {
				setAnimState( _transitionState );
				return getCurrentAnimationFrame();
			}
			else {
				Gdx.app.log( SharkRodeoConstants.LOG_TAG, "***********RETURNING NULL TEXTUREREGION FROM getCurrentAnimation()***********" );
				return null;
			}
		} // else
	} // public TextureRegion getCurrentAnimationFrame()
	
	public void addAnimation( String name, AnimationSequence animToAdd ) {
		_animations.put( name, animToAdd );
	}
	
	public void setAnimState( String newState ) {
		_transitionState = null;

		AnimationSequence newAnimation = _animations.get( newState );
		if( ( _activeAnimation == newAnimation ) && ( !_activeAnimation.isFinishing() ) ) {
			return;
		}
		if( null == newAnimation ) {
			Gdx.app.log( SharkRodeoConstants.LOG_TAG, "***********NULL ANIMATION IN setAnimState()***********" );
			return;
		}
		
		_activeAnimation = newAnimation;
		_activeAnimation.startSequence();
	} // public void setAnimState( String newState )
	
	public void transitionAnimState( String newState ) {
		_transitionState = newState;
		_activeAnimation.finish();
	}
	
	public void resetParticles() {
		for( ParticleEffect curEffect : _emitters ) {
//			curEffect.
			for( ParticleEmitter curEmitter : curEffect.getEmitters() ) {
				curEmitter.start();
			}
		}
	} // public void resetParticles()
	
	public List<ParticleEffect> getEmitters()			{ return _emitters; }
	
}
