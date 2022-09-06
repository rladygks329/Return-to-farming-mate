package com.android.become_a_farmer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.RatingBar;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.become_a_farmer.databinding.FragmentRegionInfoBinding;

public class FragmentRegionInfo extends Fragment {
    private FragmentRegionInfoBinding binding;
    private RecyclerItem item;
    private String URL = "https://map.naver.com/v5/search/";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegionInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        item = (RecyclerItem) getArguments().getSerializable("item");

        binding.regionInfoTitle.setText(item.getTitle());
        binding.regionInfoWebview.getSettings().setJavaScriptEnabled(true);
        binding.regionInfoWebview.setWebViewClient(new WebViewClient());
        binding.regionInfoWebview.clearCache(true);
        binding.regionInfoWebview.loadUrl(URL + item.getTitle());
        binding.regionInfoWebview.setClickable(false);
        /*
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        // add your fragments
        adapter.addFrag(new SampleFragment(), "Tab1");
        adapter.addFrag(new SampleFragment2(), "Tab2");
        adapter.addFrag(new SampleFragment3(), "Tab3");

        // set adapter on viewpager
        viewPager.setAdapter(adapter);
        * */
        binding.regionInfoRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        binding.regionInfoBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.regionInfoBtnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        //프레그먼트의 생명주기 때문에 메모리 누수 방지용 코드
        super.onDestroyView();
        binding = null;
    }
}
