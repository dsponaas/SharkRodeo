package com.dg.sharkrodeo;

import android.content.Context;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.scoreloop.client.android.core.model.Client;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;

public class MainActivity extends AndroidApplication {
	
	private static Client _client;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initClient( this );
        ScoreloopManagerSingleton.init( this, "SxqL9kR436ywk5qC0xabMAk3G7Dxu7Tl8uoxKYYDOIRIdJspHGeiuw==" );
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        ActionResolver actionResolver = new ActionResolverAndroid( this );
//        actionResolver.submitScore( 1, 100 );
        
        initialize( new SharkRodeo( actionResolver ), cfg );
    }
    
    private static void initClient( final Context context ) {
    	if( null == _client ) {
//    		_client = new Cli
    		_client = new Client( context, "f0300bf9-e780-48dd-b13d-616a6516cd32", "SxqL9kR436ywk5qC0xabMAk3G7Dxu7Tl8uoxKYYDOIRIdJspHGeiuw==", null );
    	}
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	ScoreloopManagerSingleton.destroy();
    }
    
} // public class MainActivity