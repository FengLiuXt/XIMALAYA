package com.example.ximalaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ximalaya.base.BaseApplication;
import com.example.ximalaya.precenter.PlayerPrecenter;
import com.example.ximalaya.util.TimeFormatUtils;


public class PlayerActivity extends AppCompatActivity implements PlayerPrecenter.IPlayerPrecenter{

    public static final int ID = 3;

    public static final String POSITION = "POSITION";
    public static final String FROME_ACTIVITYID = "FROME_ACTIVITY";

    //sp's key and name
    public static final String PLAY_MODE_SP_NAME = "PlayMod";
    public static final String PLAY_MODE_SP_KEY = "currentPlayMode";

    //播放状态
    public static final int PLAY_MODEL_LIST = 0; //列表播放
    public static final int PLAY_MODEL_SINGLE_LOOP = 1; //单曲循环播放
    public static final int PLAY_MODEL_RANDOM = 2; // 随机播放
    public static final int PLAY_MODEL_SINGLE  = 3; //单曲播放

    private TextView mTitleTv;
    private ViewPager mViewPager;
    private TextView mCurrentProgress;
    private SeekBar mSeekBar;
    private TextView mDuration;
    private ImageView mPlayModeBt;
    private ImageView mPreBt;
    private ImageView mPlayBt;
    private ImageView mNextBt;
    private ImageView mPlayListBt;


    private PlayerPrecenter mPlayerPrecenter;
    private SharedPreferences mPlayModSp;

    private int mCurrentProcess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mTitleTv = findViewById(R.id.title_tv);
        mViewPager = findViewById(R.id.track_view_pager);
        mCurrentProgress = findViewById(R.id.current_progress_tv);
        mSeekBar = findViewById(R.id.seek_bar);
        mDuration = findViewById(R.id.duration_tv);
        mPlayModeBt = findViewById(R.id.play_mode_switch_bt);
        mPreBt = findViewById(R.id.pre_bt);
        mNextBt = findViewById(R.id.next_bt);
        mPlayListBt = findViewById(R.id.play_list);
        mPlayBt = findViewById(R.id.play_bt);

        mPlayerPrecenter = new PlayerPrecenter(this);

        mPlayModSp = BaseApplication.getContext().getSharedPreferences(PLAY_MODE_SP_NAME, Context.MODE_PRIVATE);
        init();


    }

    private void init(){

        Intent intent = getIntent();
        final int position  = intent.getIntExtra(POSITION,0);
        mPlayerPrecenter.play(position);

        mPreBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPlayerPrecenter.pre();
            }
        });

        mNextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPlayerPrecenter.next();
            }
        });

        mSeekBar.setMax(mPlayerPrecenter.getDuration());
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){

                    mCurrentProcess = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mPlayerPrecenter.seekTo(mCurrentProcess);
            }
        });


        int currentState = mPlayModSp.getInt(PLAY_MODE_SP_KEY, PLAY_MODEL_LIST);
        mPlayerPrecenter.setPlayMode(currentState);
        setPlayModeBtSrc(currentState);

        mPlayModeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               int currentState =  mPlayModSp.getInt(PLAY_MODE_SP_KEY, PLAY_MODEL_LIST);

               int nextState = (currentState+1)%3;

               SharedPreferences.Editor editor = mPlayModSp.edit();
               editor.putInt(PLAY_MODE_SP_KEY, nextState);
               editor.commit();

               setPlayModeBtSrc(nextState);

            }
        });

        mPlayBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mPlayerPrecenter.isPlaying()){

                    mPlayerPrecenter.pause();
                }else {
                    mPlayerPrecenter.play();
                }
            }
        });

        if(mPlayerPrecenter.isPlaying()){

            mPlayBt.setImageDrawable(getDrawable(R.drawable.bt_play_start_selector));
            mSeekBar.setMax(mPlayerPrecenter.getDuration());
        }

    }

    private void setPlayModeBtSrc(int state){

        switch (state){

            case PLAY_MODEL_LIST:

                mPlayModeBt.setImageDrawable(getDrawable(R.drawable.bt_play_model_list_selector));
                break;

            case PLAY_MODEL_SINGLE_LOOP:

                mPlayModeBt.setImageDrawable(getDrawable(R.drawable.bt_play_mode_single_loop_selector));
                break;

            case PLAY_MODEL_RANDOM:

                mPlayModeBt.setImageDrawable(getDrawable(R.drawable.bt_play_model_randoom_selector));
                break;

            case PLAY_MODEL_SINGLE:

                mPlayModeBt.setImageDrawable(getDrawable(R.drawable.bt_play_mode_single_selector));
                break;
        }

        mPlayerPrecenter.setPlayMode(state);
    }


    @Override
    public void setAlbumTitle(String str) {

        mTitleTv.setText(str);
    }

    @Override
    public void duration(Object o) {

        mDuration.setText(TimeFormatUtils.durationFormat(o));
    }

    @Override
    public void playProgress(int currentProgress, int duration) {

        mSeekBar.setProgress(currentProgress);
        mCurrentProgress.setText(TimeFormatUtils.durationFormat(currentProgress));
    }

    @Override
    public void bufferProgress(int i) {

        mSeekBar.setSecondaryProgress((int) (i*1.0f/100*mPlayerPrecenter.getDuration()));
    }

    @Override
    public void playStart() {

        mPlayBt.setImageDrawable(getDrawable(R.drawable.bt_play_start_selector));
        mSeekBar.setMax(mPlayerPrecenter.getDuration());
    }

    @Override
    public void playPause() {

        mPlayBt.setImageDrawable(getDrawable(R.drawable.bt_play_pause_selector));
    }

    @Override
    public void onBackPressed() {

            int id = getIntent().getIntExtra(FROME_ACTIVITYID, 1);

            Intent intent = null;

            if (id == MainActivity.ID) {

                intent = new Intent(this, MainActivity.class);

            } else if (id == DetailActivity.ID) {
                intent = new Intent(this, DetailActivity.class);
            }

            intent.putExtra(POSITION, mPlayerPrecenter.getPosition());
            setResult(RESULT_OK, intent);
            finish();
        }

}
