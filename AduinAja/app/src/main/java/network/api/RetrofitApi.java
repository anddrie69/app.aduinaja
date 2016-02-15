package network.api;

import com.aduinaja.aduinaja.Aduin;

import java.util.List;

import network.model.Aduan;
import network.model.Category;
import network.model.VerNik;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Triyandi on 12/02/2016.
 */
public interface RetrofitApi {

    @GET("backend/web/index.php?r=api/verifikasinik")
    Call<VerNik> getVerNIK(@Query("nik") String nik, @Query("id_fb") String idFb);

    @GET("backend/web/index.php?r=api/getkategori")
    Call<Category> getCategoryList();

    @Multipart
    @POST("")
    Call<String> postUploadImgAduin();

    @GET("backend/web/index.php?r=api/addnewaduan")
    Call<Aduan> getAduan(@Body Aduan aduan);
}