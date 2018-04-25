package blog.droidsonroids.pl.blogpost.animation;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * * Custom Page Transformer.
 *
 * <p>Code modified from https://github.com/xmuSistone/ViewpagerTransition
 */
public class CustomPagerTransformer implements ViewPager.PageTransformer {

    public static final float SCALE_FACTOR = 0.15f;
    private int maxTranslateOffsetX;
    private ViewPager viewPager;

    public CustomPagerTransformer(Context context) {
        this.maxTranslateOffsetX = dp2px(context, 180);
    }

    public void transformPage(View view, float position) {
        if (viewPager == null) {
            viewPager = (ViewPager) view.getParent();
        }

        int leftInScreen = view.getLeft() - viewPager.getScrollX();
        int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;

        int measuredPagerWidth = viewPager.getMeasuredWidth();
        int offsetX = centerXInViewPager - measuredPagerWidth / 2;
        float offsetRate = (float) offsetX * SCALE_FACTOR / measuredPagerWidth;

        float scaleFactor = 1 - Math.abs(offsetRate);
        // Timber.d(">>> scaleFactor : %.2f | pos : %.2f", scaleFactor, position);
        if (scaleFactor > 0) {
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setTranslationX(-maxTranslateOffsetX * offsetRate);
        }
    }

    private int dp2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }
}
