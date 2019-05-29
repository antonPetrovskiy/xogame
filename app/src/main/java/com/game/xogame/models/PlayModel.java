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
import com.game.xogame.entity.TaskCallback;
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

public class PlayModel {
    private ApiService api;
    private Context context;
    public String position;
    public String error;
    public String status;


    public PlayModel(ApiService a, Context c){
        api = a;
        context = c;
    }

    public void doTask(ContentValues contentValues,PlayModel.DoTaskCallback callback) {
        PlayModel.DoTask doTask = new PlayModel.DoTask(callback);
        doTask.execute(contentValues);
    }
    public interface DoTaskCallback {
        void onDo();
    }
    class DoTask extends AsyncTask<ContentValues, Void, Void> {

        private final PlayModel.DoTaskCallback callback;

        DoTask(PlayModel.DoTaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            String token = sharedPref.getString("token", "null");
            String imagePath = params[0].getAsString("IMAGE");
            String taskid = params[0].getAsString("TASKID");
            String comment = params[0].getAsString("COMMENT");
            String taskTime = params[0].getAsString("TASKTIME");
            File file = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

            if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                Call<TaskCallback> call = api.doTask(token,photo,taskid,comment,taskTime);
                Log.i("LOG_dotask" , " token - "+token);
                Log.i("LOG_dotask" , " taskid - "+taskid);
                Log.i("LOG_dotask" , " comment - "+comment);
                Log.i("LOG_dotask" , " imagePath - "+imagePath);
                Log.i("LOG_dotask" , " taskTime - "+taskTime);
                call.enqueue(new Callback<TaskCallback>() {
                    @Override
                    public void onResponse(Call<TaskCallback> call, Response<TaskCallback> response) {
                        if (response.isSuccessful()) {
                            Log.i("LOG_dotask" , "Success(code): " + response.body().getError());

                            if(response.body().getStatus().equals("success")){
                                if (callback != null) {
                                    Log.i("LOG_dotask" , "Success(code): " + response.body().getError());
                                    status = response.body().getStatus();
                                    position = response.body().getPosition();
                                    callback.onDo();
                                }
                            }else{
                                Log.i("LOG_dotask" , "Error(code): " + response.body().getError());
                                status = response.body().getStatus();
                                error = response.body().getError();
                                callback.onDo();
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
                            callback.onDo();
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskCallback> call, Throwable t) {
                        Log.i("LOG_dotask" , t.getMessage()+" fail");
                        status = "error";
                        error = t.getMessage();
                        callback.onDo();
                    }
                });

            } else {
                Log.i("LOG_dotask" , "error internet");
                status = "error";
                error = "error internet";
                callback.onDo();
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
}
