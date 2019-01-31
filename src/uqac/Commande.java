package uqac;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Objet commande
 */
public class Commande implements Serializable {

	private ArrayList<String> commandes = new ArrayList<>();

	/**
	 * Instantiates a new Commande.
	 *
	 * @param commande the commande
	 */
	public Commande(String commande){
		for(String str : commande.split("#")){
			commandes.add(str);
		}
	}

	/**
	 * Gets commandes.
	 *
	 * @return the commandes
	 */
	public ArrayList<String> getCommandes() {
		return commandes;
	}

	/**
	 * Sets commandes.
	 *
	 * @param commandes the commandes
	 */
	public void setCommandes(ArrayList<String> commandes) {
		this.commandes = commandes;
	}

	/**
	 * Get 0 string.
	 *
	 * @return the string
	 */
	public String get0(){
		return commandes.get(0);
	}

	/**
	 * Get 1 string.
	 *
	 * @return the string
	 */
	public String get1(){
		return commandes.get(1);
	}

	/**
	 * Get 2 string.
	 *
	 * @return the string
	 */
	public String get2(){
		if (commandes.size() > 2) {
			return commandes.get(2);
		}
		return null;
	}


}
