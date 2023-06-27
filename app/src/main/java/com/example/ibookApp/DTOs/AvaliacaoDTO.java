package com.example.ibookApp.DTOs;

public class AvaliacaoDTO {

    private String id;
    private String usuid;
    private String obid;
    private String avaliacao;

    public AvaliacaoDTO(String id, String usuid, String obid, String avaliacao) {
        this.id = id;
        this.usuid = usuid;
        this.obid = obid;
        this.avaliacao = avaliacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuid() {
        return usuid;
    }

    public void setUsuid(String usuid) {
        this.usuid = usuid;
    }

    public String getObid() {
        return obid;
    }

    public void setObid(String obid) {
        this.obid = obid;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

}
