package br.com.sistema.model;

public class Matricula {
    private String rgmAluno;
    private int idDisciplina;
    private String semestre;
    private double nota;
    private int faltas;

    // Construtor vazio
    public Matricula() {
    }

    // Construtor completo
    public Matricula(String rgmAluno, int idDisciplina, String semestre, double nota, int faltas) {
        this.rgmAluno = rgmAluno;
        this.idDisciplina = idDisciplina;
        this.semestre = semestre;
        this.nota = nota;
        this.faltas = faltas;
    }

    // Métodos Getters e Setters
    public String getRgmAluno() { return rgmAluno; }
    public void setRgmAluno(String rgmAluno) { this.rgmAluno = rgmAluno; }

    public int getIdDisciplina() { return idDisciplina; }
    public void setIdDisciplina(int idDisciplina) { this.idDisciplina = idDisciplina; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }

    public int getFaltas() { return faltas; }
    public void setFaltas(int faltas) { this.faltas = faltas; }
}