package serveur;

public class Helper {

    /**
     * retourne un tableau de string avec la derniere partie du path + le reste du chemin
     * @param string
     * @return
     */
    public static String[] splitLastPart(String string) {
        String last = string.substring(string.lastIndexOf('/') + 1);
        String pathWithoutLast = string.substring(0, string.lastIndexOf("/"));
        String[] cars = {pathWithoutLast, last};

        return cars;
    }

}
