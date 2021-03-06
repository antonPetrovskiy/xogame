package com.game.xogame.api;

import com.game.xogame.entity.CreateCallback;
import com.game.xogame.entity.DefaultCallback;
import com.game.xogame.entity.FeedCallback;
import com.game.xogame.entity.GamesCallback;
import com.game.xogame.entity.GamesNewCallback;
import com.game.xogame.entity.ModerationCallback;
import com.game.xogame.entity.ProfileGamesCallback;
import com.game.xogame.entity.RatingCallback;
import com.game.xogame.entity.RegistrationCallback;
import com.game.xogame.entity.TaskCallback;
import com.game.xogame.entity.UserCallback;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiService {


    @FormUrlEncoded
    @POST("sms/")
    Call<RegistrationCallback> registration(@Field("number") String phone);


    @FormUrlEncoded
    @POST("verify/")
    Call<RegistrationCallback> checkCode(@Field("code") String code,
                                         @Field("number") String number);

    @FormUrlEncoded
    @POST("verifymail/")
    Call<DefaultCallback> verifyMail(@Field("token") String token,
                                         @Field("mail") String mail);

    @Multipart
    @POST("register/")
    Call<RegistrationCallback> setInfoRegister(@Part("nickname") String nickname,
                                        @Part("token") String token,
                                        @Part("mail") String mail,
                                        @Part MultipartBody.Part photo);


    @FormUrlEncoded
    @POST("profile/")
    Call<UserCallback> getUser(@Field("token") String token);

    @Multipart
    @POST("update/")
    Call<RegistrationCallback> editPhoto(@Part("token") String token,
                                         @Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("update/")
    Call<RegistrationCallback> editInfo(@Field("token") String token,
                                @Field("name") String name,
                                @Field("mail") String mail,
                                @Field("gender") String gender,
                                @Field("birthday") String age,
                                @Field("country") String country,
                                @Field("city") String city);


    @FormUrlEncoded
    @POST("game/")
    Call<GamesCallback> getFullGames(@Field("token") String token,
                                     @Field("lat") String lat,
                                     @Field("lon") String lon);

    @FormUrlEncoded
    @POST("game/money/")
    Call<DefaultCallback> getMoneyEmail(@Field("token") String token,
                                   @Field("gameid") String gameid,
                                   @Field("mail") String email);

    @FormUrlEncoded
    @POST("game/rate/")
    Call<RatingCallback> getFullRating(@Field("token") String token);

    @FormUrlEncoded
    @POST("game/rate/")
    Call<RatingCallback> getGameRating(@Field("token") String token,
                                       @Field("gameid") String gameid);


    @FormUrlEncoded
    @POST("game/lenta/")
    Call<FeedCallback> getFullFeeds(@Field("token") String token,
                                    @Field("flag") String flag,
                                    @Field("limit") String limit);

    @FormUrlEncoded
    @POST("game/lenta/")
    Call<FeedCallback> getFullFeeds(@Field("token") String token,
                                    @Field("gameid") String gameid);

    @FormUrlEncoded
    @POST("game/like/")
    Call<DefaultCallback> setLike(@Field("token") String token,
                                         @Field("actionid") String actionid);

    @FormUrlEncoded
    @POST("game/history/")
    Call<GamesCallback> getMyGames(@Field("token") String token);

    @FormUrlEncoded
    @POST("game/my/")
    Call<ProfileGamesCallback> getProfileGames(@Field("token") String token);

    @FormUrlEncoded
    @POST("game/ahistory/")
    Call<GamesCallback> getUserGames(@Field("token") String token,
                                     @Field("userid") String userid);

    @FormUrlEncoded
    @POST("game/amy/")
    Call<ProfileGamesCallback> getUserProfileGames(@Field("token") String token,
                                                   @Field("userid") String userid);


    @FormUrlEncoded
    @POST("game/rewards/")
    Call<GamesCallback> getMyWins(@Field("token") String token);

    @FormUrlEncoded
    @POST("game/follow/")
    Call<DefaultCallback> getSubscribe(@Field("token") String token,
                                       @Field("gameid") String gameid);

    @FormUrlEncoded
    @POST("game/follow/")
    Call<DefaultCallback> getUnsubscribe(@Field("token") String token,
                                     @Field("gameid") String gameid);

    @Multipart
    @POST("game/task/")
    Call<TaskCallback> doTask(@Part("token") String token,
                              @Part MultipartBody.Part photo,
                              @Part("taskid") String taskid,
                              @Part("comment") String comment,
                              @Part("taskTime") String tasktime);

    @Multipart
    @POST("game/moderate/")
    Call<ModerationCallback> getModerated(@Part("token") String token,
                                          @Part("gameid") String gameid);

    @Multipart
    @POST("game/complaint/")
    Call<DefaultCallback> setReport(@Part("token") String token,
                                          @Part("actionid") String actionid);

    @Multipart
    @POST("game/create/")
    Call<CreateCallback> createGame(@Part("token") String token,
                                    @Part("title") String title,
                                    @Part("description") String description,
                                    @Part MultipartBody.Part background,
                                    @Part("name[]") String[] name,
                                    @Part("lat") String lat,
                                    @Part("lon") String lon,
                                    @Part("address") String address,
                                    @Part("flevel") String flevel,
                                    @Part("category") String category,
                                    @Part("gameid") String gameid);

    @Multipart
    @POST("game/create/")
    Call<CreateCallback> createGame(@Part("token") String token,
                                    @Part("title") String title,
                                    @Part("description") String description,
                                    @Part MultipartBody.Part background,
                                    @Part("name[]") String[] name,
                                    @Part("lat") String lat,
                                    @Part("lon") String lon,
                                    @Part("address") String address,
                                    @Part("flevel") String flevel,
                                    @Part("category") String category);

    @Multipart
    @POST("game/create/")
    Call<CreateCallback> createGame(@Part("token") String token,
                                    @Part("title") String title,
                                    @Part("description") String description,
                                    @Part("name[]") String[] name,
                                    @Part("lat") String lat,
                                    @Part("lon") String lon,
                                    @Part("address") String address,
                                    @Part("flevel") String flevel,
                                    @Part("category") String category,
                                    @Part("gameid") String gameid);

    @FormUrlEncoded
    @POST("game/delete/")
    Call<DefaultCallback> deleteGame(@Field("token") String token,
                                           @Field("gameid") String gameid);

    @FormUrlEncoded
    @POST("game/mygames/")
    Call<GamesNewCallback> getCreatedGames(@Field("token") String token);

    @FormUrlEncoded
    @POST("game/dateandtime/")
    Call<DefaultCallback> setDateGame(@Field("token") String token,
                                       @Field("game_id") String gameid,
                                       @Field("game_start_date") String startdate,
                                       @Field("game_end_date") String enddate,
                                       @Field("send_push_from_time") String starttime,
                                       @Field("send_push_to_time") String endtime);
}