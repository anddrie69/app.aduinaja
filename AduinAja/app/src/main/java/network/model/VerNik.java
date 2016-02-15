package network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Triyandi on 12/02/2016.
 */


public class VerNik {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private VerNikData verNikData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VerNikData getVerNikData() {
        return verNikData;
    }

    public void setVerNikData(VerNikData verNikData) {
        this.verNikData = verNikData;
    }

    public class VerNikData {
        @SerializedName("comm")
        @Expose
        private String comm;
        @SerializedName("noTPS")
        @Expose
        private String noTPS;
        @SerializedName("alamat_tps")
        @Expose
        private String alamatTps;
        @SerializedName("nik")
        @Expose
        private String nik;
        @SerializedName("nama")
        @Expose
        private String nama;
        @SerializedName("jenis_kelamin")
        @Expose
        private String jenisKelamin;
        @SerializedName("pro")
        @Expose
        private String pro;
        @SerializedName("kab")
        @Expose
        private String kab;
        @SerializedName("kec")
        @Expose
        private String kec;
        @SerializedName("kel")
        @Expose
        private String kel;

        public String getComm() {
            return comm;
        }

        public void setComm(String comm) {
            this.comm = comm;
        }

        public String getNoTPS() {
            return noTPS;
        }

        public void setNoTPS(String noTPS) {
            this.noTPS = noTPS;
        }

        public String getAlamatTps() {
            return alamatTps;
        }

        public void setAlamatTps(String alamatTps) {
            this.alamatTps = alamatTps;
        }

        public String getNik() {
            return nik;
        }

        public void setNik(String nik) {
            this.nik = nik;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getJenisKelamin() {
            return jenisKelamin;
        }

        public void setJenisKelamin(String jenisKelamin) {
            this.jenisKelamin = jenisKelamin;
        }

        public String getPro() {
            return pro;
        }

        public void setPro(String pro) {
            this.pro = pro;
        }

        public String getKab() {
            return kab;
        }

        public void setKab(String kab) {
            this.kab = kab;
        }

        public String getKec() {
            return kec;
        }

        public void setKec(String kec) {
            this.kec = kec;
        }

        public String getKel() {
            return kel;
        }

        public void setKel(String kel) {
            this.kel = kel;
        }
    }
}