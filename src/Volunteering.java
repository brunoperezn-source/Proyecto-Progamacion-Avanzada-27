
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
    
    public Volunteering(){
        this.possible_volunteers = new HashMap<>();
        this.eligible_volunteers = new HashMap<>();
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
        System.out.println("Cargados " + possible_volunteers.size() +"voluntarios al sistema.");
    }
    
    public void evaluate(Volunteer[] voluntarios, int cantidad, Project projecto)
    {
        if (possible_volunteers.isEmpty()) {
            loadVolunteers(voluntarios, cantidad);
        }
        eligible_volunteers.clear();
        for (Volunteer voluntario : possible_volunteers.values()) {
            if (isEligibleForProject(voluntario, projecto)) {
                eligible_volunteers.put(voluntario.get_rut(), voluntario);
            }
        }
    }
    
    private boolean isEligibleForProject(Volunteer volunteer, Project projecto){
        Stats volunteerStats = volunteer.get_stats();
        if (volunteerStats.get_physical() < projecto.getFisico() ||
            volunteerStats.get_social() < projecto.getSocial() ||
            volunteerStats.get_efficiency() < projecto.getEficiencia()) {
            return false;
        }
        return true;
    }
    
    public double calculateCompatibility(Volunteer volunteer, Project projecto)
    {
        Stats stats = volunteer.get_stats();
        double total_score = 0.0;
        int valid_stats = 0;
        if (projecto.getFisico() > 0){
            double physicalMultiple = stats.get_physical() / projecto.getFisico();
            total_score += physicalMultiple;
            valid_stats++;
        }
        if (projecto.getSocial() > 0){
            double socialMultiple = stats.get_social() / projecto.getSocial();
            total_score += socialMultiple;
            valid_stats++;
        }
        if (projecto.getEficiencia() > 0){
            double efficiencyMultiple = stats.get_efficiency() / projecto.getEficiencia();
            total_score += efficiencyMultiple;
            valid_stats++;
        }
        return valid_stats > 0 ? total_score / valid_stats : 0.0;
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
}
