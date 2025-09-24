/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 *
 */
public abstract class CompatibilityCalculator {

    public abstract double calculateCompatibility(Volunteer volunteer, Project project);
    
    protected boolean isEligible(Volunteer volunteer, Project project) {
        Stats volunteerStats = volunteer.get_stats();
        return volunteerStats.get_physical() >= project.getFisico() &&
               volunteerStats.get_social() >= project.getSocial() &&
               volunteerStats.get_efficiency() >= project.getEficiencia();
    }
}