package app.fotos.chicaslindas.personas;

import java.io.Serializable;

public class Persona implements Serializable {
    private String nombre;
    private String imagen;
    private String categoria;

    public Persona() {
    }

    public Persona(String nombre,String categoria, String imagen ) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public String getCategoria() { return categoria; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setCategoria(String categoria) { this.categoria = categoria; }
}

