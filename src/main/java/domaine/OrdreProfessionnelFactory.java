package domaine;

import java.text.ParseException;
import net.sf.json.JSONObject;

public class OrdreProfessionnelFactory {

    public OrdreProfessionnel creerOrdreProfessionnel(JSONObject declaration) throws ParseException{
        switch (declaration.getString("ordre")) {
        case "architectes":
            return new OrdreArchitectes(declaration);

        case "géologues":
            return new OrdreGeologues(declaration);

        case "psychologues":
            return new OrdrePsychologues(declaration);

        case "podiatres":
            return new OrdrePodiatres(declaration);

        default:
            return null;
        }
    }
}
