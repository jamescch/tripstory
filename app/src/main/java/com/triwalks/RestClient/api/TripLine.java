package com.triwalks.RestClient.api;
import com.triwalks.RestClient.model.AnalyzePhotoInData;
import com.triwalks.RestClient.model.AnalyzePhotoOutData;
import com.triwalks.RestClient.model.AnalyzeTripInData;
import com.triwalks.RestClient.model.AnalyzeTripOutData;
import com.triwalks.RestClient.model.CreateTripLineData;
import com.triwalks.RestClient.model.ListStringData;
import com.triwalks.RestClient.model.PhotoData;
import com.triwalks.RestClient.model.ResultData;
import com.triwalks.RestClient.model.TripLineData;
import com.triwalks.RestClient.model.UpdateTripLinePathData;

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
public class TripLine {
    private static TripLineInterface TripLineService;

    public static TripLineInterface getTripApiClient() {
        if (TripLineService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://140.113.23.226:5001")
                    .build();

            TripLineService = restAdapter.create(TripLineInterface.class);
        }

        return TripLineService;
    }

    public interface TripLineInterface {
        /**
         * upload a photo
         * 上載某 trip_id裡的照片
         *
         * @param userid        TypedString
         * @param sessionid     TypedString
         * @param c_photoid     TypedString
         * @param triplineid    TypedString
         * @param photo         TypedFile   上載照片的檔案
         * @return    ResultData
         */
        @Multipart
        @POST("/user/trip_id/upload/photo")
        void uploadPhoto(@Part("photo") TypedFile photo, @Part("userid") TypedString userid,
                         @Part("sessionid") TypedString sessionid, @Part("c_photoid") TypedString c_photoid,
                         @Part("triplineid") TypedString triplineid,
                         Callback<ResultData> callback);
        /**
         * get a photo
         * 下載一張公開的照片
         *
         * @param userid        String   the userid is you want to get avator
         * @param sessionid     String
         * @param triplineid    String
         * @param s_photoid     String  Server  端的photoID
         * @param select Map<String,String> you can input big or small, and the value is "", don't need to input.
         *                                  big means 原圖
         *                                  small means 縮圖
         *                                  兩者一定要選一個
         * @return    Response  you can use ((TypedByteArray)Response.getBody()).getBytes(), it will retrun photo,
         *                      ref: https://medium.com/@giuder91/retrofit-how-to-download-get-a-file-e83a9badcf6c
         */
        @GET("/user/trip_id/get/photo")
        void downloadPhoto(@Query("userid") String userid, @Query("sessionid") String sessionid,
                           @Query("triplineid") String triplineid, @Query("s_photoid") String s_photoid,
                           @QueryMap Map<String, String> select,
                           Callback<Response> callback);

        /**
         * get IDs of self TripLine
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @return ListStringData       回傳本user所有的TripLineID
         *
         */
        @GET("/user/get/trip_ids")
        void getTripids(@Query("userid") String userid, @Query("sessionid") String sessionid, Callback<ListStringData> callback);
        /**
         * get IDs of other TripLine
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param qUserID    String     input you want to query user's userID
         * @return ListStringData       回傳本user所有的TripLineID
         *
         */
        @GET("/user/get/trip_ids")
        void getOtherTripids(@Query("userid") String userid, @Query("sessionid") String sessionid,
                             @Query("query_userid") String qUserID, Callback<ListStringData> callback);
        /**
         * get info of Specific TripLine
         *
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param triplineid String     you can use getTripids
         * @param select Map<String,String>
         *                      you just only need to set the key, and the value is "" , it means you want to its info.
         *                   if the Map is empty, it will return all info about the TripLine.
         * @return TripLineData
         *
         */
        @GET("/user/trip_id/get/info")
        void getTripInfo(@Query("userid") String userid, @Query("sessionid") String sessionid,
                         @Query("triplineid") String triplineid, @QueryMap Map<String, String> select
                , Callback<TripLineData> callback);
        /**
         * get photos of Specific TripLine
         *
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param triplineid String
         * @return ListStringData  如果查詢自己的,則回傳client端的photoID,代表著照片是存在local端
         *                         如果查詢得是公開的TripLine,則回傳資料庫中儲存的photoID,如此可用來下載server端的照片
         */
        @GET("/user/trip_id/get/photos")
        void getTripphotos(@Query("userid") String userid, @Query("sessionid") String sessionid,
                           @Query("triplineid") String triplineid, Callback<ListStringData> callback);
        /**
         * get photos of Specific TripLine
         *
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param triplineid String
         * @param queryParam Map<String,String>
         *                              s_photoid or c_photoid you need to select one to set up
         *                              s_photoid 表示server端的物件ID
         *                              c_photoid 表是client端的photoID
         *                              如果你想要全部的資訊那麼你要put("all","")
         *                              如果是指定的屬性可以自行加入,例如 put("time","")
         *                              後面的參數為"" 即可
         * @return PhotoData
         *
         */
        @GET("/user/trip_id/get/photo/description")
        void getTripPhotoInfo(@Query("userid") String userid, @Query("sessionid") String sessionid,
                              @Query("triplineid") String triplineid, @QueryMap Map<String, String> queryParam,
                              Callback<PhotoData> callback);
        /**
         * give a TripLine Like
         *
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param triplineid String
         * @return ResultData
         *
         */
        @GET("/user/trip_id/like")
        void likeTripLine(@Query("userid") String userid, @Query("sessionid") String sessionid,
                          @Query("triplineid") String triplineid, Callback<ResultData> callback);
        /**
         * give a Photo Like
         *
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param triplineid String
         * @param s_photoid  String     must be server photoID
         * @return ResultData
         *
         */
        @GET("/user/trip_id/photo/like")
        void likePhoto(@Query("userid") String userid, @Query("sessionid") String sessionid,
                       @Query("triplineid") String triplineid, @Query("s_photoid") String s_photoid,
                       Callback<ResultData> callback);

