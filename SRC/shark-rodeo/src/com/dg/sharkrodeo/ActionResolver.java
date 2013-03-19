package com.dg.sharkrodeo;

public interface ActionResolver {
	
	public void bootstrap();
	
	public void showScoreloop();
	
	public void submitScore( final int mode, final int score );
	
	public void refreshScores();
	
}
