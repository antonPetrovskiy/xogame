package com.game.xogame.models;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.game.xogame.api.ApiService;
import com.game.xogame.entity.RegistrationCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LoginModel {

    private ApiService api;
    private Context context;
    private static String phone;
    private static String newUser = "no";
    private static String error = "";
    private static String status = "";
    private static String id;


    public LoginModel(ApiService a, Context c){
        api = a;
        context = c;
    }



    public void registratePhone(ContentValues contentValues, RegistratePhoneCallback callback) {
        RegistratePhoneTask registratePhoneTask = new RegistratePhoneTask(callback);
        registratePhoneTask.execute(contentValues);
    }
    public interface RegistratePhoneCallback {
        void onRegistrate();
    }
    class RegistratePhoneTask extends AsyncTask<ContentValues, Void, Void> {

        private final RegistratePhoneCallback callback;

        RegistratePhoneTask(RegistratePhoneCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            String number = params[0].getAsString("NUMBER");
            phone = number;

            Log.i("LOG_reg" , number);
            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<RegistrationCallback> call = api.registration(number);
                call.enqueue(new Callback<RegistrationCallback>() {
                    @Override
                    public void onResponse(Call<RegistrationCallback> call, Response<RegistrationCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_reg" , "Status: " + response.body().getStatus());
                            Log.i("LOG_reg" , "Error: " + response.body().getError());
                            if (callback != null && response.body().getStatus().equals("success")) {
                                callback.onRegistrate();
                            }

                        } else {
                            JSONObject jObjError = null;
                            try {
                                jObjError = new JSONObject(response.errorBody().string()+"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                Log.i("LOG_reg" , jObjError.getString("message")+" error");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationCallback> call, Throwable t) {
                        Log.i("LOG_reg" , t.getMessage()+" fail");
                    }
                });

            } else {

                Log.i("LOG" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void checkCode(ContentValues contentValues, CheckCodeCallback callback) {
        CheckCodeTask checkCodeTask = new CheckCodeTask(callback);
        checkCodeTask.execute(contentValues);
    }
    public interface CheckCodeCallback {
        void onCheck();
    }
    class CheckCodeTask extends AsyncTask<ContentValues, Void, Void> {

        private final CheckCodeCallback callback;

        CheckCodeTask(CheckCodeCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            String code = params[0].getAsString("CODE");
            final String number = params[0].getAsString("NUMBER");
            Log.i("LOG_checkCode" , code);
            Log.i("LOG_checkCode" , number);
            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<RegistrationCallback> call = api.checkCode(code,number);
                call.enqueue(new Callback<RegistrationCallback>() {
                    @Override
                    public void onResponse(Call<RegistrationCallback> call, Response<RegistrationCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_checkCode" , "New user: " + response.body().getNewUser());
                            Log.i("LOG_checkCode" , "Id: " + response.body().getId());
                            Log.i("LOG_checkCode" , "Error: " + response.body().getError());
                            status = response.body().getStatus();
                            if(response.body().getStatus().equals("success")){
                                newUser =  response.body().getNewUser();
                                id = response.body().getId();
                                if(newUser.equals("false")){
                                    SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
                                    sharedPref.edit().putString("token", id).commit();
                                    sharedPref.edit().putString("phone", number).commit();
                                    sharedPref.edit().putString("userid", response.body().getUserid()).commit();
                                    sharedPref.edit().putString("tutorial_guide", "true").commit();
                                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/auser" + response.body().getUserid())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                }
                                            });

                                }
                            }

                            if (callback != null) {
                                callback.onCheck();
                            }
                        } else {
                            JSONObject jObjError = null;
                            try {
                                jObjError = new JSONObject(response.errorBody().string());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                Log.i("LOG_checkCode" , jObjError.getString("message")+" error");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationCallback> call, Throwable t) {
                        Log.i("LOG_checkCode" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public void editInfo(ContentValues contentValues, EditInfoCallback callback) {
        EditInfoTask editInfoTask = new EditInfoTask(callback);
        editInfoTask.execute(contentValues);
    }
    public interface EditInfoCallback {
        void onEdit();
    }
    class EditInfoTask extends AsyncTask<ContentValues, Void, Void> {

        private final EditInfoCallback callback;

        EditInfoTask(EditInfoCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            MultipartBody.Part photo = null;
            String name = params[0].getAsString("NAME");
            String email = params[0].getAsString("EMAIL");
            String imagePath = params[0].getAsString("IMAGE");
            if(email.equals("")){
                email=" ";
            }
            if(imagePath.equals("")||imagePath.equals("null")) {
                imagePath = " ";
            }else{
                File file = new File(imagePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
            }
            Log.i("LOG_photo1" , name);
            Log.i("LOG_photo2" , email);
            Log.i("LOG_photo3" , imagePath);


            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<RegistrationCallback> call = api.setInfoRegister(name,id,email,photo);
                Log.i("LOG_photo" , id);
                call.enqueue(new Callback<RegistrationCallback>() {
                    @Override
                    public void onResponse(Call<RegistrationCallback> call, Response<RegistrationCallback> response) {
                        if (response.isSuccessful()) {
                            //Log.i("LOG_photo" , "Success(code): " + response.body().getCode());
                            Log.i("LOG_photo" , "Success(error): " + response.body().getError());
                            error = response.body().getError();
                            status = response.body().getStatus();
                            if (callback != null && response.body().getStatus().equals("success")) {
                                SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
                                sharedPref.edit().putString("token", id).commit();
                                sharedPref.edit().putString("userid", response.body().getUserid()).commit();
                                sharedPref.edit().putString("tutorial_games", "true").commit();
                                sharedPref.edit().putString("tutorial_game", "true").commit();
                                sharedPref.edit().putString("tutorial_feeds", "true").commit();
                                sharedPref.edit().putString("tutorial_money", "true").commit();
                                sharedPref.edit().putString("tutorial_profile", "true").commit();
                                sharedPref.edit().putString("tutorial_guide", "true").commit();
                                FirebaseMessaging.getInstance().subscribeToTopic("/topics/auser" + response.body().getUserid())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        });
                                FirebaseMessaging.getInstance().subscribeToTopic("/topics/anews")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                            }
                                        });
                                callback.onEdit();
                            }else{
                                callback.onEdit();
                            }
                        } else {
                            String jObjError = null;
                            try {
                                jObjError = response.errorBody().string()+"";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                                Log.i("LOG_photo" , jObjError+" error");

                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationCallback> call, Throwable t) {
                        Log.i("LOG_photo" , t.getMessage()+" fail");
                    }
                });

            } else {
                Log.i("LOG_photo" , "error internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    public String getStatus(){
        return status;
    }

    public String getError(){
        return error;
    }

    public String getNumber(){
        return phone;
    }

    public String getNewUser(){
        return newUser;
    }



}
