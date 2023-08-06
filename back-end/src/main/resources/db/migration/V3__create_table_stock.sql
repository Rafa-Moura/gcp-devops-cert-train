create table tb_estoque(
	id bigint primary key auto_increment not null,
	codigo_produto varchar(10) not null,
	produto varchar(100) not null,
	quantidade int not null,
	valor numeric(19,5) not null,
	usuario_cadastro varchar(50) not null,
	status_id int not null,
	data_criacao timestamp not null,
	data_atualizacao timestamp not null
);