package domaine;

import java.text.SimpleDateFormat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import projetdesession.Application;

public class OrdreArchitectesTest {
    JSONObject declarationValide;
    OrdreProfessionnel ordre;
    
    @Before
    public void  before() throws Exception{
        Application.formatDate = new SimpleDateFormat("yyyy-MM-dd");
        declarationValide = new JSONObject();
        declarationValide.put("nom", "Test");
        declarationValide.put("prenom", "Test");
        declarationValide.put("sexe", 0);
        declarationValide.put("ordre", "architectes");
        declarationValide.put("numero_de_permis", "A0000");
        declarationValide.put("cycle", "Test");
        declarationValide.put("heures_transferees_du_cycle_precedent", 4);

        JSONArray activites = new JSONArray();
        JSONObject activite1 = new JSONObject();
        activite1.put("description", "Testestestestestestes");
        activite1.put("categorie", "cours");
        activite1.put("heures", 10);
        activite1.put("date", "2014-00-00");
        activites.add(activite1);
        
        JSONObject activite2 = new JSONObject();
        activite2.put("description", "Testestestestestestes");
        activite2.put("categorie", "cours");
        activite2.put("heures", 3);
        activite2.put("date", "0000-00-00");
        activites.add(activite2);
        
        declarationValide.put("activites", activites);
        ordre = new OrdreProfessionnelFactory().creerOrdreProfessionnel(declarationValide);
    }
    
    @Test
    public void testCreerCycle20142016Valide() throws Exception {
        Cycle cycleObtenu = ordre.creerCycle("2014-2016");
        Cycle cycleAttendu = new Cycle("2014-04-01", "2016-04-01", 40.0);
        assertEquals(cycleObtenu, cycleAttendu);
    }
    
    @Test
    public void testCreerCycle20122014Valide() throws Exception {
        Cycle cycleObtenu = ordre.creerCycle("2012-2014");
        Cycle cycleAttendu = new Cycle("2012-04-01", "2014-04-01", 42.0);
        assertEquals(cycleObtenu, cycleAttendu);
    }
    
    @Test
    public void testCreerCycle20102012Valide() throws Exception {
        Cycle cycleObtenu = ordre.creerCycle("2010-2012");
        Cycle cycleAttendu = new Cycle("2010-04-01", "2012-07-01", 42.0);
        assertEquals(cycleObtenu, cycleAttendu);
    }
    
    @Test
    public void testCreerCycleInvalide() throws Exception {
        Cycle cycleObtenu = ordre.creerCycle("2010-2016");
        assertEquals(cycleObtenu, null);
    }

    @Test
    public void testValiderNumeroDePermisValideA() {
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerNumeroDePermis(declarationValide.getString("numero_de_permis"), ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisValideT() {
        String numeroDePermis = "T0000";
        
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisInvalideLettreInitiale() {
        String numeroDePermis = "B0000";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisInvalideFormat() {
        String numeroDePermis = "AA000";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisTropCourt() {
        String numeroDePermis = "A000";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisTropLong() {
        String numeroDePermis = "A00000";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderHeuresTransfereesValide() {
        boolean resultatAttendu = true;
        boolean resultatObtenu = ((OrdreArchitectes) ordre).validerHeuresTransferees(7.0);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderHeuresTransfereesInvalideTropHaut() {
        boolean resultatAttendu = false;
        boolean resultatObtenu = ((OrdreArchitectes) ordre).validerHeuresTransferees(8.0);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderHeuresTransfereesInvalideTropNegatif() {
        boolean resultatAttendu = false;
        boolean resultatObtenu = ((OrdreArchitectes) ordre).validerHeuresTransferees(-1.0);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNbHeuresActivitesImportantesMinimumValide() {
        boolean resultatAttendu = true;
        boolean resultatObtenu = ((OrdreArchitectes) ordre).validerNbHeuresActivitesImportantesMinimum(declarationValide.getJSONArray("activites"), ((OrdreArchitectes) ordre).MIN_HEURES_CATEGORIES_IMPORTANTES);
        
        assertEquals(resultatAttendu, resultatObtenu); 
    }
    
    @Test
    public void testValiderNbHeuresActivitesImportantesMinimumInvalide() {
        boolean resultatAttendu = false;
        boolean resultatObtenu = ((OrdreArchitectes) ordre).validerNbHeuresActivitesImportantesMinimum(declarationValide.getJSONArray("activites"), 18);
        
        assertEquals(resultatAttendu, resultatObtenu); 
    }
    
    @Test
    public void testCalculerNbHeuresTotales() {
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        double resultatAttendu = 17.0;
        double resultatObtenu = ordre.calculerNbHeuresTotales();
        
        assertEquals(resultatAttendu, resultatObtenu, 0.00);
    }
    
    @Test
    public void testGestionDesHeuresParCategorieValide() {
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.gestionDesHeuresParCategorie();
        
        assertEquals(resultatAttendu, resultatObtenu); 
    }
    
    @Test
    public void testGestionDesHeuresParCategorieInvalideLimite() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        JSONObject activite = new JSONObject();
        
        activite.put("description", "Testestestestestestes");
        activite.put("categorie", "présentation");
        activite.put("heures", 24);
        activite.put("date", "2014-00-00");
        activites.add(activite);
        
        ordre.creerMapCategories(activites);
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.gestionDesHeuresParCategorie();
        
        assertEquals(resultatAttendu, resultatObtenu); 
    }
    
    @Test
    public void testGestionDesHeuresParCategorieInvalideImportant() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.remove(0);
                    
        ordre.creerMapCategories(activites);
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.gestionDesHeuresParCategorie();
        
        assertEquals(resultatAttendu, resultatObtenu); 
    }
}

 
