package com.sena.dmzjthird;

import com.sena.dmzjthird.account.bean.ComicTopicBean;
import com.sena.dmzjthird.account.bean.LoginBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean1;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean2;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean3;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:18
 */
public interface RetrofitService {

    String BASE_V3_URL = "https://nnv3api.muwai.com/";
    String BASE_USER_URL = "https://user.dmzj.com/";


    @GET("recommend/batchUpdateWithLevel")
    Observable<ComicRecommendChildBean1> getComicRecommend1(
            @Query("category_id") int category_id
    );

    @GET("recommend/batchUpdateWithLevel")
    Observable<ComicRecommendChildBean2> getComicRecommend2(
            @Query("category_id") int category_id
    );


    // nnv3api.muwai.com/recommend/batchUpdateWithLevel?uid=109697332&category_id=49
    @GET("recommend/batchUpdateWithLevel?uid=109697332&category_id=49")
    Observable<ComicRecommendChildBean3> getComicRecommend3(
            @Query("uid") String uid,
            @Query("category_id") int category_id
    );

    // http://nnv3api.muwai.com/subject_with_level/0/{page}.json
    @GET("subject_with_level/0/{page}.json")
    Observable<ComicTopicBean> getComicTopic(
      @Path("page") int page
    );



    @Multipart
    @POST("loginV2/m_confirm")
    Observable<LoginBean> confirmAccount(
            @PartMap Map<String, RequestBody> requestBodyMap
    );

}
