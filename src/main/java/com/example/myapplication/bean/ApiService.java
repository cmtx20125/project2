package com.example.myapplication.bean;

import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/login")
    Call<HttpResponseEntityS> login(@Query("userName") String userName, @Query("password") String password);
    @GET("auth/sendCode")
    Call<ReviewResponse> getVerificationCode(@Query("phone") String phone);
    @FormUrlEncoded
    @POST("auth/register")
    Call<ReviewResponse> register(@Field("phone") String phone,@Field("code") String code,@Field("userName") String userName);
    @GET("api/sendPhone")
    Call<ReviewResponse> checkPhoneAvailable(@Query("phone") String phone);
    @GET("api/sendUser")
    Call<ReviewResponse> checkUsernameAvailable(@Query("userName") String username);

    // 登录验证
    @GET("auth/verifyCode")
    Call<HttpResponseEntityS> loginWithCode(@Query("phone") String phone, @Query("code") String code);

    @GET("api/viewAllArticle")
    Call<HttpResponseEntity<List<Article>>> getAllArticles();

    @GET("api/viewAllReport")
    Call<HttpResponseEntity<List<Report>>> getAllReports(@Query("la") String la,@Query("lo") String lo,@Query("userId") String userId);

    @GET("api/viewAllPublish")
    Call<HttpResponseEntity<List<Publish>>> getAllPublishs(@Query("id") String userId);

    @GET("api/viewAllPet")
    Call<HttpResponseEntity<List<Pet>>> getAllPets(@Query("id") String userId);
        @GET("api/QueryOneArticle/{id}")
        Call<Article> getArticleDetail(@Path("id") String articleId);
    @GET("api/getAnimalById")
    Call<HttpResponseEntityd> getAnimalById(@Query("reportId") String reportId);

    @GET("api/viewAllReview")
    Call<HttpResponseEntity<List<Review>>> getAllReviews(@Query("publishId") String publishId);
    @POST("api/reviewadd")
    Call<ReviewResponse> addReview(@Body Review review);
    @POST("api/dialogueAdd")
    Call<ReviewResponse> addDialogue(@Body Dialogue dialogue);
    @GET("api/getUser")
    Call<HttpResponseEntityS> getUser(@Query("userId") String userId);
    @Multipart
    @POST("api/publish/add")
    Call<HttpResponseEntityp> addPublish(
            @Part("publishTag") RequestBody publishTag,
            @Part("publishContent") RequestBody publishContent,
            @Part("userId") RequestBody userId,@Part("publishName") RequestBody publishName,
            @Part("userName") RequestBody userName, @Part MultipartBody.Part coverImage,
            @Part MultipartBody.Part contentImage
    );
    @Multipart
    @POST("api/publish/addd")
    Call<HttpResponseEntityp> addPublisht(
            @Part("publishTag") RequestBody publishTag,
            @Part("publishContent") RequestBody publishContent,
            @Part("userId") RequestBody userId,@Part("publishName") RequestBody publishName,
            @Part("userName") RequestBody userName, @Part MultipartBody.Part coverImage,
            @Part MultipartBody.Part contentImage
    );
    @Multipart
    @POST("api/report/addd")
    Call<HttpResponseEntityp> addReportt(
            @Part("publishTag") RequestBody publishTag,
            @Part("publishContent") RequestBody publishContent,
            @Part("userId") RequestBody userId,@Part("publishName") RequestBody publishName,@Part("address") RequestBody address,
            @Part("userName") RequestBody userName, @Part("reportId") RequestBody reportId,@Part("la") RequestBody la,@Part("lo") RequestBody lo,@Part MultipartBody.Part coverImage,
            @Part MultipartBody.Part contentImage
    );
    @Multipart
    @POST("api/report/add")
    Call<HttpResponseEntityp> addReport(
            @Part("publishTag") RequestBody publishTag,
            @Part("publishContent") RequestBody publishContent,
            @Part("userId") RequestBody userId,@Part("publishName") RequestBody publishName,@Part("address") RequestBody address,
            @Part("userName") RequestBody userName,@Part("reportId") RequestBody reportId,@Part("la") RequestBody la,@Part("lo") RequestBody lo, @Part MultipartBody.Part coverImage,
            @Part MultipartBody.Part contentImage
    );
    @Multipart
    @POST("api/updateAnimal")
    Call<HttpResponseEntityp> updateAnimal(
            @Part("name") RequestBody name,
            @Part("gender") RequestBody gender,
            @Part("v") RequestBody v,@Part("jue") RequestBody jue,
            @Part("ReportId") RequestBody ReportId,
            @Part("age") RequestBody age,@Part("s") RequestBody s,
            @Part("content") RequestBody content,
            @Part("id") RequestBody id,
            @Part MultipartBody.Part animalImage
    );
    @Multipart
    @POST("api/updatePet")
    Call<updateEntity> updatePet(
            @Part("name") RequestBody name,
            @Part("gender") RequestBody gender,
            @Part("v") RequestBody v,@Part("jue") RequestBody jue,
            @Part("ReportId") RequestBody ReportId,
            @Part("age") RequestBody age,@Part("s") RequestBody s,
            @Part("content") RequestBody content,
            @Part("id") RequestBody id,
            @Part MultipartBody.Part animalImage
    );
    @Multipart
    @POST("api/petAdd")
    Call<HttpResponseEntityp> addPet(
            @Part("name") RequestBody name,
            @Part("gender") RequestBody gender,
            @Part("v") RequestBody v,@Part("jue") RequestBody jue,
            @Part("ReportId") RequestBody ReportId,
            @Part("age") RequestBody age,@Part("s") RequestBody s,
            @Part("content") RequestBody content,
            @Part MultipartBody.Part animalImage
    );
    @Multipart
    @POST("api/animalAdd")
    Call<HttpResponseEntityp> addAnimal(
            @Part("name") RequestBody name,
            @Part("gender") RequestBody gender,
            @Part("v") RequestBody v,@Part("jue") RequestBody jue,
            @Part("ReportId") RequestBody ReportId,
            @Part("age") RequestBody age,@Part("s") RequestBody s,
            @Part("content") RequestBody content,
            @Part MultipartBody.Part animalImage
    );
    @Multipart
    @POST("api/updateUserV")
    Call<updateEntity> updateUser(
            @Part("name") RequestBody name,
            @Part("gender") RequestBody gender,
            @Part("phone") RequestBody phone,@Part("pwd") RequestBody pwd,
            @Part("id") RequestBody id,
            @Part("address") RequestBody address,@Part("time") RequestBody time,
            @Part MultipartBody.Part animalImage
    );
    @GET("api/viewAllDialogue")
    Call<HttpResponseEntity<List<Dialogue>>> getAllDialogues(@Query("userId") String userId,@Query("PId") String PId);
    @GET("api/viewAllUnread")
    Call<HttpResponseEntity<List<UnreadUser>>> getAllUnreads(@Query("userId") String userId);
    @GET("api/updateDialogue")
    Call<updateEntity> updateDialogue(@Query("userId") String userId,@Query("PId") String PId);
    @GET("api/viewAllUsers")
    Call<HttpResponseEntity<List<DialogueDto>>> getUsers(@Query("userId") String userId);
    @POST("api/updateUser")
    Call<ReviewResponse> updateUserSet(@Body User user);
    @POST("api/deleteAnimal")
    Call<ReviewResponse> deleteAnimal(@Body Animal animal);
    @POST("api/addPetn")
    Call<ReviewResponse> addPet(@Body Pet pet);
    @FormUrlEncoded
    @POST("api/addJ")
    Call<ReviewResponse> addJ(@Field("userId") String userId,@Field("reportId") String reportId,@Field("reportType") String reportType);

}