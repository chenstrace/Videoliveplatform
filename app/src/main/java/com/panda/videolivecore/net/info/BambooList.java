package com.panda.videolivecore.net.info;

import android.util.JsonReader;

import java.io.IOException;

public class BambooList {
    public String bamboo = "";
    public String desc = "";
    public String done = "";
    public String id = "";
    public int interval = 0;
    public String my_task_id = "";
    public String name = "";
    public String pictures_img = "";
    public String priority = "";
    public String type = "";

    public void resetAllData() {
        this.id = "";
        this.pictures_img = "";
        this.name = "";
        this.done = "";
        this.my_task_id = "";
        this.bamboo = "";
        this.desc = "";
        this.type = "";
        this.priority = "";
        this.interval = 0;
    }

    public void read(JsonReader paramJsonReader)
            throws IOException {
        paramJsonReader.beginObject();
        while (paramJsonReader.hasNext())
            if (paramJsonReader.nextName().equalsIgnoreCase("items")) {
                paramJsonReader.beginArray();
                while (paramJsonReader.hasNext()) {
                    paramJsonReader.beginObject();
                    while (paramJsonReader.hasNext()) {
                        String str = paramJsonReader.nextName();
                        if ("id".equalsIgnoreCase(str))
                            this.id = paramJsonReader.nextString();
                        else if ("pictures".equalsIgnoreCase(str))
                            this.pictures_img = readPictureImg(paramJsonReader);
                        else if ("name".equalsIgnoreCase(str))
                            this.name = paramJsonReader.nextString();
                        else if ("done".equalsIgnoreCase(str))
                            this.done = paramJsonReader.nextString();
                        else if ("my_task_id".equalsIgnoreCase(str))
                            this.my_task_id = paramJsonReader.nextString();
                        else if ("bamboo".equalsIgnoreCase(str))
                            this.bamboo = paramJsonReader.nextString();
                        else if ("desc".equalsIgnoreCase(str))
                            this.desc = paramJsonReader.nextString();
                        else if ("type".equalsIgnoreCase(str))
                            this.type = paramJsonReader.nextString();
                        else if ("priority".equalsIgnoreCase(str))
                            this.priority = paramJsonReader.nextString();
                        else if ("interval".equalsIgnoreCase(str))
                            this.interval = Integer.valueOf(paramJsonReader.nextString()).intValue();
                        else
                            paramJsonReader.skipValue();
                    }
                    if (!this.done.equalsIgnoreCase("3"))
                        return;
                    resetAllData();
                    paramJsonReader.endObject();
                }
                paramJsonReader.endArray();
            } else {
                paramJsonReader.skipValue();
            }
        paramJsonReader.endObject();
    }

    protected String readPictureImg(JsonReader reader) throws IOException {
        reader.beginObject();
        String img = "";
        if (reader.hasNext() && reader.nextName().equalsIgnoreCase("img")) {
            img = reader.nextString();
        }
        reader.endObject();
        return img;
    }
}