package com.clemilton.firebaseappsala.model;

import com.google.firebase.database.Exclude;

public class User {
    private String id,email,nome,photoUrl;

    // armazena se o usuario recebeu solicitacao
    @Exclude
    private String typeRequest;
    public String getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }




    public User(){

    }

    public User(String id, String email, String nome) {
        this.id = id;
        this.email = email;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean equals(User u){
        return this.id.equals(u.getId());
    }
}
