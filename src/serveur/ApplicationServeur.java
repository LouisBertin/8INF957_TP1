package serveur;

import uqac.Commande;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;

public class ApplicationServeur {

    private ServerSocket socket_server;
    private Socket socket;
    private String resultat_commande;
    Class c = null;
    Hashtable<String, Object> objects = new Hashtable();

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
        System.out.println("Le serveur est prêt !");
        while (true) {
            try {
                socket = socket_server.accept(); // Un client se connecte on l'accepte

                System.out.println("Le Serveur a accepte la connexion d'un client");

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                System.out.println("Le serveur a cree les flux d'entrées/sorties");

                Commande commande = (Commande) in.readObject();
                //System.out.println(commande.get0());

                traiteCommande(commande);

                System.out.println("Le Serveur a traité la commande et l'envoie");

               

                out.writeObject(resultat_commande);
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

    /**
     * prend uneCommande dument formattée, et la traite. Dépendant du type de
     * commande, elle appelle la méthode spécialisée
     */
    public void traiteCommande(Commande commande) {
        if (commande instanceof Commande && commande.get0() != null) {
            switch (commande.get0()) {
                case "compilation":
                    // TODO : le prof ne gère pas le troisième paramètres ? :thinking:
                    traiterCompilation(commande.get1());
                    break;
                case "chargement":
                    traiterChargement(commande.get1());
                    break;
                case "creation":
                    try {
                        Class currentClass = Class.forName(commande.get1());
                        traiterCreation(currentClass, commande.get2());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "ecriture":
                    try {
                        traiterEcriture(objects.get(commande.get1()), commande.get2(), commande.get3());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                case "lecture":
                    try {
                        traiterLecture(objects.get(commande.get1()), commande.get2());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                case "fonction":
                    // TODO : implement method
                    break;
            }
        }
    }

    /**
     * traiterLecture : traite la lecture d’un attribut. Renvoies le résultat
     * par le socket
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public void traiterLecture(Object pointeurObjet, String attribut) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method[] methodes = c.getMethods();
        Field[] champs = c.getDeclaredFields();
        for (Field champ : champs) {
            int mod = champ.getModifiers();
            if (champ.getName().equals(attribut)) {
                if (Modifier.isPublic(mod)) {
                    champ.get(pointeurObjet);
                } else {
                    char[] char_table = attribut.toCharArray();
                    char_table[0] = Character.toUpperCase(char_table[0]);
                    String getter = new String(char_table);
                    getter = "get" + getter;
                    for (Method m : methodes) {
                        if (m.getName().equals(getter)) {
                            Object r = m.invoke(pointeurObjet);
                            System.out.println(r);
                        }
                    }
                }
            }
            break;
        }
    }

    /**
     * traiterEcriture : traite l'écriture d'un attribut. Confirmes au client
     * que l'écriture s'est faite correctement.
     *
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public void traiterEcriture(Object pointeurObjet, String attribut, Object valeur) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method[] methodes = c.getMethods();
        Field[] champs = c.getDeclaredFields();
        for (Field champ : champs) {
            int mod = champ.getModifiers();
            if (champ.getName().equals(attribut)) {
                if (Modifier.isPublic(mod)) {
                    champ.set(pointeurObjet, valeur);
                } else {
                    char[] char_table = attribut.toCharArray();
                    char_table[0] = Character.toUpperCase(char_table[0]);
                    String setter = new String(char_table);
                    setter = "set" + setter;
                    for (Method m : methodes) {
                        if (m.getName().equals(setter)) {
                            Object r = m.invoke(pointeurObjet, valeur);
                            System.out.println(attribut + " modifié");
                        }
                    }
                }
            }
            break;
        }

    }

    /**
     * traiterCreation : traite la création d'un objet. Confirme au client que
     * la création s'est faite correctement.
     *
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void traiterCreation(Class classeDeLobjet, String identificateur) {
        try {
            for (Constructor<?> cnst : classeDeLobjet.getConstructors()) {
                if (cnst.getParameterCount() > 0) {
                    Constructor<?> constructor = classeDeLobjet.getConstructor(cnst.getParameterTypes());
                    Object instance = constructor.newInstance(identificateur);

                    System.out.println("Instance créée : " + instance);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * traiterChargement : traite le chargement d’une classe. Confirmes au
     * client que la création s’est faite correctement.
     */
    public void traiterChargement(String nomQualifie) {
        try {
            Class currentClass = Class.forName(nomQualifie);
            //System.out.println("Classe chargée : " + currentClass);
            resultat_commande = "Classe chargée : " + currentClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * traiterCompilation : traite la compilation d’un fichier source java.
     * Confirme au client que la compilation s’est faite correctement. Le
     * fichier source est donné par son chemin relatif par rapport au chemin des
     * fichiers sources.
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
                System.out.println("Classe compilée : " + filename + ".class");
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        /**
         * DEBUG
         */
        Commande commande = new Commande("creation#uqac.Cours#8inf853");

        //serveur.traiteCommande(commande);
        //serveur.traiteCommande(new Commande("compilation#./src/uqac/Cours.java,./src/uqac/Etudiant.java#/classes"));
        //serveur.traiteCommande(new Commande("chargement#uqac.Cours"));
        //serveur.traiteCommande(new Commande("creation#uqac.Cours#8inf853"));
        //serveur.traiteCommande(new Commande("ecriture#8inf853#titre#Architecture"));
        //serveur.traiteCommande(new Commande("lecture#8inf853#titre"));
    }
}
