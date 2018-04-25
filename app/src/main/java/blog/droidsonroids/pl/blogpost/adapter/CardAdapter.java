package blog.droidsonroids.pl.blogpost.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import blog.droidsonroids.pl.blogpost.viewmodel.CardViewModel;

@Deprecated
public class CardAdapter extends BasePageAdapter<CardViewModel> {
    public CardAdapter(Context context, List<CardViewModel> values) {
        super(context, values);
    }

    @Override
    protected View createPageView() {
        return null;
    }

    @Override
    protected void bindView(ViewGroup container, int position, View view, CardViewModel item) {

    }
}
