package com.example.musicapp.Interfaces;

import android.app.ProgressDialog;

public interface IDownloadAdpater {
    void returnFromThread(Boolean result, ProgressDialog dialog);
}
