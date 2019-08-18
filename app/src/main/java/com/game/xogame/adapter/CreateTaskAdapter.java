package com.game.xogame.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.xogame.R;
import com.game.xogame.views.create.CreateTaskActivity;

import java.util.List;


public class CreateTaskAdapter  extends ArrayAdapter<String> {
    public static List<String> itemList;
    public List<String> tempList;
    private LayoutInflater mInflater;
    private CreateTaskActivity context;
    private Context context1;

    // Constructors
    public CreateTaskAdapter(CreateTaskActivity context, List<String> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        itemList = objects;
    }

    public CreateTaskAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
        this.context1 = context;
        this.mInflater = LayoutInflater.from(context);
        itemList = objects;
    }

    @Override
    public String getItem(int position) {
        return itemList.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CreateTaskAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.lay_task, parent, false);
            vh = CreateTaskAdapter.ViewHolder.create((ConstraintLayout) view);
            view.setTag(vh);

        } else {
            vh = (CreateTaskAdapter.ViewHolder) convertView.getTag();
        }

        //Fill EditText with the value you have in data source
        vh.textViewTask.setText(itemList.get(position));
        vh.textViewTask.setId(position);

        //we need to update adapter once we finish with editing
        vh.textViewTask.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    if(itemList.size()>position)
                        itemList.set(position,Caption.getText().toString());
                }
            }
        });


        vh.textViewNumber.setText("Задание "+(position+1));
        if(position==0){
            vh.imageView.setVisibility(View.GONE);
        }else{
            vh.imageView.setVisibility(View.VISIBLE);
        }
        vh.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List l = context.getList();
                l.remove(position);
                context.setList(l);
            }
        });

        if(context1!=null){
            vh.textViewTask.setEnabled(false);
            vh.textViewTask.setFocusable(false);
        }else{
            vh.textViewTask.setEnabled(true);
            vh.textViewTask.setFocusable(true);
        }



        return vh.rootView;
    }




    private static class ViewHolder {
        private final ConstraintLayout rootView;
        private final TextView textViewNumber;
        private final EditText textViewTask;
        private final ImageView imageView;

        private ViewHolder(ConstraintLayout rootView, ImageView imageView, TextView textViewNumber, EditText textViewTask) {
            this.rootView = rootView;
            this.textViewNumber = textViewNumber;
            this.textViewTask = textViewTask;
            this.imageView = imageView;
        }

        public static CreateTaskAdapter.ViewHolder create(ConstraintLayout rootView) {
            TextView textViewNumber = rootView.findViewById(R.id.textViewNumber);
            EditText textViewTask = rootView.findViewById(R.id.textViewTask);
            ImageView imageView = rootView.findViewById(R.id.imageView);
            return new CreateTaskAdapter.ViewHolder(rootView, imageView, textViewNumber, textViewTask);
        }

    }
}
