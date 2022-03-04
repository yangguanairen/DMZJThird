package com.sena.dmzjthird;

import com.sena.dmzjthird.account.bean.DmzjUserSubscribedBean;
import com.sena.dmzjthird.account.bean.LoginBean;
import com.sena.dmzjthird.account.bean.UserComicCommentBean;
import com.sena.dmzjthird.account.bean.UserNovelCommentBean;
import com.sena.dmzjthird.account.bean.UserSubscribedBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendLikeBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendOtherBean;
import com.sena.dmzjthird.comic.bean.AuthorInfoBean;
import com.sena.dmzjthird.comic.bean.ComicClassifyBean;
import com.sena.dmzjthird.comic.bean.ComicClassifyCoverBean;
import com.sena.dmzjthird.comic.bean.ComicClassifyFilterBean;
import com.sena.dmzjthird.comic.bean.ComicLatestBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendNewBean;
import com.sena.dmzjthird.comic.bean.ComicRelatedBean;
import com.sena.dmzjthird.comic.bean.ComicSearchResultBean;
import com.sena.dmzjthird.comic.bean.SearchHotBean;
import com.sena.dmzjthird.comic.bean.ComicSubscribeBean;
import com.sena.dmzjthird.comic.bean.ComicTopicBean;
import com.sena.dmzjthird.comic.bean.ComicTopicInfoBean;
import com.sena.dmzjthird.comic.bean.ComicChapterInfoBean;
import com.sena.dmzjthird.comic.bean.UserIsSubscribeBean;
import com.sena.dmzjthird.news.bean.NewsBannerBean;
import com.sena.dmzjthird.news.bean.NewsCategoryBean;
import com.sena.dmzjthird.novel.bean.NovelCategoryBean;
import com.sena.dmzjthird.novel.bean.NovelFilterBean;
import com.sena.dmzjthird.novel.bean.NovelFilterTagBean;
import com.sena.dmzjthird.novel.bean.NovelLatestBean;
import com.sena.dmzjthird.novel.bean.NovelRankBean;
import com.sena.dmzjthird.novel.bean.NovelRankTagBean;
import com.sena.dmzjthird.novel.bean.NovelRecommendBean;

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
    String BASE_ORIGIN_URL = "https://m.dmzj.com/";
    String BASE_COMMENT_URL = "https://nnv3api.dmzj1.com";



    /**************************************** 新闻 ****************************************/

    // 新闻分类
    // https://nnv3api.muwai.com/article/category.json
    @GET("article/category.json")
    Observable<List<NewsCategoryBean>> getNewsCategory();

    // 新闻轮播图
    // https://nnv3api.muwai.com/article/recommend/header.json
    @GET("article/recommend/header.json")
    Observable<NewsBannerBean> getNewsBanner();

    /**************************************** 新闻 ****************************************/


    /**************************************** 轻小说 ****************************************/

    // 轻小说首页推荐
    // https://nnv3api.muwai.com/novel/recommend.json
    @GET("novel/recommend.json")
    Observable<List<NovelRecommendBean>> getNovelRecommend();

    // 轻小说更新
    // https://nnv3api.muwai.com/novel/recentUpdate/0.json
    @GET("novel/recentUpdate/{page}.json")
    Observable<List<NovelLatestBean>> getNovelLatest(
            @Path("page") int page
    );

    // 轻小说目录
    // https://nnv3api.muwai.com/1/category.json
    @GET("1/category.json")
    Observable<List<NovelCategoryBean>> getNovelCategory();

    // 轻小说排行分类
    // https://nnv3api.muwai.com/novel/tag.json
    @GET("novel/tag.json")
    Observable<List<NovelRankTagBean>> getNovelRankTag();

    // 轻小说排行
    // https://nnv3api.muwai.com/novel/rank/0/0/0.json
    @GET("novel/rank/{tag}/{sort}/{page}.json")
    Observable<List<NovelRankBean>> getNovelRank(
            @Path("tag") int tag,
            @Path("sort") int sort,
            @Path("page") int page
    );

    // 轻小说筛选分类
    // https://nnv3api.muwai.com/novel/filter.json
    @GET("novel/filter.json")
    Observable<List<NovelFilterTagBean>> getNovelFilterTag();

    // 轻小说筛选
    // https://nnv3api.muwai.com/novel/0/0/1/0.json
    @GET("novel/{tag}/{status}/{sort}/{page}.json")
    Observable<List<NovelFilterBean>> getNovelFilter(
        @Path("tag") int tag,
        @Path("status") int status,
        @Path("sort") int sort,
        @Path("page") int page
    );
    /**************************************** 轻小说 ****************************************/


    /**************************************** 漫画 ****************************************/


    // 漫画首页推荐
    // https://nnv3api.muwai.com/recommend_new.json
    @GET("recommend_new.json")
    Observable<List<ComicRecommendNewBean>> getComicRecommend();

    // 漫画首页--猜你喜欢
    // https://nnv3api.muwai.com/recommend/batchUpdate?category_id=50
    @GET("recommend/batchUpdate?category_id=50")
    Observable<ComicRecommendLikeBean> getComicRecommendLike();

    // 漫画首页--国漫也精彩
    // https://nnv3api.muwai.com/recommend/batchUpdate?category_id=52
    @GET("recommend/batchUpdate?category_id=52")
    Observable<ComicRecommendOtherBean> getComicRecommendGuoman();

    // 漫画首页--热门连载
    // https://nnv3api.muwai.com/recommend/batchUpdate?category_id=54
    @GET("recommend/batchUpdate?category_id=54")
    Observable<ComicRecommendOtherBean> getComicRecommendHot();

    // 漫画目录
    // https://nnv3api.muwai.com/0/category_with_level.json
    @GET("0/category_with_level.json")
    Observable<ComicClassifyCoverBean> getComicCategory();

    // 漫画专题
    // https://nnv3api.muwai.com/subject/0/0.json
    @GET("subject/0/{page}.json")
    Observable<ComicTopicBean> getComicTopic(
            @Path("page") int page
    );

    // 漫画专题详情
    // https://nnv3api.muwai.com/subject/458.json
    @GET("subject_with_level/{id}.json")
    Observable<ComicTopicInfoBean> getTopicInfo(
            @Path("id") String id
    );

    // 漫画筛选
    // https://nnv3api.muwai.com/classifyWithLevel/0/0/0.json
    @GET("classifyWithLevel/{filter}/{sort}/{page}.json")
    Observable<List<ComicClassifyBean>> getComicClassify(
            @Path("filter") String filter,
            @Path("sort") String sort,
            @Path("page") int page
    );


    // 漫画相关内容
    // https://nnv3api.muwai.com/v3/comic/related/{id}.json
    @GET("v3/comic/related/{id}.json")
    Observable<ComicRelatedBean> getComicRelated(
            @Path("id") String id
    );

    // nnv3api.muwai.com/UCenter/author/7470.json
    @GET("/UCenter/author/{id}.json")
    Observable<AuthorInfoBean> getAuthorInfo(
            @Path("id") String id
    );




    // nnv3api.muwai.com/classify/filter.json
    @GET("classify/filter.json")
    Observable<List<ComicClassifyFilterBean>> getComicClassifyFilter();


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



    // m.dmzj.com/chapinfo/45854/113148.html
    @GET("chapter/{comicId}/{chapterId}.json")
    Observable<ComicChapterInfoBean> getChapterInfo(
            @Path("comicId") String comicId,
            @Path("chapterId") String chapterId
    );

    // nnv3api.muwai.com/subscribe/0/109697332/61539
    @GET("subscribe/0/{uid}/{obj_id}")
    Observable<UserIsSubscribeBean> isSubscribe(
            @Path("uid") String uid,
            @Path("obj_id") String obj_id
    );

    // nnv3api.muwai.com/search/hot/0.json
    @GET("search/hot/{type}.json")
    Observable<List<SearchHotBean>> getSearchHot(
            @Path("type") int type
    );

    // nnv3api.muwai.com/search/showWithLevel/0/一/0.json
    @GET("search/showWithLevel/0/{query}/{page}.json")
    Observable<List<ComicSearchResultBean>> getComicSearchResult(
            @Path("query") String query,
            @Path("page") int page
    );
/**************************************** 漫画 ****************************************/


/**************************************** 用户 ****************************************/

    // 动漫之家用户已订阅漫画
    // https://nnv3api.muwai.com/UCenter/subscribe?uid=109697332&sub_type=1&page=0&type=0
    @GET("UCenter/subscribe")
    Observable<List<DmzjUserSubscribedBean>> getDmzjUserSubscribed(
            @Query("uid") String uid,        // 动漫之家用户uid
            @Query("type") int type,         // 0=漫画； 1=轻小说
            @Query("sub_type") int subType,  // 1=全部； 2=未读； 3=已读； 4=完结
            @Query("page") int page          // 页数
    );



/**************************************** 用户 ****************************************/




}
