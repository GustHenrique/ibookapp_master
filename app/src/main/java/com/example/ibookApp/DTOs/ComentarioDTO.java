package com.example.ibookApp.DTOs;

import java.sql.Date;

public class ComentarioDTO {
    private String cobid;
    private String cobcomentario;
    private String usuid;
    private String obid;

    private Date dataComentario;

    public Date getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(Date dataComentario) {
        this.dataComentario = dataComentario;
    }

    public ComentarioDTO(String cobid, String cobcomentario, String usuid, String obid, Date dataComentario) {
        this.cobid = cobid;
        this.cobcomentario = cobcomentario;
        this.usuid = usuid;
        this.obid = obid;
        this.dataComentario = dataComentario;
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

