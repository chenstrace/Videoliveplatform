package com.panda.videoliveplatform.chat;

public class EmoticonBean {
    private String content;
    private String iconUri;

    public String getIconUri() {
        return this.iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmoticonBean(String iconUri, String content) {
        this.iconUri = iconUri;
        this.content = content;
    }
}
