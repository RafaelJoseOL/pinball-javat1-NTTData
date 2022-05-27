package nttdata.javat1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase principal para gestionar toda la partida.
 *
 */
public class Game {

	Random ran = new Random();
	Score score = new Score();
	Targets targets = new Targets();
	Lights lights = new Lights();
	float comboMult = 1;
	int tempScore = 0;
	int action = 0;
	int currentAction = 0;
	int roundNumber = 0;
	int autoSaveActions = 0;
	boolean autoSave = false;
	int balls = 3;
	int scoreToBeat = 5000;
	int totalScore = 0;
	List<Player> players = new ArrayList<>();

	private static final Logger LOG = LoggerFactory.getLogger(Game.class);
	
	/**
	 * Método principal, comprueba el número de jugadores e inicia la partida.
	 */
	public void launchAndStart() {		
		Scanner sc = new Scanner(System.in);
		System.out.println("How many players will there be?");		
		int numberOfPlayers = sc.nextInt();		
		sc.close();
				
		//Gestionamos la partida de cada usuario desde este método al que llamamos.
		for (int i = 1; i <= numberOfPlayers; i++) {
			newPlayer(i);
		}
		
		//Obtenemos y mostramos la puntuación final
		getScore();
	}

	/**
	 * Método para gestionar cada ronda de los jugadores.
	 * @param player
	 */
	public void round(Player player) {
		//Reiniciamos las variables necesarias para cada ronda y generamos la bola correspondiente.
		newBall();
		Ball ball = new Ball(roundNumber);
		
		LOG.info("Ball number: {}.", roundNumber);
		
		//Obtenemos la primera acción que sucederá.
		getAction(getRandomNumber(9));
		
		//Comprobamos si la puntuación ha superado la necesaria para obtener una bola extra.
		if (tempScore > scoreToBeat) {
			scoreToBeat *= 1.5;
			balls++;
		}
		
		//Le añadimos a la bola actual la puntuación total que ha obtenido y se 
		//la asignamos al jugador correspondiente
		ball.setScore(tempScore);
		player.addBall(ball);
		
		LOG.debug("{}.", ball);
	}

	/**
	 * Switch para realizar la acción que toque en función del número anteriormente generado.
	 * @param action
	 */
	public void getAction(int action) {
		LOG.debug("Action: {}", action);
		switch (action) {
		case 0:
			flippers();
			break;
		case 1:
			bumpers();
			break;
		case 2:
		case 3:{
			orbit();
			break;
			}			
		case 4:
			targets(0);
			break;
		case 5:
			targets(1);
			break;
		case 6:
			ramp();
			break;
		case 7:
			lights(0);
			break;
		case 8:
			lights(1);
			break;
		case 9:
			skillShot();
			break;
		default:
			break;
		}
	}

	/**
	 * Método donde controlaremos lo que ocurre en la zona de rebotes.
	 */
	public void bumpers() {
		int points = 0;
		int curAction = 0;
		//Mientras el número aleatorio corresponda a un bumper y no sea 0, seguirá rebotando y sumando puntos.
		do {
			points += 100;
			curAction = getRandomNumber(3);
		} while (curAction != 0);
		LOG.debug("Bumpers: {}", points);
		//Sumamos los puntos correspondientes.
		increaseAction(points);
		int nextAct = getRandomNumber(2);
		
		//Calculamos la siguiente acción, desde esta zona puede ir a la inferior, 
		//a los objetivos superiores o a la zona de luces derecha.
		switch (nextAct) {
		case 0:
			flippers();
			break;
		case 1:
			targets(0);
			break;
		case 2:
			lights(1);
			break;
		default:
			break;
		}
	}

	/**
	 * Método para gestionar la zona del tunel, cruzará de lado a lado y caerá a la zona inferior.
	 */
	public void orbit() {
		LOG.debug("Orbit: {}", 500);
		increaseAction(500);
		flippers();
	}

