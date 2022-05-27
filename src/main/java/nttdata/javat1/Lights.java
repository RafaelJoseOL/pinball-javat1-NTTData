package nttdata.javat1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

/**
 * En esta clase controlaremos el estado de cada set de luces del tablero.
 *
 */
public class Lights {
	private List<Boolean> leftLights = new ArrayList<>(Arrays.asList(new Boolean[3]));
	private List<Boolean> rightLights = new ArrayList<>(Arrays.asList(new Boolean[3]));	
	private List<Boolean> bonusLights = new ArrayList<>(Arrays.asList(new Boolean[3]));	
	
	public Lights() {
		super();
		resetAll();
	}
	
	public List<Boolean> getLeftLights() {
		return leftLights;
	}
	public void setLeftLights(List<Boolean> leftLights) {
		this.leftLights = leftLights;
	}
	public List<Boolean> getRightLights() {
		return rightLights;
	}
	public void setRightLights(List<Boolean> rightLights) {
		this.rightLights = rightLights;
	}	
	public List<Boolean> getBonusLights() {
		return bonusLights;
	}
	public void setBonusLights(List<Boolean> bonusLights) {
		this.bonusLights = bonusLights;
	}

	public void reset(List<Boolean> listToReset) {
		Collections.fill(listToReset, false);
	}
	
	public void resetAll() {
		Collections.fill(leftLights, false);
		Collections.fill(rightLights, false);
		Collections.fill(bonusLights, false);
	}
	
	/**
	 * Método para encender una luz al contacto con la bola.
	 * @param lightLit
	 * @param pos
	 */
	public void lit(List<Boolean> lightLit, int pos) {
		lightLit.set(pos, true);
	}	
	
	/**
	 * Este método se usa para comprobar si un set de luces ha sido completamente encendido, en cuyo caso retornará 
	 * true y lo reiniciará.
	 * @param targetLit
	 * @return boolean
	 */
	public boolean checkAllLit(List<Boolean> targetLit) {
		boolean allLit = false;
		if(!targetLit.contains(false)) {
			reset(targetLit);
			allLit = true;
		}
		return allLit;
	}
}
