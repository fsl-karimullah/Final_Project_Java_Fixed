/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject.ui;

/**
 *
 * @author Faisal
 */
public class ProfesiList {

    int id;
    String nama_profesi;

    public ProfesiList(int id, String nama_profesi) {
        this.id = id;
        this.nama_profesi = nama_profesi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_profesi() {
        return nama_profesi;
    }

    public void setNama_profesi(String nama_profesi) {
        this.nama_profesi = nama_profesi;
    }

}
