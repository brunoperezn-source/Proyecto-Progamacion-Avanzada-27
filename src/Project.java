/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author basse
 */
public class Project {
    private String nombre;
    private int fisico;
    private int social;
    private int eficiencia;
    private int nivelCatastrofe;

    public Project(String nombre, int fisico, int social, int eficiencia, int nivelCatastrofe) {
        this.nombre = nombre;
        this.fisico = fisico;
        this.social = social;
        this.eficiencia = eficiencia;
        this.nivelCatastrofe = nivelCatastrofe;
    }

    public String getNombre() {
        return nombre;
    }
    public int getFisico() {
        return fisico;
    }
    public int getSocial() {
        return social;
    }
    public int getEficiencia() {
        return eficiencia;
    }
    public int getNivelCatastrofe() {
        return nivelCatastrofe;
    }
    public void setNivelCatastrofe(int nivelCatastrofe) {
        this.nivelCatastrofe = nivelCatastrofe;
    }
    public void setNombre(String nombre) {
    this.nombre = nombre;
    }

    public void setFisico(int fisico) {
        this.fisico = fisico;
    }

    public void setSocial(int social) {
        this.social = social;
    }

    public void setEficiencia(int eficiencia) {
        this.eficiencia = eficiencia;
    }
}