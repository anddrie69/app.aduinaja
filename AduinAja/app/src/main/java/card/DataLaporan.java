package card;

import android.content.Context;

/**
 * Created by elmee on 05/11/2015.
 */
public class DataLaporan {
    private String imgAvatar;
    private String nama;
    private String waktu;
    private String imgPost;
    private String title;
    private String status;
    private String upVote;
    private String downVote;
    private String comments;
    private String category;
    private Context context;

    public DataLaporan(String imgAvatar, String nama, String waktu, String imgPost, String title, String status, String upVote, String downVote, String comments, String category, Context context) {

        this.imgAvatar = imgAvatar;
        this.nama = nama;
        this.waktu = waktu;
        this.imgPost = imgPost;
        this.title = title;
        this.status = status;
        this.upVote = upVote;
        this.downVote = downVote;
        this.comments = comments;
        this.category = category;
        this.context = context;
    }

    public String getImgAvatar() {
        return imgAvatar;
    }

    public void setImgAvatar(String imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getImgPost() {
        return imgPost;
    }

    public void setImgPost(String imgPost) {
        this.imgPost = imgPost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpVote() {
        return upVote;
    }

    public void setUpVote(String upVote) {
        this.upVote = upVote;
    }

    public String getDownVote() {
        return downVote;
    }

    public void setDownVote(String downVote) {
        this.downVote = downVote;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCategory() {
        return comments;
    }

    public void setCategory(String category) {
        this.comments = category;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
