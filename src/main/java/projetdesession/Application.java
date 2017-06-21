package projetdesession;

import net.sf.json.JSONObject;
import java.text.SimpleDateFormat;
import domaine.OrdreProfessionnel;
import domaine.OrdreProfessionnelFactory;
import Stats.Statistiques;
import Fondation.JSONUtils;
import Fondation.LigneDeCommande;
import Fondation.TypeAction.Action;

public class Application {

    public static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    public final static String STATS_REINITIALISEES = "Le fichier de statistiques a été réinitialisé.";
    public final static String FICHIER_INVALIDE = "Le fichier d'entrée est invalide, le cycle n'est pas complet. ";

    public static void main(String[] args) {
        Action action;
        LigneDeCommande ligneDeCommande = new LigneDeCommande();
        ligneDeCommande.validerParam(args);
        action = ligneDeCommande.getAction();

        entreprendreAction(action, args);
    }

    private static void entreprendreAction(Action action, String[] args) {
        switch (action) {
        case VALIDER_DECLARATION:
            formatDate.setLenient(false);
            JSONObject declaration = JSONUtils.jsonToObject(args[0]);
            faireValidations(args, declaration);
            break;
        case AFFICHER_STATISTIQUES:
            Statistiques.afficherStatistique();
            break;
        case REINITIALISER_STATISTIQUES:
            Statistiques.creerFichierStatsVide();
            System.out.println(STATS_REINITIALISEES);
            break;
        default:
            break;
        }
    }

    private static void faireValidations(String[] args, JSONObject declaration) {
        JSONObject resultat = null;
        try {
            resultat = validerDeclaration(declaration);
        } catch (Exception e) {
            resultat = faireJSONErreur();
            declaration.put("invalide", true);
            System.out.println(FICHIER_INVALIDE);
        } finally {
            JSONUtils.objectToJson(args[1], resultat);
            Statistiques.compilerStats(declaration);
        }
    }
    
    private static JSONObject validerDeclaration(JSONObject declaration) throws Exception {
        ValiderJSON.validerFichier(declaration);
        OrdreProfessionnel ordre = new OrdreProfessionnelFactory().creerOrdreProfessionnel(declaration);
        ordre.validerCycle();
        return declaration.getJSONObject("resultat");
    }
        
    private static JSONObject faireJSONErreur() {
        JSONObject jsonErreur = new JSONObject();

        jsonErreur.put("complet", false);
        jsonErreur.put("erreurs", FICHIER_INVALIDE);
        
        return jsonErreur;
    }
}
