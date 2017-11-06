package com.panda.videoliveplatform.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.panda.videoliveplatform.R;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"InflateParams"})
public class FacePageFragment extends Fragment {
    public static final String ARG_FACE_DATA = "ARG_FACE_DATA";
    public static final String ARG_POSITION = "position";
    Activity activity;
    private List<String> data;
    List<View> faceGridViewList;
    private ViewPager faceViewPager;
    private OnOperationListener onOperationListener;
    private LinearLayout pagePointLayout;
    List<ImageView> pointViews;
    private int position;

    public class FacePagerAdapter extends PagerAdapter {
        private List<View> gridViewList;

        public FacePagerAdapter(List<View> gridViewList) {
            this.gridViewList = gridViewList;
        }

        public int getCount() {
            return this.gridViewList.size();
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) this.gridViewList.get(arg1));
        }

        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView((View) this.gridViewList.get(arg1));
            return this.gridViewList.get(arg1);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        this.position = getArguments().getInt(ARG_POSITION);
        this.data = getArguments().getStringArrayList(ARG_FACE_DATA);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.faceGridViewList = new ArrayList();
        this.pointViews = new ArrayList();
        View rootView = inflater.inflate(R.layout.face_fragment, null);
        this.faceViewPager = (ViewPager) rootView.findViewById(R.id.faceViewPager);
        this.pagePointLayout = (LinearLayout) rootView.findViewById(R.id.pagePointLayout);
        int x = 0;
        while (true) {
            if (x < (this.data.size() % 12 == 0 ? this.data.size() / 12 : (this.data.size() / 12) + 1)) {
                int size;
                GridView view = new GridView(this.activity);
                Context context = this.activity;
                List list = this.data;
                int i = x * 12;
                if ((x + 1) * 12 > this.data.size()) {
                    size = this.data.size();
                } else {
                    size = (x + 1) * 12;
                }
                view.setAdapter(new FaceAdapter(context, list.subList(i, size)));
                view.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        if (FacePageFragment.this.onOperationListener != null) {
                            FacePageFragment.this.onOperationListener.selectedFace((String) FacePageFragment.this.data.get(position));
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
                this.faceGridViewList.add(view);
                ImageView imageView = new ImageView(this.activity);
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
                this.faceViewPager.setAdapter(new FacePagerAdapter(this.faceGridViewList));
                this.faceViewPager.setOnPageChangeListener(new OnPageChangeListener() {
                    public void onPageSelected(int index) {
                        for (int i = 0; i < FacePageFragment.this.pointViews.size(); i++) {
                            if (index == i) {
                                ((ImageView) FacePageFragment.this.pointViews.get(i)).setBackgroundResource(R.drawable.point_selected);
                            } else {
                                ((ImageView) FacePageFragment.this.pointViews.get(i)).setBackgroundResource(R.drawable.point_normal);
                            }
                        }
                    }

                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    public void onPageScrollStateChanged(int arg0) {
                    }
                });
                return rootView;
            }
        }
    }

    public OnOperationListener getOnOperationListener() {
        return this.onOperationListener;
    }

    public void setOnOperationListener(OnOperationListener onOperationListener) {
        this.onOperationListener = onOperationListener;
    }
}
