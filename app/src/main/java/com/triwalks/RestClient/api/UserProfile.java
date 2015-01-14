package com.triwalks.RestClient.api;



import com.triwalks.RestClient.model.AnalyzePhotoOutData;
import com.triwalks.RestClient.model.CreateUserData;
import com.triwalks.RestClient.model.ListStringData;
import com.triwalks.RestClient.model.ResultData;
import com.triwalks.RestClient.model.UpdateUserData;
import com.triwalks.RestClient.model.UserProfileData;
import com.triwalks.RestClient.model.WorldPhotoInData;

import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by tutul on 2014/12/28.
 */
public class UserProfile {
    private static UserProfileInterface UserProfileService;

    public static UserProfileInterface getUserApiClient() {
        if (UserProfileService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://140.113.23.226:5001")
                    .build();

            UserProfileService = restAdapter.create(UserProfileInterface.class);
        }

        return UserProfileService;
    }

    public interface UserProfileInterface {
        /**
         * upload avator
         * 上載大頭貼
         *
         * @param userid        TypedString
         * @param sessionid     TypedString
         * @param update        TypedString  if update == 1 , 大頭貼會覆蓋前者檔案,不等於1則如有檔案即回傳錯誤
         * @param avator        TypedFile   上載大頭貼的檔案
         * @return    ResultData
         */
        @Multipart
        @POST("/user/upload/avator")
        void uploadavator(@Part("avator") TypedFile avator, @Part("userid") TypedString userid,
                          @Part("sessionid") TypedString sessionid, @Part("update") TypedString update,
                          Callback<ResultData> callback);
        /**
         * get avator
         * 下載大頭貼
         *
         * @param userid        String   the userid is you want to get avator
         * @param sessionid     String
         * @return    Response  you can use ((TypedByteArray)Response.getBody()).getBytes(), it will retrun photo,
         *                      ref: https://medium.com/@giuder91/retrofit-how-to-download-get-a-file-e83a9badcf6c
         */
        @GET("/user/get/avator")
        void downloadavator(@Query("userid") String userid, @Query("sessionid") String sessionid, Callback<Response> callback);
        /**
         * login
         *
         * @param account     String
         * @param passwd      String
         * @return    UserProfileData  It will  setup the userID ,sessionID, and result
         */
        @GET("/user/login")
        void login(@Query("account") String account, @Query("passwd") String passwd, Callback<UserProfileData> callback);
        /**
         * logout
         *
         * @param userid     String     you can get it after login
         * @param sessionid  String     you can get it after login
         * @return ResultData  retrun the result
         */
        @GET("/user/loginout")
        void logout(@Query("userid") String userid, @Query("sessionid") String sessionid, Callback<ResultData> callback);
        /**
         * get User Info, you can get any user info, just input userID
         *
         * @param userid     String     input userid which your want to find
         * @param sessionid  String     you can get it after login
         * @param select     Map<String,String>
         *                   you just only need to set the key, and the value is "" , it means you want to its info.
         *                   if the Map is empty, it will return all info about the UserProfile.
         * @return UserProfileData
         */
        @GET("/user/get/info")
        void getUserInfo(@Query("userid") String userid, @Query("sessionid") String sessionid, @QueryMap Map<String, String> select, Callback<UserProfileData> callback);
        /**
         * get friends
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @return ListStringData
         */
        @GET("/user/get/friend")
        void getYourfriends(@Query("userid") String userid, @Query("sessionid") String sessionid, Callback<ListStringData> callback);
        /**
        * get your inviting friend
        *
        * @param userid     String     input your userid , you can get it after login
        * @param sessionid  String     you can get it after login
        * @return ListStringData
        */
        @GET("/user/get/inviting")
        void getYourinviting(@Query("userid") String userid, @Query("sessionid") String sessionid, Callback<ListStringData> callback);
        /**
         * get your invited friend
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @return ListStringData
         */
        @GET("/user/get/invited")
        void getYourinvited(@Query("userid") String userid, @Query("sessionid") String sessionid, Callback<ListStringData> callback);
        /**
         * get your black list
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @return ListStringData
         */
        @GET("/user/get/black_list")
        void getYourBlackList(@Query("userid") String userid, @Query("sessionid") String sessionid, Callback<ListStringData> callback);
        /**
         * get your trace user
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @return ListStringData
         */
        @GET("/user/get/trace_user")
        void getYourTraceUser(@Query("userid") String userid, @Query("sessionid") String sessionid, Callback<ListStringData> callback);
        /**
         * get your trace tripline
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @return ListStringData
         */
        @GET("/user/get/trace_tripline")
        void getYourTraceTripline(@Query("userid") String userid, @Query("sessionid") String sessionid, Callback<ListStringData> callback);
        /**
         * create a account
         *
         * @param userdata  String  以下是必要資訊 而創建時可輸入其他而外的屬性，查看CreateUserData
         *                          String account;
         *                          String passwd;
         *                          location location;
         *                          String name;
         *                  初始化 CreateUserData 的方式可以使用 gson 或是 自行new 並設定
         * @return UserProfileData
         */
        @Headers("Content-Type: application/json")
        @POST("/user/create")
        void createUser(@Body CreateUserData userdata, Callback<UserProfileData> callback);
        /**
         * update user info
         *
         * @param userdata  String  以下是必要資訊 而創建時可輸入其他而外的屬性，UpdateUserData
         *                  初始化 UpdateUserData 的方式可以使用 gson 或是 自行new 並設定
         * @return ResultData
         */
        @Headers("Content-Type: application/json")
        @POST("/user/save/info")
        void updateUserInfo(@Body UpdateUserData userdata, Callback<ResultData> callback);
        /**
         * send invite to user
         *
         * @param useridsend     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param useridreceive String  input user who you want to invited
         * @return ListStringData
         */
        @GET("/user/send/invite")
        void inviteUser(@Query("useridsend") String useridsend, @Query("sessionid") String sessionid,
                        @Query("useridreceive") String useridreceive, Callback<ResultData> callback);
        /**
         * add Friend
         *
         * @param useridsend     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param useridreceive String  input user who you want to make Friend
         * @return ListStringData
         */
        @GET("/user/add/friend")
        void addFriend(@Query("useridsend") String useridsend, @Query("sessionid") String sessionid,
                       @Query("useridreceive") String useridreceive, Callback<ResultData> callback);
        /**
         * add a user to BlackList
         *
         * @param useridsend     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param blackuser String  input user who you want to Black
         * @return ListStringData
         */
        @GET("/user/add/black_list")
        void addBlackList(@Query("useridsend") String useridsend, @Query("sessionid") String sessionid,
                          @Query("useridreceive") String blackuser, Callback<ResultData> callback);
        /**
         * add a user to trace list
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param trace_userid String  input user who you want to trace
         * @return ListStringData
         */
        @GET("/user/add/trace_user")
        void addTraceUser(@Query("userid") String userid, @Query("sessionid") String sessionid,
                          @Query("trace_userid") String trace_userid, Callback<ResultData> callback);
        /**
         * add a tripline to trace list
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param trace_triplineid String  input triple who you want to trace
         * @return ListStringData
         */
        @GET("/user/add/trace_tripline")
        void addTraceTripline(@Query("userid") String userid, @Query("sessionid") String sessionid,
                              @Query("trace_triplineid") String trace_triplineid, Callback<ResultData> callback);
        /**
         * delete a friend
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param friend String  input user who you want to del
         * @return ListStringData
         */
        @GET("/user/delete/friend")
        void deleteFriend(@Query("userid") String userid, @Query("sessionid") String sessionid,
                          @Query("friend") String friend, Callback<ResultData> callback);
        /**
         * delete a inviting     取消使用者的邀請中好友名單
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param inviting String  input user who you want to del
         * @return ListStringData
         */
        @GET("/user/delete/inviting")
        void deleteInviting(@Query("userid") String userid, @Query("sessionid") String sessionid,
                            @Query("inviting") String inviting, Callback<ResultData> callback);
        /**
         * delete a invited     取消來邀請使用者的名單
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param invited String  input user who you want to del
         * @return ListStringData
         */
        @GET("/user/delete/invited")
        void deleteInvited(@Query("userid") String userid, @Query("sessionid") String sessionid,
                           @Query("invited") String invited, Callback<ResultData> callback);
        /**
         * delete a black user
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param black_list String  input user who you want to del
         * @return ListStringData
         */
        @GET("/user/delete/black_list")
        void deleteBlackList(@Query("userid") String userid, @Query("sessionid") String sessionid,
                             @Query("black_list") String black_list, Callback<ResultData> callback);
        /**
         * delete a trace user
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param trace_user String  input user who you want to del
         * @return ListStringData
         */
        @GET("/user/delete/trace_user")
        void deleteTraceUser(@Query("userid") String userid, @Query("sessionid") String sessionid,
                             @Query("trace_user") String trace_user, Callback<ResultData> callback);
        /**
         * delete a trace tripline
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param trace_tripline String  input tripline who you want to del
         * @return ListStringData
         */
        @GET("/user/delete/trace_tripline")
        void deleteTraceTripLine(@Query("userid") String userid, @Query("sessionid") String sessionid,
                                 @Query("trace_tripline") String trace_tripline, Callback<ResultData> callback);
        /**
         * get world photos
         *
         * @param inputdata  請參考MainActiveity.java getWorldPhotos function
         *                   nlevel 可指定您要回傳的層數
         *                   windowX windowY  欲顯示的地圖 pixel 長 和 寬
         *                   picture_pixel 每一個圖片的pixel 大小,因是正方形故只有一個參數
         *                   GPS_left_up  GPS_right_down  欲顯示地圖的第一個level的左上和右下的gps位置
         *                                                這可以讓後端分析,是否重疊的一個很重要的參數
         *
         * @return AnalyzePhotoOutData
         */
        @Headers("Content-Type: application/json")
        @POST("/user/world/get/photos")
        void getWorldPhotos(@Body WorldPhotoInData inputdata, Callback<AnalyzePhotoOutData> callback);
    }
}
