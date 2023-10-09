package com.bodybody.firebase.chat.activities.managers;



import static com.bodybody.firebase.chat.activities.constants.IConstants.SLASH;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;


import com.bodybody.firebase.chat.activities.R;
import com.bodybody.firebase.chat.activities.constants.IConstants;
import com.bodybody.firebase.chat.activities.models.AttachmentTypes;
import com.bodybody.firebase.chat.activities.models.Chat;
import com.bodybody.firebase.chat.activities.models.DownloadFileEvent;

import java.io.File;

// 어플을 출시하지 않았지만 출시할 경우 다운로드 받을 수 있도록 다운로드 유틸 클래스를 생성했습니다.
// 다운로드 시작과 다운로드 중일 때 휴대폰 화면에 Toast를 뿌려 다운로드 되고 있음을 보여주도록 했습니다.
public class DownloadUtil {

    public void loading(Context context, DownloadFileEvent downloadFileEvent) {
        try {
            final Chat attach = downloadFileEvent.getAttachment();
            final File file = new File(IConstants.SDPATH + getDirectoryPath(attach.getAttachmentType()), SLASH + context.getString(R.string.app_name) + SLASH + attach.getAttachmentType() + SLASH + attach.getAttachmentFileName());
            Utils.sout("Downloading + Loading::: " + file.toString());
            if (file.exists()) {
                Utils.getOpenFileIntent(context, file.toString());
            } else {
                final Screens screens = new Screens(context);
                screens.showToast(R.string.msgDownloadingStarted);
                downloadFile(context, attach.getAttachmentPath(), attach.getAttachmentType(), attach.getAttachmentFileName());
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void downloadFile(Context context, String url, String type, String fileName) {
        final DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setDescription(context.getString(R.string.msgDownloadFile, fileName))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setVisibleInDownloadsUi(false)
                .setDestinationInExternalPublicDir(getDirectoryPath(type), SLASH + context.getString(R.string.app_name) + SLASH + type + SLASH + fileName);
        mgr.enqueue(request);
    }

    private String getDirectoryPath(String type) {
        return AttachmentTypes.getDirectoryByType(type);
    }
}
