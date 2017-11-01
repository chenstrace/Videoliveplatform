package com.panda.videoliveplatform.chat;

public interface OnOperationListener {
    void SelectGift();

    void hideGiftLayout();

    void selectedFace(String str);

    void selectedFuncation(int i);

    boolean send(String str);
}
