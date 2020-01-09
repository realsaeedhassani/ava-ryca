package com.ryca.lyric.Utils;

import java.io.FileInputStream;

public interface OnCacheCallback {

    void onSuccess(FileInputStream stream);

    void onError();
}