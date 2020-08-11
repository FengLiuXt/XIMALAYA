package com.example.ximalaya.mode;

import com.example.ximalaya.api.XimalayApi;
import com.example.ximalaya.base.IModeListener;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;

import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

public class SuggestWordsContent {

    public void loadSuggestWords(String key, final IModeListener<QueryResult> iModeListener){

        XimalayApi.getSuggestWord(key, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(SuggestWords suggestWords) {

                if (iModeListener != null){

                    iModeListener.loadSuccess(suggestWords.getKeyWordList());
                }
            }

            @Override
            public void onError(int i, String s) {

                if(iModeListener != null){

                    iModeListener.loadError(s);
                }
            }
        });
    }
}
