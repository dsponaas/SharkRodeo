package com.dg.sharkrodeo;

public interface ActionResolver {
	
	public void bootstrap();
	
	public void showScoreloop();
	
	public void submitScore( final int level, final int score );
	
	public void refreshScores();
	
}
