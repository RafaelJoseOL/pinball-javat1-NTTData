package nttdata.javat1;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Clase para almacenar las puntuaciones.
 *
 */
public class Score {
	private LinkedHashMap<Ball, Integer> scorePerBall = new LinkedHashMap<>();

	public Map<Ball, Integer> getScorePerBall() {
		return scorePerBall;
	}

	public void setScorePerBall(Map<Ball, Integer> score) {
		this.scorePerBall.putAll(score);
	}	
}
