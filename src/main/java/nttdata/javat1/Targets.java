package nttdata.javat1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * En esta clase controlaremos el estado de cada objetivo a derribar del tablero.
 *
 */
public class Targets {
	private List<Boolean> topTargets = new ArrayList<>(Arrays.asList(new Boolean[3]));
	private List<Boolean> bottomTargets = new ArrayList<>(Arrays.asList(new Boolean[3]));
	
	public Targets() {
		super();
		resetAll();
	}

	public List<Boolean> getTopTargets() {
		return topTargets;
	}

	public void setTopTargets(List<Boolean> topTargets) {
		this.topTargets = topTargets;
	}

	public List<Boolean> getBottomTargets() {
		return bottomTargets;
	}

	public void setBottomTargets(List<Boolean> bottomTargets) {
		this.bottomTargets = bottomTargets;
	}	
	
	public void reset(List<Boolean> listToReset) {
		Collections.fill(listToReset, false);
	}
	
	public void resetAll() {
		Collections.fill(topTargets, false);
		Collections.fill(bottomTargets, false);
	}
	
	/**
	 * Método para derribar un objetivo al contacto con la bola.
	 * @param targetHit
	 * @param pos
	 */
	public void hit(List<Boolean> targetHit, int pos) {
		targetHit.set(pos, true);
	}
	
	/**
	 * Método para comprobar si un set de objetivos ha sido completamente derribado y reiniciarlos.
	 * @param targetHit
	 * @return boolean
	 */
	public boolean checkAllHit(List<Boolean> targetHit) {
		boolean allHit = false;
		if(!targetHit.contains(false)) {
			reset(targetHit);
			allHit = true;
		}
		return allHit;
	}
}
