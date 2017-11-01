package com.panda.videoliveplatform.chat;

import com.panda.videolivecore.net.info.RoomInfo;

public interface EnterRoomStateEvent {
    void onChangeRoomInfo(RoomInfo roomInfo);
}
