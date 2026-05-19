-- =========================================================================
-- ETAPA 1: CRIAR O BANCO DE DADOS
-- =========================================================================
DROP DATABASE IF EXISTS db_sistema_academico;
CREATE DATABASE db_sistema_academico;
USE db_sistema_academico;

-- =========================================================================
-- ETAPA 2: CRIAR AS TABELAS (Respeitando a ordem das Chaves Estrangeiras)
-- =========================================================================

-- 1. Tabela de Cursos
CREATE TABLE tb_curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nome_curso VARCHAR(100) NOT NULL,
    campus VARCHAR(50) NOT NULL,
    periodo VARCHAR(20) NOT NULL
);

-- 2. Tabela de Alunos (Dados Pessoais e Curso)
CREATE TABLE tb_aluno (
    rgm VARCHAR(20) PRIMARY KEY, -- Chave Primária para evitar RGMs duplicados
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    email VARCHAR(100),
    endereco VARCHAR(150),
    municipio VARCHAR(50),
    uf CHAR(2),
    celular VARCHAR(15),
    id_curso INT,
    FOREIGN KEY (id_curso) REFERENCES tb_curso(id_curso)
);

-- 3. Tabela de Disciplinas
CREATE TABLE tb_disciplina (
    id_disciplina INT AUTO_INCREMENT PRIMARY KEY,
    nome_disciplina VARCHAR(100) NOT NULL
);

-- 4. Tabela Associativa: Matrícula (Notas e Faltas)
CREATE TABLE tb_matricula (
    rgm_aluno VARCHAR(20),
    id_disciplina INT,
    semestre VARCHAR(10) NOT NULL, -- Ex: '2020-1' ou '2026-1'
    nota DECIMAL(3,1),
    faltas INT DEFAULT 0,
    PRIMARY KEY (rgm_aluno, id_disciplina, semestre), -- Chave primária composta
    
    -- REGRA DE OURO DO PROFESSOR: Exclusão em cascata
    FOREIGN KEY (rgm_aluno) REFERENCES tb_aluno(rgm) 
        ON DELETE CASCADE, 
        
    FOREIGN KEY (id_disciplina) REFERENCES tb_disciplina(id_disciplina)
);

-- =========================================================================
-- ETAPA 3: CARGA INICIAL DE DADOS (Entradas para os teus JComboBox)
-- =========================================================================

-- 1. Inserir os Cursos (com Campus e Período baseados no enunciado)
INSERT INTO tb_curso (nome_curso, campus, periodo) VALUES 
('Análise e Desenvolvimento de Sistemas', 'Tatuapé', 'Matutino'),
('Análise e Desenvolvimento de Sistemas', 'Tatuapé', 'Vespertino'),
('Análise e Desenvolvimento de Sistemas', 'Tatuapé', 'Noturno'),
('Engenharia de Software', 'Pinheiros', 'Noturno'),
('Ciência da Computação', 'Tatuapé', 'Noturno');

-- 2. Inserir as Disciplinas para poderes lançar as Notas/Faltas na aba 3
INSERT INTO tb_disciplina (nome_disciplina) VALUES 
('Programação Orientada a Objetos'),
('Estrutura de Dados'),
('Banco de Dados Relacional'),
('Engenharia de Requisitos');