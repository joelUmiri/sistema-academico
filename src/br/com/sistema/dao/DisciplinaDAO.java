package br.com.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.com.sistema.model.Disciplina;
import br.com.sistema.util.ConnectionFactory;

public class DisciplinaDAO {

    /**
     * Método para listar todas as disciplinas para o JComboBox.
     */
    public List<Disciplina> listarTodas() throws Exception {
        String sql = "SELECT * FROM tb_disciplina ORDER BY nome_disciplina";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        List<Disciplina> lista = new ArrayList<>();
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setIdDisciplina(rs.getInt("id_disciplina"));
                disciplina.setNomeDisciplina(rs.getString("nome_disciplina"));
                
                lista.add(disciplina);
            }
            
            return lista;
            
        } catch (SQLException e) {
            throw new Exception("Erro ao listar disciplinas: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }
}