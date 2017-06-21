package Stats;

import domaine.OrdreProfessionnel;
import Fondation.JSONUtils;
import java.io.FileNotFoundException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import Fondation.Utf8File;
import java.util.Arrays;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;

public class Statistiques {
    final static String NOM_FICHIER_STATS = "Statistiques.json";
    final static List<String> STATISTIQUES_CONSERVEES = Arrays.asList("nb_total_declarations_traitees", "nb_total_declarations_completes", "nb_total_declarations_incompletes_ou_invalides",
                                                                      "nb_total_declarations_hommes", "nb_total_declarations_femmes", "nb_total_declarations_sexe_inconnu", 
                                                                      "nb_total_declarations_permis_invalide","nb_total_activites_valides", "nb_total_activites_valides_par_categorie", 
                                                                      "nb_total_declaration_valides_completes_par_ordre", "nb_total_declaration_valides_incompletes_par_ordre");
    
    public static JSONObject compilerStats(JSONObject declaration){
        JSONObject stats = chargerFichierStats();
        calculerStatistiques(declaration, stats);
        sauvegarderFichierStats(stats);
        return stats;
    }
    
    public static JSONObject chargerFichierStats(){
        JSONObject stats;
        
        try {
            String jsonString = Utf8File.loadFileIntoString(NOM_FICHIER_STATS);
            stats = (JSONObject) JSONSerializer.toJSON(jsonString);
        } catch (FileNotFoundException e) {
            stats = creerFichierStatsVide();
        }
        
        return stats;
    }
    
    public static JSONObject creerFichierStatsVide(){
        JSONObject stats = new JSONObject();
        
        for(int i = 0; i < STATISTIQUES_CONSERVEES.size() - 3; i++){
            stats.put(STATISTIQUES_CONSERVEES.get(i), 0);
        }
        creerListesStatsVide(stats);
        sauvegarderFichierStats(stats);
        
        return stats;
    }
    
    public static void creerListesStatsVide(JSONObject stats){
        JSONObject categories = creerListeVide("categorie_", OrdreProfessionnel.CATEGORIES_RECONNUES);
        stats.put("nb_total_activites_valides_par_categorie", categories);
        JSONObject ordresComplet = creerListeVide("declaration_completes_", OrdreProfessionnel.ORDRES_PROFESSIONNELS);
        stats.put("nb_total_declaration_valides_completes_par_ordre", ordresComplet);
        JSONObject ordresIncomplet = creerListeVide("declaration_incompletes_", OrdreProfessionnel.ORDRES_PROFESSIONNELS);
        stats.put("nb_total_declaration_valides_incompletes_par_ordre", ordresIncomplet);  
    }
    
    public static JSONObject creerListeVide(String chaine, List<String> liste) {
        JSONObject statsInitiales = new JSONObject();
        
        for(String item: liste ){
            statsInitiales.put((chaine + item), 0);
        }
 
        return statsInitiales;
    }
    
    public static void sauvegarderFichierStats(JSONObject stats){
        JSONUtils.objectToJson(NOM_FICHIER_STATS, stats);
    }
    
    public static void afficherStatistique() {
        JSONObject stats = chargerFichierStats();
        String chaineStats = stats.toString();
        chaineStats = formatSortieChaine(chaineStats);
        System.out.print(chaineStats + "\n");
    }
    
    // Retire les éléments de formatage à la JSON de la String et indente les catégories
    protected static String formatSortieChaine(String chaine) {
        chaine = chaine.replace("{", "\n");
        chaine = chaine.replace("}", "");
        chaine = chaine.replace(",", "\n");
        chaine = chaine.replace("\"", "");
        chaine = chaine.replace("nb_total_", "nombre total de ");
        chaine = chaine.replace("categorie_", "   - categorie ");
        chaine = chaine.replace("declaration_completes", "   - declaration completes");
        chaine = chaine.replace("declaration_incompletes", "   - declaration incompletes");
        chaine = chaine.replace("_", " ");
        chaine = chaine.replace(":", ": ");
        return chaine;
    }
    
