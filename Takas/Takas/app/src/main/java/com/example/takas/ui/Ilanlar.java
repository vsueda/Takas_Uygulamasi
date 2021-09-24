package com.example.takas.ui;

public class Ilanlar {
    private String goneriId;
    private String kullaniciId;
    private String gonderiBaslik;
    private String gonderiIl;
    private String gonderiKategori;
    private String gonderiResim;
    private String gonderiAciklama;

    public String getGoneriId() {
        return goneriId;
    }

    public void setGoneriId(String goneriId) {
        this.goneriId = goneriId;
    }

    public String getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(String kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getGonderiBaslik() {
        return gonderiBaslik;
    }

    public void setGonderiBaslik(String gonderiBaslik) {
        this.gonderiBaslik = gonderiBaslik;
    }

    public String getGonderiIl() {
        return gonderiIl;
    }

    public void setGonderiIl(String gonderiIl) {
        this.gonderiIl = gonderiIl;
    }

    public String getGonderiKategori() {
        return gonderiKategori;
    }

    public void setGonderiKategori(String gonderiKategori) {
        this.gonderiKategori = gonderiKategori;
    }

    public String getGonderiResim() {
        return gonderiResim;
    }

    public void setGonderiResim(String gonderiResim) {
        this.gonderiResim = gonderiResim;
    }

    public String getGonderiAciklama() {
        return gonderiAciklama;
    }

    public void setGonderiAciklama(String gonderiAciklama) {
        this.gonderiAciklama = gonderiAciklama;
    }

    public Ilanlar() {
    }

    public Ilanlar(String goneriId, String kullaniciId, String gonderiBaslik, String gonderiIl, String gonderiKategori,
                   String gonderiResim, String gonderiAciklama) {
        this.goneriId = goneriId;
        this.kullaniciId = kullaniciId;
        this.gonderiBaslik = gonderiBaslik;
        this.gonderiIl = gonderiIl;
        this.gonderiKategori = gonderiKategori;
        this.gonderiResim = gonderiResim;
        this.gonderiAciklama = gonderiAciklama;
    }
}
