package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.dg.sharkrodeo.PlayerModifier.PlayerModifierType;
import com.dg.sharkrodeo.Factories.AnimationFactory;

public class Powerup
{

	private Animation _anim;
	private ParticleEffect _particleEffect;
	
	private Vector2 _position;
	private Circle _bounds;
	
	public enum PowerupType { MULTIPLIER_2X, MULTIPLIER_4X, SPEED_UP, ONE_UP, ENDURANCE_UP };
	private PowerupType _type;
	public enum PowerupState { FADE_IN, FADE_OUT, ACTIVE };
	private PowerupState _state;
	private float _stateTimer;
	
	private boolean _active;
	
	public Powerup(PowerupType type, Vector2 position)
	{
		_type = type;
		
		_state = PowerupState.FADE_IN;
		_stateTimer = SharkRodeoConstants.POWERUP_FADE_TIMER;
		
		_position = position;
		_bounds = new Circle(_position, SharkRodeoConstants.getPowerupCollisionRadius());
		
		_particleEffect = new ParticleEffect();
		_particleEffect.load(Gdx.files.internal("data/star.p"), Gdx.files.internal("data"));
		_particleEffect.setPosition(0f, 0f);
		
		switch(_type)
		{
		case MULTIPLIER_2X:
			_anim = AnimationFactory.createAnimation(0.1f, 1, 6, ResourceManager.getInstance().getPowerupTexture("2x"));
			break;
		case MULTIPLIER_4X:
			_anim = AnimationFactory.createAnimation(0.1f, 1, 6, ResourceManager.getInstance().getPowerupTexture("4x"));
			break;
		case SPEED_UP:
			_anim = AnimationFactory.createAnimation(0.1f, 1, 6, ResourceManager.getInstance().getPowerupTexture("spd"));
			break;
		case ONE_UP:
			_anim = AnimationFactory.createAnimation(0.1f, 1, 6, ResourceManager.getInstance().getPowerupTexture("1up"));
			break;
		case ENDURANCE_UP:
			_anim = AnimationFactory.createAnimation(0.1f, 1, 6, ResourceManager.getInstance().getPowerupTexture("end"));
			break;
		}
		
		_active = true;
	}
	
	public boolean update(float delta)
	{
		_stateTimer -= delta;
		if(_stateTimer < 0f)
		{
			switch(_state)
			{
			case FADE_IN:
				_state = PowerupState.ACTIVE;
				_stateTimer = SharkRodeoConstants.POWERUP_TIMER;
				break;
			case ACTIVE:
				_state = PowerupState.FADE_OUT;
				_stateTimer = SharkRodeoConstants.POWERUP_FADE_TIMER;
				break;
			case FADE_OUT:
				_active = false;
			}
		}
		
		return _active;
	}
	
	public void checkForPickup(Circle playerBounds)
	{
		if(Utils.circlesOverlap(playerBounds, _bounds))
		{
//			Gdx.app.log(SharkRodeoConstants.LOG_TAG, "POWERUP PICKUP");
			switch(_type)
			{
			case MULTIPLIER_2X:
				GameState.setMultiplier(2);
				break;
			case MULTIPLIER_4X:
				GameState.setMultiplier(4);
				break;
			case SPEED_UP:
				GameBoard.getInstance().getPlayer().addModifier(PlayerModifierType.SPEED_UP);
				break;
			case ENDURANCE_UP:
				GameBoard.getInstance().getPlayer().addModifier(PlayerModifierType.ENDURANCE_UP);
				break;
			case ONE_UP:
				GameState.addLife();
				break;
			}
			_active = false;
		}
	}
	
	public float getAlpha()
	{
		switch(_state)
		{
		case FADE_IN:
			return (SharkRodeoConstants.POWERUP_FADE_TIMER - _stateTimer) / SharkRodeoConstants.POWERUP_FADE_TIMER;
		case FADE_OUT:
			return _stateTimer / SharkRodeoConstants.POWERUP_FADE_TIMER;
		}
		return 1f;
	}
	
	public TextureRegion getTexture()
	{
		float delta = _stateTimer;
		switch(_state)
		{
		case ACTIVE:
			delta += SharkRodeoConstants.POWERUP_FADE_TIMER;
			break;
		case FADE_OUT:
			delta += SharkRodeoConstants.POWERUP_FADE_TIMER + SharkRodeoConstants.POWERUP_TIMER;
			break;
		}
		return _anim.getKeyFrame(delta, true);
	}
	public ParticleEffect getEmitter()
	{
		if(_state == PowerupState.ACTIVE)
			return _particleEffect;
		return null;
	}
	public Vector2 getPosition()			{ return _position; }
	
}
