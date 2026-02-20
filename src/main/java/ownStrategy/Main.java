package ownStrategy;

import ownStrategy.ui.OptionUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OptionUI ui = new OptionUI(sc);
        OptionService serv = new OptionService();
        OptionRepository repo = new OptionRepository();

        OptionController controller = new OptionController(serv, repo, ui);

        controller.start();
    }
}
//chyba za duzo zapytan wyslalem i jest za duzo zapytan- jutra dalszy debug
//spring, zrobic controller, automatyczne odswiezanie/
//np data driven
//JSON do NoSQL(do bazy danych)
