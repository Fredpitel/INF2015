package domaine;

import projetdesession.Application;
import java.text.ParseException;
import java.util.Date;

public class Cycle {

    public Date dateDebut;
    public Date dateFin;
    public double minHeuresTotales;

    public Cycle(String dateDebut, String dateFin, double minHeuresTotales) throws ParseException {
        this.dateDebut = Application.formatDate.parse(dateDebut);
        this.dateFin = Application.formatDate.parse(dateFin);
        this.minHeuresTotales = minHeuresTotales;
    }
    
    @Override
    public boolean equals(Object nouveau) {
        Cycle nouveauCycle = (Cycle) nouveau;
        return (dateDebut.equals(nouveauCycle.dateDebut)
                && dateFin.equals(nouveauCycle.dateFin)
                && minHeuresTotales == nouveauCycle.minHeuresTotales);
    }
}
