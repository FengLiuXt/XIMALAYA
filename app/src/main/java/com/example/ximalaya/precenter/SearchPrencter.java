package com.example.ximalaya.precenter;

import com.example.ximalaya.base.IModeListener;
import com.example.ximalaya.base.IPrecenterBase;
import com.example.ximalaya.mode.HotWordsContent;
import com.example.ximalaya.mode.SearchContent;
import com.example.ximalaya.mode.SuggestWordsContent;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.List;

public class SearchPrencter {

    public interface ISearchPrencter extends IPrecenterBase {

        void loadHotWordsList(List<HotWord> hotWords);
        void loadSearchByKeyNextPage(List<Album> albumList);
        void loadSearchByKeyMoreResult(List<Album> albumList);
        void loadSuggestWords(List<QueryResult> suggestWordsList);
    }

    public static int LOAD_MORE = 0;
    public static int LOAD_NEXT = 1;

    private ISearchPrencter mISearchPrencter;
    private HotWordsContent mHotWordsContent;
    private SearchContent mSearchContent;
    private SuggestWordsContent mSuggestWordsContent;

    public SearchPrencter(ISearchPrencter iSearchPrencter) {

        mISearchPrencter = iSearchPrencter;
        mHotWordsContent = new HotWordsContent();
        mSearchContent = new SearchContent();
        mSuggestWordsContent = new SuggestWordsContent();
    }

    public void loadHotWords() {

        mHotWordsContent.loadHotWords(new IModeListener<HotWord>() {
            @Override
            public void loadSuccess(List<HotWord> data) {


                if(mISearchPrencter != null && data != null){

                    if(data.size() != 0){
                        mISearchPrencter.loadHotWordsList(data);
                    }
                }
            }

            @Override
            public void loadError(String errorMsg) {

                if(mISearchPrencter != null)
                    mISearchPrencter.loadError(errorMsg);
            }
        });
    }

    public void searchByKeyword(String key, int page, final int loadType){

        if(mISearchPrencter != null)
            mISearchPrencter.loading();

        mSearchContent.searchByKeyword(key, page, new IModeListener<Album>() {
            @Override
            public void loadSuccess(List<Album> data) {

                if(mISearchPrencter != null){

                    if(data.size() == 0){
                        mISearchPrencter.dataEmpty();
                    }else if(loadType == LOAD_MORE) {
                        mISearchPrencter.loadSearchByKeyMoreResult(data);
                    }
                    else if(loadType == LOAD_NEXT) {
                        mISearchPrencter.loadSearchByKeyNextPage(data);
                    }
                }
            }

            @Override
            public void loadError(String errorMsg) {

                if(mISearchPrencter != null){
                    mISearchPrencter.loadError(errorMsg);
                }
            }
        });
    }


    public void loadSuggestWords(String key){

       mSuggestWordsContent.loadSuggestWords(key, new IModeListener<QueryResult>() {
           @Override
           public void loadSuccess(List<QueryResult> data) {

               if(data != null && mISearchPrencter != null){

                   if(data.size() != 0)
                    mISearchPrencter.loadSuggestWords(data);
               }
           }

           @Override
           public void loadError(String errorMsg) {

               if(mISearchPrencter != null)
                   mISearchPrencter.loadError(errorMsg);
           }
       });

    }

}


