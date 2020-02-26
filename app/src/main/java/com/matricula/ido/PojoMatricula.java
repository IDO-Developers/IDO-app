package com.matricula.ido;

public class PojoMatricula {
    public String grado;
    public String modalidad;
    public String jornada;
    public String grupo;
    public String modulo;
    public String nombre;
    public String identidad;

    public PojoMatricula(String grado, String modalidad, String jornada, String grupo, String modulo, String nombre, String identidad) {
        this.grado = grado;
        this.modalidad = modalidad;
        this.jornada = jornada;
        this.grupo = grupo;
        this.modulo = modulo;
        this.nombre = nombre;
        this.identidad = identidad;
    }
    public PojoMatricula(){

    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }
}
