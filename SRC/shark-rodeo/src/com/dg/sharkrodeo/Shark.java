package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Shark extends GameObject
{
	
	public enum SharkState { SEARCHING, MOUNTING, MOUNTED, THRASHING, LUNGING };
	private SharkState _sharkState;
	private Vector2 _searchDest;
	private float _stateTime;
	private float _health;
	
	private static final float STATE_TIME_CRAPTAR = 2f; 
	
	public Shark(GameBoard board) {
		super(board, 36f);//TODO: magic number

		setAccelerationRate(SharkRodeoConstants.getSharkAcceleration());
		setMaxSpeed(SharkRodeoConstants.getSharkMaxSpeed());
		
		initBoundsList(2);
		Circle bounds = new Circle(this.getPosition().x, this.getPosition().y, SharkRodeoConstants.getSharkMouthHitboxRadius());
		Circle bounds2 = new Circle(this.getPosition().x, this.getPosition().y, SharkRodeoConstants.getSharkBackHitboxRadius());
		setBounds(0, bounds, new Vector2(0, 0));
		setBounds(1, bounds2, new Vector2(0, 0));
		
		this.setXPosition(800f);
		this.setYPosition(800f);

		_sharkState = SharkState.SEARCHING;
		_health = SharkRodeoConstants.DEFAULT_SHARK_HEALTH;

		TextureAtlas atlas = new TextureAtlas(SharkRodeoConstants.getSharkPack());
		Animation swimLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("shark_swim_left"));
		Animation swimRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("shark_swim_right"));
		Animation swimDownAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("shark_swim_down"));
		Animation swimUpAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("shark_swim_up"));
		Animation swimDownLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("shark_swim_downleft"));
		Animation swimDownRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("shark_swim_downright"));
		Animation swimUpLeftAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("shark_swim_upleft"));
		Animation swimUpRightAnim = com.dg.sharkrodeo.Factories.AnimationFactory.createAnimation(0.15f, 1, 4, atlas.findRegion("shark_swim_upright"));
		
		AnimationSequence swimLeftSequence = new AnimationSequence(swimLeftAnim);
		AnimationSequence swimRightSequence = new AnimationSequence(swimRightAnim);
		AnimationSequence swimDownSequence = new AnimationSequence(swimDownAnim);
		AnimationSequence swimUpSequence = new AnimationSequence(swimUpAnim);
		AnimationSequence swimDownLeftSequence = new AnimationSequence(swimDownLeftAnim);
		AnimationSequence swimDownRightSequence = new AnimationSequence(swimDownRightAnim);
		AnimationSequence swimUpLeftSequence = new AnimationSequence(swimUpLeftAnim);
		AnimationSequence swimUpRightSequence = new AnimationSequence(swimUpRightAnim);
		
		addAnimation("move_left", swimLeftSequence);
		addAnimation("move_right", swimRightSequence);
		addAnimation("move_down", swimDownSequence);
		addAnimation("move_up", swimUpSequence);
		addAnimation("move_downleft", swimDownLeftSequence);
		addAnimation("move_downright", swimDownRightSequence);
		addAnimation("move_upleft", swimUpLeftSequence);
		addAnimation("move_upright", swimUpRightSequence);
		
		this.setClipping(false);

