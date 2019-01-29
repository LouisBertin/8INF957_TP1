package uqac;

import java.io.Serializable;
import java.util.ArrayList;


public class Commande implements Serializable {
	
	private ArrayList<String> commandes = new ArrayList<>();
	
	public Commande(String commande){
		for(String str : commande.split("#")){
			commandes.add(str);
		}
	}
	
	public ArrayList<String> getCommandes() {
		return commandes;
	}

	public void setCommandes(ArrayList<String> commandes) {
		this.commandes = commandes;
	}
	
	public String get0(){
		return commandes.get(0);
	}
	
	public String get1(){
		return commandes.get(1);
	}
	
	public String get2(){
		return commandes.get(2);
	}


}
