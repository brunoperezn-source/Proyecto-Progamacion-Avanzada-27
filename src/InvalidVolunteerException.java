/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * 
 */
public class InvalidVolunteerException extends Exception {
    
    public InvalidVolunteerException() {
        super("Voluntario inválido: verifique los datos ingresados");
    }
    
    public InvalidVolunteerException(String message) {
        super("Error de voluntario: " + message);
    }
    
    public InvalidVolunteerException(String rut, String reason) {
        super("Voluntario con RUT " + rut + " inválido: " + reason);
    }
}
