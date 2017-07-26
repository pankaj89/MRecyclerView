package com.master.mrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import master.com.library.MRecyclerView;
import master.com.library.OnLoadMoreListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MRecyclerView recyclerView;
    private MyAdpater myAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myAdpater = new MyAdpater(10);
        recyclerView = (MRecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        /*recyclerView.setLoadMoreFromBottom(true, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore() called");
                runLongRunningTaskBottom();
            }
        });*/
        recyclerView.setLoadMoreFromTop(true, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore() called");
                runLongRunningTaskTop();
            }
        });
        recyclerView.setAdapter(myAdpater);
    }

    int i = 0;

    private void runLongRunningTaskTop() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myAdpater.addItemOnTop(3);
                recyclerView.setLoadMoreDone();
                i++;
                if (i > 5)
                    recyclerView.setLoadMoreFromTop(false, null);
            }
        }, 3000);
    }

    private void runLongRunningTaskBottom() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myAdpater.addItem(3);
                recyclerView.setLoadMoreDone();
                i++;
                if (i > 5)
                    recyclerView.setLoadMoreFromBottom(false, null);
            }
        }, 3000);
    }
}
