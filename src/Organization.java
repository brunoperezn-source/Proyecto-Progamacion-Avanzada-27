/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author basse
 */
public class Organization {
    private String nombre;
    private Project[] proyectos;

    public Organization(String nombre, int cantidadProyectos) {
        this.nombre = nombre;
        this.proyectos = new Project[cantidadProyectos];
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public Project[] getProyectos() {
        return proyectos;
    }
    public void setProyecto(int index, Project proyecto) {
        if (index >= 0 && index < proyectos.length) {
            proyectos[index] = proyecto;
        }
    }
}
