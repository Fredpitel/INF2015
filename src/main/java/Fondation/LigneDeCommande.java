/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fondation;

import Fondation.TypeAction.Action;

/**
 *
 * @author fbeaudoin
 */
public class LigneDeCommande {

    private Action action = Action.INVALIDE;

    public void validerParam(String[] args) {
        if (args.length == 1) {
            choisirAction(args);
        } else if (args.length == 2) {
            action = Action.VALIDER_DECLARATION;
        } else {
            afficherUsageLigneDeCommande();
            System.exit(1);
        }
    }

    public Action getAction() {
        return action;
    }

    protected void choisirAction(String[] args) {
        if (args[0].equals("-S")) {
            action = Action.AFFICHER_STATISTIQUES;
        } else if (args[0].equals("-SR")) {
            action = Action.REINITIALISER_STATISTIQUES;
        } else {
            afficherUsageLigneDeCommande();
            System.exit(1);
        }
    }

    protected void afficherUsageLigneDeCommande() {
        System.out.print("Ligne de commande invalide. Utiliser le formatage suivant : \n");
        System.out.print("java -jar ProjetDeSession.jar FichierEntree.json FichierSortie.json\n");
        System.out.print("java -jar ProjetDeSession.jar -S\n");
        System.out.print("java -jar ProjetDeSession.jar -SR\n");
    }
}
