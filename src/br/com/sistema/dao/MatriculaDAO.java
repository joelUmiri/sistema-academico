package br.com.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import br.com.sistema.model.Matricula;
import br.com.sistema.util.ConnectionFactory;

public class MatriculaDAO {

    /**
     * Insere um novo registro de nota/falta (Matrícula) no banco.
     */
    public void salvar(Matricula matricula) throws Exception {
        String sql = "INSERT INTO tb_matricula (rgm_aluno, id_disciplina, semestre, nota, faltas) "
                   + "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, matricula.getRgmAluno());
            ps.setInt(2, matricula.getIdDisciplina());
            ps.setString(3, matricula.getSemestre());
            ps.setDouble(4, matricula.getNota());
            ps.setInt(5, matricula.getFaltas());
            
            ps.execute();
            
        } catch (SQLException e) {
            throw new Exception("Erro ao salvar matrícula/nota: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Altera a nota e as faltas de um aluno numa determinada disciplina e semestre.
     */
    public void alterar(Matricula matricula) throws Exception {
        String sql = "UPDATE tb_matricula SET nota = ?, faltas = ? "
                   + "WHERE rgm_aluno = ? AND id_disciplina = ? AND semestre = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setDouble(1, matricula.getNota());
            ps.setInt(2, matricula.getFaltas());
            ps.setString(3, matricula.getRgmAluno());
            ps.setInt(4, matricula.getIdDisciplina());
            ps.setString(5, matricula.getSemestre());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new Exception("Erro ao alterar matrícula/nota: " + e.getMessage());
        } finally {
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }

    /**
     * Procura se já existe uma nota lançada para um aluno específico numa matéria específica.
     * Retorna o objeto Matricula preenchido ou null se ainda não houver nota lançada.
     */
    public Matricula buscarPorAlunoEDisciplina(String rgmAluno, int idDisciplina) throws Exception {
        String sql = "SELECT * FROM tb_matricula WHERE rgm_aluno = ? AND id_disciplina = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rgmAluno);
            ps.setInt(2, idDisciplina);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Matricula mat = new Matricula();
                mat.setRgmAluno(rs.getString("rgm_aluno"));
                mat.setIdDisciplina(rs.getInt("id_disciplina"));
                mat.setSemestre(rs.getString("semestre"));
                mat.setNota(rs.getDouble("nota"));
                mat.setFaltas(rs.getInt("faltas"));
                return mat;
            }
            
            return null; // Nenhuma nota lançada ainda para essa combinação
            
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar dados da matrícula: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }
}