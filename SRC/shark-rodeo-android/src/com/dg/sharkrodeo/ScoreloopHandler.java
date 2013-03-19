package com.dg.sharkrodeo;

import com.scoreloop.client.android.core.controller.RankingController;
import com.scoreloop.client.android.core.controller.RequestController;
import com.scoreloop.client.android.core.controller.RequestControllerObserver;
import com.scoreloop.client.android.core.controller.ScoreController;
import com.scoreloop.client.android.core.controller.ScoresController;
import com.scoreloop.client.android.core.model.Ranking;
import com.scoreloop.client.android.core.model.Score;
import com.scoreloop.client.android.core.model.SearchList;
import com.scoreloop.client.android.core.model.Session;

public class ScoreloopHandler {

	private MainActivity _context;
	
	public ScoreloopHandler( MainActivity context ) {
		_context = context;
	}
	
	public void submitScore( int scoreValue ) {
		final Score score = new Score( ( double )scoreValue, null );
		final ScoreController scoreController = new ScoreController( new ScoreSubmitObserver() );
		scoreController.submitScore( score );
	}
	
	public void getRankingForScore( int scoreValue ) {
		final Score score = new Score( ( double )scoreValue, null );
		RankingController controller = new RankingController( new RankingRequestObserver() );
		controller.loadRankingForScore( score );
	}
	
	public void getGlobalHighScores() {
		ScoresController scoreController = new ScoresController( new GlobalRankObserver() );
		scoreController.setSearchList( SearchList.getTwentyFourHourScoreSearchList() );
		scoreController.setRangeLength( 15 );
		scoreController.loadRangeForUser( Session.getCurrentSession().getUser() );
	}
	
	public void getTodayHighScores() {
		ScoresController scoreController = new ScoresController( new DailyRankObserver() );
		scoreController.setSearchList( SearchList.getGlobalScoreSearchList() );
		scoreController.setRangeLength( 15 );
		scoreController.loadRangeForUser( Session.getCurrentSession().getUser() );
	}
	
	private class RankingRequestObserver implements RequestControllerObserver {
		
		@Override
		public void requestControllerDidFail( final RequestController requestController, final Exception exception ) {
			// Log that shit?
		}
		
		@Override
		public void requestControllerDidReceiveResponse( final RequestController requestController ) {
			Ranking ranking = ( ( RankingController)requestController ).getRanking();
			_context.postRunnable( new Runnable() {
				@Override
				public void run() {
					// use a local variable to store the rank in the main project and set it here
				}
			});
		}
		
	} // private class RankingRequestObserver
	
	private class ScoreSubmitObserver implements RequestControllerObserver {
		
		@Override
		public void requestControllerDidFail( final RequestController requestController, final Exception exception ) {
			// Log that shit?
		}
		
		@Override
		public void requestControllerDidReceiveResponse( final RequestController requestController ) {
			// Log that shit?
		}
		
	} // private class ScoreSubmitObserver
	
	private class DailyRankObserver implements RequestControllerObserver {
		
		@Override
		public void requestControllerDidFail( final RequestController requestController, final Exception exception ) {
			// Log that shit?
		}
		
		@Override
		public void requestControllerDidReceiveResponse( final RequestController requestController ) {
			Ranking ranking = ( ( RankingController)requestController ).getRanking();
			_context.postRunnable( new Runnable() {
				@Override
				public void run() {
					// use a local variable to store the rank in the main project and set it here
				}
			});
		}
		
	} // private class DailyRankObserver
	
	private class GlobalRankObserver implements RequestControllerObserver {
		
		@Override
		public void requestControllerDidFail( final RequestController requestController, final Exception exception ) {
			// Log that shit?
		}
		
		@Override
		public void requestControllerDidReceiveResponse( final RequestController requestController ) {
			Ranking ranking = ( ( RankingController)requestController ).getRanking();
			_context.postRunnable( new Runnable() {
				@Override
				public void run() {
					// use a local variable to store the rank in the main project and set it here
				}
			});
		}
		
	} // private class GlobalRankObserver
	
} // public class ScoreloopHandler
