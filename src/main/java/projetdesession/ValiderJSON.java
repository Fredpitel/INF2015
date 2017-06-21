package projetdesession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ValiderJSON {
    public static class fichierInvalideException extends Exception{}
    
    public static void validerFichier(JSONObject declaration) throws fichierInvalideException {
        if (!(validerChamps(declaration)
              && validerDates(declaration.getJSONArray("activites"))
              && validerHeures(declaration)
              && validerDescriptions(declaration.getJSONArray("activites")))) {
            throw new fichierInvalideException();
        }
    }

    public static boolean validerChamps(JSONObject declaration) {
        return declaration.has("ordre")
               && declaration.has("nom")
               && declaration.has("prenom")
               && declaration.has("sexe")
               && declaration.has("numero_de_permis")
               && declaration.has("cycle")
               && ((declaration.getString("ordre").equals("architectes") && declaration.has("heures_transferees_du_cycle_precedent"))
                   || (!declaration.getString("ordre").equals("architectes") && !declaration.has("heures_transferees_du_cycle_precedent")))
               && declaration.has("activites")
               && validerChampsActivites(declaration.getJSONArray("activites"));
    }

    public static boolean validerChampsActivites(JSONArray activites) {
        for (int i = 0; i < activites.size(); i++) {
            JSONObject activite = activites.getJSONObject(i);
            if (!activite.containsKey("description") || !activite.containsKey("categorie")
                || !activite.containsKey("heures") || !activite.containsKey("date")) {
                return false;
            }
        }

        return true;
    }
    
    public static boolean validerDates(JSONArray activites) {
        for (int i = 0; i < activites.size(); i++) {
            if (activites.getJSONObject(i).getString("date").length() != 10) {
                return false;
            }
        }

        return true;
    }

    public static boolean validerHeures(JSONObject declaration) {
        Double heuresTransferees = 0.0;

        if (declaration.has("heures_transferees_du_cycle_precedent")) {
            heuresTransferees = Double.parseDouble(declaration.getString("heures_transferees_du_cycle_precedent"));
        }

        return heuresTransferees >= 0.00 && Math.floor(heuresTransferees) == heuresTransferees && validerHeuresActivites(declaration.getJSONArray("activites"));
    }

    public static boolean validerHeuresActivites(JSONArray activites) {
        for (int i = 0; i < activites.size(); i++) {
            JSONObject activite = activites.getJSONObject(i);
            Double heures = Double.parseDouble(activite.getString("heures"));
            if (heures < 0.00 || (Math.floor(heures) != heures)) {
                return false;
            }
        }

        return true;
    }

    public static boolean validerDescriptions(JSONArray activites) {
        for (int i = 0; i < activites.size(); i++) {
            if (activites.getJSONObject(i).getString("description").length() <= 20) {
                return false;
            }
        }

        return true;
    }
}
