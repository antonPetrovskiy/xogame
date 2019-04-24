package com.game.xogame.models;

import android.content.Context;

import com.game.xogame.api.ApiService;
import com.game.xogame.entity.Rating;

import java.util.List;

public class RatingModel {
    private ApiService api;
    private Context context;
    public List<Rating> ratingList;

    public RatingModel(ApiService a, Context c){
        api = a;
        context = c;
    }
}
