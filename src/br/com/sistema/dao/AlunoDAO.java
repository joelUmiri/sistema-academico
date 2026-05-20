package br.com.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import br.com.sistema.model.Aluno;
import br.com.sistema.util.ConnectionFactory;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    /**
     * Método para salvar (inserir) um novo aluno no banco de dados.
     */
    public void salvar(Aluno aluno) throws Exception {
        String sql = "INSERT INTO tb_aluno (rgm, nome, data_nascimento, cpf, email, endereco, "
                   + "municipio, uf, celular) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, aluno.getRgm());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getDataNascimento()); 
            ps.setString(4, aluno.getCpf());
            ps.setString(5, aluno.getEmail());
            ps.setString(6, aluno.getEndereco());
            ps.setString(7, aluno.getMunicipio());
            ps.setString(8, aluno.getUf());
            ps.setString(9, aluno.getCelular());
            
            ps.execute();
            
        } catch (SQLException e) {
            throw new Exception("Erro ao salvar o aluno no banco: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Método para buscar um aluno específico pelo seu RGM.
     */
    public Aluno buscarPorRgm(String rgm) throws Exception {
        String sql = "SELECT * FROM tb_aluno WHERE rgm = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null; 
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rgm);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Aluno aluno = new Aluno();
                aluno.setRgm(rs.getString("rgm"));
                aluno.setNome(rs.getString("nome"));
                aluno.setDataNascimento(rs.getString("data_nascimento"));
                aluno.setCpf(rs.getString("cpf"));
                aluno.setEmail(rs.getString("email"));
                aluno.setEndereco(rs.getString("endereco"));
                aluno.setMunicipio(rs.getString("municipio"));
                aluno.setUf(rs.getString("uf"));
                aluno.setCelular(rs.getString("celular"));
                
                return aluno; 
            }
            
            return null; 
            
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar aluno por RGM: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Insere ou atualiza o vínculo do aluno com um curso na tabela intermediária (Muitos para Muitos).
     */
    public void matricularAlunoNoCurso(String rgmAluno, int idCurso, String campus, String periodo) throws Exception {
        String sql = "INSERT INTO tb_aluno_curso (rgm_aluno, id_curso, campus, periodo) VALUES (?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE campus = ?, periodo = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, rgmAluno);
            ps.setInt(2, idCurso);
            ps.setString(3, campus);
            ps.setString(4, periodo);
            ps.setString(5, campus);
            ps.setString(6, periodo);

            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new Exception("Erro ao matricular aluno no curso: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Busca a matrícula mais recente ou ativa do aluno.
     */
    public MatriculaCurso buscarMatriculaRecente(String rgmAluno) throws Exception {
        String sql = "SELECT id_curso, campus, periodo FROM tb_aluno_curso WHERE rgm_aluno = ? ORDER BY id DESC LIMIT 1";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rgmAluno);
            
            rs = ps.executeQuery();
            if (rs.next()) {
                MatriculaCurso matricula = new MatriculaCurso();
                matricula.setIdCurso(rs.getInt("id_curso"));
                matricula.setCampus(rs.getString("campus"));
                matricula.setPeriodo(rs.getString("periodo"));
                return matricula;
            }
            return null;
            
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar matrícula do aluno: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Classe de transferência (DTO) para os dados da tabela intermediária.
     */
    public static class MatriculaCurso {
        private int idCurso;
        private String campus;
        private String periodo;

        public int getIdCurso() { return idCurso; }
        public void setIdCurso(int idCurso) { this.idCurso = idCurso; }
        public String getCampus() { return campus; }
        public void setCampus(String campus) { this.campus = campus; }
        public String getPeriodo() { return periodo; }
        public void setPeriodo(String periodo) { this.periodo = periodo; }
    }
    /**
     * Método para alterar/atualizar os dados de um aluno já existente pelo RGM.
     */
    public void alterar(Aluno aluno) throws Exception {
        String sql = "UPDATE tb_aluno SET nome = ?, data_nascimento = ?, cpf = ?, email = ?, "
                   + "endereco = ?, municipio = ?, uf = ?, celular = ? WHERE rgm = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, aluno.getNome());
            ps.setString(2, aluno.getDataNascimento()); 
            ps.setString(3, aluno.getCpf());
            ps.setString(4, aluno.getEmail());
            ps.setString(5, aluno.getEndereco());
            ps.setString(6, aluno.getMunicipio());
            ps.setString(7, aluno.getUf());
            ps.setString(8, aluno.getCelular());
            ps.setString(9, aluno.getRgm()); // O RGM vai no WHERE para atualizar o cara certo
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new Exception("Erro ao alterar os dados do aluno no banco: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }
    /**
     * Método para deletar um aluno do banco de dados através do seu RGM.
     * Nota: O cascade do MySQL apagará as matrículas vinculadas automaticamente!
     */
    public void excluir(String rgm) throws Exception {
        String sql = "DELETE FROM tb_aluno WHERE rgm = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, rgm);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new Exception("Erro ao excluir o aluno do banco: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }
    
    /**
     * Insere ou atualiza as notas e faltas do aluno na tabela tb_matricula (Aba 3).
     * Usa o 'ON DUPLICATE KEY UPDATE' para atualizar caso a nota daquele semestre já exista.
     */
    public void salvarNotasEFaltas(String rgm, int idDisciplina, String semestre, double nota, int faltas) throws Exception {
        String sql = "INSERT INTO tb_matricula (rgm_aluno, id_disciplina, semestre, nota, faltas) "
                   + "VALUES (?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE nota = ?, faltas = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            
            // Dados para o INSERT
            ps.setString(1, rgm);
            ps.setInt(2, idDisciplina);
            ps.setString(3, semestre);
            ps.setDouble(4, nota);
            ps.setInt(5, faltas);
            
            // Dados para o UPDATE (caso já exista a chave composta rgm+disciplina+semestre)
            ps.setDouble(6, nota);
            ps.setInt(7, faltas);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new Exception("Erro ao salvar notas e faltas no banco: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }
    /**
     * Busca no banco de dados apenas as disciplinas vinculadas a um determinado curso.
     */
    public List<String> buscarDisciplinasPorCurso(int idCurso) throws Exception {
        List<String> disciplinas = new ArrayList<>();
        String sql = "SELECT d.nome_disciplina FROM tb_disciplina d "
                   + "JOIN tb_curso_disciplina cd ON d.id_disciplina = cd.id_disciplina "
                   + "WHERE cd.id_curso = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCurso);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                disciplinas.add(rs.getString("nome_disciplina"));
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar disciplinas do curso: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
        return disciplinas;
    }
    
    /**
     * Salva ou atualiza a nota e faltas de um aluno para uma disciplina e semestre específicos.
     */
    public void salvarOuAtualizarNota(String rgm, String nomeDisciplina, String semestre, double nota, int faltas) throws Exception {
        // 1. Descobre o ID da disciplina com base no nome selecionado no ComboBox
        String sqlIdDisc = "SELECT id_disciplina FROM tb_disciplina WHERE nome_disciplina = ?";
        
        // 2. Query que insere ou atualiza caso a chave composta (rgm, disciplina, semestre) já exista
        String sqlSalvarNota = "INSERT INTO tb_matricula (rgm_aluno, id_disciplina, semestre, nota, faltas) "
                             + "VALUES (?, ?, ?, ?, ?) "
                             + "ON DUPLICATE KEY UPDATE nota = ?, faltas = ?";

        java.sql.Connection conn = null;
        java.sql.PreparedStatement psId = null;
        java.sql.PreparedStatement psSalvar = null;
        java.sql.ResultSet rs = null;

        try {
            // --- CORREÇÃO: Usando a sua fábrica de conexões correta ---
            conn = br.com.sistema.util.ConnectionFactory.getConnection(); 
            
            // Passo A: Buscar o ID da disciplina
            psId = conn.prepareStatement(sqlIdDisc);
            psId.setString(1, nomeDisciplina);
            rs = psId.executeQuery();
            
            int idDisciplina = -1;
            if (rs.next()) {
                idDisciplina = rs.getInt("id_disciplina");
            } else {
                throw new Exception("Disciplina '" + nomeDisciplina + "' não foi encontrada no banco de dados.");
            }
            
            // Passo B: Executar o Insert/Update na tabela tb_matricula
            psSalvar = conn.prepareStatement(sqlSalvarNota);
            psSalvar.setString(1, rgm);
            psSalvar.setInt(2, idDisciplina);
            psSalvar.setString(3, semestre);
            psSalvar.setDouble(4, nota);
            psSalvar.setInt(5, faltas);
            
            // Valores que serão atualizados no UPDATE se já existir o registro
            psSalvar.setDouble(6, nota);
            psSalvar.setInt(7, faltas);
            
            psSalvar.executeUpdate();

        } finally {
            // Garante o fechamento das conexões
            if (rs != null) rs.close();
            if (psId != null) psId.close();
            if (psSalvar != null) psSalvar.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Busca a nota e as faltas de um aluno em uma disciplina e semestre específicos.
     * Retorna um array onde a posição [0] é a nota (String) e a posição [1] são as faltas (String).
     */
    public String[] buscarNotaEFaltas(String rgm, String nomeDisciplina, String semestre) throws Exception {
        String sql = "SELECT m.nota, m.faltas FROM tb_matricula m "
                   + "INNER JOIN tb_disciplina d ON m.id_disciplina = d.id_disciplina "
                   + "WHERE m.rgm_aluno = ? AND d.nome_disciplina = ? AND m.semestre = ?";
        
        java.sql.Connection conn = null;
        java.sql.PreparedStatement ps = null;
        java.sql.ResultSet rs = null;
        
        String[] resultado = null; // Retorna null se não encontrar registro
        
        try {
            conn = br.com.sistema.util.ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rgm);
            ps.setString(2, nomeDisciplina);
            ps.setString(3, semestre);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                resultado = new String[2];
                // Substitui o ponto por vírgula para exibir no padrão do seu ComboBox (ex: "7.5" vira "7,5")
                double nota = rs.getDouble("nota");
                resultado[0] = String.valueOf(nota).replace(".", ",");
                // Se a nota terminar em ",0", você pode manter ou tratar, o ComboBox vai reconhecer
                if (resultado[0].endsWith(",0")) {
                    resultado[0] = resultado[0].substring(0, resultado[0].length() - 2) + ",0";
                }
                resultado[1] = String.valueOf(rs.getInt("faltas"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        
        return resultado;
    }
    
    /**
     * Exclui o registro de notas e faltas de um aluno para uma disciplina e semestre específicos.
     */
    public void excluirNotaEFaltas(String rgm, String nomeDisciplina, String semestre) throws Exception {
        String sql = "DELETE m FROM tb_matricula m "
                   + "INNER JOIN tb_disciplina d ON m.id_disciplina = d.id_disciplina "
                   + "WHERE m.rgm_aluno = ? AND d.nome_disciplina = ? AND m.semestre = ?";

        java.sql.Connection conn = null;
        java.sql.PreparedStatement ps = null;

        try {
            conn = br.com.sistema.util.ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rgm);
            ps.setString(2, nomeDisciplina);
            ps.setString(3, semestre);
            
            ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Busca todas as notas e faltas de um aluno para montar o boletim.
     * Retorna uma lista de arrays de String, onde cada array representa uma linha da tabela.
     */
    public java.util.List<String[]> buscarDadosBoletim(String rgm) throws Exception {
        String sql = "SELECT d.nome_disciplina, m.semestre, m.nota, m.faltas "
                   + "FROM tb_matricula m "
                   + "INNER JOIN tb_disciplina d ON m.id_disciplina = d.id_disciplina "
                   + "WHERE m.rgm_aluno = ? "
                   + "ORDER BY m.semestre ASC, d.nome_disciplina ASC";
        
        java.sql.Connection conn = null;
        java.sql.PreparedStatement ps = null;
        java.sql.ResultSet rs = null;
        
        java.util.List<String[]> lista = new java.util.ArrayList<>();
        
        try {
            conn = br.com.sistema.util.ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rgm);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                String[] linha = new String[4];
                linha[0] = rs.getString("nome_disciplina");
                linha[1] = rs.getString("semestre");
                
                double nota = rs.getDouble("nota");
                linha[2] = String.valueOf(nota).replace(".", ",");
                if (linha[2].endsWith(",0")) {
                    linha[2] = linha[2].substring(0, linha[2].length() - 2) + ",0";
                }
                
                linha[3] = String.valueOf(rs.getInt("faltas"));
                lista.add(linha);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        
        return lista;
    }
}