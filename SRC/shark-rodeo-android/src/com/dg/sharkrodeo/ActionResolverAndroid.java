package com.dg.sharkrodeo;

import android.content.Intent;
import android.os.Handler;

import com.scoreloop.client.android.ui.EntryScreenActivity;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;

public class ActionResolverAndroid implements ActionResolver {
	
	private Handler _uiThread;
	private MainActivity _context;
	private ScoreloopHandler _handler;
	
	public ActionResolverAndroid( MainActivity context ) {
		_uiThread = new Handler();
		_context = context;
		_handler = new ScoreloopHandler( _context );
	}

	@Override
	public void bootstrap() {
		_uiThread.post( new Runnable() {
			@Override
			public void run() {
				_handler.getGlobalHighScores();
				_handler.getTodayHighScores();
				ScoreloopManagerSingleton.get().submitLocalScores( null );
			}
		});
	} // public void bootstrap()

	@Override
	public void showScoreloop() {
		Intent intent = new Intent( _context, EntryScreenActivity.class );
		_context.startActivity( intent );
	} // public void showScoreloop()

	@Override
	public void submitScore( final int level, final int score ) {
		_uiThread.post( new Runnable() {
			@Override
			public void run() {
				_handler.submitScore( level, score );
				_handler.getRankingForScore( score );
			}
		});
	} // public void submitScore(int mode, int score)

	@Override
	public void refreshScores() {
		_uiThread.post( new Runnable() {
			@Override
			public void run() {
				_handler.getGlobalHighScores();
				_handler.getTodayHighScores();
			}
		});
	} // public void refreshScores()

}
