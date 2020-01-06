package com.lauzy.freedom.lyricview.Utils;

import java.io.FileInputStream;

public interface OnCacheCallback {

    void onSuccess(FileInputStream stream);

    void onError();
}