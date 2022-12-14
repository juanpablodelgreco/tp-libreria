package com.library.libraryproject;

public class Libro implements Comparable<Libro> {

    private String id;
    private String ISBN;
    private String titulo;
    private String autor;
    private String editorial;
    private int edicion;
    private int anno_de_publicacion;

    @Override
    public boolean equals(Object libro) {
        return this == libro || (libro instanceof Libro && ISBN.equals(((Libro) libro).ISBN));
    }

    @Override
    public int compareTo(Libro libro) {
        return ISBN.compareTo(libro.ISBN);
    }

    @Override
    public String toString() {
        return "id: " + id + "\n"
                + "ISBN               : " + ISBN + "\n"
                + "titulo             : " + titulo + "\n"
                + "autor              : " + autor + "\n"
                + "editorial          : " + editorial + "\n"
                + "edicion            : " + edicion + "\n"
                + "anno de publicacion: " + anno_de_publicacion + "\n";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getEdicion() {
        return edicion;
    }

    public void setEdicion(int edicion) {
        this.edicion = edicion;
    }

    public int getAnno_de_publicacion() {
        return anno_de_publicacion;
    }

    public void setAnno_de_publicacion(int anno_de_publicacion) {
        this.anno_de_publicacion = anno_de_publicacion;
    }

    public Object[] asRow() {
        Object[] objs = {ISBN, titulo, autor, editorial, edicion, anno_de_publicacion};

        return objs;
    }
}
