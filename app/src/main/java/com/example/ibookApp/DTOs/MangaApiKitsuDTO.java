package com.example.ibookApp.DTOs;

public class MangaApiKitsuDTO {

    private String id;
    private String createdAt;
    private String updatedAt;
    private String slug;
    private String synopsis;
    private String avarageRating;
    private String startDate;
    private String endDate;
    private String status;
    private String image;
    private String type;
    private String autor;

    public MangaApiKitsuDTO(String id, String createdAt, String updatedAt, String slug, String synopsis, String avarageRating, String startDate, String endDate, String status, String image, String type, String Autor) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.slug = slug;
        this.synopsis = synopsis;
        this.avarageRating = avarageRating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.image = image;
        this.type = type;
        this.autor = autor;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        autor = autor;
    }
    public String getCreatedAt() {
        return createdAt;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getAvarageRating() {
        return avarageRating;
    }

    public void setAvarageRating(String avarageRating) {
        this.avarageRating = avarageRating;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
