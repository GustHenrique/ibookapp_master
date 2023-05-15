package com.example.ibookApp.DTOs;

public class UsuarioDTO {
    private String usuid;
    private String usunome;
    private String usuemail;
    private String ususenha;
    private String usuimagem;

    public UsuarioDTO(String usuemail, String ususenha, String usunome, String usuid, String usuimagem) {
        this.usuid = usuid;
        this.usunome = usunome;
        this.usuemail = usuemail;
        this.ususenha = ususenha;
        this.usuimagem = usuimagem;
    }

    public String getUsuimagem() {
        return usuimagem;
    }

    public void setUsuimagem(String usuimagem) {
        this.usuimagem = usuimagem;
    }
    public String getUsuemail() {
        return usuemail;
    }

    public void setUsuemail(String usuemail) {
        this.usuemail = usuemail;
    }

    public String getUsusenha() {
        return ususenha;
    }

    public void setUsusenha(String ususenha) {
        this.ususenha = ususenha;
    }

    public String getUsunome() {
        return usunome;
    }

    public void setUsunome(String usunome) {
        this.usunome = usunome;
    }

    public String getUsuid() {
        return usuid;
    }

    public void setUsuid(String usuid) {
        this.usuid = usuid;
    }

}

