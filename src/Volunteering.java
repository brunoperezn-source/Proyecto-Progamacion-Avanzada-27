
import java.util.ArrayList;
import java.util.HashMap;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author basse
 */
public class Volunteering {
    private HashMap<Integer, Volunteer> possible_volunteers;
    private HashMap<Integer, Volunteer> eligible_volunteers;
    private CompatibilityCalculator calculator;
    
    public Volunteering(){
        this.possible_volunteers = new HashMap<>();
        this.eligible_volunteers = new HashMap<>();
        this.calculator = new StandardCompatibilityCalculator(); 
    }
    
    public void setCalculator(CompatibilityCalculator calculator) {
        this.calculator = calculator;
    }
    
    public void loadVolunteers(Volunteer[] voluntarios, int cantidad)
    {
        possible_volunteers.clear();
        for (int i = 0; i < cantidad; i++)
        {
            if(voluntarios[i] != null && voluntarios[i].is_valid()){
                possible_volunteers.put(voluntarios[i].get_rut(), voluntarios[i]);
            }
        }
        System.out.println("Cargados " + possible_volunteers.size() +" voluntarios al sistema.");
    }
    
    public void evaluate(Volunteer[] voluntarios, int cantidad, Project projecto)
    {
        if (possible_volunteers.isEmpty()) {
            loadVolunteers(voluntarios, cantidad);
        }
        eligible_volunteers.clear();
        for (Volunteer voluntario : possible_volunteers.values()) {
            if (calculator.isEligible(voluntario, projecto)) {
                eligible_volunteers.put(voluntario.get_rut(), voluntario);
            }
        }
    }
    
    public double calculateCompatibility(Volunteer volunteer, Project projecto) {
        return calculator.calculateCompatibility(volunteer, projecto);
    }
    
    public ArrayList<Volunteer> getBestMatches(Project project, int cantidad)
    {
        ArrayList<Volunteer> bestMatches = new ArrayList<>();
        ArrayList<VolunteerScore> scores = new ArrayList<>();
        
        for (Volunteer volunteer : eligible_volunteers.values()) {
            double score = calculateCompatibility(volunteer, project);
            scores.add(new VolunteerScore(volunteer, score));
        }
        
        scores.sort((a, b) -> Double.compare(b.score, a.score));
        int limit = Math.min(cantidad, scores.size());
        
        for (int i = 0; i < limit; i++) {
            bestMatches.add(scores.get(i).volunteer);
        }
        
        return bestMatches;
    }
    
    private static class VolunteerScore {
        Volunteer volunteer;
        double score;
        
        VolunteerScore(Volunteer volunteer, double score) {
            this.volunteer = volunteer;
            this.score = score;
        }
    }
    
    public HashMap<Integer, Volunteer> getPossibleVolunteers() {
        return this.possible_volunteers;
    }

    public HashMap<Integer, Volunteer> getEligibleVolunteers() {
        return this.eligible_volunteers;
    }
    
    public void setPossibleVolunteers(HashMap<Integer, Volunteer> possible_volunteers) {
        this.possible_volunteers = possible_volunteers;
    }

    public void setEligibleVolunteers(HashMap<Integer, Volunteer> eligible_volunteers) {
        this.eligible_volunteers = eligible_volunteers;
    }
}