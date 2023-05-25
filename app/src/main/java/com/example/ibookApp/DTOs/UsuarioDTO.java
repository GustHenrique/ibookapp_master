package com.example.ibookApp.DTOs;

public class UsuarioDTO {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String imagem;
    private Boolean administrador;

    public UsuarioDTO(String email, String senha, String nome, String id, String imagem, Boolean administrador) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.imagem = imagem;
        this.administrador = administrador;
    }


    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }
    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

