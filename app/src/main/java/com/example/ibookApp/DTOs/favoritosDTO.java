package com.example.ibookApp.DTOs;

public class favoritosDTO {
    private String id;
    private String usuid;
    private String obid;

    public favoritosDTO(String id, String usuid, String obid) {
        this.id = id;
        this.usuid = usuid;
        this.obid = obid;
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
}
