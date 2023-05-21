package com.example.ibookApp.DTOs;

public class BookApiGoogleDTO {
    private String id;
    private String title;
    private String subtitle;
    private String author;
    private String editora;
    private String dataPublicacao;
    private String descricao;
    private String isbn;
    private String paginas;
    private String capa;
    private String traducao;
    private String type;

    public BookApiGoogleDTO(String id, String title, String subtitle, String author, String editora, String dataPublicacao, String descricao, String isbn, String paginas, String capa, String traducao, String type) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.editora = editora;
        this.dataPublicacao = dataPublicacao;
        this.descricao = descricao;
        this.isbn = isbn;
        this.paginas = paginas;
        this.capa = capa;
        this.traducao = traducao;
        this.type = type;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public String getTraducao() {
        return traducao;
    }

    public void setTraducao(String traducao) {
        this.traducao = traducao;
    }

}
