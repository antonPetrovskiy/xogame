package com.game.xogame.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.game.xogame.api.ApiService;
import com.game.xogame.entity.DefaultCallback;
import com.game.xogame.entity.Feed;
import com.game.xogame.entity.FeedCallback;
import com.game.xogame.entity.Game;
import com.game.xogame.entity.GamesCallback;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class GamesModel {
    private ApiService api;
    private Context context;
    private String id;

    public List<Game> gameList;
    public List<Feed> feedList;

    public GamesModel(ApiService a, Context c){
        api = a;
        context = c;
        SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
        id = sharedPref.getString("token", "null");
    }


    public void getGames(GamesModel.GetGamesCallback callback) {
        GamesModel.GetGamesTask getGamesTask = new GamesModel.GetGamesTask(callback);
        getGamesTask.execute();
    }
    public interface GetGamesCallback {
        void onGet();
    }
    class GetGamesTask extends AsyncTask<ContentValues, Void, Void> {

        private final GamesModel.GetGamesCallback callback;

        GetGamesTask(GamesModel.GetGamesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<GamesCallback> call = api.getFullGames(id);
                call.enqueue(new Callback<GamesCallback>() {
                    @Override
                    public void onResponse(Call<GamesCallback> call, Response<GamesCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_gethistory" , "Success(error): " + response.body().getStatus());
                            if(response.body().getStatus().equals("success")){
                                gameList = response.body().getGames();
                                callback.onGet();
                            }
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.i("LOG_gethistory" , jObjError+" error");

                        }
                    }

                    @Override
                    public void onFailure(Call<GamesCallback> call, Throwable t) {
                        Log.i("LOG_gethistory" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_gethistory" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void subscribeGame(ContentValues contentValues, GamesModel.SubscribeGameCallback callback) {
        GamesModel.SubscribeGameTask subscribeGameTask = new GamesModel.SubscribeGameTask(callback);
        subscribeGameTask.execute(contentValues);
    }
    public interface SubscribeGameCallback {
        void onSubscribe();
    }
    class SubscribeGameTask extends AsyncTask<ContentValues, Void, Void> {

        private final GamesModel.SubscribeGameCallback callback;

        SubscribeGameTask(GamesModel.SubscribeGameCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            String gameId = params[0].getAsString("GAMEID");
            Log.i("LOG_subscribe" , id+gameId+" wtf");
            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<DefaultCallback> call = api.getSubscribe(id,gameId);
                call.enqueue(new Callback<DefaultCallback>() {
                    @Override
                    public void onResponse(Call<DefaultCallback> call, Response<DefaultCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_subscribe" , "Success(error): " + response.body().getError());
                            if(response.body().getStatus().equals("success")){
                                callback.onSubscribe();
                            }
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultCallback> call, Throwable t) {
                        Log.i("LOG_subscribe" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_subscribe" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void unsubscribeGame(ContentValues contentValues, GamesModel.UnsubscribeGameCallback callback) {
        GamesModel.UnsubscribeGameTask unsubscribeGameTask = new GamesModel.UnsubscribeGameTask(callback);
        unsubscribeGameTask.execute(contentValues);
    }
    public interface UnsubscribeGameCallback {
        void onUnsubscribe();
    }
    class UnsubscribeGameTask extends AsyncTask<ContentValues, Void, Void> {

        private final GamesModel.UnsubscribeGameCallback callback;

        UnsubscribeGameTask(GamesModel.UnsubscribeGameCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            String gameId = params[0].getAsString("GAMEID");
            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<DefaultCallback> call = api.getUnsubscribe(id,gameId);
                call.enqueue(new Callback<DefaultCallback>() {
                    @Override
                    public void onResponse(Call<DefaultCallback> call, Response<DefaultCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_subscribe" , "Success(error): " + response.body().getStatus());
                            if(response.body().getStatus().equals("success")){
                                callback.onUnsubscribe();
                            }
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultCallback> call, Throwable t) {
                        Log.i("LOG_subscribe" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_subscribe" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void getFeeds(GamesModel.GetFeedsCallback callback) {
        GamesModel.GetFeedsTask getFeedsTask = new GamesModel.GetFeedsTask(callback);
        getFeedsTask.execute();
    }
    public interface GetFeedsCallback {
        void onGet();
    }
    class GetFeedsTask extends AsyncTask<ContentValues, Void, Void> {

        private final GamesModel.GetFeedsCallback callback;

        GetFeedsTask(GamesModel.GetFeedsCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<FeedCallback> call = api.getFullFeeds(id);
                call.enqueue(new Callback<FeedCallback>() {
                    @Override
                    public void onResponse(Call<FeedCallback> call, Response<FeedCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_getfeeds" , "Success(error): " + response.body().getStatus());
                            if(response.body().getStatus().equals("success")){
                                feedList = response.body().getFeeds();
                                callback.onGet();
                            }
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.i("LOG_getfeeds" , jObjError+" error");

                        }
                    }

                    @Override
                    public void onFailure(Call<FeedCallback> call, Throwable t) {
                        Log.i("LOG_getfeeds" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_getfeeds" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


}

