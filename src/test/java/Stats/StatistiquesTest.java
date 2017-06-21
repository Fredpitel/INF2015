/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stats;

import Fondation.Utf8File;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyString;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author fbeaudoin
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Utf8File.class)
public class StatistiquesTest {
    
    private JSONObject declaration;
    private JSONObject statsAttendues;
    private JSONObject statsObtenues;
    private final ByteArrayOutputStream outString = new ByteArrayOutputStream();
    
    @Before
    public void  before() throws FileNotFoundException, Exception {
        declaration = creeDeclarationSimplifiee();
        statsAttendues = creeNouvelleStatsComplete();
        statsObtenues = creeNouvelleStatsComplete();
        
        System.setOut(new PrintStream(outString));
        
        String nomFichierStats = "Statistiques.json";
        String contenuFichier = "{\n"
                                + "  \"nb_total_declarations_traitees\": 0,\n"
                                + "  \"nb_total_declarations_completes\": 0,\n"
                                + "  \"nb_total_declarations_incompletes_ou_invalides\": 0,\n"
                                + "  \"nb_total_declarations_hommes\": 0,\n"
                                + "  \"nb_total_declarations_femmes\": 0,\n"
                                + "  \"nb_total_declarations_sexe_inconnu\": 0,\n"
                                + "  \"nb_total_declarations_permis_invalide\": 0,\n"
                                + "  \"nb_total_activites_valides\": 0,\n"
                                + "  \"nb_total_activites_valides_par_categorie\":   {\n"
                                + "    \"categorie_cours\": 0,\n"
                                + "    \"categorie_atelier\": 0,\n"
                                + "    \"categorie_séminaire\": 0,\n"
                                + "    \"categorie_colloque\": 0,\n"
                                + "    \"categorie_conférence\": 0,\n"
                                + "    \"categorie_lecture dirigée\": 0,\n"
                                + "    \"categorie_présentation\": 0,\n"
                                + "    \"categorie_groupe de discussion\": 0,\n"
                                + "    \"categorie_projet de recherche\": 0,\n"
                                + "    \"categorie_rédaction professionnelle\": 0\n"
                                + "  },\n"
                                + "  \"nb_total_declaration_valides_completes_par_ordre\":   {\n"
                                + "    \"declaration_completes_architectes\": 0,\n"
                                + "    \"declaration_completes_psychologues\": 0,\n"
                                + "    \"declaration_completes_geologues\": 0,\n"
                                + "    \"declaration_completes_podiatres\": 0\n"
                                + "  },\n"
                                + "  \"nb_total_declaration_valides_incompletes_par_ordre\":   {\n"
                                + "    \"declaration_incompletes_architectes\": 0,\n"
                                + "    \"declaration_incompletes_psychologues\": 0,\n"
                                + "    \"declaration_incompletes_geologues\": 0,\n"
                                + "    \"declaration_incompletes_podiatres\": 0\n"
                                + "  }\n"
                                + "}";
        
        PowerMockito.mockStatic(Utf8File.class);
        PowerMockito.when(Utf8File.loadFileIntoString(nomFichierStats)).thenReturn(contenuFichier);
        PowerMockito.doNothing().when(Utf8File.class, "saveStringIntoFile", anyString(), anyString());
        
    }
    
    @After
    public void after() {
        System.setOut(null);
    }
    
    private JSONObject creeDeclarationSimplifiee() {
        JSONObject declaration = new JSONObject();
        JSONObject resultat = new JSONObject();
        JSONArray activites = creeTroisActivitesValides();
        
        declaration.put("nom", "Stevensen");
        declaration.put("prenom", "Steven");
        declaration.put("sexe", 1);
        declaration.put("ordre", "architectes");
        declaration.put("numero_de_permis", "A1341");
        declaration.put("activites", activites);
        declaration.put("numero_permis_valide", true);
        declaration.put("invalide", false);
        declaration.put("cycle_invalide", false);
        
        resultat.put("complet", true);
        declaration.put("resultat", resultat);

        return declaration;
    }
    
