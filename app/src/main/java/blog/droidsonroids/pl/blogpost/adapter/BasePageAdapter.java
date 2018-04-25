package blog.droidsonroids.pl.blogpost.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;
/**
 * * The common {@link PagerAdapter} works with {@link android.support.v4.view.ViewPager}.
 *
 * @param <T> Item view model type
 */
public abstract class BasePageAdapter<T> extends PagerAdapter {
    public static final String TAG_CHECKED = "checked";

    protected final LayoutInflater layoutInflater;
    protected List<T> data = new LinkedList<>();

    protected int checkedPosition = -1;
    private Context context;

    public BasePageAdapter(Context context, List<T> values) {
        this.context = context;

        preInitItems();
        data.addAll(values);

        layoutInflater = LayoutInflater.from(context);
    }

    protected void preInitItems() {}

    public BasePageAdapter setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();

        return this;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = createPageView();
        T item = data.get(position);

        bindView(container, position, view, item);

        container.addView(view);
        return view;
    }

    protected abstract View createPageView();

    protected abstract void bindView(ViewGroup container, int position, View view, T item);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