        /**
         * create a tripline
         *
         * @param tripdata  String  以下是必要資訊 而創建時可輸入其他而外的屬性，查看CreateUserData
         *                          String userid;
         *                          String sessionid;
         *                  初始化 CreateTripLineData 的方式可以使用 gson 或是 自行new 並設定
         * @return TripLineData
         *
         * */
        @Headers("Content-Type: application/json")
        @POST("/user/trip_id/create")
        void createTripLine(@Body CreateTripLineData tripdata, Callback<TripLineData> callback);

        /**
         * update path of tripline info
         * 只可以用來更新每個tripLine裡的photo資訊,如果要更新tripLine本身的資訊,請使用  updateTripInfo
         * 可以用來更新photo info  只能append 和 updata photo 但還沒加入delete
         *
         * @param pathdata  String  以下是必要資訊 而創建時可輸入其他而外的屬性，UpdateTripLinePathData
         *                          String userid;
         *                          String sessionid;
         *                          String triplineid;
         *                  初始化 UpdateTripLinePathData 的方式可以使用 gson 或是 自行new 並設定
         * @return UpdateTripLinePathData
         */
        @Headers("Content-Type: application/json")
        @POST("/user/trip_id/update/path")
        void UpdateTripLinePathData(@Body UpdateTripLinePathData pathdata, Callback<ResultData> callback);

        /**
         * Update trip info
         *
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param triplineid String     the ID is you want to update trip
         * @param tripinfo  Map<String,String> you can input info which you want to update
         *                  trip_name, ispublic=0/1, description favorite=0/1
         * @return ResultData
         *
         */
        @GET("/user/trip_id/save/info")
        void updateTripInfo(@Query("userid") String userid, @Query("sessionid") String sessionid,
                            @Query("triplineid") String triplineid, @QueryMap Map<String, String> tripinfo,
                            Callback<ResultData> callback);
        /**
         * delete a tripLine
         *
         *
         * @param userid     String     input your userid , you can get it after login
         * @param sessionid  String     you can get it after login
         * @param triplineid String     the ID is you want to update trip
         *
         * @return ResultData
         *
         */
        @GET("/user/trip_id/delete")
        void deleteTripLine(@Query("userid") String userid, @Query("sessionid") String sessionid,
                            @Query("triplineid") String triplineid,
                            Callback<ResultData> callback);
        /**
         * analyze a photolist
         *
         *
         * @param photodata     AnalyzePhotoInData     請參考MainActiveity.java analyzePhotoTree function
         *                   nlevel 可指定您要回傳的層數
         *                   windowX windowY  欲顯示的地圖 pixel 長 和 寬
         *                   picture_pixel 每一個圖片的pixel 大小,因是正方形故只有一個參數
         *                   GPS_left_up  GPS_right_down  欲顯示地圖的第一個level的左上和右下的gps位置
         *                                                這可以讓後端分析,是否重疊的一個很重要的參數
         *
         *
         * @return AnalyzePhotoOutData
         *
         */
        @Headers("Content-Type: application/json")
        @POST("/user/trip_id/analyze/photo_tree")
        void analyzePhotoTree(@Body AnalyzePhotoInData photodata, Callback<AnalyzePhotoOutData> callback);

        /**
         * Get trip with analyze Tree
         *
         *
         * @param input     AnalyzeTripInData     請參考MainActiveity.java analyzePhotoTree function
         *                   nlevel 可指定您要回傳的層數
         *                   windowX windowY  欲顯示的地圖 pixel 長 和 寬
         *                   picture_pixel 每一個圖片的pixel 大小,因是正方形故只有一個參數
         *                   GPS_left_up  GPS_right_down  欲顯示地圖的第一個level的左上和右下的gps位置
         *                                                這可以讓後端分析,是否重疊的一個很重要的參數
         *
         *
         * @return AnalyzeTripOutData
         *
         */
        @Headers("Content-Type: application/json")
        @POST("/user/trip_id/get/tree")
        void getTripTree(@Body AnalyzeTripInData input, Callback<AnalyzeTripOutData> callback);





    }
}
