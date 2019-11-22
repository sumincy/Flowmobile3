package com.huateng.ebank.network;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

public interface CommonApiService {


    //上传文件单独实例化了一个Retrofit @ com.huateng.android.network.upload.RetrofitUtil
    @Headers({"Accept: */*"})
    @Multipart
    @POST("upload/upfile")
    Observable<String> uploadFile(@QueryMap Map<String, String> queryMap, @Header("X-Authorization") String token, @Part MultipartBody.Part part);

    @Headers({"Accept: application/json"})
    @POST("upload/upfile")
    Observable<String> uploadFile(@Body MultipartBody multipartBody);

    @Headers({"Accept: */*"})
    @GET("download/downloadFile")
    @Streaming
        //大文件时要加不然会OOM
    Observable<ResponseBody> downloadFile(@Header("X-Authorization") String token, @QueryMap Map<String, String> map);

    @Headers({"Accept: application/json"})
    @GET("file/getFile")
    Observable<String> getFile(@Header("X-Authorization") String token, @QueryMap Map<String, String> map);


    //获取参数
    @GET("param/list")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<String> get(@QueryMap Map<String, String> formdata, @Header("X-Authorization") String token);

    //获取参数
    @GET("upload/delete")
    @Headers({"Accept: application/json"})
    Observable<String> get(@Query("FileName") String fileName, @Header("X-Authorization") String token);


    @POST("{api}.htm")
    @Headers({"Content-Type: application/json", "Content-Type: text/html", "Accept: application/json"})
    Observable<ResponseBody> post(@Path(value = "api", encoded = true) String api, @Header("X-Authorization") String token, @Body RequestBody body);

    @GET("{api}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ResponseBody> get(@Path(value = "api", encoded = true) String api, @Header("X-Authorization") String token, @QueryMap Map<String, String> map);

}
