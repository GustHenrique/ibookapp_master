package com.example.ibookApp.DTOs;

public class EmailDTO {
    private String remetente;
    private String senha;
    private String destinatario;
    private String assunto;
    private String conteudo;

    public EmailDTO(String remetente, String senha, String destinatario, String assunto, String conteudo) {
        this.remetente = remetente;
        this.senha = senha;
        this.destinatario = destinatario;
        this.assunto = assunto;
        this.conteudo = conteudo;
    }

    public String getRemetente() {
        return remetente;
    }

    public String getSenha() {
        return senha;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getConteudo() {
        return conteudo;
    }
}
