package domaine;

import java.text.SimpleDateFormat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import projetdesession.Application;

public class OrdreGeologuesTest {
    JSONObject declarationValide;
    OrdreProfessionnel ordre;
    
    @Before
    public void  before() throws Exception {
        Application.formatDate = new SimpleDateFormat("yyyy-MM-dd");
        declarationValide = new JSONObject();
        declarationValide.put("nom", "nom");
        declarationValide.put("prenom", "prenom");
        declarationValide.put("sexe", 0);
        declarationValide.put("ordre", "géologues");
        declarationValide.put("numero_de_permis", "NP0000");
        declarationValide.put("cycle", "Test");

        JSONArray activites = new JSONArray();
        JSONObject activite1 = new JSONObject();
        activite1.put("description", "Testestestestestestes");
        activite1.put("categorie", "cours");
        activite1.put("heures", 22);
        activite1.put("date", "2014-00-00");
        activites.add(activite1);
        
        JSONObject activite2 = new JSONObject();
        activite2.put("description", "Testestestestestestes");
        activite2.put("categorie", "projet de recherche");
        activite2.put("heures", 3);
        activite2.put("date", "0000-00-00");
        activites.add(activite2);
        
        JSONObject activite3 = new JSONObject();
        activite3.put("description", "Testestestestestestes");
        activite3.put("categorie", "groupe de discussion");
        activite3.put("heures", 1);
        activite3.put("date", "0000-00-00");
        activites.add(activite3);
        
        declarationValide.put("activites", activites);
        ordre = new OrdreProfessionnelFactory().creerOrdreProfessionnel(declarationValide);
    }
    
    @Test
    public void testCreerCycleValide() throws Exception {
        Cycle cycleObtenu = ordre.creerCycle("2013-2016");
        Cycle cycleAttendu = new Cycle("2013-06-01", "2016-06-01", 55.0);
        assertEquals(cycleObtenu, cycleAttendu);
    }
    
    @Test
    public void testCreerCycleInvalide() throws Exception {
        Cycle cycleObtenu = ordre.creerCycle("2013-2017");
        assertEquals(cycleObtenu, null);
    }
    
    @Test
    public void testValiderNumeroDePermisValide() {
        boolean resultatAttendu = true;
        boolean resultatObtenu = ordre.validerNumeroDePermis(declarationValide.getString("numero_de_permis"), ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisLettresInitiales() {
        String numeroDePermis = "PN0000";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisTropCourt() {
        String numeroDePermis = "NP000";
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.validerNumeroDePermis(numeroDePermis, ordre.getPatternNumeroDePermis());
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testValiderNumeroDePermisTropLong() {
        String numeroDePermis = "NP00000";
        
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
    public void testGestionDesHeuresParCategorieInvalide() {
        JSONArray activites = declarationValide.getJSONArray("activites");
        activites.remove(0);
        
        ordre.creerMapCategories(activites);
        
        boolean resultatAttendu = false;
        boolean resultatObtenu = ordre.gestionDesHeuresParCategorie();
        
        assertEquals(resultatAttendu, resultatObtenu); 
    }
}

 
