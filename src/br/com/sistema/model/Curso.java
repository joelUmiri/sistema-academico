package br.com.sistema.model;

public class Curso {
    private int idCurso;
    private String nomeCurso;
    private String campus;
    private String periodo;

    // Construtor vazio (obrigatório para as consultas do banco)
    public Curso() {
    }

    // Construtor completo (facilita se precisares de instanciar rapidamente)
    public Curso(int idCurso, String nomeCurso, String campus, String periodo) {
        this.idCurso = idCurso;
        this.nomeCurso = nomeCurso;
        this.campus = campus;
        this.periodo = periodo;
    }

    // Métodos Getters e Setters
    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public String getNomeCurso() { return nomeCurso; }
    public void setNomeCurso(String nomeCurso) { this.nomeCurso = nomeCurso; }

    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
}