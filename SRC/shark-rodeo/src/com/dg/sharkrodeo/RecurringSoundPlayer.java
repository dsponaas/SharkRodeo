package com.dg.sharkrodeo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class RecurringSoundPlayer {
	
	private Sound _sound;
	private long _soundId;
	private float[] _volumes;
	
	public RecurringSoundPlayer( Sound sound ) {
		_sound = sound;
		_soundId = -1;
		_volumes = new float[ 16 ];
		resetVolumes();
	}
	
	private void resetVolumes() {
		for( int i = 0; i < _volumes.length; ++i ) {
			_volumes[ i ] = -1f;
		}
	}

	public void setVolume( int id, float volume ) {
		_volumes[ id ] = volume;
		float maxVolume = -1f;
		for( int i = 0; i < _volumes.length; ++i ) {
			if( _volumes[ i ] > maxVolume ) {
				maxVolume = _volumes[ i ];
			}
		}
		
		if( maxVolume > 0f ) {
			if( _soundId > 0 ) {
				_sound.setVolume( _soundId, maxVolume );
			}
			else {
				_soundId = _sound.loop( maxVolume );
				Gdx.app.log(SharkRodeoConstants.LOG_TAG, "playing sound:" + _soundId );
			}
		}
		else {
			if( _soundId > 0 ) {
				_sound.stop( _soundId );
				Gdx.app.log(SharkRodeoConstants.LOG_TAG, "stopping sound:" + _soundId );
				_soundId = -1;
			}
		}
	}
	
}
