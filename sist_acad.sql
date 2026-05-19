-- =========================================================================
-- ETAPA 1: CRIAR O BANCO DE DADOS
-- =========================================================================
DROP DATABASE IF EXISTS db_sistema_academico;
CREATE DATABASE db_sistema_academico;
USE db_sistema_academico;

-- =========================================================================
-- ETAPA 2: CRIAR AS TABELAS (Blindadas contra duplicidade)
-- =========================================================================

-- 1. Tabela de Cursos
CREATE TABLE tb_curso (
    id_curso INT AUTO_INCREMENT PRIMARY KEY,
    nome_curso VARCHAR(100) NOT NULL,
    campus VARCHAR(50) NOT NULL,
    periodo VARCHAR(20) NOT NULL,
    -- Impede a criação do MESMO curso, no mesmo campus e período novamente
    CONSTRAINT UNIQUE_curso_ofertado UNIQUE (nome_curso, campus, periodo)
);

-- 2. Tabela de Alunos (Dados Pessoais e Curso)
CREATE TABLE tb_aluno (
    rgm VARCHAR(20) PRIMARY KEY, -- Bloqueia RGM duplicado
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE, -- UNIQUE: Impede dois alunos com o mesmo CPF
    email VARCHAR(100) UNIQUE,       -- UNIQUE: Impede dois alunos com o mesmo e-mail
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
    nome_disciplina VARCHAR(100) NOT NULL UNIQUE -- UNIQUE: Impede cadastrar a mesma matéria duas vezes
);

-- 4. Tabela Associativa: Matrícula (Notas e Faltas)
CREATE TABLE tb_matricula (
    rgm_aluno VARCHAR(20),
    id_disciplina INT,
    semestre VARCHAR(10) NOT NULL, 
    nota DECIMAL(3,1),
    faltas INT DEFAULT 0,
    -- A chave primária composta já garante nativamente que o mesmo aluno 
    -- não tenha duas notas na mesma matéria dentro do mesmo semestre!
    PRIMARY KEY (rgm_aluno, id_disciplina, semestre), 
    
    FOREIGN KEY (rgm_aluno) REFERENCES tb_aluno(rgm) ON DELETE CASCADE, 
    FOREIGN KEY (id_disciplina) REFERENCES tb_disciplina(id_disciplina)
);

CREATE TABLE tb_aluno_curso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rgm_aluno VARCHAR(20) NOT NULL,
    id_curso INT NOT NULL,
    campus VARCHAR(100) NOT NULL,
    periodo VARCHAR(50) NOT NULL,
    -- Garante que o mesmo aluno não tenha duplicidade idêntica de curso de forma errada
    UNIQUE KEY uq_aluno_curso (rgm_aluno, id_curso), 
    -- Chave estrangeira ligando com a sua tabela de alunos
    FOREIGN KEY (rgm_aluno) REFERENCES tb_aluno(rgm) ON DELETE CASCADE
);

CREATE TABLE tb_curso_disciplina (
    id_curso INT NOT NULL, -- 1 = ADS, 2 = CC, 3 = Eng
    id_disciplina INT NOT NULL,
    PRIMARY KEY (id_curso, id_disciplina),
    FOREIGN KEY (id_disciplina) REFERENCES tb_disciplina(id_disciplina) ON DELETE CASCADE
    -- Nota: Se você tiver uma tabela 'tb_curso', pode colocar o FOREIGN KEY para id_curso aqui também!
);

-- =========================================================================
-- ETAPA 3: CARGA INICIAL DE DADOS
-- =========================================================================

-- Inserir os Cursos
INSERT INTO tb_curso (nome_curso, campus, periodo) VALUES 
('Análise e Desenvolvimento de Sistemas', 'Tatuapé', 'Matutino'),
('Análise e Desenvolvimento de Sistemas', 'Tatuapé', 'Vespertino'),
('Análise e Desenvolvimento de Sistemas', 'Tatuapé', 'Noturno'),
('Engenharia de Software', 'Pinheiros', 'Noturno'),
('Ciência da Computação', 'Tatuapé', 'Noturno');

INSERT INTO tb_disciplina (id_disciplina, nome_disciplina) VALUES 
(1, 'Programação Orientada a Objetos'),
(2, 'Estrutura de Dados'),
(3, 'Bancos de Dados'),
(4, 'Arquitetura de Software');

-- Vinculando as matérias aos cursos (1 = ADS, 2 = CC, 3 = Engenharia)
INSERT INTO tb_curso_disciplina (id_curso, id_disciplina) VALUES
-- ADS tem POO e Bancos de Dados
(1, 1), -- ADS tem POO
(1, 3), -- ADS tem Bancos de Dados

-- Ciência da Computação TAMBÉM tem POO, além de Estrutura de Dados e Bancos
(2, 1), -- CC também tem POO! (Olha o compartilhamento aqui)
(2, 2), -- CC tem Estrutura de Dados
(2, 3), -- CC tem Bancos de Dados

-- Engenharia de Software tem POO e Arquitetura
(3, 1), -- Engenharia também tem POO!
(3, 4); -- Engenharia tem Arquitetura de Software
