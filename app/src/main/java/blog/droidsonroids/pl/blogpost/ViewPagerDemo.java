package blog.droidsonroids.pl.blogpost;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import blog.droidsonroids.pl.blogpost.adapter.BasePageAdapter;
import blog.droidsonroids.pl.blogpost.animation.CustomPagerTransformer;
import blog.droidsonroids.pl.blogpost.viewmodel.CardViewModel;

public class ViewPagerDemo extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_layout);

        initView();
    }

    private void initView() {
        int pageSpace = getResources().getDimensionPixelSize(R.dimen.page_space_horizontal);

        // prepare the mock data set
        List<CardViewModel> items = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            items.add(new CardViewModel());
        }

        viewPager = findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(pageSpace);

        viewPager.setAdapter(new MyCardAdapter(this, items));

        viewPager.setPageTransformer(true, new CustomPagerTransformer(this));
    }

    class MyCardAdapter extends BasePageAdapter<CardViewModel> {
        public static final int DISTANCE = 8000;
        public static final String TAG_FLIPPED = "tag_flipped";
        private final float scale;

        private SparseArray<View> cachedViews = new SparseArray<>();
        private int flippedPos = -1;

        public MyCardAdapter(Context context, List values) {
            super(context, values);

            scale = getResources().getDisplayMetrics().density * DISTANCE;
        }

        private void changeCameraDistance(ViewGroup frontLayout, ViewGroup backLayout) {
            frontLayout.setCameraDistance(scale);
            backLayout.setCameraDistance(scale);
        }

        @Override
        protected View createPageView() {
            return layoutInflater.inflate(R.layout.view_item, null);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            cachedViews.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        protected void bindView(
                final ViewGroup container,
                final int position,
                View view,
                final CardViewModel item) {
            cachedViews.put(position, view);

            ViewGroup outFrontLayout = view.findViewById(R.id.front_layout);
            ViewGroup outBackLayout = view.findViewById(R.id.back_layout);

            // for new created item
            if (item.isBackLayoutVisible()) {
                if (cachedViews.get(position) != null) {
                    outBackLayout.bringToFront();
                    view.setTag(TAG_FLIPPED);
                } else {
                    // removed from adapter reused views before, treated as a new item
                    outFrontLayout.bringToFront();
                }
            } else {
                outFrontLayout.bringToFront();
            }

            view.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ViewGroup frontLayout = v.findViewById(R.id.front_layout);
                            ViewGroup backLayout = v.findViewById(R.id.back_layout);

                            changeCameraDistance(frontLayout, backLayout);

                            if (v.getTag() == null) {
                                final int lastFlippedPos = flippedPos;
                                flippedPos = position;

                                item.setBackLayoutVisible(true);
                                applyRotation(position, frontLayout, backLayout);

                                View lastFlippedView = container.findViewWithTag(TAG_FLIPPED);
                                if (lastFlippedView != null) {
                                    if (lastFlippedView != v) {
                                        lastFlippedView.callOnClick();
                                    }
                                }

                                if (lastFlippedPos >= 0)
                                    data.get(lastFlippedPos).setBackLayoutVisible(false);

                                v.setTag(TAG_FLIPPED);
                            } else {
                                item.setBackLayoutVisible(false);
                                applyRotation(-1, frontLayout, backLayout);

                                v.setTag(null);
                            }
                        }
                    });
        }

        /**
         * Setup a new 3D rotation on the item view.
         *
         * @param position the item that was clicked to show the back layout, or -1 to show the
         *     front layout
         */
        private void applyRotation(int position, ViewGroup frontLayout, ViewGroup backLayout) {
            AnimatorSet mSetRightOut;
            AnimatorSet mSetLeftIn;

            Context context = ViewPagerDemo.this;
            mSetRightOut =
                    (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.out_animation);
            mSetLeftIn =
                    (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.in_animation);

            if (position >= 0) {
                mSetRightOut.setTarget(frontLayout);
                mSetLeftIn.setTarget(backLayout);
                mSetRightOut.playSequentially(mSetLeftIn);
                mSetRightOut.start();

            } else {
                mSetRightOut.setTarget(backLayout);
                mSetLeftIn.setTarget(frontLayout);
                mSetRightOut.playSequentially(mSetLeftIn);
                mSetRightOut.start();
            }
        }
    }
}
