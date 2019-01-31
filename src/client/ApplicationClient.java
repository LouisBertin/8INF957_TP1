package client;

import uqac.Commande;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationClient {

    /**
     * prend le fichier contenant la liste des commandes, et le charge dans une
     * variable du type uqac.Commande qui est retournée
     */
    public Commande saisisCommande(ArrayList<String> commandes) {
        for(String commande : commandes){
            System.out.println(commande);
        }
        return null;
    }

    /**
     * initialise : ouvre les différents fichiers de lecture et écriture
     */
    public void initialise(String fichCommandes, String fichSortie) {
              
    }

    /**
     * prend une uqac.Commande dûment formatée, et la fait exécuter par le
     * serveur. Le résultat de l’exécution est retournée. Si la commande ne
     * retourne pas de résultat, on retourne null. Chaque appel doit ouvrir une
     * connexion, exécuter, et fermer la connexion. Si vous le souhaitez, vous
     * pourriez écrire six fonctions spécialisées, une par type de commande
     * décrit plus haut, qui seront appelées par traiteCommande(uqac.Commande
     * uneCommande)
     */
    public Object traiteCommande(Commande uneCommande) {
        Socket socket;
        try {
            socket = new Socket("localhost", 8080);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Le serveur a cree les flux d'entrées/sorties");

            out.writeObject(uneCommande);
            out.flush();

            System.out.println("Le Client a émit une commande");

            Object objetRecu = in.readObject();
            String resultat = (String) objetRecu;

            System.out.println("Le client a recu le resultat de la commande : " + resultat);

            in.close();
            out.close();
            socket.close();

            return resultat;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ApplicationClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * cette méthode vous sera fournie plus tard. Elle indiquera la séquence
     * d’étapes à exécuter pour le test. Elle fera des appels successifs à
     * saisisCommande(BufferedReader fichier) et traiteCommande(uqac.Commande
     * uneCommande).
     */
    public void scenario(String fichCommandes) {
        //Ouvre le fichier des commandes et récupère le texte:
        try {
            ArrayList<String> commandes = (ArrayList<String>) Files.readAllLines(Paths.get(fichCommandes));
            System.out.println(commandes);
            for(String commande : commandes){
                traiteCommande(new Commande(commande));
            }
        } catch (IOException ex) {
            Logger.getLogger(ApplicationClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    /**
     * programme principal. Prend 4 arguments: 1) “hostname” du serveur, 2)
     * numéro de port, 3) nom fichier commandes, et 4) nom fichier sortie. Cette
     * méthode doit créer une instance de la classe client.ApplicationClient,
     * l’initialiser, puis exécuter le scénario
     */
    public static void main(String[] args) {
        // TODO : do something
        ApplicationClient client = new ApplicationClient();
        client.scenario("inputs/commandes.txt");
    }
}
