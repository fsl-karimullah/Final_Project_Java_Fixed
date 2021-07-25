/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worker.ui;

/**
 *
 * @author Faisal
 */
public class WorkerList {

    int id;
    String nama, no_whatsapp, alamat, email, nama_kategori;
    Boolean isAcc = false;

    public WorkerList(int id,String nama, String no_whatsapp, String alamat, String email, String nama_kategori, Boolean isAcc) {
        this.id = id;
        this.nama = nama;
        this.no_whatsapp = no_whatsapp;
        this.alamat = alamat;
        this.email = email;
        this.nama_kategori = nama_kategori;
        this.isAcc = isAcc;
    }

    public Boolean isIsAcc() {
        return isAcc;
    }

    public void setIsAcc(Boolean isAcc) {
        this.isAcc = isAcc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNo_whatsapp() {
        return no_whatsapp;
    }

    public void setNo_whatsapp(String no_whatsapp) {
        this.no_whatsapp = no_whatsapp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

}
