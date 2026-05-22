package br.com.sistema.view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.com.sistema.dao.AlunoDAO;
import br.com.sistema.model.Aluno;

public class TelaSistema extends JFrame {

    // Componente principal das abas
    private JTabbedPane abas;
    private JPanel painelDadosPessoais;
    private JPanel painelCurso;
    private JPanel painelNotasFaltas;
    private JPanel painelBoletim;

    // Componentes da Aba 1: Dados Pessoais
    private JTextField txtRgm;
    private JTextField txtNome;
    private JFormattedTextField txtDataNascimento;
    private JFormattedTextField txtCpf;
    private JTextField txtEmail;
    private JTextField txtEndereco;
    private JTextField txtMunicipio;
    private JComboBox<String> cbUf;
    private JFormattedTextField txtCelular;

    // Componentes da Aba 2: Curso
    private JComboBox<String> cbCurso;
    private JComboBox<String> cbCampus;
    private JRadioButton rbMatutino;
    private JRadioButton rbVespertino;
    private JRadioButton rbNoturno;
    private ButtonGroup grupoPeriodo;

    // Botões da Aba Curso
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnConsultar;
    private JButton btnExcluir;
    private JButton btnSair;

    // Componentes da Aba 3: Notas e Faltas
    private JTextField txtNotaRgm;
    private JTextField txtNotaNomeExibir;  
    private JTextField txtNotaCursoExibir; 
    private JComboBox<String> cbNotaDisciplina;
    private JComboBox<String> cbNotaSemestre;
    private JComboBox<String> cbNotaValor;  
    private JTextField txtNotaFaltas;

    // --- CORREÇÃO 1: DECLARAÇÃO DOS BOTÕES COM ÍCONES DA ABA 3 COMO ATRIBUTOS ---
    private JButton btnNotaConsultar;
    private JButton btnNotaSalvar;
    private JButton btnNotaAlterar;
    private JButton btnNotaExcluir;
    private JButton btnNotaSair;

    // Componentes da Aba 4: Boletim
    private JTextArea txtBoletimExibir;

    // Itens de Menu Globais para Ações
    private JMenuItem itemSalvarMenu;
    private JMenuItem itemConsultarMenu;
    private JMenuItem itemNotaConsultarMenu;
    
    // --- CORREÇÃO 2: DECLARAÇÃO DO ITEM DE SALVAR NOTAS COMO ATRIBUTO ---
    private JMenuItem itemNotaSalvarMenu;
    
    private final String[] ESTADOS = {
    	    "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", 
    	    "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO"
    	};

