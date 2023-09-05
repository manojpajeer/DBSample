package info.accolade.databaseconnection.utils;

import java.util.List;

import info.accolade.databaseconnection.model.RegisterModel;
import info.accolade.databaseconnection.model.StudentModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    //for student registration
    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterModel> getRegisterResponse(
            @Field("uname") String name
    );

    //for student recycler
    @POST("student.php")
    Call<List<StudentModel>> getStudentResponse();

}
