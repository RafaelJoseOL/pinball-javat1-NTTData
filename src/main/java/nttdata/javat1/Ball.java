package nttdata.javat1;

import java.util.Objects;

/**
 * Clase para controlar la información de cada bola.
 *
 */
public class Ball {
	private int number;
	private int score = 0;	
	
	public Ball(int number) {
		super();
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(number);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ball other = (Ball) obj;
		return number == other.number;
	}
	
	@Override
	public String toString() {
		return "Bola número: " + number + ", puntuación: " + score;
	}		
}
