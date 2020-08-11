package com.example.ximalaya.mode;

import com.example.ximalaya.api.XimalayApi;
import com.example.ximalaya.base.IModeListener;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;


public class HotWordsContent {

    public void loadHotWords(final IModeListener<HotWord> iModeListener){

        XimalayApi.getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(HotWordList hotWordList) {

                iModeListener.loadSuccess(hotWordList.getHotWordList());
            }

            @Override
            public void onError(int i, String s) {

                iModeListener.loadError(s);
            }
        });
    }
}
