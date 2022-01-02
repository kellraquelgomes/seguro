    create sequence PUBLIC.hibernate_sequence start with 1 increment by 1;


    create table PUBLIC.tb_cliente (
        cliente_id integer not null,
        documento varchar(20) not null,
        nome varchar(150) not null,
        primary key (cliente_id)
    );


    create table PUBLIC.tb_cliente_acionamento_produto (
        id integer not null,
        data_acionamento timestamp not null,
        cliente_cliente_id integer not null,
        produto_produto_id integer not null,
        primary key (id)
    );


    create table PUBLIC.tb_clientes_produtos (
        cliente_id integer not null,
        produto_id integer not null,
        primary key (cliente_id, produto_id)
    );


    create table PUBLIC.tb_parceiro (
        parceiro_id integer not null,
        nome varchar(150) not null,
        primary key (parceiro_id)
    );


    create table PUBLIC.tb_produto (
        produto_id integer not null,
        nome varchar(150) not null,
        quantidade_acionamento integer not null,
        valor numeric(10,2) not null,
        parceiro_parceiro_id integer not null,
        primary key (produto_id)
    );


   alter table PUBLIC.tb_cliente_acionamento_produto
   add constraint FKtpt926mh0istc2k6fx05vy1gj
   foreign key (cliente_cliente_id)
   references PUBLIC.tb_cliente;


    alter table PUBLIC.tb_cliente_acionamento_produto
       add constraint FKr60icbmisip6wi9tevlb5jjtt
       foreign key (produto_produto_id)
       references PUBLIC.tb_produto;


    alter table PUBLIC.tb_clientes_produtos
       add constraint FKos5sg2ev2pnxlpirtqun6joq8
       foreign key (produto_id)
       references PUBLIC.tb_produto;


    alter table PUBLIC.tb_clientes_produtos
       add constraint FKe7ofa8h11kf1me70ymdn8wxxb
       foreign key (cliente_id)
       references PUBLIC.tb_cliente;


    alter table PUBLIC.tb_produto
       add constraint FKfsr8hvhy79t9d2j9iba4fkep
       foreign key (parceiro_parceiro_id)
       references PUBLIC.tb_parceiro;


