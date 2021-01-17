package com.example.avitotest;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Data implements DeleteCallback {
    //private ArrayList<Integer> list = new ArrayList<>();
    private ArrayList<Integer> deletedNum = new ArrayList<>();
    private Callback callback;

    private Timer timer = new Timer();
    private TimerTask timerTask;

    private Random rnd = new Random();

    private int size = 15;

    public Data (Callback callback){
        this.callback = callback;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                addNum();
            }
        };
        timer.schedule(timerTask, 5000,5000);
    }

    private void addNum(){
        int position = rnd.nextInt(size+1);
        if (deletedNum.size() == 0) {
            size++;
            callback.response(size, position, "add");
        } else {
            size++;
            callback.response(deletedNum.get(0), position, "add");
            deletedNum.remove(0);
        }
        System.out.println("SIZE "+size);
    }

    @Override
    public void response(int num) {
        if (!deletedNum.contains(num)) {
            deletedNum.add(num);
        }
        size--;
        System.out.println("SIZE "+size);
        System.out.println("DEL "+deletedNum);
    }
}
