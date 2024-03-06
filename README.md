# Projeto para certificação do GCP

Projeto foi criado com intuito de prover uma API REST que será utilizada na certificação Google Cloud Plataform.

### A API

A api consiste em um micro serviço rest que disponibiliza metódos para gestão de um estoque. De modo geral a api deve funcionar como um CRUD, exceto pelo fato de não conter um endpoint para atualização de um produto.

### Como utilizar

1 - Primeiro você deve clonar este projeto em sua máquina local [https://github.com/Rafa-Moura/gcp-devops-cert-train.git](https://github.com/Rafa-Moura/gcp-devops-cert-train)

2 - O próximo passo irá depender de como pretende executar o projeto
 - Executando via terminal: Caso queira apenas executar o projeto sem utilizar uma IDE ou editor de código para visualizar o código fonte, abra o terminal de sua preferência na pasta back-end existente dentro do clone do projeto
e digite ./mvnw springboot:run. Esse comando irá inicializar o projeto.
 - Executando via IDE ou editor de texto: Abra o projeto em seu editor ou IDE de sua preferência, ajuste sua JDK (no projeto foi utilizada a JDK 17), realize o download das depências do projeto. Caso o download das dependências
   não ocorra de maneira automática, você deverá utilizar o recurso disponível no seu editor/IDE ou utilizar o comando mvn dependency:copy-dependencies no terminal dentro da pasta back-end do projeto que contém o arquivo pom.xml.

3 - Com o projeto em execução, você deverá acessar a URL [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui/index.html). Esse endereço levará você so swagger contendo a documentação da aplicação e disponibilizando
os endpoints e suas funcionalidades.

### Swagger

O Swagger da aplicação conta com a documentação de todas as funcionalidades disponíveis e os schemas dos representations models disponíveis

| Method | Url | Description | Representation Model |
| ------ | --- | ----------- | ------------------------- |
| POST   | http://localhost:8080/api/product | Insere produto no banco de dados | ProductRequestDto
| GET    | http://localhost:8080/api/product?page=0&size=20 | Lista todos os produtos cadastrados | PageableResponseDto
| GET    | http://localhost:8080/api/product/{code} | Retorna dados de um produto pelo iD | ProducResponseDto
| POST   | http://localhost:8080/api/product/output | Realiza o registro de uma retirada de um produto do banco de dados | Request: ProductOutputRequestDto Response: ProducResponseDto
| DELETE | http://localhost:8080/api/product/code   | Realiza uma deleção lógica de um produto alterando seu status para "Item removido do estoque" |
