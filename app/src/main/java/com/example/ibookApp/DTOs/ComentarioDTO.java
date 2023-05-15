package com.example.ibookApp.DTOs;

public class ComentarioDTO {
    private String cobid;
    private String cobcomentario;
    private String usuid;
    private String obid;

    public ComentarioDTO(String cobid, String cobcomentario, String usuid, String obid) {
        this.cobid = cobid;
        this.cobcomentario = cobcomentario;
        this.usuid = usuid;
        this.obid = obid;
    }

    public String getCobid() {
        return cobid;
    }

    public void setCobid(String cobid) {
        this.cobid = cobid;
    }

    public String getCobcomentario() {
        return cobcomentario;
    }

    public void setCobcomentario(String cobcomentario) {
        this.cobcomentario = cobcomentario;
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

}

