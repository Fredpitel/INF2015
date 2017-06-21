package domaine;

import java.text.SimpleDateFormat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.text.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import projetdesession.Application;

public class OrdreProfessionnelTest {
    JSONObject declarationValide;
    OrdreProfessionnel ordre;
    
    @Before
    public void  before() throws ParseException {
        Application.formatDate = new SimpleDateFormat("yyyy-MM-dd");
        declarationValide = new JSONObject();
        declarationValide.put("nom", "Test");
        declarationValide.put("prenom", "Test");
        declarationValide.put("sexe", 0);
        declarationValide.put("ordre", "géologues");
        declarationValide.put("numero_de_permis", "Test");
        declarationValide.put("cycle", "2013-2016");

        JSONArray activites = new JSONArray();
        JSONObject activite1 = new JSONObject();
        activite1.put("description", "Testestestestestestes");
        activite1.put("categorie", "cours");
        activite1.put("heures", 30);
        activite1.put("date", "2010-01-01");
        activites.add(activite1);
        
        JSONObject activite2 = new JSONObject();
        activite2.put("description", "Testestestestestestes");
        activite2.put("categorie", "séminaire");
        activite2.put("heures", 5);
        activite2.put("date", "2011-12-31");
        activites.add(activite2);
        
        JSONObject activite3 = new JSONObject();
        activite3.put("description", "Testestestestestestes");
        activite3.put("categorie", "cours");
        activite3.put("heures", 20);
        activite3.put("date", "2011-12-31");
        activites.add(activite3);
        
        declarationValide.put("activites", activites);
        ordre = new OrdreProfessionnelFactory().creerOrdreProfessionnel(declarationValide);
    }
    
    @Test
    public void testValiderCycleValide() throws ParseException {
        // Pour éviter de poursuivre vers validerActivites()
        OrdreGeologues ordreMock = Mockito.mock(OrdreGeologues.class);
        Mockito.doNothing().when(ordreMock).validerActivites();
        
        ordreMock.declaration = this.ordre.declaration;
        ordreMock.cycle = this.ordre.cycle;
        
        ordreMock.validerCycle();
        assertFalse(ordreMock.declaration.getBoolean("cycle_invalide"));
    }
    
    @Test
    public void testValiderCycleInvalide() throws ParseException {
        // Invalider le cycle artificiellement
        ordre.cycle = null;
        
        ordre.validerCycle();
        assertTrue(ordre.declaration.getBoolean("cycle_invalide"));
    }
    
    @Test
    public void testInitialiserActivites() {
        JSONArray activites = new JSONArray();
        activites.add(new JSONObject());
        activites.add(new JSONObject());
        activites.add(new JSONObject());
        
        ordre.initialiserActivites(activites);
        
        assertEquals(3, activites.size());
        for (int i = 0 ; i < activites.size() ; i++) {
            assertTrue(activites.getJSONObject(i).getBoolean("valide"));
        }
    }
    
    @Test
    public void testCreeOrdreProfessionnelInvalide() throws ParseException {
        declarationValide.replace("ordre", "ordre404");
        ordre = new OrdreProfessionnelFactory().creerOrdreProfessionnel(declarationValide);
        assertEquals(null, ordre);
    }
    
