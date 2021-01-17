package com.example.avitotest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    Logic model;
    LiveData<ArrayList<Integer>> list;
    ArrayList<Integer> numList = new ArrayList<>();
    Adapter adapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        model = new ViewModelProvider(this).get(Logic.class);
        list = model.getData();
        System.out.println(list.getValue());
        for (int i = 0; i < list.getValue().size(); i ++){
            numList.add(list.getValue().get(i));
        }
        recyclerView = findViewById(R.id.recycler);
        adapter = new Adapter(numList, model.getDataArray(), model);
        recyclerView.setAdapter(adapter);


        list.observe(this, new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                int position = -1;
                int length = (numList.size() < integers.size()) ? numList.size() : integers.size();
                for (int i = 0; i < length; i++) {
                    if (numList.get(i) != integers.get(i)) {
                        position = i;
                        break;
                    }
                }
                if (numList.size() < integers.size()) {
                    if (position == -1) {
                        position = integers.size()-1;
                        numList.add(integers.get(position));
                    }
                    else if (position < integers.size()) {
                        numList.add(position, integers.get(position));
                        adapter.addNum(position);
                    }
                } else if (numList.size() > integers.size()){
                    if (position == -1) position = numList.size()-1;
                    numList.remove(position);
                }
                System.out.println("NUMLIST"+numList);
            }
        });

        int spanCount = getResources().getInteger(R.integer.column_counter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder>{
        ArrayList<Integer> data;
        public Adapter (ArrayList<Integer> numList, DeleteCallback delcallback, Callback callback){
            this.callback = callback;
            this.delCallback = delcallback;
            data = numList;
        }
        DeleteCallback delCallback;
        Callback callback;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_layout, parent, false);
            Button close = view.findViewById(R.id.close_button);
            LinearLayout cell = view.findViewById(R.id.container);
            Animation btnAnim = AnimationUtils.loadAnimation(parent.getContext(), R.anim.button_anim);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(btnAnim);
                    TextView num = view.findViewById(R.id.numtext);
                    int position = -1;
                    position = recyclerView.getChildLayoutPosition(cell);
                    if (position != -1) {
                        notifyItemRemoved(position);
                        data.remove(position);
                        callback.response(0, position, "delete");
                        delCallback.response(Integer.valueOf(num.getText().toString()));
                    }
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            int num = data.get(position);
            holder.num.setText(Integer.toString(num));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void addNum(int position) {
            notifyItemInserted(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cell;
        TextView num;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cell = itemView.findViewById(R.id.container);
            num = itemView.findViewById(R.id.numtext);
        }
    }
}