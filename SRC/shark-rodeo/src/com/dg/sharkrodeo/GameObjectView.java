package com.dg.sharkrodeo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameObjectView {

	private List<ParticleEffect> _emitters;
//	private TextureAtlas _atlas;

	private Map<String, AnimationSequence> _animations;
	private AnimationSequence _activeAnimation;
	private String _transitionState;
//	private float _animStateTime;
	
	public GameObjectView()
	{
		_animations = new HashMap<String, AnimationSequence>();
		
//		_animStateTime = 0f;
		
		_emitters = new ArrayList<ParticleEffect>();
		ParticleEffect particle = new ParticleEffect();
		particle.load(Gdx.files.internal("data/bubble.p"), Gdx.files.internal("data"));
		particle.setPosition(0f, 0f);
		_emitters.add(particle);
		
//		_activeAnimation = idleLeftSequence;
//		_activeAnimation.startSequence();
		_activeAnimation = null;

		_transitionState = null;
	}
	
	public void update(float delta)
	{
		if(_activeAnimation == null)
		{
			Gdx.app.log(SharkRodeoConstants.LOG_TAG, "*********** _activeAnimation is null in GameObjectView.update(float)***********");
			return;
		}
		_activeAnimation.update(delta);
	}
	
	public TextureRegion getCurrentAnimationFrame()
	{
		if(_activeAnimation == null)
		{
			Gdx.app.log(SharkRodeoConstants.LOG_TAG, "*********** _activeAnimation is null in GameObjectView.getCurrentAnimationFrame()***********");
			return null;
		}
			
		TextureRegion tex =_activeAnimation.getCurrentFrame();
		
		if(tex != null)
			return tex;
		else
		{
			if(_transitionState != null)
			{
				setAnimState(_transitionState);
				return getCurrentAnimationFrame();
			}
			else
			{
				Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***********RETURNING NULL TEXTUREREGION FROM getCurrentAnimation()***********");
				return null;
			}
		}
	}
	
	public void addAnimation(String name, AnimationSequence animToAdd)
	{
		_animations.put(name, animToAdd);
	}
	
	public void setAnimState(String newState)
	{
		AnimationSequence newAnimation = _animations.get(newState);
		if(null == newAnimation)
		{
			Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***********NULL ANIMATION IN setAnimState()***********");
			return;
		}
		
		_activeAnimation = newAnimation;
		_activeAnimation.startSequence();
		_transitionState = null;
	}
	
	public void transitionAnimState(String newState)
	{
		_transitionState = newState;
		_activeAnimation.finish();
	}
	
	public List<ParticleEffect> getEmitters()			{ return _emitters; }
	
}
