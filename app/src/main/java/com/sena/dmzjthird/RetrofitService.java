package com.sena.dmzjthird;

import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean1;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean2;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:18
 */
public interface RetrofitService {


    public static final String BASE_URL = "https://nnv3api.muwai.com/";

    // https://nnv3api.muwai.com/recommend/batchUpdateWithLevel?category_id=50
    @GET("recommend/batchUpdateWithLevel")
    Observable<ComicRecommendChildBean1> getComicRecommend1(
            @Query("category_id") int category_id
    );

    @GET("recommend/batchUpdateWithLevel")
    Observable<ComicRecommendChildBean2> getComicRecommend2 (
            @Query("category_id") int category_id
    );

}
