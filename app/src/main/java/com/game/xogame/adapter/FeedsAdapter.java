package com.game.xogame.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.game.xogame.R;
import com.game.xogame.api.ApiService;
import com.game.xogame.api.RetroClient;
import com.game.xogame.entity.DefaultCallback;
import com.game.xogame.entity.Feed;
import com.game.xogame.views.game.ReportActivity;
import com.game.xogame.views.profile.UserProfileActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FeedsAdapter extends ArrayAdapter<Feed> {
    private static List<Feed> feedList;
    private Context context;
    private LayoutInflater mInflater;


    // Constructors
    public FeedsAdapter(Context context, List<Feed> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        feedList = objects;

    }

    @Override
    public Feed getItem(int position) {
        return feedList.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FeedsAdapter.ViewHolder vh;
        final Feed item = getItem(position);
        if (convertView == null) {
            View view;
            view = mInflater.inflate(R.layout.layout_feed, parent, false);
            vh = FeedsAdapter.ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (FeedsAdapter.ViewHolder) convertView.getTag();
        }

        vh.rootView.setScrollContainer(false);
        vh.rootView.setNestedScrollingEnabled(false);
        vh.rootView.setClickable(false);
        vh.rootView.setClipToOutline(true);

        assert item != null;
        if (item.getType().equals("post")) {
            vh.layPost.setVisibility(View.VISIBLE);
            vh.layRate.setVisibility(View.GONE);
            vh.gameLayout.setVisibility(View.GONE);
            initPost(vh, item);
        } else {
            vh.layPost.setVisibility(View.GONE);
            vh.layRate.setVisibility(View.VISIBLE);
            vh.gameLayout.setVisibility(View.VISIBLE);
            initRate(vh, item);
        }

        return vh.rootView;
    }

    private void actions(final FeedsAdapter.ViewHolder vh, final Feed item) {
        vh.imageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("NAME", item.getUserName());
                intent.putExtra("NICKNAME", item.getUserNickname());
                intent.putExtra("PHOTO", item.getUserPhotoUrl());
                intent.putExtra("USERID", item.getUserId());
                context.startActivity(intent);
            }
        });
        vh.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("NAME", item.getUserName());
                intent.putExtra("NICKNAME", item.getUserNickname());
                intent.putExtra("PHOTO", item.getUserPhotoUrl());
                intent.putExtra("USERID", item.getUserId());
                context.startActivity(intent);
            }
        });
        vh.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(v.getRootView().getContext());
                @SuppressLint("InflateParams") View promptView = layoutInflater.inflate(R.layout.popup_settings, null);
                final android.support.v7.app.AlertDialog alertD = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext()).create();
                final TextView btn1 = promptView.findViewById(R.id.textView1);
                final TextView btn2 = promptView.findViewById(R.id.textView2);
                final TextView btn3 = promptView.findViewById(R.id.textView3);
                final TextView btn4 = promptView.findViewById(R.id.textView4);
                final TextView btn5 = promptView.findViewById(R.id.textView5);


                btn1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //to get the image from the ImageView (say iv)
                        BitmapDrawable draw = (BitmapDrawable) vh.imageViewPhoto.getDrawable();
                        Bitmap bitmap = draw.getBitmap();

                        FileOutputStream outStream = null;
                        File sdCard = Environment.getExternalStorageDirectory();
                        File dir = new File(sdCard.getAbsolutePath() + "/paparazzi");
                        dir.mkdirs();
                        @SuppressLint("DefaultLocale") String fileName = String.format("%d.jpg", System.currentTimeMillis());
                        File outFile = new File(dir, fileName);
                        try {
                            outStream = new FileOutputStream(outFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        try {
                            assert outStream != null;
                            outStream.flush();
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(v.getRootView().getContext(), context.getString(R.string.adapterFeeds_saved),
                                Toast.LENGTH_SHORT).show();
                        alertD.cancel();
                    }
                });

                btn2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (btn3.getVisibility() == View.GONE) {
                            btn3.setVisibility(View.VISIBLE);
                            btn4.setVisibility(View.VISIBLE);
                            btn5.setVisibility(View.VISIBLE);
                        } else {
                            btn3.setVisibility(View.GONE);
                            btn4.setVisibility(View.GONE);
                            btn5.setVisibility(View.GONE);
                        }
                    }
                });

                btn3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(v.getRootView().getContext(), context.getString(R.string.adapterFeeds_reported),
                                Toast.LENGTH_SHORT).show();
                        alertD.cancel();
                    }
                });

                btn4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(v.getRootView().getContext(), context.getString(R.string.adapterFeeds_reported),
                                Toast.LENGTH_SHORT).show();
                        alertD.cancel();
                    }
                });

                btn5.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ReportActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        alertD.cancel();
                    }
                });

                alertD.setView(promptView);
                alertD.show();


            }
        });
    }

    private void initRate(final FeedsAdapter.ViewHolder vh, final Feed item) {
        vh.rootView.setScrollContainer(false);
        vh.rootView.setNestedScrollingEnabled(false);
        vh.rootView.setClickable(false);

        vh.rootView.setClipToOutline(true);
        assert item != null;
        vh.textViewCompany1.setText(item.getCompany());
        vh.textViewTitle1.setText(item.getTitle());
        switch (item.getStatus()) {
            case "Active":
                vh.textViewTimeLeft1.setText(context.getString(R.string.txt_active));
                break;
            case "Completed":
                vh.textViewTimeLeft1.setText(context.getString(R.string.adapterRating_ended));
                break;
            case "Moderation":
                vh.textViewTimeLeft1.setText(context.getString(R.string.adapterRating_moderation));
                break;
        }

        vh.textViewPeople1.setText(item.getFollowers());
        Picasso.with(context).load(item.getLogo() + "").placeholder(getPlaceholder(item.getCompany())).error(getPlaceholder(item.getCompany())).into(vh.imageViewCompany1);
//        Picasso.with(context).load(item.getUserPhotoUrl()+"").placeholder(android.R.color.holo_red_dark).error(android.R.color.holo_red_dark).into(vh.imageViewUser);
//        Picasso.with(context).load(item.getTaskPhotoUrl()+"").placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageViewPhoto);


        if (item.getTop().size() > 0) {
            vh.top1Layout.setVisibility(View.VISIBLE);
            if (item.getTop().get(0).getName().length() != 0) {
                vh.placeholder1.setText(item.getTop().get(0).getName().substring(0, 1).toUpperCase());
            } else {
                vh.placeholder1.setText("А");
            }
            vh.photo1.setImageResource(getPlaceholder(item.getTop().get(0).getNickname()));
            Picasso.with(context).load(item.getTop().get(0).getPhoto() + "").placeholder(getPlaceholder(item.getTop().get(0).getNickname())).error(getPlaceholder(item.getTop().get(0).getNickname())).into(vh.photo1, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    vh.placeholder1.setText("");
                }

                @Override
                public void onError() {

                }
            });
            vh.name1.setText(item.getTop().get(0).getNickname());
            vh.task1.setText(item.getTop().get(0).getComplited() + "/" + item.getTasks());
            int n = 1000 / Integer.parseInt(item.getTasks());
            n = n * Integer.parseInt(item.getTop().get(0).getComplited());
            vh.bar1.setProgress(n);
            vh.photo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", item.getTop().get(0).getName());
                    intent.putExtra("NICKNAME", item.getTop().get(0).getNickname());
                    intent.putExtra("PHOTO", item.getTop().get(0).getPhoto());
                    intent.putExtra("USERID", item.getTop().get(0).getUserid());
                    context.startActivity(intent);
                }
            });
            vh.name1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", item.getTop().get(0).getName());
                    intent.putExtra("NICKNAME", item.getTop().get(0).getNickname());
                    intent.putExtra("PHOTO", item.getTop().get(0).getPhoto());
                    intent.putExtra("USERID", item.getTop().get(0).getUserid());
                    context.startActivity(intent);
                }
            });
        } else {
            vh.top1Layout.setVisibility(View.GONE);
        }

        if (item.getTop().size() > 1) {
            vh.top2Layout.setVisibility(View.VISIBLE);
            if (item.getTop().get(1).getName().length() != 0) {
                vh.placeholder2.setText(item.getTop().get(1).getName().substring(0, 1));
            } else {
                vh.placeholder2.setText("А");
            }
            vh.photo2.setImageResource(getPlaceholder(item.getTop().get(1).getNickname()));
            Picasso.with(context).load(item.getTop().get(1).getPhoto() + "").placeholder(getPlaceholder(item.getTop().get(1).getNickname())).error(getPlaceholder(item.getTop().get(1).getNickname())).into(vh.photo2, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    vh.placeholder2.setText("");
                }

                @Override
                public void onError() {

                }
            });
            vh.name2.setText(item.getTop().get(1).getName());
            vh.nickname2.setText(item.getTop().get(1).getNickname());
            vh.place2.setText(item.getTop().get(1).getPosition() + " " + context.getString(R.string.adapterRating_place));
            vh.task2.setText(item.getTop().get(1).getComplited() + "/" + item.getTasks());
            int n = 1000 / Integer.parseInt(item.getTasks());
            n = n * Integer.parseInt(item.getTop().get(1).getComplited());
            vh.bar2.setProgress(n);
            vh.photo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", item.getTop().get(1).getName());
                    intent.putExtra("NICKNAME", item.getTop().get(1).getNickname());
                    intent.putExtra("PHOTO", item.getTop().get(1).getPhoto());
                    intent.putExtra("USERID", item.getTop().get(1).getUserid());
                    context.startActivity(intent);
                }
            });
            vh.name2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", item.getTop().get(1).getName());
                    intent.putExtra("NICKNAME", item.getTop().get(1).getNickname());
                    intent.putExtra("PHOTO", item.getTop().get(1).getPhoto());
                    intent.putExtra("USERID", item.getTop().get(1).getUserid());
                    context.startActivity(intent);
                }
            });
        } else {
            vh.top2Layout.setVisibility(View.GONE);
        }

        if (item.getTop().size() > 2) {
            vh.top3Layout.setVisibility(View.VISIBLE);
            if (item.getTop().get(2).getName().length() != 0) {
                vh.placeholder3.setText(item.getTop().get(2).getName().substring(0, 1).toUpperCase());
            } else {
                vh.placeholder3.setText("А");
            }
            vh.photo3.setImageResource(getPlaceholder(item.getTop().get(2).getNickname()));
            Picasso.with(context).load(item.getTop().get(2).getPhoto() + "").placeholder(getPlaceholder(item.getTop().get(2).getNickname())).error(getPlaceholder(item.getTop().get(2).getNickname())).into(vh.photo3, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    vh.placeholder3.setText("");
                }

                @Override
                public void onError() {

                }
            });
            vh.name3.setText(item.getTop().get(2).getName());
            vh.nickname3.setText(item.getTop().get(2).getNickname());
            vh.place3.setText(item.getTop().get(2).getPosition() + " " + context.getString(R.string.adapterRating_place));
            vh.task3.setText(item.getTop().get(2).getComplited() + "/" + item.getTasks());
            int n = 1000 / Integer.parseInt(item.getTasks());
            n = n * Integer.parseInt(item.getTop().get(2).getComplited());
            vh.bar3.setProgress(n);
            vh.photo3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", item.getTop().get(2).getName());
                    intent.putExtra("NICKNAME", item.getTop().get(2).getNickname());
                    intent.putExtra("PHOTO", item.getTop().get(2).getPhoto());
                    intent.putExtra("USERID", item.getTop().get(2).getUserid());
                    context.startActivity(intent);
                }
            });
            vh.name3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", item.getTop().get(2).getName());
                    intent.putExtra("NICKNAME", item.getTop().get(2).getNickname());
                    intent.putExtra("PHOTO", item.getTop().get(2).getPhoto());
                    intent.putExtra("USERID", item.getTop().get(2).getUserid());
                    context.startActivity(intent);
                }
            });
        } else {
            vh.top3Layout.setVisibility(View.GONE);
        }

        if (item.getTop().size() > 3) {
            vh.top4Layout.setVisibility(View.VISIBLE);
            if (item.getTop().get(3).getName().length() != 0) {
                vh.placeholder4.setText(item.getTop().get(3).getName().substring(0, 1));
            } else {
                vh.placeholder4.setText("А");
            }
            vh.photo4.setImageResource(getPlaceholder(item.getTop().get(3).getNickname()));
            Picasso.with(context).load(item.getTop().get(3).getPhoto() + "").placeholder(getPlaceholder(item.getTop().get(3).getNickname())).error(getPlaceholder(item.getTop().get(3).getNickname())).into(vh.photo4, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    vh.placeholder4.setText("");
                }

                @Override
                public void onError() {

                }
            });
            vh.name4.setText(item.getTop().get(3).getName());
            vh.nickname4.setText(item.getTop().get(3).getNickname());
            vh.place4.setText(item.getTop().get(3).getPosition() + " " + context.getString(R.string.adapterRating_place));
            vh.task4.setText(item.getTop().get(3).getComplited() + "/" + item.getTasks());
            int n = 1000 / Integer.parseInt(item.getTasks());
            n = n * Integer.parseInt(item.getTop().get(3).getComplited());
            vh.bar4.setProgress(n);
            vh.photo4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", item.getTop().get(3).getName());
                    intent.putExtra("NICKNAME", item.getTop().get(3).getNickname());
                    intent.putExtra("PHOTO", item.getTop().get(3).getPhoto());
                    intent.putExtra("USERID", item.getTop().get(3).getUserid());
                    context.startActivity(intent);
                }
            });
            vh.name4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("NAME", item.getTop().get(3).getName());
                    intent.putExtra("NICKNAME", item.getTop().get(3).getNickname());
                    intent.putExtra("PHOTO", item.getTop().get(3).getPhoto());
                    intent.putExtra("USERID", item.getTop().get(0).getUserid());
                    context.startActivity(intent);
                }
            });
        } else {
            vh.top4Layout.setVisibility(View.GONE);
        }


    }

    private void initPost(final FeedsAdapter.ViewHolder vh, final Feed item) {
        vh.textViewCompany.setText(item.getCompany());
        vh.textViewTitle.setText(item.getTitle());
        vh.textViewName.setText(item.getUserName());
        vh.textViewNickname.setText(item.getUserNickname());
        vh.textViewDescription.setText(item.getTaskDescription());
        vh.textViewTask.setText(item.getTaskNumber() + "/" + item.getTasks());
        vh.textViewLike.setText(item.getFeedLikes());
        vh.textViewTag.setText("#" + item.getTaskComment());
        //todo
        long minutes = Long.parseLong(item.getTaskTime()) / (1000 * 60);
        long seconds = Long.parseLong(item.getTaskTime()) / 1000 % 60;
        long millis = Long.parseLong(item.getTaskTime()) % 1000;
        String hms = String.format("%02d:%02d.%02d", minutes, seconds, millis);
        hms = hms.substring(0, 8);
        vh.textViewTime.setText(hms);
//        if (Long.parseLong(item.getTaskTime()) > 59000) {
//            int sec = Integer.parseInt(item.getTaskTime()) - 60;
//            vh.textViewTime.setText("1:" + sec);
//        } else {
//            vh.textViewTime.setText("0:" + item.getTaskTime());
//        }

        vh.placeholder01.setText(item.getCompany().substring(0, 1).toUpperCase());
        vh.imageViewCompany.setImageResource(getPlaceholder(item.getCompany()));
        if (item.getUserName().length() != 0) {
            vh.placeholder02.setText(item.getUserName().substring(0, 1).toUpperCase());
        } else {
            vh.placeholder02.setText("A");
        }
        vh.imageViewPhoto.setImageResource(getPlaceholder(item.getCompany()));


        Picasso.with(context).load(item.getLogoSponsorUrl() + "").placeholder(getPlaceholder(item.getCompany())).error(getPlaceholder(item.getCompany())).into(vh.imageViewCompany, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                vh.placeholder01.setText("");
            }

            @Override
            public void onError() {

            }
        });
        Picasso.with(context).load(item.getUserPhotoUrl() + "").placeholder(getPlaceholder(item.getUserNickname())).error(getPlaceholder(item.getUserNickname())).into(vh.imageViewUser, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                vh.placeholder02.setText("");
            }

            @Override
            public void onError() {

            }
        });
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.unknow);
        requestOptions.error(R.drawable.unknow);
        requestOptions.override(1024, 1024);
        requestOptions.centerCrop();

        Glide.with(context).setDefaultRequestOptions(requestOptions).load(item.getTaskPhotoUrl()).thumbnail(0.3f).into(vh.imageViewPhoto);
        //Picasso.with(context).load(item.getTaskPhotoUrl()+"").centerCrop().resize(1024,1024).placeholder(R.drawable.unknow).error(R.drawable.unknow).into(vh.imageViewPhoto);
        if (item.getUserLike().equals("0")) {
            vh.imageViewLike.setImageResource(R.drawable.like_unused);
            vh.imageViewLike.setTag("unused");
        } else {
            vh.imageViewLike.setImageResource(R.drawable.like);
            vh.imageViewLike.setTag("used");
        }

        vh.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (String.valueOf(vh.imageViewLike.getTag()).equals("used")) {
                    vh.imageViewLike.setImageResource(R.drawable.like_unused);
                    vh.textViewLike.setText(Integer.parseInt(vh.textViewLike.getText().toString()) - 1 + "");
                    vh.imageViewLike.setTag("unused");
                } else {
                    vh.imageViewLike.setImageResource(R.drawable.like);
                    vh.textViewLike.setText(Integer.parseInt(vh.textViewLike.getText().toString()) + 1 + "");
                    vh.imageViewLike.setTag("used");
                }


                SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
                String id = sharedPref.getString("token", "null");
                ApiService api = RetroClient.getApiService();
                if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                    Call<DefaultCallback> call = api.setLike(id, item.getFeedId());
                    call.enqueue(new Callback<DefaultCallback>() {
                        @Override
                        public void onResponse(Call<DefaultCallback> call, Response<DefaultCallback> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                Log.i("LOG_like", "Success(error): " + response.body().getStatus() + item.getFeedId());
                            } else {
                                String jObjError = null;
                                try {
                                    assert response.errorBody() != null;
                                    jObjError = response.errorBody().string() + "";
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.i("LOG_like", jObjError + " error");

                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultCallback> call, Throwable t) {
                            Log.i("LOG_like", t.getMessage() + " fail");
                        }
                    });

                } else {
                    Log.i("LOG_gethistory", "error internet");
                }
            }
        });

        actions(vh, item);
    }

    private int getPlaceholder(String s) {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZЯЧСМИТЬБЮФЫВАПРОЛДЖЭЁЙЦУКЕНГШЩЗХЪІЄ0123456789";
        int n = abc.indexOf(s.substring(0, 1).toUpperCase());
        if (n == 0 || n == 20 || n == 40 || n == 60) {
            return R.color.color1;
        }
        if (n == 1 || n == 21 || n == 41 || n == 61) {
            return R.color.color2;
        }
        if (n == 2 || n == 22 || n == 42 || n == 62) {
            return R.color.color3;
        }
        if (n == 3 || n == 23 || n == 43 || n == 63) {
            return R.color.color4;
        }
        if (n == 4 || n == 24 || n == 44 || n == 64) {
            return R.color.color5;
        }
        if (n == 5 || n == 25 || n == 45 || n == 65) {
            return R.color.color6;
        }
        if (n == 6 || n == 26 || n == 46 || n == 66) {
            return R.color.color7;
        }
        if (n == 7 || n == 27 || n == 47 || n == 67) {
            return R.color.color8;
        }
        if (n == 8 || n == 28 || n == 48 || n == 68) {
            return R.color.color9;
        }
        if (n == 9 || n == 29 || n == 49 || n == 69) {
            return R.color.color10;
        }
        if (n == 10 || n == 30 || n == 50 || n == 70) {
            return R.color.color11;
        }
        if (n == 11 || n == 31 || n == 51 || n == 71) {
            return R.color.color12;
        }
        if (n == 12 || n == 32 || n == 52 || n == 72) {
            return R.color.color13;
        }
        if (n == 13 || n == 33 || n == 53 || n == 73) {
            return R.color.color14;
        }
        if (n == 14 || n == 34 || n == 54 || n == 74) {
            return R.color.color15;
        }
        if (n == 15 || n == 35 || n == 55 || n == 75) {
            return R.color.color16;
        }
        if (n == 16 || n == 36 || n == 56 || n == 76) {
            return R.color.color17;
        }
        if (n == 17 || n == 37 || n == 57 || n == 77) {
            return R.color.color18;
        }
        if (n == 18 || n == 38 || n == 58 || n == 78) {
            return R.color.color19;
        }
        if (n == 19 || n == 39 || n == 59 || n == 79) {
            return R.color.color20;
        }
        return R.color.color1;
    }

    private static class ViewHolder {
        private final LinearLayout rootView;

        private final LinearLayout layPost;
        private final ImageView imageViewCompany;
        private final ImageView imageViewUser;
        private final ImageView imageViewPhoto;
        private final ImageView imageViewLike;
        private final TextView textViewCompany;
        private final TextView textViewTitle;
        private final TextView textViewName;
        private final TextView textViewNickname;
        private final TextView textViewTime;
        private final TextView textViewDescription;
        private final TextView textViewTask;
        private final TextView textViewLike;
        private final TextView textViewTag;
        private final TextView placeholder01;
        private final TextView placeholder02;
        private final ImageView setting;

        private final LinearLayout layRate;
        private final ImageView imageViewCompany1;
        private final TextView textViewCompany1;
        private final TextView textViewTitle1;
        private final TextView textViewTimeLeft1;
        private final TextView textViewPeople1;

        private final LinearLayout gameLayout;
        private final LinearLayout top1Layout;
        private final LinearLayout top2Layout;
        private final LinearLayout top3Layout;
        private final LinearLayout top4Layout;

        private final TextView placeholder1;
        private final TextView placeholder2;
        private final TextView placeholder3;
        private final TextView placeholder4;
        private final ImageView photo1;
        private final ImageView photo2;
        private final ImageView photo3;
        private final ImageView photo4;
        private final TextView name1;
        private final TextView name2;
        private final TextView name3;
        private final TextView name4;
        private final TextView nickname2;
        private final TextView nickname3;
        private final TextView nickname4;
        private final TextView place2;
        private final TextView place3;
        private final TextView place4;
        private final TextView task1;
        private final TextView task2;
        private final TextView task3;
        private final TextView task4;
        private final ProgressBar bar1;
        private final ProgressBar bar2;
        private final ProgressBar bar3;
        private final ProgressBar bar4;


        private ViewHolder(LinearLayout rootView, LinearLayout layPost, ImageView imageView, ImageView imageView1, ImageView imageView2, ImageView imageView3, TextView textView1, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8, TextView textView9, TextView placeholder01, TextView placeholder02, ImageView setting,
                           LinearLayout layRate, LinearLayout gameLayout, LinearLayout top1Layout, LinearLayout top2Layout, LinearLayout top3Layout, LinearLayout top4Layout, ImageView imageView111, TextView textView11, TextView textView21, TextView textView41, TextView textView51,
                           TextView placeholder1, TextView placeholder2, TextView placeholder3, TextView placeholder4, ImageView photo1, ImageView photo2, ImageView photo3, ImageView photo4, TextView name1, TextView name2, TextView name3, TextView name4,
                           TextView nickname2, TextView nickname3, TextView nickname4, TextView place2, TextView place3, TextView place4,
                           TextView task1, TextView task2, TextView task3, TextView task4, ProgressBar bar1, ProgressBar bar2, ProgressBar bar3, ProgressBar bar4) {
            this.rootView = rootView;
            this.layPost = layPost;
            this.imageViewCompany = imageView;
            this.imageViewUser = imageView1;
            this.imageViewPhoto = imageView2;
            this.imageViewLike = imageView3;
            this.textViewCompany = textView9;
            this.textViewName = textView2;
            this.textViewNickname = textView3;
            this.textViewTime = textView4;
            this.textViewDescription = textView5;
            this.textViewTask = textView6;
            this.textViewLike = textView7;
            this.textViewTag = textView8;
            this.textViewTitle = textView1;
            this.placeholder01 = placeholder01;
            this.placeholder02 = placeholder02;
            this.setting = setting;

            this.layRate = layRate;
            this.imageViewCompany1 = imageView111;
            this.textViewCompany1 = textView11;
            this.textViewTitle1 = textView21;
            this.textViewTimeLeft1 = textView41;
            this.textViewPeople1 = textView51;


            this.gameLayout = gameLayout;
            this.top1Layout = top1Layout;
            this.top2Layout = top2Layout;
            this.top3Layout = top3Layout;
            this.top4Layout = top4Layout;

            this.placeholder1 = placeholder1;
            this.placeholder2 = placeholder2;
            this.placeholder3 = placeholder3;
            this.placeholder4 = placeholder4;
            this.photo1 = photo1;
            this.photo2 = photo2;
            this.photo3 = photo3;
            this.photo4 = photo4;
            this.name1 = name1;
            this.name2 = name2;
            this.name3 = name3;
            this.name4 = name4;
            this.nickname2 = nickname2;
            this.nickname3 = nickname3;
            this.nickname4 = nickname4;
            this.place2 = place2;
            this.place3 = place3;
            this.place4 = place4;
            this.task1 = task1;
            this.task2 = task2;
            this.task3 = task3;
            this.task4 = task4;
            this.bar1 = bar1;
            this.bar2 = bar2;
            this.bar3 = bar3;
            this.bar4 = bar4;
        }

        public static FeedsAdapter.ViewHolder create(LinearLayout rootView) {
            LinearLayout layPost = rootView.findViewById(R.id.layPost);
            ImageView imageViewCompany = rootView.findViewById(R.id.imageView1);
            ImageView imageViewUser = rootView.findViewById(R.id.imageView2);
            ImageView imageViewPhoto = rootView.findViewById(R.id.imageView3);
            ImageView imageViewLike = rootView.findViewById(R.id.imageView4);
            TextView textViewCompany = rootView.findViewById(R.id.textView1);
            TextView textViewName = rootView.findViewById(R.id.textView2);
            TextView textViewNickname = rootView.findViewById(R.id.textView3);
            TextView textViewTime = rootView.findViewById(R.id.textView4);
            TextView textViewDescription = rootView.findViewById(R.id.textView6);
            TextView textViewTask = rootView.findViewById(R.id.textView7);
            TextView textViewLike = rootView.findViewById(R.id.textView8);
            TextView textViewTag = rootView.findViewById(R.id.textView9);
            TextView textViewTitle = rootView.findViewById(R.id.textView0);
            TextView placeholder01 = rootView.findViewById(R.id.textHolder01);
            TextView placeholder02 = rootView.findViewById(R.id.textHolder02);
            ImageView setting = rootView.findViewById(R.id.imageView0);

            LinearLayout layRate = rootView.findViewById(R.id.layRate);
            ImageView imageViewCompany1 = rootView.findViewById(R.id.imageView);
            TextView textViewCompany1 = rootView.findViewById(R.id.textView01);
            TextView textViewTitle1 = rootView.findViewById(R.id.textView02);
            TextView textViewTimeLeft1 = rootView.findViewById(R.id.textView04);
            TextView textViewPeople1 = rootView.findViewById(R.id.textView5);

            LinearLayout gameLayout = rootView.findViewById(R.id.game);
            LinearLayout top1Layout = rootView.findViewById(R.id.layUser1);
            LinearLayout top2Layout = rootView.findViewById(R.id.layUser2);
            LinearLayout top3Layout = rootView.findViewById(R.id.layUser3);
            LinearLayout top4Layout = rootView.findViewById(R.id.layUser4);

            TextView placeholder1 = rootView.findViewById(R.id.textHolder2);
            TextView placeholder2 = rootView.findViewById(R.id.textHolder3);
            TextView placeholder3 = rootView.findViewById(R.id.textHolder4);
            TextView placeholder4 = rootView.findViewById(R.id.textHolder5);
            ImageView photo1 = rootView.findViewById(R.id.imageView11);
            ImageView photo2 = rootView.findViewById(R.id.imageView12);
            ImageView photo3 = rootView.findViewById(R.id.imageView13);
            ImageView photo4 = rootView.findViewById(R.id.imageView14);
            TextView name1 = rootView.findViewById(R.id.textView11);
            TextView name2 = rootView.findViewById(R.id.textView15);
            TextView name3 = rootView.findViewById(R.id.textView19);
            TextView name4 = rootView.findViewById(R.id.textView23);
            TextView nickname2 = rootView.findViewById(R.id.textView17);
            TextView nickname3 = rootView.findViewById(R.id.textView21);
            TextView nickname4 = rootView.findViewById(R.id.textView25);
            TextView place2 = rootView.findViewById(R.id.textView16);
            TextView place3 = rootView.findViewById(R.id.textView20);
            TextView place4 = rootView.findViewById(R.id.textView24);
            TextView task1 = rootView.findViewById(R.id.textView14);
            TextView task2 = rootView.findViewById(R.id.textView18);
            TextView task3 = rootView.findViewById(R.id.textView22);
            TextView task4 = rootView.findViewById(R.id.textView26);
            ProgressBar bar1 = rootView.findViewById(R.id.bar1);
            ProgressBar bar2 = rootView.findViewById(R.id.bar2);
            ProgressBar bar3 = rootView.findViewById(R.id.bar3);
            ProgressBar bar4 = rootView.findViewById(R.id.bar4);


            return new FeedsAdapter.ViewHolder(rootView, layPost, imageViewCompany, imageViewUser, imageViewPhoto, imageViewLike, textViewCompany, textViewName, textViewNickname, textViewTime, textViewDescription, textViewTask, textViewLike, textViewTag, textViewTitle, placeholder01, placeholder02, setting,
                    layRate, gameLayout, top1Layout, top2Layout, top3Layout, top4Layout, imageViewCompany1, textViewCompany1, textViewTitle1, textViewTimeLeft1, textViewPeople1,
                    placeholder1, placeholder2, placeholder3, placeholder4, photo1, photo2, photo3, photo4, name1, name2, name3, name4,
                    nickname2, nickname3, nickname4, place2, place3, place4,
                    task1, task2, task3, task4, bar1, bar2, bar3, bar4);
        }

    }


}