//		setAnimState("move_right");
		searchToDest(getNextSearchDest());
	}
	
	public void update(float delta)
	{
		_stateTime -= delta;
		
		if(!this.getClipping())
		{
			if(this.isInBounds())
			{
				this.setClipping(true);
			}
		}
		
		if((_sharkState == SharkState.MOUNTED) || (_sharkState == SharkState.THRASHING))
		{
			_health -= delta; //TODO: times some bonus multiplier? (spurs?)
		}
		
//		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***********: dest: x:" + _searchDest.x + " y:" + _searchDest.y);
		if((_sharkState == SharkState.SEARCHING) || (_sharkState == SharkState.MOUNTED) || (_sharkState == SharkState.LUNGING))
		{
			Direction oldDirection = this.getDirection();
			Vector2 positionDelta = (new Vector2(_searchDest)).sub(this.getPosition());
			this.accelerateInDirection(positionDelta);
			Direction newDirection = this.getDirection();
			if(newDirection != oldDirection)
			{
				changeDirection(newDirection);
			}
			
//			Vector2 newPosition = (new Vector2(this.getAcceleration())).mul(delta).add(this.getPosition());
//			Vector2 newDelta = (new Vector2(_searchDest)).sub(newPosition);
			Vector2 oldDelta = (new Vector2(_searchDest)).sub(this.getPosition());
//			if(newDelta.len() > oldDelta.len())
			if(oldDelta.len() < 50f)// TODO: MAGIC NUMBER
			{
				searchToDest(getNextSearchDest());
			}
		}//if(_sharkState == SharkState.SEARCHING)
		
		if(_stateTime < 0f)
		{
			if(_sharkState == SharkState.MOUNTED)
			{
				_sharkState = SharkState.THRASHING;
				this.setUpatePosition(false);
				_stateTime = STATE_TIME_CRAPTAR;
				GameBoard.getInstance().startCameraShake();
			}
			else if(_sharkState == SharkState.THRASHING)
			{
				_sharkState = SharkState.MOUNTED;
				this.setUpatePosition(true);
				_stateTime = STATE_TIME_CRAPTAR;
				GameBoard.getInstance().endCameraShake();
			}
		}
		
		super.update(delta);
	}
	
	public void searchToDest(Vector2 dest)
	{
//		Gdx.app.log(SharkRodeoConstants.LOG_TAG, "***********: dest: x:" + dest.x + " y:" + dest.y);

		_searchDest = new Vector2(dest);
		Direction oldDirection = this.getDirection();
		Vector2 delta = dest.sub(this.getPosition());
		this.accelerateInDirection(delta);
		Direction newDirection = this.getDirection();
		
		if((newDirection != oldDirection) || ((_sharkState != SharkState.SEARCHING) && (_sharkState != SharkState.MOUNTED)))
		{
			changeDirection(newDirection);
			if(_sharkState != SharkState.MOUNTED)
			{
				_sharkState = SharkState.SEARCHING;
			}
		}
	}
	
	public void lungeAtPlayer()
	{
		Vector2 sharkPos = getPosition();
		Vector2 playerPos = GameBoard.getInstance().getPlayerPos();
		Vector2 sharkToPlayer = (new Vector2(playerPos.x - sharkPos.x, playerPos.y - sharkPos.y)).mul(0.8f); //TODO: magic number
		Vector2 sharkDest = (new Vector2(playerPos)).add(sharkToPlayer);
		
		_sharkState = SharkState.LUNGING;
		
//		Direction oldDirection = this.getDirection();
		Vector2 delta = sharkDest.sub(this.getPosition());
		this.accelerateInDirection(delta);
		Direction newDirection = this.getDirection();
		
//		if((newDirection != oldDirection) || ((_sharkState != SharkState.SEARCHING) && (_sharkState != SharkState.MOUNTED)))
//		{
		changeDirection(newDirection);
//			if(_sharkState != SharkState.MOUNTED)
//			{
//				_sharkState = SharkState.SEARCHING;
//			}
//		}

		//curShark.searchToDest(sharkDest);
		setAccelerationRate(3f * SharkRodeoConstants.getSharkAcceleration()); //TODO: magic number
		setMaxSpeed(3f * SharkRodeoConstants.getSharkMaxSpeed()); //TODO: magic number
	}
	
	private void changeDirection(Direction newDirection)
	{
		float distance = SharkRodeoConstants.getSharkHitboxOffset();
		float diagDist = (float)Math.sqrt((distance * distance) / 2f);//TODO: calculate this beforehand, store as a constant
		
		switch(newDirection)
		{
		case UP:
			this.setAnimState("move_up");
			_boundsOffsets[0].set(0, distance);
			_boundsOffsets[1].set(0, -1f * distance);
			break;
		case DOWN:
			this.setAnimState("move_down");
			_boundsOffsets[0].set(0, -1f * distance);
			_boundsOffsets[1].set(0, distance);
			break;
		case LEFT:
			this.setAnimState("move_left");
			_boundsOffsets[0].set(-1f * distance, 0);
			_boundsOffsets[1].set(distance, 0);
			break;
		case RIGHT:
			this.setAnimState("move_right");
			_boundsOffsets[0].set(distance, 0);
			_boundsOffsets[1].set(-1f * distance, 0);
			break;
		case UP_LEFT:
			this.setAnimState("move_upleft");
			_boundsOffsets[0].set(-1f * diagDist, diagDist);
			_boundsOffsets[1].set(diagDist, -1f * diagDist);
			break;
		case UP_RIGHT:
			this.setAnimState("move_upright");
			_boundsOffsets[0].set(diagDist, diagDist);
			_boundsOffsets[1].set(-1f * diagDist, -1f * diagDist);
			break;
		case DOWN_LEFT:
			this.setAnimState("move_downleft");
			_boundsOffsets[0].set(-1f * diagDist, -1f * diagDist);
			_boundsOffsets[1].set(diagDist, diagDist);
			break;
		case DOWN_RIGHT:
			this.setAnimState("move_downright");
			_boundsOffsets[0].set(diagDist, -1f * diagDist);
			_boundsOffsets[1].set(-1f * diagDist, diagDist);
			break;
		}
	}
	
	public void mountingShark()
	{
		_sharkState = SharkState.MOUNTING;
		this.setUpatePosition(true);
	}
	
	public void mountShark()
	{
		_sharkState = SharkState.MOUNTED;
		setAccelerationRate(SharkRodeoConstants.getMountedSharkAcceleration());
		setMaxSpeed(SharkRodeoConstants.getMountedSharkMaxSpeed());
		// TODO: change anim state... maybe do that in changeDirection instead and just call it here? also increase speed
		this.setUpatePosition(true);
		_stateTime = STATE_TIME_CRAPTAR;
		
	}
	
	public void dismount()
	{
		_sharkState = SharkState.SEARCHING;
		this.setPosition(1200f, 1200f); //TODO: magic number
		setAccelerationRate(SharkRodeoConstants.getSharkAcceleration());
		setMaxSpeed(SharkRodeoConstants.getSharkMaxSpeed());
		this.setUpatePosition(true);
		GameBoard.getInstance().endCameraShake();
//		Gdx.input.cancelVibrate();
	}
	
	private Vector2 getNextSearchDest()
	{
		Vector2 retval = new Vector2();
//		Vector2 oldPos = this.getPosition();
//		do
//		{
//			float x = Utils.coinFlip() ? Utils.getRandomFloatInRange(200f, 800f) : -1f * Utils.getRandomFloatInRange(600f, 800f);
//			float y = Utils.coinFlip() ? Utils.getRandomFloatInRange(200f, 800f) : -1f * Utils.getRandomFloatInRange(600f, 800f);
//			Gdx.app.log(SharkRodeoConstants.LOG_TAG, "   ********: : x:" + x + " y:" + y);
//			retval.x = oldPos.x + x;
//			retval.y = oldPos.y + y;
//		}while(!this.getGameBoard().isPositionInBounds(retval));
		
		float minDist = 500f;// TODO: MAGIC NUMBER
		float maxDist = this.getClipping() ? 600f : 999999f;// TODO: MAGIC NUMBER
		
		float len;
		Vector2 temp;
		do
		{
			retval.x = Utils.getRandomFloatInRange(200f, GameBoard.getInstance().getWidth() - 200f);// TODO: MAGIC NUMBER
			retval.y = Utils.getRandomFloatInRange(200f, GameBoard.getInstance().getHeight() - 200f);// TODO: MAGIC NUMBER
			temp = new Vector2(retval);
			temp.sub(this.getPosition());
			len = temp.len();
		}
		while((len < minDist) && (len > maxDist));
		
		return retval;
	}
	
	public float getMass()
	{
		if((_sharkState == SharkState.MOUNTED) || (_sharkState == SharkState.THRASHING))
		{
			return 1.4f;//TODO:magic number
		}
		return 1.0f;//TODO:magic number
	}
	
	public boolean isPointInLineOfSight(float x, float y)
	{
		float SIGHT_DISTANCE = 350f; //TODO: magic number
		float SIGHT_DISTANCE_DIAG = 247.487f;//(float)Math.sqrt((SIGHT_DISTANCE * SIGHT_DISTANCE) / 2f);
		float SIGHT_OFFSET = 150f; //TODO: magic number
		float SIGHT_OFFSET_DIAG = 106.066f;//(float)Math.sqrt((SIGHT_OFFSET * SIGHT_OFFSET) / 2f);
		
		Vector2 dist2 = new Vector2(this.getPosition());
		dist2.sub(x, y);
		if(dist2.len2() > (SIGHT_DISTANCE * SIGHT_DISTANCE))
			return false;
		
		Vector2 sightExtent = new Vector2(getPosition());
		Vector2 t1 = new Vector2();
		Vector2 t2 = new Vector2();
		
		switch(getDirection())
		{
		case UP:
			sightExtent.y += SIGHT_DISTANCE;
			t1.set(sightExtent);
			t2.set(sightExtent);
			t1.x += SIGHT_OFFSET;
			t2.x -= SIGHT_OFFSET;
			break;
		case DOWN:
			sightExtent.y -= SIGHT_DISTANCE;
			t1.set(sightExtent);
			t2.set(sightExtent);
			t1.x -= SIGHT_OFFSET;
			t2.x += SIGHT_OFFSET;
			break;
		case LEFT:
			sightExtent.x -= SIGHT_DISTANCE;
			t1.set(sightExtent);
			t2.set(sightExtent);
			t1.y += SIGHT_OFFSET;
			t2.y -= SIGHT_OFFSET;
			break;
		case RIGHT:
			sightExtent.x += SIGHT_DISTANCE;
			t1.set(sightExtent);
			t2.set(sightExtent);
			t1.y -= SIGHT_OFFSET;
			t2.y += SIGHT_OFFSET;
			break;
		case UP_LEFT:
			sightExtent.y += SIGHT_DISTANCE_DIAG;
			sightExtent.x -= SIGHT_DISTANCE_DIAG;
			t1.set(sightExtent);
			t2.set(sightExtent);
			t1.x -= SIGHT_OFFSET_DIAG;
			t1.y -= SIGHT_OFFSET_DIAG;
			t2.x += SIGHT_OFFSET_DIAG;
			t2.y += SIGHT_OFFSET_DIAG;
			break;
		case UP_RIGHT:
			sightExtent.y += SIGHT_DISTANCE_DIAG;
			sightExtent.x += SIGHT_DISTANCE_DIAG;
			t1.set(sightExtent);
			t2.set(sightExtent);
			t1.x -= SIGHT_OFFSET_DIAG;
			t1.y += SIGHT_OFFSET_DIAG;
			t2.x += SIGHT_OFFSET_DIAG;
			t2.y -= SIGHT_OFFSET_DIAG;
			break;
		case DOWN_LEFT:
			sightExtent.y -= SIGHT_DISTANCE_DIAG;
			sightExtent.x -= SIGHT_DISTANCE_DIAG;
			t1.set(sightExtent);
			t2.set(sightExtent);
			t1.x -= SIGHT_OFFSET_DIAG;
			t1.y += SIGHT_OFFSET_DIAG;
			t2.x += SIGHT_OFFSET_DIAG;
			t2.y -= SIGHT_OFFSET_DIAG;
			break;
		case DOWN_RIGHT:
			sightExtent.y -= SIGHT_DISTANCE_DIAG;
			sightExtent.x += SIGHT_DISTANCE_DIAG;
			t1.set(sightExtent);
			t2.set(sightExtent);
			t1.x -= SIGHT_OFFSET_DIAG;
			t1.y -= SIGHT_OFFSET_DIAG;
			t2.x += SIGHT_OFFSET_DIAG;
			t2.y += SIGHT_OFFSET_DIAG;
			break;
		}
		
		Vector2 v0 = (new Vector2(t1)).sub(getPosition());
		Vector2 v1 = (new Vector2(t2)).sub(getPosition());
		Vector2 v2 = (new Vector2(x, y)).sub(getPosition());
		
		float dot00 = v0.dot(v0);
		float dot01 = v0.dot(v1);
		float dot02 = v0.dot(v2);
		float dot11 = v1.dot(v1);
		float dot12 = v1.dot(v2);

		float invDenim = 1f / ((dot00 * dot11) - (dot01 * dot01));
		float u = ((dot11 * dot02) - (dot01 * dot12)) * invDenim;
		float v = ((dot00 * dot12) - (dot01 * dot02)) * invDenim;
		
		return (u >= 0f) && (v >= 0f) && ((u + v) < 1f);
	}

	public SharkState getSharkState()			{ return _sharkState; }
	public float getHealth()					{ return _health; }
	public float getHealthPercent()				{ return _health / SharkRodeoConstants.DEFAULT_SHARK_HEALTH; }

}
