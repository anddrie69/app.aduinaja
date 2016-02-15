package network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Triyandi on 13/02/2016.
 */
public class Aduan {
    @SerializedName("member")
    @Expose
    private String member;
    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("category")
    @Expose
    private int category;
    @SerializedName("kecamatan")
    @Expose
    private String kecamatan;
    @SerializedName("image")
    @Expose
    private String image;

    public Aduan() {}

    public Aduan(String member, String judul, String deskripsi, int category, String kecamatan, String image){
        setMember(member);
        setJudul(judul);
        setDeskripsi(deskripsi);
        setCategory(category);
        setKecamatan(kecamatan);
        setImage(image);
    }


    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
