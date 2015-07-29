package com.architjn.materialicons.ui.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architjn.materialicons.R;
import com.architjn.materialicons.adapters.IconsAdapter;
import com.architjn.materialicons.others.SpacesItemDecoration;

/**
 * Created by architjn on 28/07/15.
 */
public class IconFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.icon_fragment, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int numOfRows = (int) (size.x / getResources().getDimension(R.dimen.size_of_grid_item));
        RecyclerView gridview = (RecyclerView) view.findViewById(R.id.icons_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), numOfRows);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        gridview.setLayoutManager(layoutManager);
        gridview.setHasFixedSize(true);
        gridview.addItemDecoration(new SpacesItemDecoration(10, 5));
        gridview.setAdapter(new IconsAdapter(view.getContext(),
                getArguments().getInt("iconsArrayId", 0)));
        return view;
    }

//    public int dpToPx(Context context, int dp) {
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
//        return px;
//    }

}
