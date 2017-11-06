package com.panda.videoliveplatform.chat;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import com.panda.videolivecore.utils.ImeUtils;
import com.panda.videoliveplatform.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageInputToolBox extends RelativeLayout {
    private static RelativeLayout faceLayout;
    private RelativeLayout bottomHideLayout;
    private Context context;
    private Button faceButton;
    FaceCategroyAdapter faceCategroyAdapter;
    private PagerSlidingTabStrip faceCategroyTabs;
    private ViewPager faceCategroyViewPager;
    private Map<Integer, ArrayList<String>> faceData;
    private FragmentManager fragmentManager;
    private ViewPager fuctionViewPager;
    private List<Option> functionData;
    List<View> functionGridViewList;
    private ImageButton giftSwitchBtn;
    private EmoticonsEditText messageEditText;
    private LinearLayout moreTypeLayout;
    private OnOperationListener onOperationListener;
    private LinearLayout pagePointLayout;
    List<ImageView> pointViews;
    private Button sendButton;

    public MessageInputToolBox(Context context) {
        super(context);
        this.context = context;
        this.fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        LayoutInflater.from(context).inflate(R.layout.message_input_tool_box, this);
    }

    public MessageInputToolBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        LayoutInflater.from(context).inflate(R.layout.message_input_tool_box, this);
    }

    public MessageInputToolBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        LayoutInflater.from(context).inflate(R.layout.message_input_tool_box, this);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        this.messageEditText = (EmoticonsEditText) findViewById(R.id.messageEditText);
        this.sendButton = (Button) findViewById(R.id.sendButton);
        this.giftSwitchBtn = (ImageButton) findViewById(R.id.gift_switch);
        this.bottomHideLayout = (RelativeLayout) findViewById(R.id.bottomHideLayout);
        this.faceButton = (Button) findViewById(R.id.faceButton);
        this.moreTypeLayout = (LinearLayout) findViewById(R.id.moreTypeLayout);
        faceLayout = (RelativeLayout) findViewById(R.id.faceLayout);
        this.faceCategroyViewPager = (ViewPager) findViewById(R.id.faceCategroyViewPager);
        this.faceCategroyTabs = (PagerSlidingTabStrip) findViewById(R.id.faceCategroyTabs);
        this.fuctionViewPager = (ViewPager) findViewById(R.id.fuctionViewPager);
        this.pagePointLayout = (LinearLayout) findViewById(R.id.pagePointLayout);
        this.fuctionViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int arg0) {
                for (int i = 0; i < MessageInputToolBox.this.pointViews.size(); i++) {
                    if (arg0 == i) {
                        ((ImageView) MessageInputToolBox.this.pointViews.get(i)).setBackgroundResource(R.drawable.point_selected);
                    } else {
                        ((ImageView) MessageInputToolBox.this.pointViews.get(i)).setBackgroundResource(R.drawable.point_normal);
                    }
                }
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
        this.faceCategroyAdapter = new FaceCategroyAdapter(this.fragmentManager);
        this.sendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MessageInputToolBox.this.sendMessage();
            }
        });
        this.giftSwitchBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MessageInputToolBox.this.onOperationListener != null) {
                    MessageInputToolBox.this.onOperationListener.SelectGift();
                }
            }
        });
        this.faceButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MessageInputToolBox.faceLayout.getVisibility() == 0) {
                    MessageInputToolBox.this.hideFaceLayout();
                    ImeUtils.showSoftInputBox(MessageInputToolBox.this.context);
                    return;
                }
                MessageInputToolBox.this.showFaceLayout();
            }
        });
        this.messageEditText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MessageInputToolBox.this.hideFaceLayout();
                if (MessageInputToolBox.this.onOperationListener != null) {
                    MessageInputToolBox.this.onOperationListener.hideGiftLayout();
                }
            }
        });
        this.messageEditText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null) {
                    MessageInputToolBox.this.sendMessage();
                } else if (!event.isShiftPressed()) {
                    MessageInputToolBox.this.sendMessage();
                }
                return false;
            }
        });
        this.messageEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || "".equals(s.toString().trim())) {
                    MessageInputToolBox.this.sendButton.setVisibility(8);
                    MessageInputToolBox.this.giftSwitchBtn.setVisibility(0);
                    return;
                }
                MessageInputToolBox.this.sendButton.setVisibility(0);
                MessageInputToolBox.this.giftSwitchBtn.setVisibility(8);
                MessageInputToolBox.this.onOperationListener.hideGiftLayout();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    public EditText getEditText() {
        return this.messageEditText;
    }

    public void showFaceLayout() {
        ImeUtils.hideSoftInputBox(this.context);
        postDelayed(new Runnable() {
            public void run() {
                MessageInputToolBox.this.moreTypeLayout.setVisibility(8);
                MessageInputToolBox.faceLayout.setVisibility(0);
                MessageInputToolBox.this.bottomHideLayout.setVisibility(0);
            }
        }, 50);
    }

    public void hideFaceLayout() {
        this.moreTypeLayout.setVisibility(8);
        faceLayout.setVisibility(8);
        this.bottomHideLayout.setVisibility(8);
    }

    public void hide() {
        faceLayout.setVisibility(8);
        this.bottomHideLayout.setVisibility(8);
        ImeUtils.hideSoftInputBox(this.context);
    }

    private void sendMessage() {
        if ("".equals(this.messageEditText.getText().toString().trim())) {
            Toast.makeText(this.context, R.string.chat_send_empty_message, 0).show();
        } else if (this.onOperationListener != null) {
            String content = this.messageEditText.getText().toString().trim();
            ImeUtils.hideSoftInputBox(this.context);
            boolean bSend = this.onOperationListener.send(content);
            this.messageEditText.setText("");
        }
    }

    public OnOperationListener getOnOperationListener() {
        return this.onOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.onOperationListener = onOperationListener;
        this.faceCategroyAdapter.setOnOperationListener(onOperationListener);
    }

    public Map<Integer, ArrayList<String>> getFaceData() {
        return this.faceData;
    }

    public void setFaceData(Map<Integer, ArrayList<String>> faceData) {
        this.faceData = faceData;
        this.faceCategroyAdapter.setData(faceData);
        this.faceCategroyViewPager.setAdapter(this.faceCategroyAdapter);
        this.faceCategroyTabs.setViewPager(this.faceCategroyViewPager);
        if (faceData.size() < 2) {
            this.faceCategroyTabs.setVisibility(8);
        }
    }

    public void setEmoticonList(ArrayList<EmoticonBean> emoticonBeanList) {
        this.messageEditText.InitEmoticons(emoticonBeanList);
    }

    public List<Option> getFunctionData() {
        return this.functionData;
    }

    public void setFunctionData(List<Option> functionData) {
        this.functionData = functionData;
        this.pointViews = new ArrayList();
        this.functionGridViewList = new ArrayList();
        int x = 0;
        while (true) {
            if (x < (functionData.size() % 8 == 0 ? functionData.size() / 8 : (functionData.size() / 8) + 1)) {
                int size;
                GridView view = new GridView(this.context);
                Context context = this.context;
                int i = x * 8;
                if ((x + 1) * 8 > functionData.size()) {
                    size = functionData.size();
                } else {
                    size = (x + 1) * 8;
                }
                view.setAdapter(new FunctionAdapter(context, functionData.subList(i, size)));
                view.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        if (MessageInputToolBox.this.onOperationListener != null) {
                            MessageInputToolBox.this.onOperationListener.selectedFuncation(position);
                        }
                    }
                });
                view.setNumColumns(4);
                view.setBackgroundColor(0);
                view.setHorizontalSpacing(1);
                view.setVerticalSpacing(1);
                view.setStretchMode(2);
                view.setCacheColorHint(0);
                view.setPadding(5, 0, 5, 0);
                view.setSelector(new ColorDrawable(0));
                view.setLayoutParams(new LayoutParams(-1, -2));
                view.setGravity(17);
                this.functionGridViewList.add(view);
                ImageView imageView = new ImageView(this.context);
                imageView.setBackgroundResource(R.drawable.point_normal);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(-2, -2));
                layoutParams.leftMargin = 10;
                layoutParams.rightMargin = 10;
                layoutParams.width = 8;
                layoutParams.height = 8;
                this.pagePointLayout.addView(imageView, layoutParams);
                if (x == 0) {
                    imageView.setBackgroundResource(R.drawable.point_selected);
                }
                this.pointViews.add(imageView);
                x++;
            } else {
                this.fuctionViewPager.setAdapter(new PagerAdapter() {
                    public int getCount() {
                        return MessageInputToolBox.this.functionGridViewList.size();
                    }

                    public boolean isViewFromObject(View arg0, Object arg1) {
                        return arg0 == arg1;
                    }

                    public int getItemPosition(Object object) {
                        return super.getItemPosition(object);
                    }

                    public void destroyItem(View arg0, int arg1, Object arg2) {
                        System.out.println(arg0 + "  " + arg1 + "  " + arg2);
                        ((ViewPager) arg0).removeView((View) MessageInputToolBox.this.functionGridViewList.get(arg1));
                    }

                    public Object instantiateItem(View arg0, int arg1) {
                        ((ViewPager) arg0).addView((View) MessageInputToolBox.this.functionGridViewList.get(arg1));
                        return MessageInputToolBox.this.functionGridViewList.get(arg1);
                    }
                });
                return;
            }
        }
    }
}