	/**
	 * Método para gestionar los objetivos
	 * @param pos
	 */
	public void targets(int pos) {
		int points = 0;
		int hit = getRandomNumber(3);
		
		//Controlamos si golpeamos o no algun objetivo y si es de los superiores o los inferiores.
		if (pos == 0) {
			if (hit != 3 && Boolean.TRUE.equals(!targets.getTopTargets().get(hit))) {
				points += checkTargets(hit, targets.getTopTargets());
			}
		} else {
			if (hit != 3 && Boolean.TRUE.equals(!targets.getBottomTargets().get(hit))) {
				points += checkTargets(hit, targets.getBottomTargets());
			}
		}
		LOG.debug("Targets: {}", points);
		
		//Sumamos puntos y, en función de en que zona de objetivos estuviera, decidimos
		//si volverá a los flippers o si podrá caer de nuevo en los rebotadores.
		increaseAction(points);
		if(pos == 1) {
			flippers();
		}else {
			int nextAct = getRandomNumber(2);
			switch (nextAct) {
			case 0:
				flippers();
				break;
			case 1:
			case 2:
				bumpers();
				break;
			default:
				break;
			}
		}		
	}

	/**
	 * Gestionamos la zona de bonus tras la rampa desde este método.
	 */
	public void ramp() {
		int points = 0;
		int lit = getRandomNumber(2);
		
		//Pasamos por una de las 3 luces y la encendemos si estaba apagada.
		if (Boolean.TRUE.equals(!lights.getBonusLights().get(lit))) {
			points += checkLights(lit, lights.getBonusLights(), true) * 3;
		}
		//Controlamos cuantos rebotes haremos y si alguno de ellos nos hará pasar de nuevo por la zona de luces.
		int curAction = 0;
		do {
			if (curAction == 4) {
				lit = getRandomNumber(2);
				if (Boolean.TRUE.equals(!lights.getBonusLights().get(lit))) {
					points += checkLights(lit, lights.getBonusLights(), true) * 3;
				}
			} else {
				points += 300;
			}
			curAction = getRandomNumber(4);
		} while (curAction != 0);
		LOG.debug("Ramp: {}", points);
		//Sumamos puntos y volvemos a la zona inferior.
		increaseAction(points);
		flippers();
	}

	/**
	 * Se gestionan los sets de luces desde aquí.
	 * @param pos
	 */
	public void lights(int pos) {
		int points = 0;
		int lit = getRandomNumber(3);
		if (pos == 0) {
			if (lit != 3 && Boolean.TRUE.equals(!lights.getLeftLights().get(lit))) {
				points += checkLights(lit, lights.getLeftLights(), false);
			}
		} else {
			if (lit != 3 && Boolean.TRUE.equals(!lights.getRightLights().get(lit))) {
				points += checkLights(lit, lights.getRightLights(), false);
			}
		}
		LOG.debug("Lights: {}", points);
		//Sumamos puntos y volvemos a la zona inferior o a la de rebotadores, según corresponda.
		increaseAction(points);
		if(pos == 0) {
			flippers();
		}else {
			bumpers();
		}
	}
	
	/**
	 * Comprobamos si hemos realizado un skill shot (tiro disponible solo en el primer movimiento)
	 */
	public void skillShot() {
		LOG.debug("Skill shot! You earned 10000 points");
		//Sumamos los puntos y volvemos a la zona inferior.
		tempScore = 10000;
		flippers();
	}

	/**
	 * Método que gestiona las paletas inferiores, y controla si perdemos la bola o la golpeamos.
	 */
	public void flippers() {
		if (!autoSave) {
			int chanceToSaveBall = getRandomNumber(8);
			LOG.debug("Flippers: {}", chanceToSaveBall);
			if (chanceToSaveBall == 0) {
				LOG.warn("Ball number {} lost.", roundNumber);
			} else if (chanceToSaveBall > 0 && chanceToSaveBall < 4) {
				slingShot();
			} else {
				int nextAction = getRandomNumber(7) + 1;
				getAction(nextAction);
			}
			//Si el autosave estaba activo, nos salvamos y lo desactivamos.
		} else {
			LOG.info("Autosave was on, ball saved.");
			int nextAction = getRandomNumber(7) + 1;
			getAction(nextAction);
			autoSave = false;
			autoSaveActions = 0;
		}
	}

