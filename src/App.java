package src;

import src.service.AutoDeploy;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("DEPLOY START\n");

        AutoDeploy.run();

        System.out.println("\nDEPLOY ENDED");
    }
}
