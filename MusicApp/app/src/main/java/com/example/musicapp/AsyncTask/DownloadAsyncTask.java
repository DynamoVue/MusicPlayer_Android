package com.example.musicapp.AsyncTask;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.musicapp.Adapter.PlaylistAdapter;


public class DownloadAsyncTask extends AsyncTask< String, Integer, Boolean > {
    PlaylistAdapter adapter;
    ProgressDialog dialog;
    DownloadManager downloadManager;

    public DownloadAsyncTask(PlaylistAdapter adapter, ProgressDialog dialog, DownloadManager downloadManager) {
        this.adapter = adapter; this.dialog = dialog; this.downloadManager = downloadManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    private boolean downloadFile(long downloadId) {
        boolean finishDownload = false;

        while (!finishDownload) {
            Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_FAILED: {
                        finishDownload = true;
                        break;
                    }
                    case DownloadManager.STATUS_PAUSED:
                        break;
                    case DownloadManager.STATUS_PENDING:
                        break;
                    case DownloadManager.STATUS_RUNNING: {
                        final long total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (total >= 0) {
                            final long downloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            // if you use downloadmanger in async task, here you can use like this to display progress.
                            // Don't forget to do the division in long to get more digits rather than double.
                            publishProgress((int) ((downloaded * 100L) / total));
                        }
                        break;
                    }
                    case DownloadManager.STATUS_SUCCESSFUL: {
                        // if you use aysnc task
                        // publishProgress(100);
                        finishDownload = true;
                        break;
                    }
                }
            }
        }
        return finishDownload;
    }

    @Override
    protected void onProgressUpdate(Integer... Integer)
    {
        dialog.setMessage("Downloaded " + ((int) Integer[0]) + "%...");
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        return downloadFile(Long.parseLong(strings[0]));
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        super.onPostExecute( result );
        adapter.returnFromThread(result, dialog);
    }
}
