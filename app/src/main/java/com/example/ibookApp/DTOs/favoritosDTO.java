package com.example.ibookApp.DTOs;

public class favoritosDTO {
    private String favid;
    private String usuid;
    private String obid;

    public favoritosDTO(String favid, String usuid, String obid) {
        this.favid = favid;
        this.usuid = usuid;
        this.obid = obid;
    }

    public String getFavid() {
        return favid;
    }

    public void setFavid(String favid) {
        this.favid = favid;
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