    private JSONArray creeTroisActivitesValides() {
        JSONArray activites = new JSONArray();
        JSONObject activiteValide1 = new JSONObject();
        JSONObject activiteValide2 = new JSONObject();
        JSONObject activiteValide3 = new JSONObject();
        
        activiteValide1.put("valide", true);
        activiteValide1.put("categorie", "cours");
        
        activiteValide2.put("valide", true);
        activiteValide2.put("categorie", "cours");
        
        activiteValide3.put("valide", true);
        activiteValide3.put("categorie", "rédaction professionnelle");
        
        activites.add(activiteValide1);
        activites.add(activiteValide2);
        activites.add(activiteValide3);
        
        return activites;
    }
    
    private JSONObject creeNouvelleStatsComplete() {
        JSONObject stats = creeNouvelleListeStatsConservees();
        JSONObject listeCategories = creeNouvelleListeCategories();
        JSONObject listeDeclarationsCompletes = creeNouvelleListeDeclarationsCompletes();
        JSONObject listeDeclarationsIncompletes = creeNouvelleListeDeclarationsIncompletes();
        
        stats.put("nb_total_activites_valides_par_categorie", listeCategories);
        stats.put("nb_total_declaration_valides_completes_par_ordre", listeDeclarationsCompletes);
        stats.put("nb_total_declaration_valides_incompletes_par_ordre", listeDeclarationsIncompletes);
        
        return stats;
    }
    
    private JSONObject creeNouvelleListeStatsConservees() {
        JSONObject listeStatsConservees = new JSONObject();
        listeStatsConservees.put("nb_total_declarations_traitees", 0);
        listeStatsConservees.put("nb_total_declarations_completes", 0);
        listeStatsConservees.put("nb_total_declarations_incompletes_ou_invalides", 0);
        listeStatsConservees.put("nb_total_declarations_hommes", 0);
        listeStatsConservees.put("nb_total_declarations_femmes", 0);
        listeStatsConservees.put("nb_total_declarations_sexe_inconnu", 0);
        listeStatsConservees.put("nb_total_declarations_permis_invalide", 0);
        listeStatsConservees.put("nb_total_activites_valides", 0);
        listeStatsConservees.put("nb_total_activites_valides_par_categorie", 0);
        listeStatsConservees.put("nb_total_declaration_valides_completes_par_ordre", 0);
        listeStatsConservees.put("nb_total_declaration_valides_incompletes_par_ordre", 0);
        
        return listeStatsConservees;
    }
    
    private JSONObject creeNouvelleListeCategories() {
        JSONObject listeCategories = new JSONObject();
        listeCategories.put("categorie_cours", 0);
        listeCategories.put("categorie_atelier", 0);
        listeCategories.put("categorie_séminaire", 0);
        listeCategories.put("categorie_colloque", 0);
        listeCategories.put("categorie_conférence", 0);
        listeCategories.put("categorie_lecture dirigée", 0);
        listeCategories.put("categorie_présentation", 0);
        listeCategories.put("categorie_groupe de discussion", 0);
        listeCategories.put("categorie_projet de recherche", 0);
        listeCategories.put("categorie_rédaction professionnelle", 0);
        
        return listeCategories;
    }
    
    private JSONObject creeNouvelleListeDeclarationsCompletes() {
        JSONObject listeDeclarationsCompletes = new JSONObject();
        listeDeclarationsCompletes.put("declaration_completes_architectes", 0);
        listeDeclarationsCompletes.put("declaration_completes_psychologues", 0);
        listeDeclarationsCompletes.put("declaration_completes_geologues", 0);
        listeDeclarationsCompletes.put("declaration_completes_podiatres", 0);
        
        return listeDeclarationsCompletes;
    }
    
