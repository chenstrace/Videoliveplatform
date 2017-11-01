package com.panda.videolivecore.net;

import com.panda.videolivecore.net.http.HttpRequest;
import com.panda.videolivecore.net.http.IHttpRequestEvent;
import java.io.File;

public class UserRequest {
    private HttpRequest m_request = null;

    public UserRequest(IHttpRequestEvent event) {
        this.m_request = new HttpRequest(event);
    }

    public HttpRequest get() {
        return this.m_request;
    }

    public void sendDownloadImage(String strUrl, String strContext) {
        this.m_request.send(strUrl, false, strContext);
    }

    public void sendUploadImage(String strFilePath, String strContext) {
        if (new File(strFilePath).exists()) {
            this.m_request.post(UrlConst.getUploadImgUrl(), strFilePath, strContext);
        }
    }

    public void sendSetUserinfo(String strNickName, String strAvatar, String strContext) {
        this.m_request.post(UrlConst.getSetUserinfoUrl(strNickName, strAvatar), "", strContext);
    }
}
