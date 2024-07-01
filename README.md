# Desafio Wishlist

Informações sobre o projeto, regras de negócio e requisitos técnicos para o desafio técnico do Wishlist

## Tecnologias usadas

- **Java 21**: O projeto foi desenvolvido usando a versão LTS do Java 21
- **Spring Boot**: Framework utilizado para criar o projeto
- **MongoDB**: Banco de dados não relacional (NoSQL)
- **Gradle**: Gerenciado de dependências
- **Arquitetura MVC**: Por ser tratar de um projeto relativamente simples foi utilizado a arquitetura MVC
- **Testes**: O projeto utiliza o JUnit5 e Mockito para testes unitários com cobertura mínima de 95%

## Orientações Gerais

- **Documentação**: Você pode acessar o Swagger da aplicação através do endpoint `/swagger-ui.html`
- **Boas Práticas**: O projeto segue boas práticas de programação e design patterns
- **Versionamento**: O projeto foi versionado utilizando Git e hospedado no GitHub. A branch que contém o projeto implementado é a `develop`


## O que deve ser feito?
O objetivo é que você desenvolva um serviço HTTP resolvendo a funcionalidade de Wishlist do cliente. Esse serviço deve atender os seguintes requisitos:

- Adicionar um produto na Wishlist do cliente;
- Remover um produto da Wishlist do cliente;
- Consultar todos os produtos da Wishlist do cliente;
- Consultar se um determinado produto está na Wishlist do cliente;

## Orientações
Imagine que esse serviço fará parte de uma plataforma construída em uma arquitetura baseada em micro-serviços. Portanto não se preocupe com a gestão das informações de Produtos e/ou Clientes, coloque sua energia apenas no serviço da Wishlist.
É importante estabelecer alguns limites para proteger o bom funcionamento do ecossistema, dessa forma a Wishlist do cliente deve possuir um limite máximo de 20 produtos.

## Rodar o projeto
- Baixe o arquivo [`docker-componse.yml`](https://github.com/ValdirCezar/wishlist/blob/master/docker-compose.yml)
- Rode o comando `docker-compose up` dentro do diretório onde o arquivo anterior foi baixado
- Acesse o [swagger da applicação](http://localhost:8080/swagger-ui.html)
- Basta realizar as operações seguindo as observações abaixo

## Observações no desenvolvimento
- Para que eu possa gerenciar produtos de uma wishlist é necessário oferecer a opção de criar uma wishlist através de algum recurso, então criei o endpoint `POST /v1/wishlists`.
- Ao realizar a operação de criação de uma wishlist, será retornado no headers um atributo `location: http://{servidor}/v1/wishlists/{idNovoObjeto}` que disponibiliza o caminho de acesso a esse novo recurso.
- Uma wishlist pode conter como regra de negócio o máximo de 20 produtos, porém não foi definido uma regra quanto a quantidade desejada do produto. Ex.: Eu seleciono um lápis na loja mas informo que quero 10 unidades desse lápis ao adicioná-lo em minha wishlist.
- Como os produtos são uma entidade técnicamente gerenciada por outro serviço, não armazenamos qualquer informação no banco de wishlist que não seja o id do produto, pois as informações do produto podem mudar e teríamos uma inconsistência dos dados.
- Ao adicionar um novo produto a wishlist, estamos realizando uma operação de atualização parcial das informações desse objeto, logo, para esse fluxo foi usado o verbo `HTTP PATCH`. 
- Ao retornar a wishlist com as informações dos produtos, vamos buscar esses produtos em um serviço externo e então calculamos o valor total da wishlist.
- Se por algum motivo o front solicite adicionar um novo produto na wishlist, mas a wishlist já contém esse produto, vamos apenas incrementar a quantidade do mesmo.
- Todos os endpoints possuem exemplos de payloads e parâmetros, isso não significa que existam no banco, logo, será necessário analisar o ids de produtos e usuários válidos nos exemplos da documentação.
- A imagem do projeto foi gerada e disponibilizada publicamente no Dockerhub.
- Para rodar o projeto basta ter o Docker Desktop instalado e através de um terminal executar o comando `docker-compose up` para o arquivo `docker-compose.yml` que se enconta na raiz do projeto.
- Não foi implementado qualquer mecânismo de segurança pois não era a intensão do teste.



## Massa de testes

### Produtos válidos
Caso queira simular o cenário de no máximo 20 produtos podes copiar essa estrutura abaixo e colar ao criar uma nova wishlist
```
[
  { "id": "336b3efb74e106091aae50d3", "quantity": 1 },
  { "id": "997f2e1a9f5cf6e2ca4beae4", "quantity": 1 },
  { "id": "693f2e1a9f5cf6e2ca4becag", "quantity": 1 },
  { "id": "333abc510d2ef9143eefa7f1", "quantity": 1 },
  { "id": "336b3efb74e106091aae50d8", "quantity": 1 },
  { "id": "997f2e1a9f5cf6e2ca4beae9", "quantity": 1 },
  { "id": "693f2e1a9f5cf6e2ca4becah", "quantity": 1 },
  { "id": "333abc510d2ef9143eefa7f2", "quantity": 1 },
  { "id": "336b3efb74e106091aae50d9", "quantity": 1 },
  { "id": "997f2e1a9f5cf6e2ca4beae1", "quantity": 1 },
  { "id": "693f2e1a9f5cf6e2ca4beca1", "quantity": 1 },
  { "id": "333abc510d2ef9143eefa7f3", "quantity": 1 },
  { "id": "336b3efb74e106091aae50d1", "quantity": 1 },
  { "id": "997f2e1a9f5cf6e2ca4beae2", "quantity": 1 },
  { "id": "693f2e1a9f5cf6e2ca4beca2", "quantity": 1 },
  { "id": "333abc510d2ef9143eefa7f4", "quantity": 1 },
  { "id": "336b3efb74e106091aae50d2", "quantity": 1 },
  { "id": "997f2e1a9f5cf6e2ca4beae3", "quantity": 1 },
  { "id": "693f2e1a9f5cf6e2ca4beca3", "quantity": 1 },
  { "id": "333abc510d2ef9143eefa7f5", "quantity": 1 },
  { "id": "336b3efb74e106091aae50d4", "quantity": 1 }
]
```

### Usuário válidos
```
[
  {
    "id": "606b3efb74e106091aae50d8",
    "name": "Valdir Cezar",
    "email": "valdir@mail.com"
  },
  {
    "id": "607f2e1a9f5cf6e2ca4beae7",
    "name": "Allan Turing",
    "email": "turing@mail.com"
  },
  {
    "id": "607f2e1a9f5cf6e2ca4becae",
    "name": "Margaret Hamilton",
    "email": "hamilton@mail.com"
  },
  {
    "id": "618abc510dcef9143eefa7f1",
    "name": "Linus Torvalds",
    "email": "torvalds@mail.com"
  }
]

```
