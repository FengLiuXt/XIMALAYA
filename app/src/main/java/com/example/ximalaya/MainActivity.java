package com.example.ximalaya;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ximalaya.precenter.MainActivityPrecenter;
import com.squareup.picasso.Picasso;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class MainActivity extends AppCompatActivity implements MainActivityPrecenter.IMainActivityPrecenter {

    public  static final int ID = 1;

    private MagicIndicator mMagicIndicator;
    private CommonNavigator commonNavigator;
    private ViewPager mViewPager;
    private RelativeLayout mPlayerPane;
    private ImageView mCoverImage;
    private TextView mAlbumTitle;
    private TextView mAuthor;
    private ImageView mPlayerControl;

    private int mPosition = -1;
    private MainActivityPrecenter mMainActivityPrecenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mMagicIndicator = findViewById(R.id.magic_indicator);
//        mViewPager = findViewById(R.id.view_pager);
//
//        commonNavigator = new CommonNavigator(this);


        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);


//        mCoverImage = findViewById(R.id.cover_image);
//        mAlbumTitle = findViewById(R.id.title);
//        mAuthor = findViewById(R.id.author);
//
//        mMainActivityPrecenter = new MainActivityPrecenter(this);
//        init();
    }

    private void init(){

        mPlayerPane = findViewById(R.id.player);
        mPlayerPane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mPosition != -1) {
                    Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                    intent.putExtra(PlayerActivity.FROME_ACTIVITYID, ID);
                    intent.putExtra(PlayerActivity.POSITION, mPosition);
                    startActivityForResult(intent, PlayerActivity.ID);
                }
            }
        });

        mPlayerControl = findViewById(R.id.play_control);

        mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mMainActivityPrecenter.isPlaying()){

                    if(mPosition != -1)
                        mMainActivityPrecenter.pause();
                }else {

                    if(mPosition != -1)
                        mMainActivityPrecenter.play();
                }

            }
        });
    }

    @Override
    public void playStart() {

        mPlayerControl.setImageDrawable(getDrawable(R.drawable.bt_play_start_selector));
    }

    @Override
    public void playPause() {

        mPlayerControl.setImageDrawable(getDrawable(R.drawable.bt_play_pause_selector));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DetailActivity.ID && resultCode == RESULT_OK){

            mPosition = data.getIntExtra(PlayerActivity.POSITION,-1);

            if(mPosition != -1) {
                String url = mMainActivityPrecenter.getCoverUrl(mPosition);
                String title = mMainActivityPrecenter.getSongName(mPosition);
                String author = mMainActivityPrecenter.getAuthorNam(mPosition);

                Picasso.with(this).load(url).into(mCoverImage);
                mAlbumTitle.setText(title);
                mAuthor.setText(author);
            }
        }

        else if(requestCode == PlayerActivity.ID && resultCode == RESULT_OK){

            mPosition = data.getIntExtra(PlayerActivity.POSITION, 0);

            String url = mMainActivityPrecenter.getCoverUrl(mPosition);
            String title = mMainActivityPrecenter.getSongName(mPosition);
            String author = mMainActivityPrecenter.getAuthorNam(mPosition);

            Picasso.with(this).load(url).into(mCoverImage);
            mAlbumTitle.setText(title);
            mAuthor.setText(author);
        }
    }
}
