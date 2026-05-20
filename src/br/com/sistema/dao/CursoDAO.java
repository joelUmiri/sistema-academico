package br.com.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // Olha a facilidade voltando!
import java.util.List;
import br.com.sistema.model.Curso;
import br.com.sistema.util.ConnectionFactory;

public class CursoDAO {

    /**
     * Método para listar todos os cursos cadastrados de forma dinâmica.
     */
    public List<Curso> listarTodos() throws Exception {
        String sql = "SELECT * FROM tb_curso ORDER BY nome_curso";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // Criamos uma lista dinâmica que cresce sozinha
        List<Curso> lista = new ArrayList<>();
        
        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("id_curso"));
                curso.setNomeCurso(rs.getString("nome_curso"));
                curso.setCampus(rs.getString("campus"));
                curso.setPeriodo(rs.getString("periodo"));
                
                // Só joga para dentro da lista, sem se preocupar com tamanho!
                lista.add(curso);
            }
            
            return lista;
            
        } catch (SQLException e) {
            throw new Exception("Erro ao listar cursos: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            ConnectionFactory.closeConnection(conn);
        }
    }
}