package com.game.xogame.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.game.xogame.api.ApiService;
import com.game.xogame.entity.CreateCallback;
import com.game.xogame.entity.DefaultCallback;
import com.game.xogame.entity.Feed;
import com.game.xogame.entity.FeedCallback;
import com.game.xogame.entity.GameNew;
import com.game.xogame.entity.GamesNewCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CreateGameModel {
    public String error;
    public String status;
    public String gameid;
    public String url;
    public String[] list;
    public List<GameNew> gameList;
    private ApiService api;
    private Context context;
    public List<Feed> feedList;

    public CreateGameModel(ApiService a, Context c){
        api = a;
        context = c;
    }



    public void createGame(ContentValues contentValues, String[] list, CreateGameModel.CreateGameCallback callback) {
        this.list = list;
        CreateGameModel.CreateGame createGame = new CreateGameModel.CreateGame(callback);
        createGame.execute(contentValues);
    }
    public void deleteGame(ContentValues contentValues, CreateGameModel.DeleteGameCallback callback) {
        CreateGameModel.DeleteGame deleteGame = new CreateGameModel.DeleteGame(callback);
        deleteGame.execute(contentValues);
    }
    public void getGames(CreateGameModel.GetGamesCallback callback) {
        CreateGameModel.GetGames getGames = new CreateGameModel.GetGames(callback);
        getGames.execute();
    }
    public void setDateGame(ContentValues contentValues, CreateGameModel.SetDateGameCallback callback) {
        CreateGameModel.SetDateGame setDateGame = new CreateGameModel.SetDateGame(callback);
        setDateGame.execute(contentValues);
    }


    public interface GetGamesCallback {
        void onGet(String status, String error);
    }
    public interface DeleteGameCallback {
        void onDelete(String status, String error);
    }

    public String getStatus(){
        return status;
    }

    public String getError(){
        return error;
    }

    public interface CreateGameCallback {
        void onCreate(String status, String error, String url);
    }
    class DeleteGame extends AsyncTask<ContentValues, Void, Void> {

        private final CreateGameModel.DeleteGameCallback callback;

        DeleteGame(CreateGameModel.DeleteGameCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String token = sharedPref.getString("token", "null");
            String gameid = params[0].getAsString("GAMEID");

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<DefaultCallback> call = api.deleteGame(token,gameid);
                Log.i("LOG_delete" , " token - "+token);
                call.enqueue(new Callback<DefaultCallback>() {
                    @Override
                    public void onResponse(Call<DefaultCallback> call, Response<DefaultCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_delete" , "Success(code): " + response.body().getError());

                            if(response.body().getStatus().equals("success")){
                                if (callback != null) {
                                    Log.i("LOG_delete" , "Success(code): " + response.body().getError());
                                    status = response.body().getStatus();
                                    callback.onDelete(status,error);
                                }
                            }else{
                                Log.i("LOG_delete" , "Error(code): " + response.body().getError());
                                status = response.body().getStatus();
                                error = response.body().getError();
                                callback.onDelete(status,error);
                            }

                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.i("LOG_delete" , jObjError+" error");
                            status = "error";
                            error = jObjError;
                            callback.onDelete(status,error);
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultCallback> call, Throwable t) {
                        Log.i("LOG_delete" , t.getMessage()+" fail");
                        status = "error";
                        error = t.getMessage();
                        callback.onDelete(status,error);
                    }
                });

            } else {
                Log.i("LOG_delete" , "error internet");
                status = "error";
                error = "error internet";
                callback.onDelete(status,error);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }
    public interface SetDateGameCallback {
        void onSet(String status, String error);
    }

    class CreateGame extends AsyncTask<ContentValues, Void, Void> {

        private final CreateGameModel.CreateGameCallback callback;

        CreateGame(CreateGameModel.CreateGameCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String token = sharedPref.getString("token", "null");
            String title = params[0].getAsString("TITLE");
            String background = params[0].getAsString("BACKGROUND");
            String description = params[0].getAsString("DESCRIPTION");
            String lat = params[0].getAsString("LAT");
            String lon = params[0].getAsString("LON");
            String address = params[0].getAsString("ADDRESS");
            String flevel = params[0].getAsString("FLEVEL");
            String category = params[0].getAsString("CATEGORY");
            String gameid = params[0].getAsString("GAMEID")+"";
            Log.i("LOG_create" , " category - "+category);
            Log.i("LOG_create" , " address - "+address);
            Log.i("LOG_create" , " lat - "+lat);
            Log.i("LOG_create" , " lon - "+lon);
            Log.i("LOG_create" , " flevel - "+flevel);
            Log.i("LOG_create" , " id - "+gameid);
            Log.i("LOG_create" , " bg - "+background);
            Log.i("LOG_create" , " list - "+list.length);
            MultipartBody.Part photo;
            if(background!=null) {
                File file = new File(background);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                photo = MultipartBody.Part.createFormData("background", file.getName(), requestFile);
            }else{
                photo=null;
            }

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<CreateCallback> call;
                if(!gameid.equals("")&&!gameid.equals("null")){
                    if(background!=null && background.length()>6 && background.charAt(0)=='h' && background.charAt(5)==':'){
                        call = api.createGame(token,title,description,list,lat,lon,address,flevel,category,gameid);
                    }else{
                        call = api.createGame(token,title,description,photo,list,lat,lon,address,flevel,category,gameid);
                    }
                }else{
                    call = api.createGame(token,title,description,photo,list,lat,lon,address,flevel,category);
                }

                Log.i("LOG_create" , " token - "+token);
                call.enqueue(new Callback<CreateCallback>() {
                    @Override
                    public void onResponse(Call<CreateCallback> call, Response<CreateCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_create" , "Success(code): " + response.body().getError());

                            if(response.body().getStatus().equals("success")){
                                if (callback != null) {
                                    Log.i("LOG_create" , "Success(code): " + response.body().getError());
                                    status = response.body().getStatus();
                                    url = response.body().getUrl();
                                    callback.onCreate(status,error,url);
                                }
                            }else{
                                Log.i("LOG_create" , "Error(code): " + response.body().getError());
                                status = response.body().getStatus();
                                error = response.body().getError();
                                callback.onCreate(status,error,url);
                            }

                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.i("LOG_create" , jObjError+" error");
                            status = "error";
                            error = jObjError;
                            callback.onCreate(status,error,url);
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateCallback> call, Throwable t) {
                        Log.i("LOG_create" , t.getMessage()+" fail");
                        status = "error";
                        error = t.getMessage();
                        callback.onCreate(status,error,url);
                    }
                });

            } else {
                Log.i("LOG_create" , "error internet");
                status = "error";
                error = "error internet";
                callback.onCreate(status,error,url);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }

    class GetGames extends AsyncTask<ContentValues, Void, Void> {

        private final CreateGameModel.GetGamesCallback callback;

        GetGames(CreateGameModel.GetGamesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String token = sharedPref.getString("token", "null");

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<GamesNewCallback> call = api.getCreatedGames(token);
                Log.i("LOG_dotask" , " token - "+token);
                call.enqueue(new Callback<GamesNewCallback>() {
                    @Override
                    public void onResponse(Call<GamesNewCallback> call, Response<GamesNewCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_dotask" , "Success(code): " + response.body().getError());

                            if(response.body().getStatus().equals("success")){
                                if (callback != null) {
                                    Log.i("LOG_dotask" , "Success(code): " + response.body().getError());
                                    status = response.body().getStatus();
                                    gameList = response.body().getGames();
                                    callback.onGet(response.body().getStatus()+"",response.body().getError()+"");
                                }
                            }else{
                                Log.i("LOG_dotask" , "Error(code): " + response.body().getError());
                                status = response.body().getStatus();
                                error = response.body().getError();
                                callback.onGet(response.body().getStatus()+"",response.body().getError()+"");
                            }

                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.i("LOG_dotask" , jObjError+" error");
                            status = "error";
                            error = jObjError;
                            callback.onGet(response.body().getStatus()+"",response.body().getError()+"");
                        }
                    }

                    @Override
                    public void onFailure(Call<GamesNewCallback> call, Throwable t) {
                        Log.i("LOG_dotask" , t.getMessage()+" fail");
                        status = "error";
                        error = t.getMessage();
                        callback.onGet(status+"",error+"");
                    }
                });

            } else {
                Log.i("LOG_dotask" , "error internet");
                status = "error";
                error = "error internet";
                callback.onGet(status, error);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }

    class SetDateGame extends AsyncTask<ContentValues, Void, Void> {

        private final CreateGameModel.SetDateGameCallback callback;

        SetDateGame(CreateGameModel.SetDateGameCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String token = sharedPref.getString("token", "null");
            String startdate = params[0].getAsString("STARTDATE");
            String enddate = params[0].getAsString("ENDDATE");
            String starttime = params[0].getAsString("STARTTIME");
            String endtime = params[0].getAsString("ENDTIME");
            String gameid = params[0].getAsString("GAMEID")+"";
            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<DefaultCallback> call = api.setDateGame(token,gameid,startdate,enddate,starttime,endtime);
                call.enqueue(new Callback<DefaultCallback>() {
                    @Override
                    public void onResponse(Call<DefaultCallback> call, Response<DefaultCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_dotask" , "Success(code): " + response.body().getError());
                            if(response.body().getStatus().equals("success")){
                                if (callback != null) {
                                    Log.i("LOG_dotask" , "Success(code): " + response.body().getError());
                                    status = response.body().getStatus();
                                    callback.onSet(response.body().getStatus()+"",response.body().getError()+"");
                                }
                            }else{
                                Log.i("LOG_dotask" , "Error(code): " + response.body().getError());
                                status = response.body().getStatus();
                                error = response.body().getError();
                                callback.onSet(response.body().getStatus()+"",response.body().getError()+"");
                            }

                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.i("LOG_dotask" , jObjError+" error");
                            status = "error";
                            error = jObjError;
                            callback.onSet(response.body().getStatus()+"",response.body().getError()+"");
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultCallback> call, Throwable t) {
                        Log.i("LOG_dotask" , t.getMessage()+" fail");
                        status = "error";
                        error = t.getMessage();
                        callback.onSet(status+"",error+"");
                    }
                });

            } else {
                Log.i("LOG_dotask" , "error internet");
                status = "error";
                error = "error internet";
                callback.onSet(status, error);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

    }






    public void getFeeds(String gameid,GamesModel.GetFeedsCallback callback) {
        CreateGameModel.GetFeedsTask getFeedsTask = new CreateGameModel.GetFeedsTask(callback);
        getFeedsTask.execute();
        this.gameid = gameid;
    }
    public interface GetFeedsCallback {
        void onGet(String status, String error);
    }
    class GetFeedsTask extends AsyncTask<ContentValues, Void, Void> {

        private final GamesModel.GetFeedsCallback callback;

        GetFeedsTask(GamesModel.GetFeedsCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String id = sharedPref.getString("token", "null");
            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<FeedCallback> call = api.getFullFeeds(id,gameid);
                call.enqueue(new Callback<FeedCallback>() {
                    @Override
                    public void onResponse(Call<FeedCallback> call, Response<FeedCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_getfeeds" , "Success(error): " + response.body().getStatus());
                            feedList = response.body().getFeeds();
                            callback.onGet(response.body().getStatus()+"",response.body().getError()+"");

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
