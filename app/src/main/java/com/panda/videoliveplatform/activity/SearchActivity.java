package com.panda.videoliveplatform.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.panda.videolivecore.Interface.SearchResultListener;
import com.panda.videolivecore.SharePreference.SearchHistoryStore;
import com.panda.videolivecore.data.GsonRequest;
import com.panda.videolivecore.data.ImageCacheManager;
import com.panda.videolivecore.data.RequestManager;
import com.panda.videolivecore.data.SearchItemInfo;
import com.panda.videolivecore.data.SearchRoomIdInfo;
import com.panda.videolivecore.data.SearchRoomIdInfo.ResponseData;
import com.panda.videolivecore.net.NumericUtils;
import com.panda.videolivecore.net.UrlConst;
import com.panda.videolivecore.utils.CookieContiner;
import com.panda.videolivecore.utils.ImeUtils;
import com.panda.videoliveplatform.R;
import com.panda.videoliveplatform.fragment.SearchLiveCloseFragment;
import com.panda.videoliveplatform.fragment.SearchLiveCloseFragment.OnSearchLiveCloseFragmentListener;
import com.panda.videoliveplatform.fragment.SearchLiveOpenFragment;
import com.panda.videoliveplatform.fragment.SearchLiveOpenFragment.OnSearchLiveOpenFragmentListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SearchActivity extends FragmentActivity implements OnSearchLiveOpenFragmentListener, OnSearchLiveCloseFragmentListener {
    private ArrayList<Fragment> mFragmentArray;
    private LinearLayout mHistoryListContainer;
    private LinearLayout mLayoutActivityTitle;
    private LinearLayout mLayoutPagerTitle;
    private TextView mLiveClose;
    private LinearLayout mLiveCloseLine;
    private TextView mLiveOpen;
    private LinearLayout mLiveOpenLine;
    private SearchLiveCloseFragment mLivecloseFragment;
    private SearchLiveOpenFragment mLiveopenFragment;
    private ViewPager mPager;
    private EditText mSearchEditText;
    private LinearLayout mSearchHistoryLayout;
    private SearchHistoryStore mSearchHistoryStore = new SearchHistoryStore();
    private LinearLayout mSearchResultLayout;
    private TextView mSearchRoomFanscount;
    private ImageView mSearchRoomImg;
    private LinearLayout mSearchRoomLayout;
    private TextView mSearchRoomName;
    private TextView mSearchRoomUsername;
    private boolean mSearchingClose = false;
    private boolean mSearchingOpen = false;
    private boolean mSearchingRoom = false;
    private ArrayList<String> mTitleArray;

    public class MyOnPageChangeListener implements OnPageChangeListener {
        public void onPageSelected(int arg0) {
            SearchActivity.this.selectTab(arg0);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        if (VERSION.SDK_INT >= 19) {
            findViewById(R.id.statusbar_dummy).setVisibility(0);
            Window win = getWindow();
            LayoutParams winParams = win.getAttributes();
            winParams.flags |= 67108864;
            win.setAttributes(winParams);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    public void onBackPressed() {
        if (this.mSearchHistoryLayout.getVisibility() == 0) {
            showSearchResultLayoout();
        } else {
            finish();
        }
    }

    private void initViews() {
        this.mSearchResultLayout = (LinearLayout) findViewById(R.id.search_result_layout);
        this.mSearchHistoryLayout = (LinearLayout) findViewById(R.id.search_history_layout);
        this.mHistoryListContainer = (LinearLayout) findViewById(R.id.history_list_container);
        this.mLayoutPagerTitle = (LinearLayout) findViewById(R.id.layout_viewpager_title);
        this.mLayoutActivityTitle = (LinearLayout) findViewById(R.id.title_layout);
        this.mLiveOpen = (TextView) findViewById(R.id.liveroom_chat_btn);
        this.mLiveClose = (TextView) findViewById(R.id.liveroom_presenter_btn);
        this.mLiveOpenLine = (LinearLayout) findViewById(R.id.liveroom_chat_line);
        this.mLiveCloseLine = (LinearLayout) findViewById(R.id.liveroom_presenter_line);
        this.mSearchEditText = (EditText) findViewById(R.id.searchedit);
        findViewById(R.id.image_button_back).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchActivity.this.setResult(-1, new Intent());
                SearchActivity.this.finish();
            }
        });
        this.mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    SearchActivity.this.showSearchHistoryLayoout();
                }
            }
        });
        this.mSearchEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SearchActivity.this.showSearchHistoryLayoout();
            }

            public void afterTextChanged(Editable editable) {
            }
        });
        this.mSearchEditText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId != 3) {
                    return false;
                }
                SearchActivity.this.searchKeywork(SearchActivity.this.mSearchEditText.getText().toString());
                return true;
            }
        });
        findViewById(R.id.clear_text_btn).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SearchActivity.this.mSearchEditText.setText("");
            }
        });
        findViewById(R.id.image_button_search).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchActivity.this.searchKeywork(SearchActivity.this.mSearchEditText.getText().toString());
            }
        });
        this.mPager = (ViewPager) findViewById(R.id.viewpager);
        this.mLiveopenFragment = SearchLiveOpenFragment.newInstance(this, new SearchResultListener() {
            public void SearchResult(boolean sucess) {
                SearchActivity.this.mSearchingOpen = false;
            }
        });
        this.mLivecloseFragment = SearchLiveCloseFragment.newInstance(this, new SearchResultListener() {
            public void SearchResult(boolean sucess) {
                SearchActivity.this.mSearchingClose = false;
            }
        });
        this.mFragmentArray = new ArrayList();
        this.mFragmentArray.add(this.mLiveopenFragment);
        this.mFragmentArray.add(this.mLivecloseFragment);
        this.mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            public Fragment getItem(int i) {
                return (Fragment) SearchActivity.this.mFragmentArray.get(i);
            }

            public CharSequence getPageTitle(int position) {
                return (CharSequence) SearchActivity.this.mTitleArray.get(position);
            }

            public int getCount() {
                return SearchActivity.this.mFragmentArray.size();
            }
        });
        this.mPager.setCurrentItem(0);
        this.mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        this.mPager.setOffscreenPageLimit(2);
        selectTab(0);
        this.mSearchRoomLayout = (LinearLayout) findViewById(R.id.search_room_layout);
        this.mSearchRoomImg = (ImageView) findViewById(R.id.search_room_img);
        this.mSearchRoomName = (TextView) findViewById(R.id.search_room_name);
        this.mSearchRoomUsername = (TextView) findViewById(R.id.search_room_username);
        this.mSearchRoomFanscount = (TextView) findViewById(R.id.search_room_fanscount);
        findViewById(R.id.delete_all).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final AlertDialog dlg = new AlertDialog(SearchActivity.this, SearchActivity.this.getResources().getString(R.string.search_history_clear_all), SearchActivity.this.getResources().getString(R.string.search_history_clear_yes), SearchActivity.this.getResources().getString(R.string.search_history_clear_no), AlertDialog.DEFAULT_BTN.DEFAULT_NONE);
                dlg.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        if (R.id.button_continue == dlg.GetClickType() && SearchActivity.this.mSearchHistoryStore.deleteAllKey()) {
                            SearchActivity.this.RelayoutHistoryContainer();
                            SearchActivity.this.showSearchResultLayoout();
                        }
                    }
                });
                dlg.show();
            }
        });
        RelayoutHistoryContainer();
    }

    private void RelayoutHistoryContainer() {
        this.mHistoryListContainer.removeAllViews();
        LayoutInflater li = (LayoutInflater) getSystemService("layout_inflater");
        String[] allKey = this.mSearchHistoryStore.getAllKey();
        if (allKey != null && allKey.length > 0) {
            for (int i = 0; i < allKey.length; i++) {
                final String strKey = allKey[i];
                View itemView = li.inflate(R.layout.search_history_item, null);
                ((TextView) itemView.findViewById(R.id.item_text)).setText(strKey);
                itemView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        SearchActivity.this.mSearchEditText.setText(strKey);
                        SearchActivity.this.searchKeywork(strKey);
                    }
                });
                final int index = i;
                ((ImageButton) itemView.findViewById(R.id.del_btn)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (SearchActivity.this.mSearchHistoryStore.deleteKey(index)) {
                            SearchActivity.this.RelayoutHistoryContainer();
                        }
                    }
                });
                if (i == allKey.length - 1) {
                    itemView.findViewById(R.id.line).setVisibility(8);
                }
                this.mHistoryListContainer.addView(itemView);
            }
        }
    }

    public void showSearchHistoryLayoout() {
        if (this.mSearchHistoryStore.getAllKey().length > 0) {
            this.mSearchHistoryLayout.setVisibility(0);
            this.mSearchResultLayout.setVisibility(8);
            RelayoutHistoryContainer();
        }
    }

    public void showSearchResultLayoout() {
        this.mSearchHistoryLayout.setVisibility(8);
        this.mSearchResultLayout.setVisibility(0);
    }

    private void selectTab(int id) {
        if (id >= 0 && id < this.mFragmentArray.size()) {
            ViewGroup.LayoutParams linearParams;
            switch (id) {
                case 0:
                    this.mLiveOpen.setBackgroundColor(getResources().getColor(R.color.liveroom_text_background));
                    this.mLiveOpen.setTextColor(getResources().getColor(R.color.liveroom_text_color));
                    this.mLiveClose.setBackgroundColor(getResources().getColor(R.color.liveroom_text_background_disable));
                    this.mLiveClose.setTextColor(getResources().getColor(R.color.liveroom_text_color_disable));
                    this.mLiveOpenLine.setBackgroundColor(getResources().getColor(R.color.title_color));
                    linearParams = this.mLiveOpenLine.getLayoutParams();
                    linearParams.height = (int) getResources().getDimension(R.dimen.liveroom_underline_size);
                    this.mLiveOpenLine.setLayoutParams(linearParams);
                    this.mLiveCloseLine.setBackgroundColor(getResources().getColor(R.color.liveroom_underline_color_disable));
                    linearParams = this.mLiveCloseLine.getLayoutParams();
                    linearParams.height = (int) getResources().getDimension(R.dimen.liveroom_underline_size_disable);
                    this.mLiveCloseLine.setLayoutParams(linearParams);
                    break;
                case 1:
                    this.mLiveOpen.setBackgroundColor(getResources().getColor(R.color.liveroom_text_background_disable));
                    this.mLiveOpen.setTextColor(getResources().getColor(R.color.liveroom_text_color_disable));
                    this.mLiveClose.setBackgroundColor(getResources().getColor(R.color.liveroom_text_background));
                    this.mLiveClose.setTextColor(getResources().getColor(R.color.liveroom_text_color));
                    this.mLiveOpenLine.setBackgroundColor(getResources().getColor(R.color.liveroom_underline_color_disable));
                    linearParams = this.mLiveOpenLine.getLayoutParams();
                    linearParams.height = (int) getResources().getDimension(R.dimen.liveroom_underline_size_disable);
                    this.mLiveOpenLine.setLayoutParams(linearParams);
                    this.mLiveCloseLine.setBackgroundColor(getResources().getColor(R.color.title_color));
                    linearParams = this.mLiveCloseLine.getLayoutParams();
                    linearParams.height = (int) getResources().getDimension(R.dimen.liveroom_underline_size);
                    this.mLiveCloseLine.setLayoutParams(linearParams);
                    break;
            }
            this.mLayoutPagerTitle.requestLayout();
        }
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickLiveOpen(View view) {
        this.mPager.setCurrentItem(0);
    }

    public void onClickLiveClose(View view) {
        this.mPager.setCurrentItem(1);
    }

    public void onSearchLiveOpenItemClick(SearchItemInfo info) {
        openLiveRoom("", "", info.pictures.img != null ? info.pictures.img : "", info.roomid);
    }

    public void onSearchLiveCloseItemClick(SearchItemInfo info) {
        openLiveRoom("", "", info.pictures.img != null ? info.pictures.img : "", info.roomid);
    }

    public void openLiveRoom(String addrStream, String urlRoom, String urlImage, String idRoom) {
//        Intent intent = new Intent();
//        intent.setClass(this, LiveRoomActivity.class);
//        intent.putExtra("addrStream", addrStream);
//        intent.putExtra("urlRoom", urlRoom);
//        intent.putExtra("urlImage", urlImage);
//        intent.putExtra("idRoom", idRoom);
//        startActivity(intent);
    }

    private boolean searchRoomId(String roomId) {
        boolean isRoomId = Pattern.compile("[0-9]*").matcher(roomId).matches();
        try {
            long roomNo = Long.parseLong(roomId);
            if (!isRoomId || roomNo <= 0) {
                this.mSearchRoomLayout.setVisibility(8);
                return false;
            }
            executeRequest(new GsonRequest(0, getUrl(roomId), ResponseData.class, CookieContiner.getCookieHeaderMap(), new Listener<ResponseData>() {
                public void onResponse(ResponseData response) {
                    if (response.errno == 0) {
                        int total = 0;
                        try {
                            total = Integer.parseInt(response.data.total);
                        } catch (NumberFormatException e) {
                        }
                        if (total > 0) {
                            SearchActivity.this.mSearchRoomLayout.setVisibility(0);
                            final SearchRoomIdInfo item = (SearchRoomIdInfo) response.data.items.get(0);
                            SearchActivity.this.mSearchRoomLayout.setOnClickListener(new OnClickListener() {
                                public void onClick(View view) {
                                    SearchActivity.this.openLiveRoom("", "", item.pictures.img != null ? item.pictures.img : "", item.roomid);
                                }
                            });
                            if (item.pictures.img != null) {
                                Drawable defaultDrawablem = SearchActivity.this.getResources().getDrawable(R.drawable.defaultlivebg);
                                ImageCacheManager.loadImage(item.pictures.img, ImageCacheManager.getImageListener(SearchActivity.this.mSearchRoomImg, defaultDrawablem, defaultDrawablem));
                            }
                            SearchActivity.this.mSearchRoomName.setText(item.name);
                            SearchActivity.this.mSearchRoomUsername.setText(item.nickname);
                            SearchActivity.this.mSearchRoomFanscount.setText(NumericUtils.getLivePeople(item.person_num));
                        } else {
                            SearchActivity.this.mSearchRoomLayout.setVisibility(8);
                        }
                        SearchActivity.this.mSearchingRoom = false;
                    }
                }
            }, new ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    SearchActivity.this.mSearchRoomLayout.setVisibility(8);
                    SearchActivity.this.mSearchingRoom = false;
                }
            }));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.mSearchRoomLayout.setVisibility(8);
            return false;
        }
    }

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }

    protected String getUrl(String roomId) {
        return UrlConst.getSearchRoomIdUrl(roomId);
    }

    protected void searchKeywork(String text) {
        if (!TextUtils.isEmpty(text) && !this.mSearchingRoom && !this.mSearchingOpen && !this.mSearchingClose) {
            showSearchResultLayoout();
            this.mSearchingRoom = searchRoomId(text);
            this.mSearchingOpen = this.mLiveopenFragment.search(text);
            this.mSearchingClose = this.mLivecloseFragment.search(text);
            this.mSearchHistoryStore.addKey(text);
            ImeUtils.hideSoftInputBox(this);
        }
    }
}
