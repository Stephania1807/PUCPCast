package com.example.pucpcast.DTO;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nombre;
    private String correo;
    private String codigo;
    private String rol;
    private String key;
    private String url;
    private String filename;

    public Usuario() {
    }

    public Usuario(String nombre, String correo, String codigo, String rol, String key, String filename) {
        this.nombre = nombre;
        this.correo = correo;
        this.codigo = codigo;
        this.rol = rol;
        this.key = key;
        this.filename = filename;
    }

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getCorreo() {return correo;}

    public void setCorreo(String correo) {this.correo = correo;}

    public String getCodigo() {return codigo;}

    public void setCodigo(String codigo) {this.codigo = codigo;}

    public String getRol() {return rol;}

    public void setRol(String rol) {this.rol = rol;}

    public String getKey() {return key;}

    public void setKey(String key) {this.key = key;}

    public String getUrl() {return url;}

    public void setUrl(String url) {this.url = url;}

    public String getFilename() {return filename;}

    public void setFilename(String filename) {this.filename = filename;}

    public String getDetalleAImprimir_usuario(){
        return "Nombre: "+this.nombre+"\n"+
                "Correo: "+this.correo+"\n"+
                "Codigo: "+this.codigo+"\n"+
                "Rol: "+this.rol+"\n";


    }

    public String getDetalleAImprimir_admin(){
        return "Nombre: "+this.nombre+"\n"+
                "Correo: "+this.correo+"\n"+
                "Codigo: "+this.codigo+"\n";



    }

}
