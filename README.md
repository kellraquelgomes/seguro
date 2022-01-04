# Requisitos do microservice de seguro
 
 * Cadastrar os dados relacionados a um cliente e seus seguros contratados;
 * Cadastrar o acionamento de um produto;
 * Exibir os dados relacionados a um cliente (seu portfolio de produtos contratados) e seus respectivos
   acionamentos.
 
 ### Considere o cadastro com dados básicos:
 
 * Criação cliente-produto: nome, documento, Id produto, nome produto (Opcional)
 * Criação cliente-acionamento: documento, data acionamento, Id Produto e nome produto (Opcional)
 
 
 ### Requisitos de Regras de Negócio:
 
 * Um cliente não pode contratar dois ou mais produtos iguais.
 * Um cliente pode nunca ter acionado seguro.
 * Cada produto tem seu limite de acionamento.
 * Um cliente não deve conseguir acionar um seguro, se o número de acionamento estiver no limite.
 * O periodo entre os acionamentos do mesmo produtos, é de no mínimo 30 dias, a partir da data do
 último acionamento.
 
 ### Banco de dados 
 
 O banco de dados utilizado neste Microservice foi o HSQLDB em memóría. 

 * A criação das tabelas está no diretório resources no arquivo: schema.sql que é chamado de forma automática ao start 
 o Spring boot. 
 
 * A criação das cargas do Produto e Parceiro está no diretório resources no arquivo: data.sql que é chamado de forma automática ao start 
 o Spring boot. 
 
 * Para start o banco HSBLDB é preciso iniciar o spring boot. 
 
 ### Para acessar a API REST Seguro usando Swagger start o spring boot e clique no link abaixo: 
 
 * [Acesso da API](http://localhost:8080/swagger-ui.html)
