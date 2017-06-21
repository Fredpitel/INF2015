package domaine;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

public class OrdrePodiatres extends OrdreProfessionnel {
    
    final Map<String, Double> MAP_CATEGORIES_MINIMUM = new HashMap(); {
        MAP_CATEGORIES_MINIMUM.put("cours", 22.00);     
        MAP_CATEGORIES_MINIMUM.put("projet de recherche", 3.0);
        MAP_CATEGORIES_MINIMUM.put("groupe de discussion", 1.0);
    }
    final String PATTERN_NUMERO_DE_PERMIS = "^[0-9]{5}$";    
    
    public OrdrePodiatres(JSONObject declaration) throws ParseException {
        super(declaration);
    }
    
    @Override
    public String getPatternNumeroDePermis() {
        return PATTERN_NUMERO_DE_PERMIS;
    }

    @Override
    public Cycle creerCycle(String cycle) throws ParseException {
        switch (cycle) {
        case "2013-2016":
            return new Cycle("2013-06-01", "2016-06-01", 60.0);

        default:
            declaration.getJSONObject("resultat").getJSONArray("erreurs").add("Le cycle fourni n'est pas valide.");
            declaration.getJSONObject("resultat").replace("complet", false);
            return null;
        }
    }

    /*
     * Fait les validations des heures des activités qui sont particulières à
     * cet ordre professionnel.
     */
    @Override
    public boolean gestionDesHeuresParCategorie() {
        boolean res = true;
        for(Map.Entry<String, Double> categorie : MAP_CATEGORIES_MINIMUM.entrySet()){
            if(!validerNbHeuresMinimumParCategorie(categorie.getKey(), categorie.getValue())) {
                res = false;
            }
        }
        
        return res;
    }
}
