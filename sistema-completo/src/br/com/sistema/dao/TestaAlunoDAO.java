/*package br.com.sistema.dao;

import br.com.sistema.model.Aluno;

public class TestaAlunoDAO {
    public static void main(String[] args) {
        AlunoDAO dao = new AlunoDAO();
        
        // 1. Criamos um aluno fictício para testar o INSERT
        // Atenção: id_curso = 1 (Estamos assumindo que o curso com ID 1 existe devido à nossa carga inicial)
        Aluno novoAluno = new Aluno(
            "123456-7", 
            "Thiago Cavalcante", 
            "2000-05-18", 
            "123.456.789-00", 
            "thiago@email.com", 
            "Rua Humberto Godoy, 35", 
            "São Paulo", 
            "SP", 
            "(11) 99999-9999", 
            1 
        );
        
        System.out.println("Tentando salvar o aluno no banco...");
        
        try {
            // Executa o insert
            dao.salvar(novoAluno);
            System.out.println("✅ Aluno salvo com sucesso!\n");
            
            // 2. Agora tentamos BUSCAR o aluno que acabamos de salvar para testar o SELECT
            System.out.println("Tentando buscar o aluno pelo RGM '123456-7'...");
            Aluno alunoEncontrado = dao.buscarPorRgm("123456-7");
            
            if (alunoEncontrado != null) {
                System.out.println("=========================================");
                System.out.println("🎉 ALUNO ENCONTRADO NO BANCO!");
                System.out.println("=========================================");
                System.out.println("RGM: " + alunoEncontrado.getRgm());
                System.out.println("Nome: " + alunoEncontrado.getNome());
                System.out.println("CPF: " + alunoEncontrado.getCpf());
                System.out.println("Cidade: " + alunoEncontrado.getMunicipio() + " - " + alunoEncontrado.getUf());
                System.out.println("=========================================");
            } else {
                System.out.println("❌ Erro: O comando rodou, mas o aluno não foi encontrado.");
            }
            
        } catch (Exception e) {
            System.err.println("\n❌ ERRO NO TESTE DA DAO!");
            System.err.println("Motivo: " + e.getMessage());
            System.err.println("\nDica: Se disser 'Foreign key constraint fails', certifique-se de rodar a carga inicial de cursos no MySQL primeiro!");
        }
    }
}*/