package com.game.xogame.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.game.xogame.api.ApiService;
import com.game.xogame.entity.Game;
import com.game.xogame.entity.GamesCallback;
import com.game.xogame.entity.ProfileGamesCallback;
import com.game.xogame.entity.RegistrationCallback;
import com.game.xogame.entity.User;
import com.game.xogame.entity.UserCallback;

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

public class UserInfoModel {
    private ApiService api;
    private Context context;
    public User user;
    public List<Game> gameList;
    public List<Game> winGameList;

    public List<Game> profileNowGameList;
    public List<Game> profileFutureGameList;

    public UserInfoModel(ApiService a, Context c){
        api = a;
        context = c;
    }


    public void getInfo(UserInfoModel.GetInfoCallback callback) {
        UserInfoModel.GetInfoTask getInfoTask = new UserInfoModel.GetInfoTask(callback);
        getInfoTask.execute();
    }
    public interface GetInfoCallback {
        void onGet();
    }
    class GetInfoTask extends AsyncTask<ContentValues, Void, Void> {

        private final UserInfoModel.GetInfoCallback callback;

        GetInfoTask(UserInfoModel.GetInfoCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String token = sharedPref.getString("token", "null");

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<UserCallback> call = api.getUser(token);
                Log.i("LOG_getinfo" , token);
                call.enqueue(new Callback<UserCallback>() {
                    @Override
                    public void onResponse(Call<UserCallback> call, Response<UserCallback> response) {
                        if (response.isSuccessful()) {
                            //Log.i("LOG_photo" , "Success(code): " + response.body().getCode());

                            UserCallback userCallback = response.body();
                            if(userCallback.getStatus().equals("success")){
                                user = userCallback.getUser();
                                if (callback != null) {
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
                            Log.i("LOG_getinfo" , jObjError+" error");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserCallback> call, Throwable t) {
                        Log.i("LOG_getinfo" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_getinfo" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void editPhoto(ContentValues contentValues, UserInfoModel.EditPhotoCallback callback) {
        UserInfoModel.EditPhotoTask editPhotoTask = new UserInfoModel.EditPhotoTask(callback);
        editPhotoTask.execute(contentValues);
    }
    public interface EditPhotoCallback {
        void onEdit();
    }
    class EditPhotoTask extends AsyncTask<ContentValues, Void, Void> {

        private final UserInfoModel.EditPhotoCallback callback;

        EditPhotoTask(UserInfoModel.EditPhotoCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            String imagePath = params[0].getAsString("IMAGE");
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String id = sharedPref.getString("token", "null");

            Log.i("LOG_photo1" , id);
            File file = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<RegistrationCallback> call = api.editPhoto(id,photo);
                call.enqueue(new Callback<RegistrationCallback>() {
                    @Override
                    public void onResponse(Call<RegistrationCallback> call, Response<RegistrationCallback> response) {
                        if (response.isSuccessful()) {
                            //Log.i("LOG_photo" , "Success(code): " + response.body().getCode());
                            Log.i("LOG_photo1" , "Success(error): " + response.body().getError());
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.i("LOG_photo1" , jObjError+" error");

                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationCallback> call, Throwable t) {
                        Log.i("LOG_photo1" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_photo1" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void editInfo(ContentValues contentValues, UserInfoModel.EditInfoCallback callback) {
        UserInfoModel.EditInfoTask editInfoTask = new UserInfoModel.EditInfoTask(callback);
        editInfoTask.execute(contentValues);
    }
    public interface EditInfoCallback {
        void onEdit();
    }
    class EditInfoTask extends AsyncTask<ContentValues, Void, Void> {

        private final UserInfoModel.EditInfoCallback callback;

        EditInfoTask(UserInfoModel.EditInfoCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            String name = params[0].getAsString("NAME");
            //String info = params[0].getAsString("INFO");
            String gender = params[0].getAsString("GENDER");
            String age = params[0].getAsString("AGE");
            String email = params[0].getAsString("EMAIL");
            String country = params[0].getAsString("COUNTRY");
            String city = params[0].getAsString("CITY");
            String card = params[0].getAsString("CARD");
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String id = sharedPref.getString("token", "null");
            Log.i("LOG_edit" , id);
            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<RegistrationCallback> call = api.editInfo(id,name,email,gender,age,country,city,card);
                call.enqueue(new Callback<RegistrationCallback>() {
                    @Override
                    public void onResponse(Call<RegistrationCallback> call, Response<RegistrationCallback> response) {
                        if (response.isSuccessful()) {
                            //Log.i("LOG_photo" , "Success(code): " + response.body().getCode());
                            Log.i("LOG_edit" , "Success(error): " + response.body().getStatus());
                            Log.i("LOG_edit" , "Success(error): " + response.body().getError());
                            callback.onEdit();
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.i("LOG_edit" , jObjError+" error");

                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationCallback> call, Throwable t) {
                        Log.i("LOG_edit" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_edit" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void getMyGames(UserInfoModel.GetMyGamesCallback callback) {
        UserInfoModel.GetMyGamesTask getMyGamesTask = new UserInfoModel.GetMyGamesTask(callback);
        getMyGamesTask.execute();
    }
    public interface GetMyGamesCallback {
        void onGet();
    }
    class GetMyGamesTask extends AsyncTask<ContentValues, Void, Void> {

        private final UserInfoModel.GetMyGamesCallback callback;

        GetMyGamesTask(UserInfoModel.GetMyGamesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String id = sharedPref.getString("token", "null");

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<GamesCallback> call = api.getMyGames(id);
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


    public void getMyWins(UserInfoModel.GetMyWinsCallback callback) {
        UserInfoModel.GetMyWinsTask getMyWinsTask = new UserInfoModel.GetMyWinsTask(callback);
        getMyWinsTask.execute();
    }
    public interface GetMyWinsCallback {
        void onGet();
    }
    class GetMyWinsTask extends AsyncTask<ContentValues, Void, Void> {

        private final UserInfoModel.GetMyWinsCallback callback;

        GetMyWinsTask(UserInfoModel.GetMyWinsCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String id = sharedPref.getString("token", "null");

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<GamesCallback> call = api.getMyWins(id);
                call.enqueue(new Callback<GamesCallback>() {
                    @Override
                    public void onResponse(Call<GamesCallback> call, Response<GamesCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_gethistory" , "Success(error): " + response.body().getStatus());
                            if(response.body().getStatus().equals("success")){
                                winGameList = response.body().getGames();
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

    public void getProfileGames(UserInfoModel.GetProfileGamesCallback callback) {
        UserInfoModel.GetProfileGamesTask getProfileGamesTask = new UserInfoModel.GetProfileGamesTask(callback);
        getProfileGamesTask.execute();
    }
    public interface GetProfileGamesCallback {
        void onGet();
    }
    class GetProfileGamesTask extends AsyncTask<ContentValues, Void, Void> {

        private final UserInfoModel.GetProfileGamesCallback callback;

        GetProfileGamesTask(UserInfoModel.GetProfileGamesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String id = sharedPref.getString("token", "null");
            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<ProfileGamesCallback> call = api.getProfileGames(id);
                call.enqueue(new Callback<ProfileGamesCallback>() {
                    @Override
                    public void onResponse(Call<ProfileGamesCallback> call, Response<ProfileGamesCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_getprofilegames" , "Success(error): " + response.body().getStatus());
                            if(response.body().getStatus().equals("success")){
                                profileNowGameList = response.body().getGames().getNowGames();
                                profileFutureGameList = response.body().getGames().getNowGames();
                                callback.onGet();
                            }
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Log.i("LOG_getprofilegames" , jObjError+" error");

                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileGamesCallback> call, Throwable t) {
                        Log.i("LOG_getprofilegames" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_getprofilegames" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
