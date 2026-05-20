package br.com.sistema.model;

public class Disciplina {
    private int idDisciplina;
    private String nomeDisciplina;

    // Construtor vazio
    public Disciplina() {
    }

    // Construtor completo
    public Disciplina(int idDisciplina, String nomeDisciplina) {
        this.idDisciplina = idDisciplina;
        this.nomeDisciplina = nomeDisciplina;
    }

    // Métodos Getters e Setters
    public int getIdDisciplina() { return idDisciplina; }
    public void setIdDisciplina(int idDisciplina) { this.idDisciplina = idDisciplina; }

    public String getNomeDisciplina() { return nomeDisciplina; }
    public void setNomeDisciplina(String nomeDisciplina) { this.nomeDisciplina = nomeDisciplina; }
}