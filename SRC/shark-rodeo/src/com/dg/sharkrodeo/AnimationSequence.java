package com.dg.sharkrodeo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationSequence
{
	private enum AnimationSequenceState { INTRO, LOOP, SINGLE, OUTRO, FINISHED };
	private AnimationSequenceState _animState;
	private boolean _loop;
	
	private Animation _intro;
	private float _elapsedTime; 
	
	private Animation _animation;
	private Animation _outro;
	
	private boolean _finished;
	
	public AnimationSequence(Animation anim)
	{
		this(anim, true);
	}
	
	public AnimationSequence(Animation anim, boolean loop)
	{
		_intro = null;
		_elapsedTime = 0f;
		
		_animation = anim;
//		_outro= null;
//		_animState = AnimationSequenceState.INTRO;
		_loop = loop;
	}
	
	public void update(float delta)
	{
		_elapsedTime += delta;
	}
	
	public void setIntro(Animation intro)
	{
		_intro = intro;
	}
	
	public void setOutro(Animation outro)
	{
		_outro = outro;
	}
	
	public void startSequence()
	{
		if(_intro != null)
		{
			_animState = AnimationSequenceState.INTRO;
		}
		else if(_loop)
		{
			_animState = AnimationSequenceState.LOOP;
		}
		else
		{
			_animState = AnimationSequenceState.SINGLE;
		}
		
		_elapsedTime = 0f;
		_finished = false;
	}
	
	public void finish()
	{
		_finished = true;
	}
	
	private void changeState(AnimationSequenceState newState)
	{
		_animState = newState;
		_finished = false;
		_elapsedTime = 0f;
	}
	
	public TextureRegion getCurrentFrame()
	{
		//adjust this to show outro when requestfinish... wait until cur _animation run through is finished before starting outro
		switch(_animState)
		{
		case INTRO:
			if(_intro.isAnimationFinished(_elapsedTime))
			{
				if(_loop)
					changeState(AnimationSequenceState.LOOP);
				else
					changeState(AnimationSequenceState.SINGLE);
				return getCurrentFrame();
			}
			return _intro.getKeyFrame(_elapsedTime, false);

		case LOOP:
			if(_animation.isAnimationFinished(_elapsedTime))
			{
				if(_finished)
					changeState(AnimationSequenceState.OUTRO);
				else
					changeState(AnimationSequenceState.LOOP);
				return getCurrentFrame();
			}
			return _animation.getKeyFrame(_elapsedTime, false);

		case SINGLE:
			if(_animation.isAnimationFinished(_elapsedTime))
			{
				changeState(AnimationSequenceState.OUTRO);
			}
			return _animation.getKeyFrame(_elapsedTime, false);

		case OUTRO:
			if(_outro == null)
				return null;
			if(_outro.isAnimationFinished(_elapsedTime))
			{
				changeState(AnimationSequenceState.FINISHED);
			}
			return _outro.getKeyFrame(_elapsedTime, false);

		case FINISHED:
			return null;
		}
		
		return null;
	}// getCurrentFrame()
	
}
