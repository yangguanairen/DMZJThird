package com.sena.dmzjthird;

import com.sena.dmzjthird.account.bean.LoginBean;
import com.sena.dmzjthird.account.bean.UserComicCommentBean;
import com.sena.dmzjthird.account.bean.UserNovelCommentBean;
import com.sena.dmzjthird.account.bean.UserSubscribedBean;
import com.sena.dmzjthird.comic.bean.AuthorInfoBean;
import com.sena.dmzjthird.comic.bean.ComicClassifyBean;
import com.sena.dmzjthird.comic.bean.ComicClassifyCoverBean;
import com.sena.dmzjthird.comic.bean.ComicClassifyFilterBean;
import com.sena.dmzjthird.comic.bean.ComicComplaintRankBean;
import com.sena.dmzjthird.comic.bean.ComicLatestBean;
import com.sena.dmzjthird.comic.bean.ComicPopularityRankBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean1;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean2;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean3;
import com.sena.dmzjthird.comic.bean.ComicRelatedBean;
import com.sena.dmzjthird.comic.bean.ComicSubscribeBean;
import com.sena.dmzjthird.comic.bean.ComicSubscribeRankBean;
import com.sena.dmzjthird.comic.bean.ComicTopicBean;
import com.sena.dmzjthird.comic.bean.ComicTopicInfoBean;

import java.util.List;
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
    String BASE_ORIGIN_URL = "http://m.dmzj.com/";


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

    // nnv3api.muwai.com/0/category_with_level.json
    @GET("0/category_with_level.json")
    Observable<ComicClassifyCoverBean> getComicClassifyCover(

    );

    // nnv3api.muwai.com/subject_with_level/0/0.json
    @GET("subject_with_level/0/{page}.json")
    Observable<ComicTopicBean> getComicTopic(
            @Path("page") int page
    );

    // https://m.dmzj.com/latest/0.json
    @GET("latest/{page}.json")
    Observable<List<ComicLatestBean>> getLatestComic(
            @Path("page") int page
    );

    // https://m.dmzj.com/rank/0-0-0-0.json
    @GET("rank/0-0-{time}-{page}.json")
    Observable<List<ComicPopularityRankBean>> getPopularityRankComic(
            @Path("time") int time,
            @Path("page") int page
    );

    // https://m.dmzj.com/rank/1-0-0-0.json
    @GET("rank/1-0-{time}-{page}.json")
    Observable<List<ComicComplaintRankBean>> getComplaintRankComic(
            @Path("time") int time,
            @Path("page") int page
    );

    // https://m.dmzj.com/rank/2-0-0-0.json
    @GET("rank/2-0-{time}-{page}.json")
    Observable<List<ComicSubscribeRankBean>> getSubscribeRankComic(
            @Path("time") int time,
            @Path("page") int page
    );

    @GET("v3/comic/related/{id}.json")
    Observable<ComicRelatedBean> getComicRelated(
            @Path("id") String id
    );

    // nnv3api.muwai.com/UCenter/author/7470.json
    @GET("/UCenter/author/{id}.json")
    Observable<AuthorInfoBean> getAuthorInfo(
            @Path("id") String id
    );

    // nnv3api.muwai.com//classifyWithLevel/0/0/0.json
    @GET("classifyWithLevel/{filter}/{sort}/{page}.json")
    Observable<List<ComicClassifyBean>> getComicClassify(
            @Path("filter") String filter,
            @Path("sort") String sort,
            @Path("page") int page
    );


    // nnv3api.muwai.com/classify/filter.json
    @GET("classify/filter.json")
    Observable<List<ComicClassifyFilterBean>> getComicClassifyFilter(
    );


    @Multipart
    @POST("loginV2/m_confirm")
    Observable<LoginBean> confirmAccount(
            @PartMap Map<String, RequestBody> requestBodyMap
    );

    @GET("subscribe/add")
    Observable<ComicSubscribeBean> subscribeComic(
//            obj_ids=59829&uid=109697332&type=mh
            @Query("obj_ids") String id,
            @Query("uid") String uid,
            @Query("type") String type
    );

    @GET("subscribe/cancel")
    Observable<ComicSubscribeBean> cancelSubscribeComic(
            @Query("obj_ids") String id,
            @Query("uid") String uid,
            @Query("type") String type
    );

    // nnv3api.muwai.com/v3/subscribe/0/109697332/0.json?uid=109697332
    // 0:Comic, 1:Novel
    @GET("v3/subscribe/{type}/{targetUid}/{page}.json")
    Observable<List<UserSubscribedBean>> getUserSubscribed(
            @Path("type") int type,
            @Path("targetUid") String targetUid,
            @Path("page") int page,
            @Query("uid") String uid

    );

    // nnv3api.muwai.com/v3/old/comment/owner/0/100021094/0.json
    @GET("v3/old/comment/owner/0/{uid}/{page}.json")
    Observable<List<UserComicCommentBean>> getUserComicComments(
            @Path("uid") String uid,
            @Path("page") int page
    );

    // nnv3api.muwai.com/comment/owner/1/100021094/0.json
    @GET("comment/owner/1/{uid}/{page}.json")
    Observable<List<UserNovelCommentBean>> getUserNovelComments(
            @Path("uid") String uid,
            @Path("page") int page
    );

    // nnv3api.muwai.com/subject_with_level/458.json
    @GET("subject_with_level/{id}.json")
    Observable<ComicTopicInfoBean> getTopicInfo(
            @Path("id") String id
    );

}