    private JSONObject creeNouvelleListeDeclarationsIncompletes() {
        JSONObject listeDeclarationsIncompletes = new JSONObject();
        listeDeclarationsIncompletes.put("declaration_incompletes_architectes", 0);
        listeDeclarationsIncompletes.put("declaration_incompletes_psychologues", 0);
        listeDeclarationsIncompletes.put("declaration_incompletes_geologues", 0);
        listeDeclarationsIncompletes.put("declaration_incompletes_podiatres", 0);
        
        return listeDeclarationsIncompletes;
    }
    
    @Test
    public void testCompilerStats() {
        statsAttendues.replace("nb_total_declarations_traitees", 1);
        statsAttendues.replace("nb_total_declarations_completes", 1);
        statsAttendues.replace("nb_total_declarations_hommes", 1);
        statsAttendues.replace("nb_total_activites_valides", 3);
        
        JSONObject activiteValide = statsAttendues.getJSONObject("nb_total_activites_valides_par_categorie");
        activiteValide.replace("categorie_cours", 2);
        activiteValide.replace("categorie_rédaction professionnelle", 1);
        
        JSONObject declarationCompleteParOrdre = statsAttendues.getJSONObject("nb_total_declaration_valides_completes_par_ordre");
        declarationCompleteParOrdre.replace("declaration_completes_architectes", 1);
        
        statsObtenues = Statistiques.compilerStats(declaration);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test (expected = net.sf.json.JSONException.class)
    public void testCompilerStatsDeclarationVide() {
        statsObtenues = Statistiques.compilerStats(new JSONObject());
        assertEquals(statsAttendues, statsObtenues);
    }

    @Test
    public void testCreerListeVide() {
        String chaine = "categorie_";
        List<String> categories = Arrays.asList("cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée", "présentation",
                                                           "groupe de discussion", "projet de recherche", "rédaction professionnelle");
    
        JSONObject listeObtenu = Statistiques.creerListeVide(chaine, categories);
        JSONObject listeAttendu = creeNouvelleListeCategories();
        
        assertEquals(listeAttendu, listeObtenu);
    }
    
    @Test
    public void testCreerListesStatsVide() {
        JSONObject listeObtenue = new JSONObject();
        JSONObject listeAttendue = new JSONObject();
        
        JSONObject listeCategories = creeNouvelleListeCategories();
        JSONObject listeDeclarationsCompletes = creeNouvelleListeDeclarationsCompletes();
        JSONObject listeDeclarationIncompletes = creeNouvelleListeDeclarationsIncompletes();
        
        listeAttendue.put("nb_total_activites_valides_par_categorie", listeCategories);
        listeAttendue.put("nb_total_declaration_valides_completes_par_ordre", listeDeclarationsCompletes);
        listeAttendue.put("nb_total_declaration_valides_incompletes_par_ordre", listeDeclarationIncompletes);
        
        Statistiques.creerListesStatsVide(listeObtenue);

        assertEquals(listeAttendue, listeObtenue);
    }
    
    @Test
    public void testCreerJSONObjectStatsVide() {
        JSONObject resultatObtenu = Statistiques.creerFichierStatsVide();
        
        assertEquals(statsAttendues, resultatObtenu);
    }
    
    @Test
    public void afficherStatisque() {
        String resultatAttendu = "\nnombre total de declarations traitees: 0"
                               + "\nnombre total de declarations completes: 0"
                               + "\nnombre total de declarations incompletes ou invalides: 0"
                               + "\nnombre total de declarations hommes: 0"
                               + "\nnombre total de declarations femmes: 0"
                               + "\nnombre total de declarations sexe inconnu: 0"
                               + "\nnombre total de declarations permis invalide: 0"
                               + "\nnombre total de activites valides: 0"
                               + "\nnombre total de activites valides par categorie: "
                               + "\n   - categorie cours: 0"
                               + "\n   - categorie atelier: 0"
                               + "\n   - categorie séminaire: 0"
                               + "\n   - categorie colloque: 0"
                               + "\n   - categorie conférence: 0"
                               + "\n   - categorie lecture dirigée: 0"
                               + "\n   - categorie présentation: 0"
                               + "\n   - categorie groupe de discussion: 0"
                               + "\n   - categorie projet de recherche: 0"
                               + "\n   - categorie rédaction professionnelle: 0"
                               + "\nnombre total de declaration valides completes par ordre: "
                               + "\n   - declaration completes architectes: 0"
                               + "\n   - declaration completes psychologues: 0"
                               + "\n   - declaration completes geologues: 0"
                               + "\n   - declaration completes podiatres: 0"
                               + "\nnombre total de declaration valides incompletes par ordre: "
                               + "\n   - declaration incompletes architectes: 0"
                               + "\n   - declaration incompletes psychologues: 0"
                               + "\n   - declaration incompletes geologues: 0"
                               + "\n   - declaration incompletes podiatres: 0\n";
        
        Statistiques.afficherStatistique();
        assertEquals(resultatAttendu, outString.toString());
    }
    
    @Test
    public void testFormatageDesStatistiquesALaConsole() {
        String resultatObtenu = Statistiques.formatSortieChaine(statsAttendues.toString());
        String resultatAttendu = "\nnombre total de declarations traitees: 0"
                               + "\nnombre total de declarations completes: 0"
                               + "\nnombre total de declarations incompletes ou invalides: 0"
                               + "\nnombre total de declarations hommes: 0"
                               + "\nnombre total de declarations femmes: 0"
                               + "\nnombre total de declarations sexe inconnu: 0"
                               + "\nnombre total de declarations permis invalide: 0"
                               + "\nnombre total de activites valides: 0"
                               + "\nnombre total de activites valides par categorie: "
                               + "\n   - categorie cours: 0"
                               + "\n   - categorie atelier: 0"
                               + "\n   - categorie séminaire: 0"
                               + "\n   - categorie colloque: 0"
                               + "\n   - categorie conférence: 0"
                               + "\n   - categorie lecture dirigée: 0"
                               + "\n   - categorie présentation: 0"
                               + "\n   - categorie groupe de discussion: 0"
                               + "\n   - categorie projet de recherche: 0"
                               + "\n   - categorie rédaction professionnelle: 0"
                               + "\nnombre total de declaration valides completes par ordre: "
                               + "\n   - declaration completes architectes: 0"
                               + "\n   - declaration completes psychologues: 0"
                               + "\n   - declaration completes geologues: 0"
                               + "\n   - declaration completes podiatres: 0"
                               + "\nnombre total de declaration valides incompletes par ordre: "
                               + "\n   - declaration incompletes architectes: 0"
                               + "\n   - declaration incompletes psychologues: 0"
                               + "\n   - declaration incompletes geologues: 0"
                               + "\n   - declaration incompletes podiatres: 0";
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testChargerStatsReturnNotNull() {
        JSONObject resultat = Statistiques.chargerFichierStats();
        assertNotNull(resultat);
    }
    
    @Test
    public void testAjouterTotalDeclarationMessageElementInvalide() {
        String statTraitee = "stat_invalide";
        String messageAttendu = "Erreur de format du fichier de statistique :\nLa statistique [stat_invalide] est inexistante dans le fichier de statistique.\n";
        
        Statistiques.ajouterTotalDeclaration(statsAttendues, statTraitee);
        assertEquals(messageAttendu, outString.toString());
    }
    
    @Test 
    public void testAjouterTotalDeclarationTraitee() {
        String statTraitee = "nb_total_declarations_traitees";
        
        statsAttendues.replace(statTraitee, 1);
        
        Statistiques.ajouterTotalDeclarationTraitee(statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationCompleteValide() {        
        declaration.replace("invalide", false);
        declaration.getJSONObject("resultat").replace("complet", true);
        
        statsAttendues.replace("nb_total_declarations_completes", 1);

        Statistiques.ajouterTotalDeclarationCompleteOuIncomplete(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationCompleteInvalide() {
        declaration.replace("invalide", true);
        declaration.getJSONObject("resultat").replace("complet", true);
        
        statsAttendues.replace("nb_total_declarations_incompletes_ou_invalides", 1);

        Statistiques.ajouterTotalDeclarationCompleteOuIncomplete(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationIncompleteValide() {
        declaration.replace("invalide", false);
        declaration.getJSONObject("resultat").replace("complet", false);
        
        statsAttendues.replace("nb_total_declarations_incompletes_ou_invalides", 1);

        Statistiques.ajouterTotalDeclarationCompleteOuIncomplete(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationIncompleteInvalide() {
        declaration.replace("invalide", true);
        declaration.getJSONObject("resultat").replace("complet", false);
        
        statsAttendues.replace("nb_total_declarations_incompletes_ou_invalides", 1);

        Statistiques.ajouterTotalDeclarationCompleteOuIncomplete(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterDeclarationSexeInconnu() {
        declaration.replace("sexe", 0);
        
        statsAttendues.replace("nb_total_declarations_sexe_inconnu", 1);

        Statistiques.ajouterTotalDeclarationSelonSexe(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterDeclarationHomme() {
        declaration.replace("sexe", 1);
        
        statsAttendues.replace("nb_total_declarations_hommes", 1);

        Statistiques.ajouterTotalDeclarationSelonSexe(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterDeclarationFemme() {
        declaration.replace("sexe", 2);
        
        statsAttendues.replace("nb_total_declarations_femmes", 1);
        
        Statistiques.ajouterTotalDeclarationSelonSexe(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterDeclarationSelonSexeInvalide() {
        declaration.replace("sexe", 404);

        Statistiques.ajouterTotalDeclarationSelonSexe(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test 
    public void testAjouterTotalDeclarationPermisInvalideValeurValide() {
        declaration.replace("numero_permis_valide", true);

        Statistiques.ajouterTotalDeclarationPermisInvalide(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test 
    public void testAjouterTotalDeclarationPermisInvalide() {
        declaration.replace("numero_permis_valide", false);
        
        statsAttendues.replace("nb_total_declarations_permis_invalide", 1);

        Statistiques.ajouterTotalDeclarationPermisInvalide(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalActiviteesValide3Sur3() {
        Statistiques.ajouterTotalActiviteesValides(declaration, statsObtenues);
        
        statsAttendues.replace("nb_total_activites_valides", 3);
        statsAttendues.getJSONObject("nb_total_activites_valides_par_categorie").replace("categorie_cours", 2);
        statsAttendues.getJSONObject("nb_total_activites_valides_par_categorie").replace("categorie_rédaction professionnelle", 1);
        
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalActiviteesValide2Sur3() {
        declaration.getJSONArray("activites").getJSONObject(0).replace("valide", false); // categorie: cours
        
        statsAttendues.replace("nb_total_activites_valides", 2);
        statsAttendues.getJSONObject("nb_total_activites_valides_par_categorie").replace("categorie_cours", 1);
        statsAttendues.getJSONObject("nb_total_activites_valides_par_categorie").replace("categorie_rédaction professionnelle", 1);
        
        Statistiques.ajouterTotalActiviteesValides(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalActiviteesValide0Sur3() {
        JSONArray activites = declaration.getJSONArray("activites");
        
        for (int i = 0 ; i < activites.size() ; i++) {
            activites.getJSONObject(i).replace("valide", false);
        }
        
        Statistiques.ajouterTotalActiviteesValides(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalActiviteesValideListeActiviteVide() {
        JSONArray activites = new JSONArray();
        
        // Remplace le array de 3 activités avec un array vide
        declaration.remove("activites");
        declaration.put("activites", activites);

        Statistiques.ajouterTotalActiviteesValides(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationArchitecteValideComplete() {
        declaration.replace("ordre", "architectes");
        declaration.getJSONObject("resultat").replace("complet", true);
        statsAttendues.getJSONObject("nb_total_declaration_valides_completes_par_ordre").replace("declaration_completes_architectes", 1);
        
        Statistiques.ajouterTotalDeclarationParOrdre(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationArchitecteValideInomplete() {
        declaration.replace("ordre", "architectes");
        declaration.getJSONObject("resultat").replace("complet", false);
        
        statsAttendues.getJSONObject("nb_total_declaration_valides_incompletes_par_ordre").replace("declaration_incompletes_architectes", 1);
        
        Statistiques.ajouterTotalDeclarationParOrdre(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationPsychologueValideComplete() {
        declaration.replace("ordre", "psychologues");
        declaration.getJSONObject("resultat").replace("complet", true);
        
        statsAttendues.getJSONObject("nb_total_declaration_valides_completes_par_ordre").replace("declaration_completes_psychologues", 1);
        
        Statistiques.ajouterTotalDeclarationParOrdre(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationPsychologueValideIncomplete() {
        declaration.replace("ordre", "psychologues");
        declaration.getJSONObject("resultat").replace("complet", false);
        
        statsAttendues.getJSONObject("nb_total_declaration_valides_incompletes_par_ordre").replace("declaration_incompletes_psychologues", 1);
        
        Statistiques.ajouterTotalDeclarationParOrdre(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationGeologuesValideComplete() {
        declaration.replace("ordre", "geologues");
        declaration.getJSONObject("resultat").replace("complet", true);
        
        statsAttendues.getJSONObject("nb_total_declaration_valides_completes_par_ordre").replace("declaration_completes_geologues", 1);
        
        Statistiques.ajouterTotalDeclarationParOrdre(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationGeologuesValideIncomplete() {
        declaration.replace("ordre", "geologues");
        declaration.getJSONObject("resultat").replace("complet", false);
        
        statsAttendues.getJSONObject("nb_total_declaration_valides_incompletes_par_ordre").replace("declaration_incompletes_geologues", 1);
        
        Statistiques.ajouterTotalDeclarationParOrdre(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationPodiatresValideComplete() {
        declaration.replace("ordre", "podiatres");
        declaration.getJSONObject("resultat").replace("complet", true);
        
        statsAttendues.getJSONObject("nb_total_declaration_valides_completes_par_ordre").replace("declaration_completes_podiatres", 1);
        
        Statistiques.ajouterTotalDeclarationParOrdre(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testAjouterTotalDeclarationPodiatresValideIncomplete() {
        declaration.replace("ordre", "podiatres");
        declaration.getJSONObject("resultat").replace("complet", false);
        
        statsAttendues.getJSONObject("nb_total_declaration_valides_incompletes_par_ordre").replace("declaration_incompletes_podiatres", 1);
        
        Statistiques.ajouterTotalDeclarationParOrdre(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
    
    @Test
    public void testCalculerStatistiques() {
        statsAttendues.replace("nb_total_declarations_traitees", 1);
        statsAttendues.replace("nb_total_declarations_completes", 1);
        statsAttendues.replace("nb_total_declarations_hommes", 1);
        statsAttendues.replace("nb_total_activites_valides", 3);
        
        JSONObject activiteValide = statsAttendues.getJSONObject("nb_total_activites_valides_par_categorie");
        activiteValide.replace("categorie_cours", 2);
        activiteValide.replace("categorie_rédaction professionnelle", 1);
        
        JSONObject declarationCompleteParOrdre = statsAttendues.getJSONObject("nb_total_declaration_valides_completes_par_ordre");
        declarationCompleteParOrdre.replace("declaration_completes_architectes", 1);
        
        Statistiques.calculerStatistiques(declaration, statsObtenues);
        assertEquals(statsAttendues, statsObtenues);
    }
}