    /**
     * Test of validerDates method, of class OrdreProfessionnel.
     * @throws java.lang.Exception
     */
    @Test
    public void testValiderDatesValideMin() throws Exception {
        Cycle cycle = new Cycle("2010-01-01", "2011-12-31", 0);
        
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerDates(declarationValide.getJSONArray("activites"), cycle);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderDatesInvalideMin() throws Exception {
        Cycle cycle = new Cycle("2010-01-01", "2011-12-31", 0);
        
        JSONArray activites = declarationValide.getJSONArray("activites");
        JSONObject activite = new JSONObject();
        activite.put("description", "Testestestestestestes");
        activite.put("categorie", "Test");
        activite.put("heures", 0);
        activite.put("date", "2009-12-31");
        
        activites.add(activite);
        
        declarationValide.replace("activites", activites);
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerDates(declarationValide.getJSONArray("activites"), cycle);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderDatesInvalideMax() throws Exception {
        Cycle cycle = new Cycle("2010-01-01", "2011-12-31", 0);
        
        JSONArray activites = declarationValide.getJSONArray("activites");
        JSONObject activite = new JSONObject();
        activite.put("description", "Testestestestestestes");
        activite.put("categorie", "Test");
        activite.put("heures", 0);
        activite.put("date", "2012-01-01");
        
        activites.add(activite);
        
        declarationValide.replace("activites", activites);
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerDates(declarationValide.getJSONArray("activites"), cycle);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }

    /**
     * Test of validerCategories method, of class OrdreProfessionnel.
     */
    @Test
    public void testValiderCategoriesValide() {
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerCategories(declarationValide.getJSONArray("activites"));
        
        assertEquals(resultatAttendu, resultatObtenu);   
    }
    
    @Test
    public void testValiderCategoriesInvalide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        JSONObject activite = new JSONObject();
        activite.put("description", "Testestestestestestes");
        activite.put("categorie", "Test");
        activite.put("heures", 0);
        activite.put("date", "Test");
        
        activites.add(activite);
        
        declarationValide.replace("activites", activites);
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerCategories(declarationValide.getJSONArray("activites"));
        
        assertEquals(resultatAttendu, resultatObtenu);   
    }

    /**
     * Test of validerSexe method, of class OrdreProfessionnel.
     */
    @Test
    public void testValiderSexeValide() {
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerSexe(declarationValide.getInt("sexe"));
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderSexeInvalide() {
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerSexe(3);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderSexeInvalideNegatif() {
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerSexe(-1);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }

    /**
     * Test of calculerTotalHeuresParCategorie method, of class OrdreProfessionnel.
     */
    @Test
    public void testCalculerTotalHeuresParCategorie() {
        double resultatAttendu = 50.0;
        double resultatObtenu = ordre.calculerTotalHeuresParCategorie(declarationValide.getJSONArray("activites"), "cours");
        
        assertEquals(resultatAttendu, resultatObtenu, 0.00);
    }

    /**
     * Test of validerNbHeuresMaximumParCategorie method, of class OrdreProfessionnel.
     */
    @Test
    public void testValiderNbHeuresMaximumParCategorieValide() {
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerNbHeuresMaximumParCategorie("cours", 50.0);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNbHeuresMaximumParCategorieInvalide() {
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNbHeuresMaximumParCategorie("cours", 49.0);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }

    /**
     * Test of validerNbHeuresMinimumParCategorie method, of class OrdreProfessionnel.
     */
    @Test
    public void testValiderNbHeuresMinimumParCategorieValide() {
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerNbHeuresMinimumParCategorie("cours", 50.0);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNbHeuresMinimumParCategorieInvalide() {
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNbHeuresMinimumParCategorie("cours", 51.0);
        
        assertEquals(resultatAttendu, resultatObtenu);
    }

    /**
     * Test of validerNbHeuresTotales method, of class OrdreProfessionnel.
     * @throws java.lang.Exception
     */
    @Test
    public void testValiderNbHeuresTotalesValide() throws Exception {
        ordre.cycle = ordre.creerCycle(declarationValide.getString("cycle"));
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerNbHeuresTotales();
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNbHeuresTotalesInvalide() throws Exception {
        ordre.cycle = new Cycle("0000-00-00", "0000-00-00", 56.0);
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNbHeuresTotales();
        
        assertEquals(resultatAttendu, resultatObtenu);
    }

    /**
     * Test of calculerNbHeuresTotales method, of class OrdreProfessionnel.
     */
    @Test
    public void testCalculerNbHeuresTotales() {
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        double resultatAttendu = 55.0;
        double resultatObtenu = ordre.calculerNbHeuresTotales();
        
        assertEquals(resultatAttendu, resultatObtenu, 0.00);
    }    
}
