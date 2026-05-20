package br.com.sistema.model;

public class Aluno {
    private String rgm;
    private String nome;
    private String dataNascimento; 
    private String cpf;
    private String email;
    private String endereco;
    private String municipio;
    private String uf;
    private String celular;
    private int idCurso; 

    // Construtor vazio
    public Aluno() {
    }

    // Construtor completo
    public Aluno(String rgm, String nome, String dataNascimento, String cpf, String email, 
                 String endereco, String municipio, String uf, String celular, int idCurso) {
        this.rgm = rgm;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
        this.municipio = municipio;
        this.uf = uf;
        this.celular = celular;
        this.idCurso = idCurso;
    }

    // Métodos Getters e Setters
    public String getRgm() { return rgm; }
    public void setRgm(String rgm) { this.rgm = rgm; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }
}