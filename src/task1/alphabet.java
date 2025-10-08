package task1;

public class alphabet {

    static char[] ABC = {'A','B','C','D','E','F','G', 'H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

    public static void check(char ch){
        for (int  i = 0; i < ABC.length; i++) {
            if (ch==ABC[i]){System.out.println("Am gasit litera corecta!" + ABC[i]);}
        }
    }
}
