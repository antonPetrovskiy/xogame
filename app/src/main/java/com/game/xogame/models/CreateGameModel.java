package com.game.xogame.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.game.xogame.api.ApiService;
import com.game.xogame.entity.DefaultCallback;
import com.game.xogame.entity.Game;
import com.game.xogame.entity.GamesCallback;

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
    public String[] list;
    public List<Game> gameList;
    private ApiService api;
    private Context context;


    public CreateGameModel(ApiService a, Context c){
        api = a;
        context = c;
    }

    public void createGame(ContentValues contentValues, String[] list, CreateGameModel.CreateGameCallback callback) {
        this.list = list;
        CreateGameModel.CreateGame createGame = new CreateGameModel.CreateGame(callback);
        createGame.execute(contentValues);
    }

    public void getGames(CreateGameModel.GetGamesCallback callback) {
        CreateGameModel.GetGames getGames = new CreateGameModel.GetGames(callback);
        getGames.execute();
    }

    public String getStatus(){
        return status;
    }

    public String getError(){
        return error;
    }
    public interface CreateGameCallback {
        void onCreate();
    }
    public interface GetGamesCallback {
        void onGet(String status, String error);
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
            String limpeople = params[0].getAsString("LIMPEOPLE");
            String city = params[0].getAsString("CITY");
            String address = params[0].getAsString("ADDRESS");
            String flevel = params[0].getAsString("FLEVEL");
            File file = new File(background);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("background", file.getName(), requestFile);

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<DefaultCallback> call = api.createGame(token,title,description,photo,list,limpeople,city,address,flevel);
                Log.i("LOG_dotask" , " token - "+token);
                call.enqueue(new Callback<DefaultCallback>() {
                    @Override
                    public void onResponse(Call<DefaultCallback> call, Response<DefaultCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_dotask" , "Success(code): " + response.body().getError());

                            if(response.body().getStatus().equals("success")){
                                if (callback != null) {
                                    Log.i("LOG_dotask" , "Success(code): " + response.body().getError());
                                    status = response.body().getStatus();
                                    callback.onCreate();
                                }
                            }else{
                                Log.i("LOG_dotask" , "Error(code): " + response.body().getError());
                                status = response.body().getStatus();
                                error = response.body().getError();
                                callback.onCreate();
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
                            callback.onCreate();
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultCallback> call, Throwable t) {
                        Log.i("LOG_dotask" , t.getMessage()+" fail");
                        status = "error";
                        error = t.getMessage();
                        callback.onCreate();
                    }
                });

            } else {
                Log.i("LOG_dotask" , "error internet");
                status = "error";
                error = "error internet";
                callback.onCreate();
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
                Call<GamesCallback> call = api.getCreatedGames(token);
                Log.i("LOG_dotask" , " token - "+token);
                call.enqueue(new Callback<GamesCallback>() {
                    @Override
                    public void onResponse(Call<GamesCallback> call, Response<GamesCallback> response) {
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
                    public void onFailure(Call<GamesCallback> call, Throwable t) {
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
}