    protected static void calculerStatistiques(JSONObject declaration, JSONObject stats) {
        ajouterTotalDeclarationTraitee(stats);
        ajouterTotalDeclarationCompleteOuIncomplete(declaration, stats);
        if(!declaration.getBoolean("invalide")){
            ajouterTotalDeclarationSelonSexe(declaration, stats);
            ajouterTotalDeclarationPermisInvalide(declaration, stats);
            if(!declaration.getBoolean("cycle_invalide")){
                ajouterTotalActiviteesValides(declaration, stats);
            }
            ajouterTotalDeclarationParOrdre(declaration, stats);
        }
    }
    
    // Version générique pour toutes les statistiques
    protected static void ajouterTotalDeclaration(JSONObject stats, String elementTraite) {
        try {
            int valeurCourante = stats.getInt(elementTraite);
            stats.replace(elementTraite, ++valeurCourante);
        } catch (JSONException e)
        {
            System.out.print("Erreur de format du fichier de statistique :\nLa statistique [" + elementTraite + "] est inexistante dans le fichier de statistique.\n");
        }
    }
    protected static void ajouterTotalDeclarationTraitee(JSONObject stats) {
        ajouterTotalDeclaration(stats, "nb_total_declarations_traitees");
    }
    
    protected static void ajouterTotalDeclarationCompleteOuIncomplete(JSONObject declaration, JSONObject stats) {
        if (!declaration.getBoolean("invalide") && declaration.getJSONObject("resultat").getBoolean("complet")) {
            ajouterTotalDeclaration(stats, "nb_total_declarations_completes");
        } else {
            ajouterTotalDeclaration(stats, "nb_total_declarations_incompletes_ou_invalides");
        }
    }
    
    protected static void ajouterTotalDeclarationSelonSexe(JSONObject declaration, JSONObject stats) {
        int sexe = declaration.getInt("sexe");
        switch (sexe) {
        case 0:
            ajouterTotalDeclaration(stats, "nb_total_declarations_sexe_inconnu");
            break;
        case 1:
            ajouterTotalDeclaration(stats, "nb_total_declarations_hommes");
            break;
        case 2:
            ajouterTotalDeclaration(stats, "nb_total_declarations_femmes");
            break;
        default:
            break;
        }
    }
    
    protected static void ajouterTotalDeclarationPermisInvalide(JSONObject declaration, JSONObject stats) {
        if(!declaration.getBoolean("numero_permis_valide")){
            ajouterTotalDeclaration(stats, "nb_total_declarations_permis_invalide");
        }
    }
    
    protected static void ajouterTotalActiviteesValides(JSONObject declaration, JSONObject stats) {
        JSONArray activites = declaration.getJSONArray("activites");
        JSONObject listeCategorie = stats.getJSONObject("nb_total_activites_valides_par_categorie");
        String categorie = "";
        for (int i = 0; i < activites.size(); i++) {
            if (activites.getJSONObject(i).getBoolean("valide")) {
                ajouterTotalDeclaration(stats, "nb_total_activites_valides");
                categorie = activites.getJSONObject(i).getString("categorie");
                ajouterTotalDeclaration(listeCategorie, ("categorie_" + categorie));
            }
        }
    }
    
    protected static void ajouterTotalDeclarationParOrdre(JSONObject declaration, JSONObject stats) {
        String ordre = declaration.getString("ordre");
        if(declaration.getJSONObject("resultat").getBoolean("complet")){
            JSONObject listeOrdresComplet = stats.getJSONObject("nb_total_declaration_valides_completes_par_ordre");
            ajouterTotalDeclaration(listeOrdresComplet, ("declaration_completes_" + ordre));
        } else {
            JSONObject listeOrdresIncomplet = stats.getJSONObject("nb_total_declaration_valides_incompletes_par_ordre");
            ajouterTotalDeclaration(listeOrdresIncomplet, ("declaration_incompletes_" + ordre));
        }
    }
}
