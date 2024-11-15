import java.util.Random;

public class bataille {
    public static int[][] gridComp = new int[10][10];
    public static int[][] gridPlay = new int[10][10];
    public static int[] taillesBateaux = { 5, 4, 3, 3, 2 };
    public static String[] nomsBateaux = { "porte-avions", "croiseur", "contre-torpilleurs", "sous-marin",
            "torpilleur" };

    public static Random rand = new Random();

    public static int randRange(int a, int b) {
        return rand.nextInt(b - a + 1) + a;
    }

    public static boolean posOk(int[][] grille, int l, int c, int d, int t) {
        for (int i = 0; i < t; i++) {
            if ((d == 1 && (c + t > grille.length || grille[l][c + i] != 0))
                    || (d == 2 && (l + t > grille.length || grille[l + i][c] != 0))) {
                return false;
            }
        }
        return true;
    }

    public static String readString() {
        String s = System.console().readLine();
        while (s.equals("")) {
            s = System.console().readLine();
        }
        return s;
    }

    public static boolean isInt(String s) {
        return s.matches("\\d+");
    }

    public static int readInt() {
        while (true) {
            String s = readString();
            if (isInt(s)) return Integer.parseInt(s);
        }
    }

    public static void initGridComp() {
        for (int i = 0; i < 5; i++) {
            int l = randRange(0, 9);
            int c = randRange(0, 9);
            int d = randRange(1, 2);
            while (!posOk(gridComp, l, c, d, taillesBateaux[i])) {
                l = randRange(0, 9);
                c = randRange(0, 9);
                d = randRange(1, 2);
            }
            for (int j = 0; j < taillesBateaux[i]; j++) {
                if (d == 1) {
                    gridComp[l][c + j] = i + 1;
                } else {
                    gridComp[l + j][c] = i + 1;
                }
            }
        }
    }

    public static void initGridPlay() {
        for (int i = 0; i < 5; i++) {
            prtinGrid(gridPlay);
            int l = 10;
            int c = 10;
            int d = 0;
            boolean a = true;
            do {
                a = false;
                while (c > 9) {
                    System.out.print("Donnez la lettre pour le " + nomsBateaux[i] + " : ");
                    String input = readString();
                    String lettres = "ABCDEFGHIJ";
                    for (int j = 0; j < lettres.length(); j++) {
                        if (lettres.charAt(j) == input.charAt(0)) {
                            c = j;
                        }
                    }
                }
                while (l < 0 || l > 9) {
                    System.out.print("Donnez le nombre pour le " + nomsBateaux[i] + " : ");
                    l = readInt() - 1;
                }
                while (d < 1 || d > 2) {
                    System.out.print("Voulez-vous qu’il soit horizontal (1) ou vertical (2) ? ");
                    d = readInt();
                }
                if (!posOk(gridPlay, l, c, d, taillesBateaux[i])) {
                    System.out.println("Erreur : Le "+nomsBateaux[i]+" ne rentre pas dans la grille.");
                    a = true;
                    l = 10;
                    c = 10;
                    d = 0;
                }
            } while (a);
            for (int j = 0; j < taillesBateaux[i]; j++) {
                if (d == 1) {
                    gridPlay[l][c + j] = i + 1;
                } else {
                    gridPlay[l + j][c] = i + 1;
                }
            }
        }
    }

    public static void prtinGrid(int[][] grille) {
        System.out.println("\n    A B C D E F G H I J");
        System.err.println();
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille.length; j++) {
                if (j == 0 && i + 1 < 10) {
                    System.out.print(i + 1 + "   ");
                } else if (j == 0) {
                    System.out.print(i + 1 + "  ");
                }
                System.out.print(grille[i][j] + " ");
            }
            System.out.println();
        }
        System.err.println();
    }

    public static boolean hasDrowned(int[][] grille, int n) {
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == n) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void oneMove(int[][] grille, int l, int c) {
        int x = grille[l][c];
        if (x != 0 && x != 6) {
            grille[l][c] = 6;
            if (hasDrowned(grille, x)) {
                System.out.println(nomsBateaux[x-1]+" coulé !");
            } else {
                System.out.println(nomsBateaux[x-1]+" touché !");
            }
        } else {
            System.out.println("À l’eau !");
        }
        System.out.println();
    }

    public static int[] playComp() {
        int[] r = new int[2];
        r[0] = randRange(0,9);
        r[1] = randRange(0,9);
        return r;
    }
    
    public static boolean isOver(int[][] grille) {
        for (int i = 0; i < 5; i++) {
            if (!hasDrowned(grille, i+1)) {
                return false;
            }
        }
        return true;
    }

    public static void play() {
        initGridComp();
        initGridPlay();
        while (!isOver(gridComp) && !isOver(gridPlay)) {
            // prtinGrid(gridComp);
            int[] comp = playComp();
            System.out.print("\nOrdinateur : ");
            oneMove(gridPlay, comp[0], comp[1]);
            if (!isOver(gridPlay)) {
                prtinGrid(gridPlay);
                int l = 10;
                int c = 10;
                do {
                    System.out.print("Donnez la lettre : ");
                    String input = readString();
                    String lettres = "ABCDEFGHIJ";
                    for (int j = 0; j < lettres.length(); j++) {
                        if (lettres.charAt(j) == input.charAt(0)) {
                            c = j;
                        }
                    }
                } while (c > 9);
                do {
                    System.out.print("Donnez le nombre : ");
                    l = readInt() - 1;
                } while (l < 0 || l > 9);
                System.out.print("\nJoueur : ");
                oneMove(gridComp, l, c);
                if (isOver(gridComp)) {
                    System.out.println("Le joueur a gagné !");
                }
            } else {
                System.out.println("L'ordinateur a gagné !");
            }
        }
    }

    public static void main(String[] args) {
        play();
    }
}