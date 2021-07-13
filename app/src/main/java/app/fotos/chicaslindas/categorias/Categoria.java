package app.fotos.chicaslindas.categorias;

import java.io.Serializable;

public class Categoria implements Serializable  {

    String nombre;
    String imagen;

    public Categoria() {
    }

    public Categoria(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
