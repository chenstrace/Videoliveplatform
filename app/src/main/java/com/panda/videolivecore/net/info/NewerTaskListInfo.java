package com.panda.videolivecore.net.info;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewerTaskListInfo {
    public List<TaskListItem> mTaskListData = new ArrayList();

    public class TaskListItem {
        public String appkey;
        public String bamboo;
        public String desc;
        public String done;
        public String icon;
        public String id;
        public String my_task_id;
        public String name;
        public String type;
    }

    public void read(JsonReader paramJsonReader)
            throws IOException {
        paramJsonReader.beginObject();
        while (paramJsonReader.hasNext()) {
            String str1 = paramJsonReader.nextName();
            if (str1.equalsIgnoreCase("items")) {
                paramJsonReader.beginArray();
                while (paramJsonReader.hasNext()) {
                    paramJsonReader.beginObject();
                    TaskListItem localTaskListItem = new TaskListItem();
                    while (paramJsonReader.hasNext()) {
                        String str2 = paramJsonReader.nextName();
                        if (str2.equalsIgnoreCase("id")) {
                            localTaskListItem.id = paramJsonReader.nextString();
                        } else if (str2.equalsIgnoreCase("pictures")) {
                            paramJsonReader.beginObject();
                            while (paramJsonReader.hasNext())
                                if (paramJsonReader.nextName().equalsIgnoreCase("img"))
                                    localTaskListItem.icon = paramJsonReader.nextString();
                                else
                                    paramJsonReader.skipValue();
                            paramJsonReader.endObject();
                        } else if (str2.equalsIgnoreCase("name")) {
                            localTaskListItem.name = paramJsonReader.nextString();
                        } else if (str2.equalsIgnoreCase("done")) {
                            localTaskListItem.done = paramJsonReader.nextString();
                        } else if (str2.equalsIgnoreCase("my_task_id")) {
                            localTaskListItem.my_task_id = paramJsonReader.nextString();
                        } else if (str2.equalsIgnoreCase("bamboo")) {
                            localTaskListItem.bamboo = paramJsonReader.nextString();
                        } else if (str2.equalsIgnoreCase("type")) {
                            localTaskListItem.type = paramJsonReader.nextString();
                        } else if (str2.equalsIgnoreCase("appkey")) {
                            localTaskListItem.appkey = paramJsonReader.nextString();
                        } else if (str2.equalsIgnoreCase("desc")) {
                            localTaskListItem.desc = paramJsonReader.nextString();
                        } else {
                            paramJsonReader.skipValue();
                        }
                    }
                    this.mTaskListData.add(localTaskListItem);
                    paramJsonReader.endObject();
                }
                paramJsonReader.endArray();
            } else if (str1.equalsIgnoreCase("total")) {
                paramJsonReader.nextInt();
            } else {
                paramJsonReader.skipValue();
            }
        }
        paramJsonReader.endObject();
    }
}
