package uqac;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Objet commande
 */
public class Commande implements Serializable {

	private ArrayList<String> commandes = new ArrayList<>();
	private String resultat;

	/**
	 * Instantiates a new Commande.
	 *
	 * @param commande the commande
	 */
	public Commande(String commande){
		for(String str : commande.split("#")){
			commandes.add(str);
		}
		resultat = "";
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
	 * Set resultat commandes.
	 *
	 * @param resultat the commandes
	 */
	public void setResultatCommande(String resultat){
		this.resultat = resultat;
	}

	/**
	 * Get resultat_commande string.
	 *
	 * @return the string of resultat_commande
	 */
	public String getResultatCommande(){
		return this.resultat;
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

	/**
	 * Get 3 string.
	 *
	 * @return the string
	 */
	public String get3(){
		if (commandes.size() > 3) {
			return commandes.get(3);
		}
		return null;
	}


}
