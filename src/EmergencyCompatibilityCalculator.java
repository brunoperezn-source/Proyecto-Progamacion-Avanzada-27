/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * 
 */
public class EmergencyCompatibilityCalculator extends CompatibilityCalculator {
    
    @Override
    public double calculateCompatibility(Volunteer volunteer, Project project) {
        if (!isEligible(volunteer, project)) {
            return 0.0;
        }

        double baseCompatibility = calculateBaseCompatibility(volunteer, project);
        
        if (project.getNivelCatastrofe() > 0) {
            double emergencyBonus = calculateEmergencyBonus(project.getNivelCatastrofe());
            baseCompatibility += emergencyBonus;
        }
        
        return baseCompatibility;
    }
    
    private double calculateBaseCompatibility(Volunteer volunteer, Project project) {
        Stats stats = volunteer.get_stats();
        double totalScore = 0.0;
        int validStats = 0;
        
        if (project.getFisico() > 0) {
            double physicalMultiple = stats.get_physical() / project.getFisico();
            totalScore += physicalMultiple;
            validStats++;
        }
        
        if (project.getSocial() > 0) {
            double socialMultiple = stats.get_social() / project.getSocial();
            totalScore += socialMultiple;
            validStats++;
        }
        
        if (project.getEficiencia() > 0) {
            double efficiencyMultiple = stats.get_efficiency() / project.getEficiencia();
            totalScore += efficiencyMultiple;
            validStats++;
        }
        
        return validStats > 0 ? totalScore / validStats : 0.0;
    }
    
    private double calculateEmergencyBonus(int disasterLevel) {
        return 0.1 * disasterLevel;
    }
}