    public TelaSistema() {
        setTitle("Sistema Acadêmico - UNICID");
        setSize(700, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Inicializa a Barra de Menus
        configurarMenu();

        // 2. Inicializa o JTabbedPane e as Abas
        abas = new JTabbedPane();
        
        painelDadosPessoais = new JPanel(null);
        painelCurso = new JPanel(null);
        painelNotasFaltas = new JPanel(null);
        painelBoletim = new JPanel(null);

        // Desenha os componentes internos de cada uma das 4 abas
        construirAbaDadosPessoais();
        construirAbaCurso();
        construirAbaNotasFaltas();
        construirAbaBoletim();

        // Adiciona os painéis dentro do componente de abas
        abas.addTab("Dados Pessoais", painelDadosPessoais);
        abas.addTab("Curso", painelCurso);
        abas.addTab("Notas e Faltas", painelNotasFaltas);
        abas.addTab("Boletim", painelBoletim);

        add(abas, BorderLayout.CENTER);

        // 3. Ativa as ações dos botões (Listeners)
        configurarAcoes();
    }

    /**
     * Método centralizado para gerenciar os cliques de botões e menus.
     */
    private void configurarAcoes() {
        // --- AÇÃO CONSULTAR ---
        btnConsultar.addActionListener(e -> executarConsulta());     // Ativa a Aba Curso
        btnNotaConsultar.addActionListener(e -> executarConsulta()); // Ativa a Aba Notas e Faltas
        itemConsultarMenu.addActionListener(e -> executarConsulta());
        itemNotaConsultarMenu.addActionListener(e -> executarConsulta());

        // --- AÇÃO SALVAR ALUNO ---
        btnSalvar.addActionListener(e -> executarSalvar());
        itemSalvarMenu.addActionListener(e -> executarSalvar());
        
        // --- AÇÃO SALVAR NOTAS ---
        btnNotaSalvar.addActionListener(e -> executarSalvarNotas());
        itemNotaSalvarMenu.addActionListener(e -> executarSalvarNotas());
        
        // --- AÇÃO SAIR ---
        btnSair.addActionListener(e -> System.exit(0));
        btnNotaSair.addActionListener(e -> System.exit(0));
        
     // --- AÇÃO ALTERAR ---
        btnAlterar.addActionListener(e -> executarAlterar());
        btnNotaAlterar.addActionListener(e -> executarAlterarNotas()); // <- Atualizado aqui
        
        // --- AÇÃO EXCLUIR ---
        btnExcluir.addActionListener(e -> executarExcluir());
        btnNotaExcluir.addActionListener(e -> executarExcluirNotas()); // <- Atualizado aqui

        // --- ESCUTA OS COMBOBOXES PARA ATUALIZAR AS NOTAS AUTOMATICAMENTE ---
        cbNotaDisciplina.addActionListener(e -> carregarNotaFaltaCampos());
        cbNotaSemestre.addActionListener(e -> carregarNotaFaltaCampos());
    }
   
    
    /**
     * Captura os dados da tela, valida e salva um novo aluno no banco de dados.
     */
    private void executarSalvar() {
        // 1. Validação básica de campos vazios
        if (txtRgm.getText().trim().isEmpty() || txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "RGM e Nome são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. NOVA VALIDAÇÃO: Impedir cadastro de RGM duplicado
        try {
            AlunoDAO daoVerifica = new AlunoDAO();
            if (daoVerifica.buscarPorRgm(txtRgm.getText().trim()) != null) {
                JOptionPane.showMessageDialog(this, "Erro: Este RGM já está cadastrado no sistema. Use o botão 'Alterar' para modificar os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao verificar RGM: " + e.getMessage());
            return;
        }

        // 3. Se passou pela verificação, executa o salvar normal
        try {
            Aluno aluno = new Aluno();
            // ... (seu código original de preencher o objeto aluno) ...
            
            AlunoDAO dao = new AlunoDAO();
            dao.salvar(aluno);
            // ... (seu código original de matricular) ...
            
            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar aluno: " + ex.getMessage());
        }
    }
    /**
     * Captura as informações da Aba 3 e envia para o banco de dados.
     */
    private void executarSalvarNotas() {
        String rgm = txtNotaRgm.getText().trim();
        
        // Validação básica se o aluno foi consultado primeiro
        if (rgm.isEmpty() || txtNotaNomeExibir.getText().contains("carregado automaticamente")) {
            JOptionPane.showMessageDialog(this, "Por favor, consulte um aluno válido pelo RGM antes de lançar notas!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validação se há alguma disciplina carregada
        if (cbNotaDisciplina.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina selecionada ou disponível para este aluno.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String disciplina = cbNotaDisciplina.getSelectedItem().toString();
            String semestre = cbNotaSemestre.getSelectedItem().toString();
            
            // Converte a nota selecionada (ex: "7,5" vira 7.5)
            String notaTexto = cbNotaValor.getSelectedItem().toString().replace(",", ".");
            double nota = Double.parseDouble(notaTexto);
            
            // Valida e converte o campo de faltas
            String faltasTexto = txtNotaFaltas.getText().trim();
            int faltas = 0;
            if (!faltasTexto.isEmpty()) {
                faltas = Integer.parseInt(faltasTexto);
                if (faltas < 0) {
                    throw new NumberFormatException();
                }
            }

            // Envia os dados processados para a regra de negócio no DAO
            AlunoDAO dao = new AlunoDAO();
            dao.salvarOuAtualizarNota(rgm, disciplina, semestre, nota, faltas);

            JOptionPane.showMessageDialog(this, "Notas e faltas salvas com sucesso no banco de dados!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
         // --- NOVA LINHA: Força o boletim a atualizar logo após salvar ---
            gerarBoletimTexto(rgm, txtNotaNomeExibir.getText(), txtNotaCursoExibir.getText());
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "O campo 'Faltas' deve conter apenas números inteiros positivos!", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar notas: " + ex.getMessage(), "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Altera as notas e faltas já existentes na Aba 3 (chama a mesma lógica do salvar).
     */
    private void executarAlterarNotas() {
        String rgm = txtNotaRgm.getText().trim();
        
        if (rgm.isEmpty() || txtNotaNomeExibir.getText().contains("carregado automaticamente")) {
            JOptionPane.showMessageDialog(this, "Por favor, consulte um aluno válido pelo RGM antes de alterar as notas!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbNotaDisciplina.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina selecionada para alteração.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Como o nosso método do DAO usa ON DUPLICATE KEY UPDATE, salvar e alterar executam a mesma lógica com sucesso!
        try {
            String disciplina = cbNotaDisciplina.getSelectedItem().toString();
            String semestre = cbNotaSemestre.getSelectedItem().toString();
            String notaTexto = cbNotaValor.getSelectedItem().toString().replace(",", ".");
            double nota = Double.parseDouble(notaTexto);
            
            String faltasTexto = txtNotaFaltas.getText().trim();
            int faltas = 0;
            if (!faltasTexto.isEmpty()) {
                faltas = Integer.parseInt(faltasTexto);
                if (faltas < 0) throw new NumberFormatException();
            }

            AlunoDAO dao = new AlunoDAO();
            dao.salvarOuAtualizarNota(rgm, disciplina, semestre, nota, faltas);

            JOptionPane.showMessageDialog(this, "Notas e faltas alteradas com sucesso no banco de dados!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
         // --- NOVA LINHA: Força o boletim a atualizar logo após alterar ---
            gerarBoletimTexto(rgm, txtNotaNomeExibir.getText(), txtNotaCursoExibir.getText());
            
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "O campo 'Faltas' deve conter apenas números inteiros positivos!", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar notas: " + ex.getMessage(), "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Pede confirmação ao utilizador e remove o registo de nota/falta selecionado na Aba 3.
     */
    private void executarExcluirNotas() {
        String rgm = txtNotaRgm.getText().trim();
        Object discObj = cbNotaDisciplina.getSelectedItem();
        Object semObj = cbNotaSemestre.getSelectedItem();

        if (rgm.isEmpty() || discObj == null || semObj == null || txtNotaNomeExibir.getText().contains("carregado automaticamente")) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um aluno e uma disciplina válidos antes de excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String disciplina = discObj.toString();
        String semestre = semObj.toString();

        int resposta = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja apagar o registro de notas/faltas da disciplina:\n" + disciplina + " (" + semestre + ")?", 
                "Confirmar Exclusão de Nota", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                AlunoDAO dao = new AlunoDAO();
                dao.excluirNotaEFaltas(rgm, disciplina, semestre);

                // --- RESET VISUAL (PARA JCOMBOBOX) ---
                cbNotaValor.setSelectedIndex(0); // Volta para a primeira opção (ex: "0,0")
                txtNotaFaltas.setText("0");

                JOptionPane.showMessageDialog(this, "Registro removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // --- ATUALIZAÇÃO DO BOLETIM ---
                // Como você manteve o Enum, ao chamar o gerarBoletimTexto, 
                // ele deve ler o estado atual do banco (que agora está sem a nota)
                gerarBoletimTexto(rgm, txtNotaNomeExibir.getText(), txtNotaCursoExibir.getText());
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir registro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Executa a busca no banco de dados e joga as informações na interface visual
     */
    private void executarConsulta() {
        String rgmParaBuscar = txtRgm.getText().trim();
        if (rgmParaBuscar.isEmpty()) {
            rgmParaBuscar = txtNotaRgm.getText().trim();
        }

        if (rgmParaBuscar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um RGM para consultar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            AlunoDAO dao = new AlunoDAO();
            Aluno aluno = dao.buscarPorRgm(rgmParaBuscar);

            if (aluno != null) {
                txtRgm.setText(aluno.getRgm());
                txtNome.setText(aluno.getNome());
                
                String dataBanco = aluno.getDataNascimento();
                if (dataBanco != null && dataBanco.length() == 10) {
                    String[] partes = dataBanco.split("-");
                    String dataFormatadaBr = partes[2] + "/" + partes[1] + "/" + partes[0];
                    txtDataNascimento.setText(dataFormatadaBr);
                } else {
                    txtDataNascimento.setText("");
                }
                
                txtCpf.setText(aluno.getCpf());
                txtEmail.setText(aluno.getEmail());
                txtEndereco.setText(aluno.getEndereco());
                txtMunicipio.setText(aluno.getMunicipio());
                cbUf.setSelectedItem(aluno.getUf());
                txtCelular.setText(aluno.getCelular());

                AlunoDAO.MatriculaCurso matricula = dao.buscarMatriculaRecente(rgmParaBuscar);
                String nomeCurso = "Análise e Desenvolvimento de Sistemas"; 

                if (matricula != null) {
                    cbCampus.setSelectedItem(matricula.getCampus());
                    
                    if ("Matutino".equalsIgnoreCase(matricula.getPeriodo())) rbMatutino.setSelected(true);
                    else if ("Vespertino".equalsIgnoreCase(matricula.getPeriodo())) rbVespertino.setSelected(true);
                    else if ("Noturno".equalsIgnoreCase(matricula.getPeriodo())) rbNoturno.setSelected(true);
                    else grupoPeriodo.clearSelection();

                    if (matricula.getIdCurso() == 2) nomeCurso = "Ciência da Computação";
                    else if (matricula.getIdCurso() == 3) nomeCurso = "Engenharia de Software";
                    cbCurso.setSelectedItem(nomeCurso);

                    cbNotaDisciplina.removeAllItems();
                    java.util.List<String> listaMaterias = dao.buscarDisciplinasPorCurso(matricula.getIdCurso());
                    for (String materia : listaMaterias) {
                        cbNotaDisciplina.addItem(materia);
                    }

                    // --- CARREGA OS VALORES ATUAIS DA DISCIPLINA SELECIONADA ---
                    carregarNotaFaltaCampos();

                } else {
                    grupoPeriodo.clearSelection();
                    cbNotaDisciplina.removeAllItems();
                    cbNotaValor.setSelectedIndex(0);
                    txtNotaFaltas.setText("");
                }

                txtNotaRgm.setText(aluno.getRgm());
                txtNotaNomeExibir.setText(aluno.getNome());
                txtNotaCursoExibir.setText(nomeCurso);
                
             // --- NOVA LINHA: Atualiza a Aba 4 com o boletim completo ---
                gerarBoletimTexto(aluno.getRgm(), aluno.getNome(), nomeCurso);
                
                JOptionPane.showMessageDialog(this, "Aluno encontrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this, "Nenhum aluno encontrado com o RGM informado.", "Não Encontrado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar: " + ex.getMessage(), "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Método auxiliar para ler o banco e preencher os campos de nota e falta na Aba 3.
     */
    private void carregarNotaFaltaCampos() {
        String rgm = txtNotaRgm.getText().trim();
        Object discObj = cbNotaDisciplina.getSelectedItem();
        Object semObj = cbNotaSemestre.getSelectedItem();

        if (!rgm.isEmpty() && discObj != null && semObj != null) {
            try {
                AlunoDAO dao = new AlunoDAO();
                String[] dadosNota = dao.buscarNotaEFaltas(rgm, discObj.toString(), semObj.toString());

                if (dadosNota != null) {
                    cbNotaValor.setSelectedItem(dadosNota[0]); // Seleciona a nota correspondente (ex: "7,5")
                    txtNotaFaltas.setText(dadosNota[1]);       // Escreve as faltas salvas
                } else {
                    // Reseta para os valores padrões caso ainda não existam notas para essa matéria/semestre
                    cbNotaValor.setSelectedIndex(0); // "0,0"
                    txtNotaFaltas.setText("0");
                }
            } catch (Exception ex) {
                // Deixa silencioso para não interromper a navegação nos ComboBoxes
            }
        }
    }
    
    /**
     * Busca os dados do aluno e gera o texto formatado do boletim no JTextArea (Sem Aprovado/Reprovado).
     */
    private void gerarBoletimTexto(String rgm, String nomeAluno, String nomeCurso) {
        try {
            AlunoDAO dao = new AlunoDAO();
            java.util.List<String[]> dadosBoletim = dao.buscarDadosBoletim(rgm);
            
            StringBuilder sb = new StringBuilder();
            sb.append("=================================================================\n");
            sb.append("           EXTRATO DE RENDIMENTO ACADÊMICO - UNICID              \n");
            sb.append("=================================================================\n");
            sb.append(String.format(" RGM: %-15s Aluno: %-40s\n", rgm, nomeAluno));
            sb.append(String.format(" Curso: %-58s\n", nomeCurso));
            sb.append("-----------------------------------------------------------------\n");
            
            // Cabeçalho atualizado: Removeu a coluna SITUAÇÃO
            // %-35s = Disciplina (espaço ampliado para 35 caracteres)
            // %-12s = Semestre
            // %-8s  = Nota
            // %-6s  = Faltas
            sb.append(String.format(" %-35s | %-12s | %-8s | %-6s\n", "DISCIPLINA", "SEMESTRE", "NOTA", "FALTAS"));
            sb.append("-----------------------------------------------------------------\n");
            
            if (dadosBoletim.isEmpty()) {
                sb.append("\n          Nenhuma nota ou falta lançada para este aluno.\n\n");
            } else {
                for (String[] reg : dadosBoletim) {
                    String disciplina = reg[0];
                    String semestre = reg[1];
                    String notaStr = reg[2];
                    String faltasStr = reg[3];
                    
                    // Se o nome da disciplina for muito longo, corta para não quebrar o layout da tabela
                    if (disciplina.length() > 35) {
                        disciplina = disciplina.substring(0, 32) + "...";
                    }
                    
                    // Linha de dados atualizada: Removeu o %-9s do final e a variável situacao
                    sb.append(String.format(" %-35s | %-12s | %-8s | %-6s\n", 
                            disciplina, semestre, notaStr, faltasStr));
                }
            }
            
            sb.append("=================================================================\n");
            sb.append("   * Documento para simples consulta de notas e frequência. *\n");
            sb.append("=================================================================");
            
            txtBoletimExibir.setText(sb.toString());
            
        } catch (Exception e) {
            txtBoletimExibir.setText("\n Erro ao gerar boletim automático: " + e.getMessage());
        }
    }
    
    /**
     * Captura os dados alterados na tela e atualiza o cadastro do aluno no banco.
     */
    private void executarAlterar() {
        if (txtRgm.getText().trim().isEmpty() || txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "RGM e Nome são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. NOVA VALIDAÇÃO: Garantir que o aluno realmente existe no banco
        try {
            AlunoDAO daoVerifica = new AlunoDAO();
            if (daoVerifica.buscarPorRgm(txtRgm.getText().trim()) == null) {
                JOptionPane.showMessageDialog(this, "Erro: Aluno não encontrado. Não é possível alterar um registro que não existe.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao verificar existência: " + e.getMessage());
            return;
        }

        // 2. Se passou, executa o alterar normal
        try {
            Aluno aluno = new Aluno();
            // ... (seu código original de preencher o aluno) ...
            
            AlunoDAO dao = new AlunoDAO();
            dao.alterar(aluno);
            // ... (seu código original de matricular) ...
            
            JOptionPane.showMessageDialog(this, "Dados do aluno atualizados com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar dados: " + ex.getMessage());
        }
    }
    
    /**
     * Valida o RGM, pede confirmação ao usuário e deleta o aluno do sistema.
     */
    private void executarExcluir() {
        String rgmParaExcluir = txtRgm.getText().trim();
        
        if (rgmParaExcluir.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o RGM do aluno que deseja excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja excluir permanentemente o aluno de RGM " + rgmParaExcluir + "?\nEsta ação não poderá ser desfeita.", 
                "Confirmar Exclusão", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                AlunoDAO dao = new AlunoDAO();
                dao.excluir(rgmParaExcluir);

                txtRgm.setText("");
                txtNome.setText("");
                txtDataNascimento.setText("");
                txtCpf.setText("");
                txtEmail.setText("");
                txtEndereco.setText("");
                txtMunicipio.setText("");
                cbUf.setSelectedIndex(0);
                txtCelular.setText("");
                grupoPeriodo.clearSelection();
                
                txtNotaRgm.setText("");
                txtNotaNomeExibir.setText("Nome do aluno carregado automaticamente...");
                txtNotaCursoExibir.setText("Curso do aluno carregado automaticamente...");

                JOptionPane.showMessageDialog(this, "Aluno e suas matrículas foram excluídos com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
             // --- CORREÇÃO AQUI: Usando o campo de texto diretamente ---
                gerarBoletimTexto(txtNotaRgm.getText().trim(), txtNotaNomeExibir.getText(), txtNotaCursoExibir.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- MÉTODOS DE CONSTRUÇÃO DE LAYOUT ---

    private void construirAbaDadosPessoais() {
        MaskFormatter mascaraData = null; MaskFormatter mascaraCpf = null; MaskFormatter mascaraCelular = null;
        try {
            mascaraData = new MaskFormatter("##/##/####"); mascaraCpf = new MaskFormatter("###.###.###-##"); mascaraCelular = new MaskFormatter("(##)#####-####");
            mascaraData.setPlaceholderCharacter('_'); mascaraCpf.setPlaceholderCharacter('_'); mascaraCelular.setPlaceholderCharacter('_');
        } catch (Exception e) {}

        JLabel lblRgm = new JLabel("RGM"); lblRgm.setBounds(20, 30, 40, 25); painelDadosPessoais.add(lblRgm);
        txtRgm = new JTextField(); txtRgm.setBounds(65, 30, 120, 25); painelDadosPessoais.add(txtRgm);
        JLabel lblNome = new JLabel("Nome"); lblNome.setBounds(200, 30, 40, 25); painelDadosPessoais.add(lblNome);
        txtNome = new JTextField(); txtNome.setBounds(245, 30, 410, 25); painelDadosPessoais.add(txtNome);
        JLabel lblDataNasc = new JLabel("Data de Nascimento"); lblDataNasc.setBounds(20, 75, 130, 25); painelDadosPessoais.add(lblDataNasc);
        txtDataNascimento = new JFormattedTextField(mascaraData); txtDataNascimento.setBounds(150, 75, 100, 25); painelDadosPessoais.add(txtDataNascimento);
        JLabel lblCpf = new JLabel("CPF"); lblCpf.setBounds(270, 75, 30, 25); painelDadosPessoais.add(lblCpf);
        txtCpf = new JFormattedTextField(mascaraCpf); txtCpf.setBounds(305, 75, 140, 25); painelDadosPessoais.add(txtCpf);
        JLabel lblEmail = new JLabel("Email"); lblEmail.setBounds(20, 120, 40, 25); painelDadosPessoais.add(lblEmail);
        txtEmail = new JTextField(); txtEmail.setBounds(65, 120, 590, 25); painelDadosPessoais.add(txtEmail);
        JLabel lblEndereco = new JLabel("End."); lblEndereco.setBounds(20, 165, 40, 25); painelDadosPessoais.add(lblEndereco);
        txtEndereco = new JTextField(); txtEndereco.setBounds(65, 165, 590, 25); painelDadosPessoais.add(txtEndereco);
        JLabel lblMunicipio = new JLabel("Município"); lblMunicipio.setBounds(20, 210, 70, 25); painelDadosPessoais.add(lblMunicipio);
        txtMunicipio = new JTextField(); txtMunicipio.setBounds(90, 210, 170, 25); painelDadosPessoais.add(txtMunicipio);
        
        JLabel lblUf = new JLabel("UF"); lblUf.setBounds(275, 210, 20, 25); painelDadosPessoais.add(lblUf);
        cbUf = new JComboBox<>(ESTADOS);
        cbUf.setBounds(300, 210, 60, 25); 
        painelDadosPessoais.add(cbUf);
        
        JLabel lblCelular = new JLabel("Celular"); lblCelular.setBounds(375, 210, 50, 25); painelDadosPessoais.add(lblCelular);
        txtCelular = new JFormattedTextField(mascaraCelular); txtCelular.setBounds(425, 210, 230, 25); painelDadosPessoais.add(txtCelular);
    }

    private void construirAbaCurso() {
        JLabel lblCurso = new JLabel("Curso"); lblCurso.setBounds(20, 30, 60, 25); painelCurso.add(lblCurso);
        String[] cursosDisponiveis = {"Análise e Desenvolvimento de Sistemas", "Ciência da Computação", "Engenharia de Software"};
        cbCurso = new JComboBox<>(cursosDisponiveis); cbCurso.setBounds(90, 30, 565, 25); painelCurso.add(cbCurso);
        JLabel lblCampus = new JLabel("Campus"); lblCampus.setBounds(20, 80, 60, 25); painelCurso.add(lblCampus);
        String[] campiDisponiveis = {"Tatuapé", "Pinheiros", "Anália Franco"};
        cbCampus = new JComboBox<>(campiDisponiveis); cbCampus.setBounds(90, 80, 565, 25); painelCurso.add(cbCampus);
        JLabel lblPeriodo = new JLabel("Período"); lblPeriodo.setBounds(20, 130, 60, 25); painelCurso.add(lblPeriodo);
        rbMatutino = new JRadioButton("Matutino"); rbMatutino.setBounds(90, 130, 90, 25);
        rbVespertino = new JRadioButton("Vespertino"); rbVespertino.setBounds(190, 130, 100, 25);
        rbNoturno = new JRadioButton("Noturno"); rbNoturno.setBounds(300, 130, 90, 25);
        grupoPeriodo = new ButtonGroup(); grupoPeriodo.add(rbMatutino); grupoPeriodo.add(rbVespertino); grupoPeriodo.add(rbNoturno);
        painelCurso.add(rbMatutino); painelCurso.add(rbVespertino); painelCurso.add(rbNoturno);

        btnSalvar = new JButton("Salvar"); btnSalvar.setBounds(20, 200, 115, 65); painelCurso.add(btnSalvar);
        btnAlterar = new JButton("Alterar"); btnAlterar.setBounds(150, 200, 115, 65); painelCurso.add(btnAlterar);
        btnConsultar = new JButton("Consultar"); btnConsultar.setBounds(280, 200, 115, 65); painelCurso.add(btnConsultar);
        btnExcluir = new JButton("Excluir"); btnExcluir.setBounds(410, 200, 115, 65); painelCurso.add(btnExcluir);
        btnSair = new JButton("Sair"); btnSair.setBounds(540, 200, 115, 65); painelCurso.add(btnSair);
    }


    private void construirAbaNotasFaltas() {
        JLabel lblNotaRgm = new JLabel("RGM"); lblNotaRgm.setBounds(20, 30, 40, 25); painelNotasFaltas.add(lblNotaRgm);
        txtNotaRgm = new JTextField(); txtNotaRgm.setBounds(65, 30, 120, 25); painelNotasFaltas.add(txtNotaRgm);
        txtNotaNomeExibir = new JTextField("Nome do aluno carregado automaticamente..."); txtNotaNomeExibir.setBounds(20, 75, 635, 25); txtNotaNomeExibir.setEditable(false); txtNotaNomeExibir.setBackground(new Color(230, 230, 230)); painelNotasFaltas.add(txtNotaNomeExibir);
        txtNotaCursoExibir = new JTextField("Curso do aluno carregado automaticamente..."); txtNotaCursoExibir.setBounds(20, 120, 635, 25); txtNotaCursoExibir.setEditable(false); txtNotaCursoExibir.setBackground(new Color(230, 230, 230)); painelNotasFaltas.add(txtNotaCursoExibir);
        
        JLabel lblNotaDisciplina = new JLabel("Disciplina"); lblNotaDisciplina.setBounds(20, 165, 70, 25); painelNotasFaltas.add(lblNotaDisciplina);
        cbNotaDisciplina = new JComboBox<>(); cbNotaDisciplina.setBounds(90, 165, 565, 25); painelNotasFaltas.add(cbNotaDisciplina);
        
        JLabel lblNotaSemestre = new JLabel("Semestre"); lblNotaSemestre.setBounds(20, 210, 70, 25); painelNotasFaltas.add(lblNotaSemestre);
        String[] semestres = {"2026-1", "2026-2", "2027-1", "2027-2"}; cbNotaSemestre = new JComboBox<>(semestres); cbNotaSemestre.setBounds(90, 210, 100, 25); painelNotasFaltas.add(cbNotaSemestre);
        JLabel lblNotaValor = new JLabel("Nota"); lblNotaValor.setBounds(210, 210, 40, 25); painelNotasFaltas.add(lblNotaValor);
        String[] notasValores = {"0,0", "0,5", "1,0", "1,5", "2,0", "2,5", "3,0", "3,5", "4,0", "4,5", "5,0", "5,5", "6,0", "6,5", "7,0", "7,5", "8,0", "8,5", "9,0", "9,5", "10,0"}; cbNotaValor = new JComboBox<>(notasValores); cbNotaValor.setBounds(250, 210, 70, 25); painelNotasFaltas.add(cbNotaValor);
        JLabel lblNotaFaltas = new JLabel("Faltas"); lblNotaFaltas.setBounds(340, 210, 50, 25); painelNotasFaltas.add(lblNotaFaltas);
        txtNotaFaltas = new JTextField(); txtNotaFaltas.setBounds(390, 210, 60, 25); painelNotasFaltas.add(txtNotaFaltas);

        // --- BOTÕES TIPO TEXTO PURO (SEM IMAGEM / SEM ÍCONE) ---
        btnNotaSalvar = new JButton("Salvar");
        btnNotaSalvar.setBounds(20, 260, 115, 65);
        painelNotasFaltas.add(btnNotaSalvar);

        btnNotaAlterar = new JButton("Alterar");
        btnNotaAlterar.setBounds(150, 260, 115, 65);
        painelNotasFaltas.add(btnNotaAlterar);

        btnNotaConsultar = new JButton("Consultar");
        btnNotaConsultar.setBounds(280, 260, 115, 65);
        painelNotasFaltas.add(btnNotaConsultar);

        btnNotaExcluir = new JButton("Excluir");
        btnNotaExcluir.setBounds(410, 260, 115, 65);
        painelNotasFaltas.add(btnNotaExcluir);

        btnNotaSair = new JButton("Sair");
        btnNotaSair.setBounds(540, 260, 115, 65);
        painelNotasFaltas.add(btnNotaSair);
    }
    
    private void construirAbaBoletim() {
        JLabel lblTituloBoletim = new JLabel("Extrato de Rendimento Acadêmico (Boletim Oficial)"); lblTituloBoletim.setFont(new Font("Arial", Font.BOLD, 14)); lblTituloBoletim.setBounds(20, 15, 400, 25); painelBoletim.add(lblTituloBoletim);
        txtBoletimExibir = new JTextArea(); txtBoletimExibir.setEditable(false); txtBoletimExibir.setFont(new Font("Monospaced", Font.PLAIN, 12)); txtBoletimExibir.setText("\n  Consulte um aluno pelo RGM para gerar o boletim completo aqui...");
        JScrollPane scrollBoletim = new JScrollPane(txtBoletimExibir); scrollBoletim.setBounds(20, 50, 635, 360); painelBoletim.add(scrollBoletim);
    }

    private void configurarMenu() {
        JMenuBar barraMenu = new JMenuBar();
        JMenu menuAluno = new JMenu("Aluno");
        itemSalvarMenu = new JMenuItem("Salvar (Ctrl+S)"); 
        JMenuItem itemAlterar = new JMenuItem("Alterar");
        itemConsultarMenu = new JMenuItem("Consultar"); 
        JMenuItem itemExcluir = new JMenuItem("Excluir");
        JMenuItem itemSair = new JMenuItem("Sair (Shift+R)");
        menuAluno.add(itemSalvarMenu); menuAluno.add(itemAlterar); menuAluno.add(itemConsultarMenu); menuAluno.add(itemExcluir); menuAluno.addSeparator(); menuAluno.add(itemSair);

        JMenu menuNotasFaltas = new JMenu("Notas e Faltas");
        
        // Sincronizado para usar a variável global da classe
        itemNotaSalvarMenu = new JMenuItem("Salvar");
        
        JMenuItem itemNotaAlterar = new JMenuItem("Alterar (Ctrl+A)");
        JMenuItem itemNotaExcluir = new JMenuItem("Excluir");
        itemNotaConsultarMenu = new JMenuItem("Consultar"); 
        
        menuNotasFaltas.add(itemNotaSalvarMenu); menuNotasFaltas.add(itemNotaAlterar); menuNotasFaltas.add(itemNotaExcluir); menuNotasFaltas.add(itemNotaConsultarMenu);

        JMenu menuAjuda = new JMenu("Ajuda"); JMenuItem itemSobre = new JMenuItem("Sobre"); menuAjuda.add(itemSobre);
        barraMenu.add(menuAluno); barraMenu.add(menuNotasFaltas); barraMenu.add(menuAjuda);
        setJMenuBar(barraMenu);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { new TelaSistema().setVisible(true); });
    }
}