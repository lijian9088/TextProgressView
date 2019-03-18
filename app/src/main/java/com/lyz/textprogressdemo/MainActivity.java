package com.lyz.textprogressdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @author liyanze
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextProgressView tpv = findViewById(R.id.tpv);
        final OvalProgressView opv = findViewById(R.id.opv);
        final TextView tvProgrss = findViewById(R.id.tvProgress);
        //初始化
        tpv.setCurrentSize(7);
        tpv.setMaxSize(90);
        tpv.init(new float[]{7, 14, 30, 60, 90},
                new String[]{"7天", "14天", "30天", "60天", "90天"},
                new String[]{"$50", "$100", "$200", "$300", "$500"});


        opv.setProgress(7);
        opv.setMax(100);

        final SeekBar sb = findViewById(R.id.seekBar);
        sb.setMax(100);
        sb.setProgress(7);
        tvProgrss.setText(String.valueOf(7));
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tpv.setCurrentSize(progress);
                tvProgrss.setText(String.valueOf(progress));
                opv.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = sb.getProgress();
                System.out.println("progress:" + progress);
                tpv.anim(progress);
                opv.anim(progress);
            }
        });

    }
}
