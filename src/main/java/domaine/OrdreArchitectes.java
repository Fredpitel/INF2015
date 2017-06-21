package domaine;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

public class OrdreArchitectes extends OrdreProfessionnel {

    final Double HEURES_TRANSFERABLES = 7.00;
    final double MIN_HEURES_CATEGORIES_IMPORTANTES = 17.00;
    final List<String> CATEGORIES_IMPORTANTES = Arrays.asList("cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée");
    final Map<String, Double> MAP_CATEGORIES_LIMITEES = new HashMap(); {
        MAP_CATEGORIES_LIMITEES.put("présentation", 23.00);     
        MAP_CATEGORIES_LIMITEES.put("groupe de discussion", 17.00);
        MAP_CATEGORIES_LIMITEES.put("projet de recherche", 23.00);
        MAP_CATEGORIES_LIMITEES.put("rédaction professionnelle", 17.00);
    }
    final String PATTERN_NUMERO_DE_PERMIS = "^[A,T][0-9]{4}$";
    
    public OrdreArchitectes(JSONObject declaration) throws ParseException {
        super(declaration);
    }
    
    @Override
    public String getPatternNumeroDePermis() {
        return PATTERN_NUMERO_DE_PERMIS;
    }
    
    @Override
    public void validerCycle() throws ParseException{
        validerHeuresTransferees(declaration.getDouble("heures_transferees_du_cycle_precedent"));
        super.validerCycle();
    }

    @Override
    public Cycle creerCycle(String cycle) throws ParseException {
        switch (cycle) {
        case "2010-2012":
            return new Cycle("2010-04-01", "2012-07-01", 42.0);

        case "2012-2014":
            return new Cycle("2012-04-01", "2014-04-01", 42.0);

        case "2014-2016":
            return new Cycle("2014-04-01", "2016-04-01", 40.0);

        default:
            declaration.getJSONObject("resultat").getJSONArray("erreurs").add("Le cycle fourni n'est pas valide.");
            declaration.getJSONObject("resultat").replace("complet", false);
            return null;
        }
    }

    public boolean validerHeuresTransferees(double heureTransferees) {
        boolean res = true;
        
        if (heureTransferees < 0.00) {
            declaration.replace("heures_transferees_du_cycle_precedent", 0.00);
            res = false;
        } else if (heureTransferees > HEURES_TRANSFERABLES) {
            declaration.replace("heures_transferees_du_cycle_precedent", HEURES_TRANSFERABLES);
            declaration.getJSONObject("resultat").getJSONArray("erreurs").add("Vous ne pouvez pas transférer plus de " + HEURES_TRANSFERABLES + " heures du cycle précédent.");
            res = false;
        }
        
        return res;
    }
    
    /*
     * Fait les validations des heures des activités qui sont particulières à
     * cet ordre professionnel.
     */
    @Override
    public boolean gestionDesHeuresParCategorie() {
        boolean res = true;
        
        for(Map.Entry<String, Double> categorie : MAP_CATEGORIES_LIMITEES.entrySet()){
            if(!validerNbHeuresMaximumParCategorie(categorie.getKey(), categorie.getValue())) {
                res = false;
            }
        }
        if(!validerNbHeuresActivitesImportantesMinimum(declaration.getJSONArray("activites"), MIN_HEURES_CATEGORIES_IMPORTANTES)) {
            res = false;
        }
        
        return res;
    }
    
    public boolean validerNbHeuresActivitesImportantesMinimum(JSONArray activites, double min) {
        double total = declaration.getDouble("heures_transferees_du_cycle_precedent");
        boolean res = true;
        
        for(String activite : CATEGORIES_IMPORTANTES) {
            total += calculerTotalHeuresParCategorie(activites, activite);
        }

        if (total < min) {
            declaration.getJSONObject("resultat").getJSONArray("erreurs").add("Il manque " + (min - total) + " heures de formations dans une catégorie principale pour compléter le cycle");
            declaration.getJSONObject("resultat").replace("complet", false);
            res = false;
        }
        
        return res;
    }

    /*
     * Calcule le nombre d'heures totales pour le cycle.
     */
    @Override
    public double calculerNbHeuresTotales() {
       double total = super.calculerNbHeuresTotales();
       total += declaration.getDouble("heures_transferees_du_cycle_precedent");
       
       return total;
    }
}