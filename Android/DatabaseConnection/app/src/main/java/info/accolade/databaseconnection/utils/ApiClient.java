package info.accolade.databaseconnection.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getApiClient()
    {
        Gson gson  = new GsonBuilder().setLenient().create();

        OkHttpClient client = new OkHttpClient();
        if(retrofit==null)
        {
            retrofit = new Retrofit.Builder().baseUrl(GlobalVariable.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }
}