	/**
	 * Método que controla los rebotadores que hay en la zona inferior de la máquina.
	 */
	public void slingShot() {
		int points = 0;
		int chance = getRandomNumber(2);
		LOG.debug("SlingShot: {}", chance);
		//Controlamos el número de rebotes y vamos sumando los puntos, hasta que la bola caiga.
		while (chance != 0) {
			points += 100;
			chance = getRandomNumber(2);
			LOG.debug("SlingShot: {}", chance);
		}
		tempScore += points * comboMult;
		LOG.debug("Current score for ball {}: {} ", roundNumber, tempScore);
		flippers();
	}

	/**
	 * Comprobación de un set de objetivos, y suma de los puntos correspondientes.
	 * @param hit
	 * @param list
	 * @return int
	 */
	public int checkTargets(int hit, List<Boolean> list) {
		targets.hit(list, hit);
		int points = 200;
		if(targets.checkAllHit(list)) {
			points += 1500;
		}
		return points;
	}

	/**
	 * Comprobación de un set de luces, casi idéntico al de objetivos.
	 * @param lit
	 * @param list
	 * @param bonus
	 * @return int
	 */
	public int checkLights(int lit, List<Boolean> list, boolean bonus) {
		lights.lit(list, lit);
		int points = 200;
		if(lights.checkAllLit(list)) {
			points += 1500;
			//Extra exclusivo de la zona de luces bonus, que activará un booleano.
			//Este sirve para salvarnos automáticamente de una posible pérdida de bola
			//en las siguientes 5 rondas.
			if(bonus) {
				autoSave = true;
				autoSaveActions = 5;
			}
		}
		return points;
	}

	/**
	 * Reseteo de variables con cada bola.
	 */
	public void newBall() {
		comboMult = 1;
		tempScore = 0;
		action = 0;
		currentAction = 1;
		roundNumber++;
		targets.resetAll();
		lights.resetAll();
	}
	
	/**
	 * Añadimos un nuevo jugador y reseteamos los parámetros necesarios.
	 * @param playerNumber
	 */
	public void newPlayer(int playerNumber) {
		Player player = new Player(playerNumber);
		players.add(player);
		roundNumber = 0;
		scoreToBeat = 5000;	
		balls = 3;
		while (balls > 0) {
			round(player);
			balls--;
		}
	}

	/**
	 * Método que controla la suma de puntos, el autoSave, y el multiplicador de combos.
	 * @param points
	 */
	public void increaseAction(int points) {
		tempScore += points * comboMult;
		currentAction++;
		if (currentAction % 3 == 0) {
			comboMult += 0.2f;
		}
		if(autoSave) {
			autoSaveActions--;
			if(autoSaveActions <= 0) {
				autoSave = false;
			}
		}
		LOG.debug("Current score for ball {}: {} ", roundNumber, tempScore);
	}
	
	/**
	 * Método para obtener la puntuación de cada jugador, por bola y total.
	 */
	public void getScore() {
		for (Player player : players) {
			int playerNumb = player.getPlayerNumber();
			LOG.info("Player number {}", playerNumb);
			totalScore = 0;
			for (Ball ball : player.getPlayerBalls()) {
				LOG.info("Ball number {}, score: {}.", ball.getNumber(), ball.getScore());
				totalScore += ball.getScore();
			}
			LOG.info("Player {} total score: {}.", playerNumb, totalScore);
		}
	}

	/**
	 * Método para calcular números aleatorios.
	 * @param max
	 * @return int
	 */
	public int getRandomNumber(int max) {
		return ran.nextInt(max + 1);
	}
}
