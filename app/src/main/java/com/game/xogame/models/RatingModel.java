package com.game.xogame.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.game.xogame.api.ApiService;
import com.game.xogame.entity.Rating;
import com.game.xogame.entity.RatingCallback;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class RatingModel {
    private ApiService api;
    private Context context;
    public List<Rating> ratingList;
    public Rating ratingGame;
    private String id;
    private String gameid;

    public RatingModel(ApiService a, Context c){
        api = a;
        context = c;
        SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
        id = sharedPref.getString("token", "null");
    }

    public void getRating(RatingModel.GetRatingCallback callback) {
        RatingModel.GetRatingTask getRatingTask = new RatingModel.GetRatingTask(callback);
        getRatingTask.execute();
    }
    public interface GetRatingCallback {
        void onGet();
    }
    class GetRatingTask extends AsyncTask<ContentValues, Void, Void> {

        private final RatingModel.GetRatingCallback callback;

        GetRatingTask(RatingModel.GetRatingCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<RatingCallback> call = api.getFullRating(id);
                call.enqueue(new Callback<RatingCallback>() {
                    @Override
                    public void onResponse(Call<RatingCallback> call, Response<RatingCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_rating" , "Success(error): " + response.body().getStatus());
                            if(response.body().getStatus().equals("success")){
                                ratingList = response.body().getRating();
                                callback.onGet();
                            }
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.i("LOG_rating" , jObjError+" error");

                        }
                    }

                    @Override
                    public void onFailure(Call<RatingCallback> call, Throwable t) {
                        Log.i("LOG_rating" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_rating" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public void getGameRating(String gameid, RatingModel.GetGameRatingCallback callback) {
        RatingModel.GetGameRatingTask getGameRatingTask = new RatingModel.GetGameRatingTask(callback);
        this.gameid = gameid;
        getGameRatingTask.execute();
    }
    public interface GetGameRatingCallback {
        void onGet();
    }
    class GetGameRatingTask extends AsyncTask<ContentValues, Void, Void> {

        private final RatingModel.GetGameRatingCallback callback;

        GetGameRatingTask(RatingModel.GetGameRatingCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<RatingCallback> call = api.getGameRating(id,gameid);
                call.enqueue(new Callback<RatingCallback>() {
                    @Override
                    public void onResponse(Call<RatingCallback> call, Response<RatingCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_rating" , "Success(error): " + response.body().getStatus());
                            Log.i("LOG_rating" , "Success(error): " + response.body().getRating().size());
                            Log.i("LOG_rating" , "Success(error): " + id);
                            Log.i("LOG_rating" , "Success(error): " + gameid);
                            if(response.body().getStatus().equals("success")){
                                if(response.body().getRating().size()!=0) {
                                    ratingGame = response.body().getRating().get(0);
                                    callback.onGet();
                                }else{
                                    //#TODO
                                    callback.onGet();
                                }
                            }
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.i("LOG_rating" , jObjError+" error");

                        }
                    }

                    @Override
                    public void onFailure(Call<RatingCallback> call, Throwable t) {
                        Log.i("LOG_rating" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_rating" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

}
