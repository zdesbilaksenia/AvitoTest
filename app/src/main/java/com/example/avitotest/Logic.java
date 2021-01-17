package com.example.avitotest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class Logic extends ViewModel implements Callback{

    private MutableLiveData<ArrayList<Integer>> data;
    ArrayList<Integer> list = new ArrayList<>();
    Data dataArray;

    public LiveData<ArrayList<Integer>> getData(){
        if (data == null) {
            data = new MutableLiveData<>();
            loadData();
        }
        return data;
    }
    public Data getDataArray(){
        return dataArray;
    }

    public void loadData(){
        dataArray = new Data(this);
        for (int i = 0; i < 15; i ++) {
            list.add(i+1);
        }
        System.out.println("LIST "+list);
        data.setValue(list);
    }

    @Override
    public void response(int num, int position, String key) {
        if (key == "add") {
            list.add(position, num);
        } else {
            list.remove(position);
        }
        data.postValue(list);
    }
}
