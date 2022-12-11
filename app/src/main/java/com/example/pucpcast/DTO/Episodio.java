package com.example.pucpcast.DTO;

public class Episodio {

    String id;
    String titulo;
    String categoria;
    String descripcion;
    String registrador;
    String url;

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getTitulo() {return titulo;}

    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getCategoria() {return categoria;}

    public void setCategoria(String categoria) {this.categoria = categoria;}

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}


    public String getRegistrador() {
        return registrador;
    }

    public void setRegistrador(String registrador) {
        this.registrador = registrador;
    }

}
