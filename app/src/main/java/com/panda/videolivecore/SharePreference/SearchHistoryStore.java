package com.panda.videolivecore.SharePreference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.panda.videolivecore.CoreApplication;
import java.util.ArrayList;
import java.util.Iterator;

public class SearchHistoryStore {
    private final int MAX_SIZE;
    private final String PREFER_KEY;
    private SharedPreferences mPreferences;
    private ArrayList<String> mSearchHistory;

    public class SearchKey {
        public ArrayList<String> items;
    }

    public SearchHistoryStore() {
        this.PREFER_KEY = "SEARCH_HISTORY_KEYWORD";
        this.MAX_SIZE = 8;
        this.mSearchHistory = new ArrayList();
        this.mPreferences = null;
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(CoreApplication.getInstance().getApplication());
        loadPreference();
    }

    public String[] getAllKey() {
        String[] allKey = new String[this.mSearchHistory.size()];
        for (int i = 0; i < this.mSearchHistory.size(); i++) {
            allKey[i] = new String((String) this.mSearchHistory.get(i));
        }
        return allKey;
    }

    public boolean addKey(String key) {
        boolean bRetValue = false;
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        try {
            int index = findKey(key);
            if (index != -1) {
                this.mSearchHistory.remove(index);
            }
            while (this.mSearchHistory.size() >= 8) {
                this.mSearchHistory.remove(this.mSearchHistory.size() - 1);
            }
            this.mSearchHistory.add(0, key);
            storeValueData();
            bRetValue = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bRetValue;
    }

    public boolean deleteKey(String key) {
        boolean bRetValue = false;
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        try {
            int index = findKey(key);
            if (index != -1) {
                this.mSearchHistory.remove(index);
                storeValueData();
                bRetValue = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bRetValue;
    }

    public boolean deleteKey(int index) {
        if (index < 0) {
            return false;
        }
        try {
            if (index >= this.mSearchHistory.size()) {
                return false;
            }
            this.mSearchHistory.remove(index);
            storeValueData();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllKey() {
        try {
            this.mSearchHistory.clear();
            storeValueData();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int findKey(String key) {
        for (int i = 0; i < this.mSearchHistory.size(); i++) {
            if (((String) this.mSearchHistory.get(i)).equalsIgnoreCase(key)) {
                return i;
            }
        }
        return -1;
    }

    public void loadPreference() {
        try {
            String valueStr = this.mPreferences.getString("SEARCH_HISTORY_KEYWORD", "");
            this.mSearchHistory.clear();
            parseValueData(valueStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseValueData(String default_value) {
        if (!TextUtils.isEmpty(default_value)) {
            SearchKey storeData = (SearchKey) new Gson().fromJson(default_value, SearchKey.class);
            if (storeData != null && storeData.items != null) {
                Iterator i$ = storeData.items.iterator();
                while (i$.hasNext()) {
                    this.mSearchHistory.add((String) i$.next());
                }
            }
        }
    }

    private void storeValueData() {
//        Object storeData = new SearchKey();
//        storeData.items = this.mSearchHistory;
//        String gsonText = new Gson().toJson(storeData);
//        Editor editor = this.mPreferences.edit();
//        editor.putString("SEARCH_HISTORY_KEYWORD", gsonText);
//        editor.commit();
    }
}
