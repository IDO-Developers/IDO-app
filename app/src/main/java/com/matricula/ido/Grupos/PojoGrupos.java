package com.matricula.ido.Grupos;


public class PojoGrupos {

  public int id;
  public String grado;
  public String modalidad;
  public String jornada;
  public String grupo;
  public String modulo;
  public String num_hombres;
  public String num_mujeres;

  public PojoGrupos(int id, String grado, String modalidad,String jornada,String grupo,String modulo,String num_hombres,String num_mujeres) {
    this.id = id;
    this.grado = grado;
    this.modalidad = modalidad;
    this.jornada = jornada;
    this.grupo = grupo;
    this.modulo = modulo;
    this.num_hombres = num_hombres;
    this.num_mujeres = num_mujeres;
  }

  public PojoGrupos() {

  }

  public String getNum_hombres() {
    return num_hombres;
  }

  public void setNum_hombres(String num_hombres) {
    this.num_hombres = num_hombres;
  }

  public String getNum_mujeres() {
    return num_mujeres;
  }

  public void setNum_mujeres(String num_mujeres) {
    this.num_mujeres = num_mujeres;
  }

  public String getModulo() {
    return modulo;
  }

  public void setModulo(String modulo) {
    this.modulo = modulo;
  }

  public String getJornada() {
    return jornada;
  }

  public String getGrupo() {
    return grupo;
  }

  public void setGrupo(String grupo) {
    this.grupo = grupo;
  }

  public void setJornada(String jornada) {
    this.jornada = jornada;
  }

  public String getModalidad() {
    return modalidad;
  }

  public void setModalidad(String modalidad) {
    this.modalidad = modalidad;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getGrado() {
    return grado;
  }

  public void setGrado(String grado) {
    this.grado = grado;
  }
}