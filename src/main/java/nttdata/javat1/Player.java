package nttdata.javat1;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona cada jugador.
 *
 */
public class Player {
	private int playerNumber;
	private List<Ball> playerBalls = new ArrayList<>();
	
	public Player(int playerNumber) {
		super();
		this.playerNumber = playerNumber;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public List<Ball> getPlayerBalls() {
		return playerBalls;
	}

	public void setPlayerBalls(List<Ball> playerBalls) {
		this.playerBalls = playerBalls;
	}
	
	public void addBall(Ball ball) {
		this.playerBalls.add(ball);
	}
}
