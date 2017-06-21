package domaine;

import java.text.ParseException;
import net.sf.json.JSONObject;

public class OrdrePsychologues extends OrdreProfessionnel {

    final double MIN_HEURES_COURS = 25.0;
    final double MAX_HEURES_CONFERENCE = 15.00;
    final String PATTERN_NUMERO_DE_PERMIS = "^[0-9]{5}-[0-9]{2}$";
    
    public OrdrePsychologues(JSONObject declaration) throws ParseException {
        super(declaration);
    }
    
    @Override
    public String getPatternNumeroDePermis() {
        return PATTERN_NUMERO_DE_PERMIS;
    }
    
    @Override
    public Cycle creerCycle(String cycle) throws ParseException {
        switch (cycle) {
        case "2012-2016":
            return new Cycle("2012-01-01", "2017-01-01", 90.0);

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
        
        if(!validerNbHeuresMinimumParCategorie("cours", MIN_HEURES_COURS)){
            res = false;
        }
        if(!validerNbHeuresMaximumParCategorie("conférence", MAX_HEURES_CONFERENCE)) {
            res = false;
        }
        
        return res;
    }
}