package network.api;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Triyandi on 12/02/2016.
 */
public class RetrofitApiSingleton {

    private static RetrofitApiSingleton singleton;
    private RetrofitApi retrofitApi;

    public static RetrofitApiSingleton getInstance(){
        if(null == singleton){
            singleton = new RetrofitApiSingleton();
        }
        return singleton;
    }

    public void init(String homeUrl){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(homeUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofitApi = retrofit.create(RetrofitApi.class);
    }

    public RetrofitApi getApi(){
       return retrofitApi;
    }
}