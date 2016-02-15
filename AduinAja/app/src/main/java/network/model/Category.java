package network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Triyandi on 13/02/2016.
 */
public class Category {
    @SerializedName("data")
    @Expose
    private List<CategoryDatum> categryDatumList = new ArrayList<CategoryDatum>();
    @SerializedName("status")
    @Expose
    private String status;

    public List<CategoryDatum> getCategoryDatumList() {
        return categryDatumList;
    }

    public void setCategryDatumList(List<CategoryDatum> categryDatumList) {
        this.categryDatumList = categryDatumList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class CategoryDatum{
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getDeskripsi() {
            return deskripsi;
        }

        public void setDeskripsi(String deskripsi) {
            this.deskripsi = deskripsi;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("nama")
        @Expose
        private String nama;
        @SerializedName("slug")
        @Expose
        private String slug;
        @SerializedName("deskripsi")
        @Expose
        private String deskripsi;
        @SerializedName("status")
        @Expose
        private String status;
    }
}
