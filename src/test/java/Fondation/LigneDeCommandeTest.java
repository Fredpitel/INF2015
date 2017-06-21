/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fondation;

import Fondation.TypeAction.Action;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;

/**
 *
 * @author fbeaudoin
 */
public class LigneDeCommandeTest {
    LigneDeCommande ligneDeCommande;
    
    private final ByteArrayOutputStream outString = new ByteArrayOutputStream();
    
    @Rule
    public final ExpectedSystemExit SYSTEM_EXIT = ExpectedSystemExit.none();
    
    @Before
    public void setUp() {
        ligneDeCommande = new LigneDeCommande();
        System.setOut(new PrintStream(outString));
    }
    
    @After
    public void after() {
        System.setOut(null);
    }
    
    @Test
    public void testChoisirActionAfficherStats() {
        String[] args = {"-S"};
        ligneDeCommande.choisirAction(args);
        Action resultatObtenu = ligneDeCommande.getAction();
        Action resultatAttendu = Action.AFFICHER_STATISTIQUES;
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testChoisirActionReinitialiserStats() {
        String[] args = {"-SR"};
        ligneDeCommande.choisirAction(args);
        Action resultatObtenu = ligneDeCommande.getAction();
        Action resultatAttendu = Action.REINITIALISER_STATISTIQUES;
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test
    public void testChoisirActionValeurInvalideFinProgramme() {
        SYSTEM_EXIT.expectSystemExitWithStatus(1);
        String[] args = {"-test"};
        ligneDeCommande.choisirAction(args);
    }
    
    @Test
    public void testAfficherMessageUsage() {
        ligneDeCommande.afficherUsageLigneDeCommande();
        String messageAttendu = "Ligne de commande invalide. Utiliser le formatage suivant : \n"
                                + "java -jar ProjetDeSession.jar FichierEntree.json FichierSortie.json\n"
                                + "java -jar ProjetDeSession.jar -S\n"
                                + "java -jar ProjetDeSession.jar -SR\n";
        
        assertEquals(messageAttendu, outString.toString());
    }
    
    @Test 
    public void testValiderParamAucunArgumentFinProgramme() {
        SYSTEM_EXIT.expectSystemExitWithStatus(1);
        String[] args = {""};
        ligneDeCommande.validerParam(args);
        
    }
    
    @Test
    public void testValiderParamUnArgumentValide() {
        String[] args = {"-S"};
        ligneDeCommande.validerParam(args);
        
        Action resultatObtenu = ligneDeCommande.getAction();
        Action resultatAttendu = Action.AFFICHER_STATISTIQUES;
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test public void testValiderParamDeuxArgumentsValiderDeclaration() {
        String[] args = {"arg1", "arg2"};
        ligneDeCommande.validerParam(args);
        
        Action resultatObtenu = ligneDeCommande.getAction();
        Action resultatAttendu = Action.VALIDER_DECLARATION;
        
        assertEquals(resultatAttendu, resultatObtenu);
    }
    
    @Test public void testValiderParamTroisArgumentsFinProgramme() {
        SYSTEM_EXIT.expectSystemExitWithStatus(1);
        String[] args = {"arg1", "arg2", "arg3"};
        ligneDeCommande.validerParam(args);
    }
}
