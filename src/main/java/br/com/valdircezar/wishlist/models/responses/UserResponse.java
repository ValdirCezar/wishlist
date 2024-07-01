package br.com.valdircezar.wishlist.models.responses;

/*
* Estrutura não foi documentada com as anotations do swagger
* pois o client não terá acesso a mesma. É apenas uma "DTO"
* usada internamente pelo componente para validar os dados
* do cliente.
* */
public record UserResponse(String id, String name, String email) { }