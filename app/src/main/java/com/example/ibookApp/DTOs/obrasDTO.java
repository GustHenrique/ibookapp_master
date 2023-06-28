package com.example.ibookApp.DTOs;

public class obrasDTO {
    private String id;
    private String title;
    private String subtitle;
    private String synopsis;
    private String author;
    private String editora;
    private String dataPublicacao;
    private String dataFinalizacao;
    private String isbn;
    private String paginas;
    private String image;
    private String traducao;
    private String type;
    private String avarageRating;
    private String status;
    private String categorias;
    private String usuid;

    public obrasDTO(String id, String title, String subtitle, String synopsis, String author, String editora, String dataPublicacao, String dataFinalizacao, String isbn, String paginas, String image, String traducao, String type, String avarageRating, String status, String categorias,String usuid) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.synopsis = synopsis;
        this.author = author;
        this.editora = editora;
        this.dataPublicacao = dataPublicacao;
        this.dataFinalizacao = dataFinalizacao;
        this.isbn = isbn;
        this.paginas = paginas;
        this.image = image;
        this.traducao = traducao;
        this.type = type;
        this.avarageRating = avarageRating;
        this.status = status;
        this.categorias = categorias;
        this.usuid = usuid;
    }

    public String getUsuid() {
        return usuid;
    }

    public void setUsuid(String usuid) {
        this.usuid = usuid;
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

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
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

    public String getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(String dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTraducao() {
        return traducao;
    }

    public void setTraducao(String traducao) {
        this.traducao = traducao;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvarageRating() {
        return avarageRating;
    }

    public void setAvarageRating(String avarageRating) {
        this.avarageRating = avarageRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

}
