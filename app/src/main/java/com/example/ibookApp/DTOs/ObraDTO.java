package com.example.ibookApp.DTOs;

public class ObraDTO {
    private String obid;
    private String obtitulo;
    private String obisbn;
    private String obeditora;
    private String obavaliacao;
    private String obautor;
    private String obimage;
    private String obcategoria;
    private String obstatus;
    private String obsinopse;

    public ObraDTO(String obid, String obtitulo, String obeditora, String obavaliacao, String obautor, String obimage, String obcategoria, String obstatus, String obsinopse, String obisbn) {
        this.obid = obid;
        this.obtitulo = obtitulo;
        this.obeditora = obeditora;
        this.obavaliacao = obavaliacao;
        this.obautor = obautor;
        this.obimage = obimage;
        this.obcategoria = obcategoria;
        this.obstatus = obstatus;
        this.obsinopse = obsinopse;
        this.obisbn = obisbn;
    }


    public String getObisbn() {
        return obisbn;
    }

    public void setObisbn(String obisbn) {
        this.obisbn = obisbn;
    }
    public String getObid() {
        return obid;
    }

    public void setObid(String obid) {
        this.obid = obid;
    }

    public String getObtitulo() {
        return obtitulo;
    }

    public void setObtitulo(String obtitulo) {
        this.obtitulo = obtitulo;
    }

    public String getObeditora() {
        return obeditora;
    }

    public void setObeditora(String obeditora) {
        this.obeditora = obeditora;
    }

    public String getObavaliacao() {
        return obavaliacao;
    }

    public void setObavaliacao(String obavaliacao) {
        this.obavaliacao = obavaliacao;
    }

    public String getObautor() {
        return obautor;
    }

    public void setObautor(String obautor) {
        this.obautor = obautor;
    }

    public String getObimage() {
        return obimage;
    }

    public void setObimage(String obimage) {
        this.obimage = obimage;
    }

    public String getObcategoria() {
        return obcategoria;
    }

    public void setObcategoria(String obcategoria) {
        this.obcategoria = obcategoria;
    }

    public String getObstatus() {
        return obstatus;
    }

    public void setObstatus(String obstatus) {
        this.obstatus = obstatus;
    }

    public String getObsinopse() {
        return obsinopse;
    }

    public void setObsinopse(String obsinopse) {
        this.obsinopse = obsinopse;
    }
}

