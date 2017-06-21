package projetdesession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ValiderJSONTest {
    JSONObject declarationValide;
    
    @Before
    public void  setUp() {
        declarationValide = new JSONObject();
        declarationValide.put("nom", "Test");
        declarationValide.put("prenom", "Test");
        declarationValide.put("sexe", 0);
        declarationValide.put("ordre", "architectes");
        declarationValide.put("numero_de_permis", "Test");
        declarationValide.put("cycle", "Test");
        declarationValide.put("heures_transferees_du_cycle_precedent", 0);

        JSONArray activites = new JSONArray();
        JSONObject activite1 = new JSONObject();
        activite1.put("description", "Testestestestestestes");
        activite1.put("categorie", "Test");
        activite1.put("heures", 0);
        activite1.put("date", "0000-00-00");
        activites.add(activite1);
        
        JSONObject activite2 = new JSONObject();
        activite2.put("description", "Testestestestestestes");
        activite2.put("categorie", "Test");
        activite2.put("heures", 0);
        activite2.put("date", "0000-00-00");
        activites.add(activite2);
        
        declarationValide.put("activites", activites);
    }
    
    /**
     * Test of validerFichier method, of class ValiderJSON.
     */
    @Test
    public void testValiderFichier() {
        boolean expResult = true;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }

    /**
     * Test of validerChamps method, of class ValiderJSON.
     */
    @Test
    public void testValiderChampsArchitectesValide() {     
        boolean expResult = true;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsGeologuesValide() {
        declarationValide.replace("ordre", "geologues");
        declarationValide.remove("heures_transferees_du_cycle_precedent");
        
        boolean expResult = true;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsPsychologuesValide() {
        declarationValide.replace("ordre", "psychologues");
        declarationValide.remove("heures_transferees_du_cycle_precedent");
        
        boolean expResult = true;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsPodiatresValide() {
        declarationValide.replace("ordre", "podiatres");
        declarationValide.remove("heures_transferees_du_cycle_precedent");
        
        boolean expResult = true;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsNomInvalide() {
        declarationValide.remove("nom");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsPrenomInvalide() {
        declarationValide.remove("prenom");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }

    @Test
    public void testValiderChampsSexeInvalide() {
        declarationValide.remove("sexe");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsOrdreInvalide() {
        declarationValide.remove("ordre");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsNumeroDePermisInvalide() {
        declarationValide.remove("numero_de_permis");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsCycleInvalide() {
        declarationValide.remove("cycle");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsArchitectesInvalide() {
        declarationValide.remove("heures_transferees_du_cycle_precedent");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsGeologuesInvalide() {
        declarationValide.replace("ordre", "geologues");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsPsychologuesInvalide() {
        declarationValide.replace("ordre", "psychologues");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsPodiatresInvalide() {
        declarationValide.replace("ordre", "podiatres");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsActivitesInvalide() {
        declarationValide.remove("activites");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChamps(declarationValide);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of validerChampsActivites method, of class ValiderJSON.
     */
    @Test
    public void testValiderChampsActivitesValide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        
        boolean expResult = true;
        boolean result = ValiderJSON.validerChampsActivites(activites);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsActivitesDescriptionInvalide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.getJSONObject(1).remove("description");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChampsActivites(activites);
        assertEquals(expResult, result);
    }

    @Test
    public void testValiderChampsActivitesCategorieInvalide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.getJSONObject(1).remove("categorie");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChampsActivites(activites);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsActivitesHeuresInvalide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.getJSONObject(1).remove("heures");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChampsActivites(activites);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderChampsActivitesDateInvalide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.getJSONObject(1).remove("date");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerChampsActivites(activites);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of validerDates method, of class ValiderJSON.
     */
    @Test
    public void testValiderDatesValide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        
        boolean expResult = true;
        boolean result = ValiderJSON.validerDates(activites);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderDatesInvalideTropCourt() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.getJSONObject(1).replace("date", "0000-00-0");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerDates(activites);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderDatesInvalideTropLong() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.getJSONObject(1).replace("date", "0000-00-000");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerDates(activites);
        assertEquals(expResult, result);
    }

    /**
     * Test of validerHeures method, of class ValiderJSON.
     */
    @Test
    public void testValiderHeuresValide() {
        boolean expResult = true;
        boolean result = ValiderJSON.validerHeures(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderHeuresInvalideNegatif() {
        declarationValide.replace("heures_transferees_du_cycle_precedent", -1);
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerHeures(declarationValide);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderHeuresInvalideDecimal() {
        declarationValide.replace("heures_transferees_du_cycle_precedent", 0.5);
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerHeures(declarationValide);
        assertEquals(expResult, result);
    }

    /**
     * Test of validerHeuresActivites method, of class ValiderJSON.
     */
    @Test
    public void testValiderHeuresActivites() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        
        boolean expResult = true;
        boolean result = ValiderJSON.validerHeuresActivites(activites);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderHeuresActivitesInvalideNegatif() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.getJSONObject(1).replace("heures", -1);
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerHeuresActivites(activites);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderHeuresActivitesInvalideDecimal() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.getJSONObject(1).replace("heures", 0.5);
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerHeuresActivites(activites);
        assertEquals(expResult, result);
    }

    /**
     * Test of validerDescriptions method, of class ValiderJSON.
     */
    @Test
    public void testValiderDescriptionsValide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        
        boolean expResult = true;
        boolean result = ValiderJSON.validerDescriptions(activites);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testValiderDescriptionsInvalide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.getJSONObject(1).replace("description", "Testestestestesteste");
        
        boolean expResult = false;
        boolean result = ValiderJSON.validerDescriptions(activites);
        assertEquals(expResult, result);
    }
}
