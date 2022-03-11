package com.sena.dmzjthird.account;

import com.sena.dmzjthird.account.bean.ResultBean;
import com.sena.dmzjthird.account.bean.UserHistoryBean;
import com.sena.dmzjthird.account.bean.UserResultBean;
import com.sena.dmzjthird.account.bean.UserSubscribedBean;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * FileName: UserRetrofitService
 * Author: JiaoCan
 * Date: 2022/1/21 9:40
 */

public interface MyRetrofitService {

    String MY_BASE_URL = "https://yangguanairen.plus/";

    String DMZJ_UID = "109697332";
    String DMZJ_TOKEN = "ad1c752a57819508ddf6f335491bb126";

    int TYPE_COMIC = 1;
    int TYPE_NOVEL = 2;

    @Multipart
    @POST("user/add")
    Observable<UserResultBean> createAccount(
            @PartMap Map<String, RequestBody> requestBodyMap
    );

    @Multipart
    @POST("user/login")
    Observable<UserResultBean> loginAccount(
            @PartMap Map<String, RequestBody> requestBodyMap
    );

    @GET("user/query")
    Observable<UserResultBean> queryAccount(
            @Query("uid") long uid
    );

    @Multipart
    @POST("/user/updateProfile")
    Observable<UserResultBean> updateAccount(
           @PartMap Map<String, RequestBody> requestBodyMap
    );

    @Multipart
    @POST("/user/updatePassword")
    Observable<ResultBean> updatePassword(
            @PartMap Map<String, RequestBody> requestBodyMap
    );

    @Multipart
    @POST("image/upload")
    Observable<ResultBean> uploadImage(
            @Part List<MultipartBody.Part> partList
    );


    @GET("subscribe/control")
    Observable<ResultBean> controlSubscribe(
            @Query("uid") long uid,
            @Query("objectId") String objectId,
            @Query("objectCover") String objectCover,
            @Query("objectName") String objectName,
            @Query("author") String author,
            @Query("type") int type
    );

    @GET("subscribe/query")
    Observable<ResultBean> querySubscribe(
            @Query("uid") long uid,
            @Query("objectId") String objectId,
            @Query("type") int type
    );

    @GET("subscribe/all")
    Observable<List<UserSubscribedBean>> getAllSubscribe(
            @Query("uid") long uid,
            @Query("type") int type
    );

    @GET("history/all")
    Observable<List<UserHistoryBean>> getAllHistory(
            @Query("uid") long uid,
            @Query("type") int type
    );

    @GET("history/add")
    Observable<ResultBean> addHistory(
            @Query("uid") long uid,
            @Query("objectId") String objectId,
            @Query("objectCover") String objectCover,
            @Query("objectName") String objectName,
            @Query("objectType") int objectType,
            @Query("volumeId") int volumeId,
            @Query("volumeName") String volumeName,
            @Query("chapterId") int chapterId,
            @Query("chapterName") String chapterName
    );

}
