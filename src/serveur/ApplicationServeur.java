package serveur;

import java.io.BufferedReader;
import uqac.Commande;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationServeur {

    public String PROJECT_DIR = System.getProperty("user.dir");
    private ServerSocket socket_server;
    private Socket socket;

    /**
     * prend le numéro de port, crée un SocketServer sur le port
     */
    public ApplicationServeur(int port) {
        try {
            socket_server = new ServerSocket(port);
            aVosOrdres();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Se met en attente de connexions des clients. Suite aux connexions, elle
     * lit ce qui est envoyé à travers la Socket, recrée l’objet uqac.Commande
     * envoyé par le client, et appellera traiterCommande(uqac.Commande
     * uneCommande)
     */
    public void aVosOrdres() {
        Runnable run_server = new Runnable() {
            public void run() {
                System.out.println("Lancement du serveur...");
                while (true) {
                    try {
                        socket = socket_server.accept(); // Un client se connecte on l'accepte

                        System.out.println("Le Serveur a accepte la connexion d'un client");
                        
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        Commande commande = (Commande) in.readObject();
                        traiteCommande(commande);
                        
                        System.out.println("Le Serveur a traité la commande");
                        
                        String resultat_commande = "Test pour l'instant";
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject("Le serveur renvoie le resultat de la commande :"+resultat_commande);
                        out.flush();

                        in.close();
                        out.close();
                        socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ApplicationServeur.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        Thread thread_server = new Thread(run_server);
        thread_server.start();
        System.out.println("Le serveur est prêt !");
    }

    /**
     * prend uneCommande dument formattée, et la traite. Dépendant du type de
     * commande, elle appelle la méthode spécialisée
     */
    public void traiteCommande(Commande uneCommande) {
        // TODO : do something
    }

    /**
     * traiterLecture : traite la lecture d’un attribut. Renvoies le résultat
     * par le socket
     */
    public void traiterLecture(Object pointeurObjet, String attribut) {
        // TODO : do something
    }

    /**
     * traiterEcriture : traite l’écriture d’un attribut. Confirmes au client
     * que l’écriture s’est faite correctement.
     */
    public void traiterEcriture(Object pointeurObjet, String attribut, Object valeur) {
        // TODO : do something
    }

    /**
     * traiterCreation : traite la création d’un objet. Confirme au client que
     * la création s’est faite correctement.
     */
    public void traiterCreation(Class classeDeLobjet, String identificateur) {
        // TODO : do something
    }

    /**
     * traiterChargement : traite le chargement d’une classe. Confirmes au
     * client que la création s’est faite correctement.
     */
    public void traiterChargement(String nomQualifie) {
        // TODO : do something
    }

    /**
     * traiterCompilation : traite la compilation d’un fichier source java.
     * Confirme au client que la compilation s’est faite correctement. Le
     * fichier source est donné par son chemin relatif par rapport au chemin des
     * fichiers sources.
     */
    public void traiterCompilation(String cheminRelatifFichierSource) {

        // TODO : adapter quand on aura l'objet commande
        String[] chemins = Helper.splitLastPart(cheminRelatifFichierSource);
        String output = PROJECT_DIR + "/src/serveur/classes";
        String command = "javac -d " + output + " -g " + chemins[1];

        try {
            // compiler la classe .java en .class dans le directory output
            Runtime.getRuntime().exec(command, null, new File(chemins[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * traiterAppel : traite l’appel d’une méthode, en prenant comme argument
     * l’objet sur lequel on effectue l’appel, le nom de la fonction à appeler,
     * un tableau de nom de types des arguments, et un tableau d’arguments pour
     * la fonction. Le résultat de la fonction est renvoyé par le serveur au
     * client (ou le message que tout s’est bien passé) /** public void
     * traiterAppel(Object pointeurObjet, String nomFonction, String[] types,
     * Object[] valeurs) {…}
     *
     * /**
     * programme principal. Prend 4 arguments: 1) numéro de port, 2) répertoire
     * source, 3) répertoire classes, et 4) nom du fichier de traces (sortie)
     * Cette méthode doit créer une instance de la classe
     * serveur.ApplicationServeur, l’initialiser puis appeler aVosOrdres sur cet
     * objet
     */
    public static void main(String[] args) {
        ApplicationServeur serveur = new ApplicationServeur(8080);
        // serveur.traiterCompilation(serveur.PROJECT_DIR + "/src/uqac/Produit.java");
    }
}
