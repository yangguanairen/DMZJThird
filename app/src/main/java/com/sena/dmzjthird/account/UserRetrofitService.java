package com.sena.dmzjthird.account;

import com.sena.dmzjthird.account.bean.ResultBean;
import com.sena.dmzjthird.account.bean.UserResultBean;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * FileName: UserRetrofitService
 * Author: JiaoCan
 * Date: 2022/1/21 9:40
 */

public interface UserRetrofitService {

    String MY_BASE_URL = "https://yangguanairen.plus/";

    String DMZJ_UID = "109697332";
    String DMZJ_TOKEN = "ad1c752a57819508ddf6f335491bb126";

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

}
