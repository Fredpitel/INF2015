package domaine;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import projetdesession.Application;

public abstract class OrdreProfessionnel {
    
    final static public List<String> ORDRES_PROFESSIONNELS = Arrays.asList("architectes", "psychologues", "geologues", "podiatres");
    final static public List<String> CATEGORIES_RECONNUES = Arrays.asList("cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée", "présentation",
                                                           "groupe de discussion", "projet de recherche", "rédaction professionnelle");
    
    Map<String, Double> tableauCategories = new HashMap();
    JSONObject declaration;
    public Cycle cycle;
    
    public OrdreProfessionnel(JSONObject declaration) throws ParseException {
        this.declaration = declaration;
        initialiserOrdre();
    }
    
    public final void initialiserOrdre() throws ParseException {
        declaration.put("numero_permis_valide", true);
        declaration.put("invalide", false);
        declaration.put("cycle_invalide", false);
        JSONObject resultat = new JSONObject();
        resultat.put("complet", true);
        resultat.put("erreurs", new JSONArray());
        declaration.put("resultat", resultat);
        cycle = creerCycle(declaration.getString("cycle"));
    }

    public void validerCycle() throws ParseException {
        validerNumeroDePermis(declaration.getString("numero_de_permis"), getPatternNumeroDePermis());
        validerSexe(declaration.getInt("sexe"));
        if(cycle != null){
            validerActivites();
        } else {
            declaration.replace("cycle_invalide", true);
        }
    }
    
    public void validerActivites() throws ParseException {
        initialiserActivites(declaration.getJSONArray("activites"));
        validerDates(declaration.getJSONArray("activites"), cycle);
        validerCategories(declaration.getJSONArray("activites"));
        creerMapCategories(declaration.getJSONArray("activites"));
        gestionDesHeuresParCategorie();
        validerNbHeuresTotales();
    }
    
    abstract Cycle creerCycle(String cycle) throws ParseException;
    
   /*
    * Insère une valeur "valide" à chaque activité et l'initialise à true
    */ 
    protected void initialiserActivites(JSONArray activites){
        for (int i = 0; i < activites.size(); i++) {
            activites.getJSONObject(i).put("valide", true);
        }
    }
    
    public boolean validerDates(JSONArray activites, Cycle cycle) throws ParseException {
        boolean res = true;
        
        for (int i = 0; i < activites.size(); i++) {
            Date date = Application.formatDate.parse(activites.getJSONObject(i).getString("date"));
            if (date.before(cycle.dateDebut) || date.after(cycle.dateFin)) {
                declaration.getJSONObject("resultat").getJSONArray("erreurs").add("La date pour l'activité " + activites.getJSONObject(i).getString("description") + " est hors cycle. L'activité sera ignorée.");
                activites.getJSONObject(i).replace("heures", 0.00);
                activites.getJSONObject(i).replace("valide", false);
                res = false;
            }
        }
        
        return res;
    }

    public boolean validerCategories(JSONArray activites) {
        boolean res = true;
        
        for (int i = 0; i < activites.size(); i++) {
            String categorie = activites.getJSONObject(i).getString("categorie");

            if (!CATEGORIES_RECONNUES.contains(categorie)) {
                declaration.getJSONObject("resultat").getJSONArray("erreurs").add("La catégorie pour l'activité " + activites.getJSONObject(i).getString("description") + " n'est pas reconnue. L'activité sera ignorée.");
                activites.getJSONObject(i).replace("valide", false);
                res = false;
            }
        }
        
        return res;
    }

    public boolean validerNumeroDePermis(String numeroDePermis, String pattern){
        boolean res = true;
        
        Pattern pat = Pattern.compile(pattern);
        Matcher match = pat.matcher(numeroDePermis);

        if(!match.find()){
            declaration.getJSONObject("resultat").getJSONArray("erreurs").add("Le numéro de permis est invalide");
            declaration.getJSONObject("resultat").replace("complet", false);
            declaration.replace("numero_permis_valide", false);
            res = false;
        }
        
        return res;
    }
    
    abstract String getPatternNumeroDePermis();
    
    
    public boolean validerSexe(int sexe) {
        boolean res = true;
        
        if(sexe > 2 || sexe < 0) {
            declaration.getJSONObject("resultat").getJSONArray("erreurs").add("Le sexe est invalide");
            declaration.getJSONObject("resultat").replace("complet", false);
            res = false;
        }
        
        return res;
    }
        
    public void creerMapCategories(JSONArray activites){
        for (String categorie : CATEGORIES_RECONNUES) {
            tableauCategories.put(categorie, calculerTotalHeuresParCategorie(activites, categorie));
        }
    }
    
    public double calculerTotalHeuresParCategorie(JSONArray activites, String categorie) {
        double total = 0.00;

        for (int i = 0; i < activites.size(); i++) {
            if (activites.getJSONObject(i).getString("categorie").equals(categorie)) {
                total += activites.getJSONObject(i).getDouble("heures");
            }
        }

        return total;
    }
    
    abstract boolean gestionDesHeuresParCategorie();
    
    public boolean validerNbHeuresMaximumParCategorie(String categorie, double nbHeuresMax){
        boolean res = true;
        
        if (tableauCategories.get(categorie) > nbHeuresMax){
            tableauCategories.replace(categorie, nbHeuresMax);
            res = false;
        }
        
        return res;
    }
    
    
    public boolean validerNbHeuresMinimumParCategorie(String categorie, double nbHeuresMin){
        boolean res = true;
        
        if (tableauCategories.get(categorie) < nbHeuresMin) {
            declaration.getJSONObject("resultat").getJSONArray("erreurs").add("Il manque " + (nbHeuresMin - tableauCategories.get(categorie)) + " heures de formations dans la catégorie " + categorie + " pour compléter le cycle");
            declaration.getJSONObject("resultat").replace("complet", false);
            res = false;
        }
        
        return res;
    }

    public boolean validerNbHeuresTotales() {
        double total = calculerNbHeuresTotales();
        boolean res = true;
        
        if (total < cycle.minHeuresTotales) {
            declaration.getJSONObject("resultat").getJSONArray("erreurs").add("Il manque " + (cycle.minHeuresTotales - total) + " heures de formation pour compléter le cycle.");
            declaration.getJSONObject("resultat").replace("complet", false);
            res = false;
        }
        
        return res;
    }
    
    /*
     * Calcule le nombre d'heures totales pour le cycle.
     */
    public double calculerNbHeuresTotales() {
        double total = 0.00;
        for(Map.Entry<String, Double> categorie : tableauCategories.entrySet()){
            total += categorie.getValue();
        }
        
        return total;
    }
}
