CREATE TABLE cadastro_pet_adocao (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     raca VARCHAR(100),
                                     cor VARCHAR(100),
                                     porte VARCHAR(50),
                                     estado VARCHAR(50),
                                     cidade VARCHAR(100),
                                     bairro VARCHAR(100),
                                     telefone VARCHAR(20),
                                     email VARCHAR(100)
);

CREATE TABLE USUARIO (
                         cd_id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                         nm_nome VARCHAR(60) NOT NULL,
                         nm_sobrenome VARCHAR(100),
                         nm_usuario VARCHAR(50) UNIQUE NOT NULL,
                         cd_cpf CHAR(14) UNIQUE NOT NULL,
                         nm_email VARCHAR(100) UNIQUE NOT NULL,
                         cd_telefone VARCHAR(20),
                         cd_cep CHAR(9),
                         nm_estado VARCHAR(30),
                         nm_cidade VARCHAR(100),
                         nm_bairro VARCHAR(100),
                         ds_senha VARCHAR(255) NOT NULL,
                         dt_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

