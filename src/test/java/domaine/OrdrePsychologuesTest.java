package domaine;

import java.text.SimpleDateFormat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import projetdesession.Application;

public class OrdrePsychologuesTest {
    JSONObject declarationValide;
    OrdreProfessionnel ordre;
    
    @Before
    public void  before() throws Exception {
        Application.formatDate = new SimpleDateFormat("yyyy-MM-dd");
        declarationValide = new JSONObject();
        declarationValide.put("nom", "Test");
        declarationValide.put("prenom", "Test");
        declarationValide.put("sexe", 0);
        declarationValide.put("ordre", "psychologues");
        declarationValide.put("numero_de_permis", "00000-00");
        declarationValide.put("cycle", "Test");

        JSONArray activites = new JSONArray();
        JSONObject activite1 = new JSONObject();
        activite1.put("description", "Testestestestestestes");
        activite1.put("categorie", "cours");
        activite1.put("heures", 25);
        activite1.put("date", "2014-00-00");
        activites.add(activite1);
        
        JSONObject activite2 = new JSONObject();
        activite2.put("description", "Testestestestestestes");
        activite2.put("categorie", "conférence");
        activite2.put("heures", 15);
        activite2.put("date", "0000-00-00");
        activites.add(activite2);
        
        declarationValide.put("activites", activites);
        ordre = new OrdreProfessionnelFactory().creerOrdreProfessionnel(declarationValide);
    }
    
    @Test
    public void testCreerCycleValide() throws Exception {
        Cycle cycleObtenu = ordre.creerCycle("2012-2016");
        Cycle cycleAttendu = new Cycle("2012-01-01", "2017-01-01", 90.0);
        assertEquals(cycleObtenu, cycleAttendu);
    }
    
    @Test
    public void testCreerCycleInvalide() throws Exception {
        Cycle cycleObtenu = ordre.creerCycle("2010-2012");
        assertEquals(cycleObtenu, null);
    }
    
    @Test
    public void testValiderNumeroDePermisValideT() {
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerNumeroDePermis(declarationValide.getString("numero_de_permis"), ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisInvalideFormat() {
        String numeroDePermis = "0000000";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisTropCourt1() {
        String numeroDePermis = "0000-00";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisTropCourt2() {
        String numeroDePermis = "00000-0";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisTropLong1() {
        String numeroDePermis = "000000-00";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisTropLong2() {
        String numeroDePermis = "00000-000";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testGestionDesHeuresParCategorieValide() {
        ordre.creerMapCategories(declarationValide.getJSONArray("activites"));
        
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.gestionDesHeuresParCategorie();
        
        assertEquals(resultatAttendu, resultatObtenu); 
    }
    
    @Test
    public void testGestionDesHeuresParCategorieInvalideConference() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        JSONObject activite = new JSONObject();
        
        activite.put("description", "Testestestestestestes");
        activite.put("categorie", "conférence");
        activite.put("heures", 1);
        activite.put("date", "2014-00-00");
        activites.add(activite);
        
        ordre.creerMapCategories(activites);
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.gestionDesHeuresParCategorie();
        
        assertEquals(resultatAttendu, resultatObtenu); 
    }
    
    @Test
    public void testGestionDesHeuresParCategorieInvalideCours() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.remove(0);
        
        ordre.creerMapCategories(activites);
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.gestionDesHeuresParCategorie();
        
        assertEquals(resultatAttendu, resultatObtenu); 
    }
}

 
