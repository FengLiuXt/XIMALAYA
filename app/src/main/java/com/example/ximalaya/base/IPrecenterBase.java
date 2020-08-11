package com.example.ximalaya.base;

public interface IPrecenterBase {

    void loading();
    void loadError(String error);
    void netError();
    void dataEmpty();
}
