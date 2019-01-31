package serveur;

import uqac.Commande;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class ApplicationServeur {

    public String PROJECT_DIR = System.getProperty("user.dir");

    /**
     * prend le numéro de port, crée un SocketServer sur le port
     */
    public ApplicationServeur (int port) {
        // TODO : do something
    }

    /**
     * Se met en attente de connexions des clients. Suite aux connexions, elle lit
     * ce qui est envoyé à travers la Socket, recrée l’objet uqac.Commande envoyé par
     * le client, et appellera traiterCommande(uqac.Commande uneCommande)
     */
    public void aVosOrdres() {
        // TODO : do something
    }

    /**
     * prend uneCommande dument formattée, et la traite. Dépendant du type de commande,
     * elle appelle la méthode spécialisée
     */
    public void traiteCommande(Commande commande) {
        if(commande instanceof Commande && commande.get0() != null) {
            switch (commande.get0()) {
                case "compilation":
                    // TODO : le prof ne gère pas le troisième paramètres ? :thinking:
                    traiterCompilation(commande.get1());
                    break;
                case "chargement":
                    // TODO : implement method
                    break;
                case "creation":
                    // TODO : implement method
                    break;
                case "ecriture":
                    // TODO : implement method
                    break;
                case "lecture":
                    // TODO : implement method
                    break;
                case "fonction":
                    // TODO : implement method
                    break;
            }
        }
    }

    /**
     * traiterLecture : traite la lecture d’un attribut. Renvoies le résultat par le
     * socket
     */
    public void traiterLecture(Object pointeurObjet, String attribut) {
        // TODO : do something
    }

    /**
     * traiterEcriture : traite l’écriture d’un attribut. Confirmes au client que l’écriture
     * s’est faite correctement.
     */
    public void traiterEcriture(Object pointeurObjet, String attribut, Object valeur) {
        // TODO : do something
    }

    /**
     * traiterCreation : traite la création d’un objet. Confirme au client que la création
     * s’est faite correctement.
     */
    public void traiterCreation(Class classeDeLobjet, String identificateur) {
        // TODO : do something
    }

    /**
     * traiterChargement : traite le chargement d’une classe. Confirmes au client que la création
     * s’est faite correctement.
     */
    public void traiterChargement(String nomQualifie) {
        // TODO : do something
    }

    /**
     * traiterCompilation : traite la compilation d’un fichier source java. Confirme au client
     * que la compilation s’est faite correctement. Le fichier source est donné par son chemin
     * relatif par rapport au chemin des fichiers sources.
     */
    public void traiterCompilation(String cheminRelatifFichierSource) {
        // on check si la string contient plusieurs classes à compiler
        if (cheminRelatifFichierSource.contains(",")) {
            String[] parts = cheminRelatifFichierSource.split(",");
            for (int i = 0; i < parts.length; i++) {
                compileClass(parts[i]);
            }
        } else {
            compileClass(cheminRelatifFichierSource);
        }
    }

    /**
     * compile la classe Java
     *
     * @param classPath chemin de la classe
     */
    private void compileClass(String classPath) {
        // on récupère le  compilateur Java du système
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // on compile
        int result = compiler.run(null, null, null, classPath);

        if (result == 0) {
            // on déplace le fichier compilé dans le répertoire cible
            int index = classPath.lastIndexOf(".");
            String fileWithoutExtension = classPath.substring(classPath.lastIndexOf("/"), index);
            String filename = fileWithoutExtension.replace("/", "");

            try {
                Files.move(Paths.get(classPath.substring(0, index) + ".class"), Paths.get("./src/serveur/classes/" + filename + ".class"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * traiterAppel : traite l’appel d’une méthode, en prenant comme argument l’objet
     * sur lequel on effectue l’appel, le nom de la fonction à appeler, un tableau de nom de
     * types des arguments, et un tableau d’arguments pour la fonction. Le résultat de la
     * fonction est renvoyé par le serveur au client (ou le message que tout s’est bien
     * passé)
     /**
     public void traiterAppel(Object pointeurObjet, String nomFonction, String[] types,
     Object[] valeurs) {…}

     /**
     * programme principal. Prend 4 arguments: 1) numéro de port, 2) répertoire source, 3)
     * répertoire classes, et 4) nom du fichier de traces (sortie)
     * Cette méthode doit créer une instance de la classe serveur.ApplicationServeur, l’initialiser
     * puis appeler aVosOrdres sur cet objet
     */
    public static void main(String[] args) {
        ApplicationServeur serveur = new ApplicationServeur(8080);

        /**
         * DEBUG
         */
        Commande commande = new Commande("compilation#./src/uqac/Cours.java,./src/uqac/Etudiant.java#/classes");
        // serveur.traiteCommande(commande);
    }
}
