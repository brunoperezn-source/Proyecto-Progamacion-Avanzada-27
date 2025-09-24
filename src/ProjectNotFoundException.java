/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * 
 */
public class ProjectNotFoundException extends Exception {
    
    public ProjectNotFoundException() {
        super("Proyecto no encontrado en el sistema");
    }
    
    public ProjectNotFoundException(String projectName) {
        super("Proyecto '" + projectName + "' no encontrado");
    }
    
    public ProjectNotFoundException(String projectName, String organizationName) {
        super("Proyecto '" + projectName + "' no encontrado en la organización '" + organizationName + "'");
    }
}
