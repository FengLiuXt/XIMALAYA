package com.example.ximalaya.base;

import java.util.List;

public interface IModeListener<T> {

    void loadSuccess(List<T> data);
    void loadError(String errorMsg);
}
