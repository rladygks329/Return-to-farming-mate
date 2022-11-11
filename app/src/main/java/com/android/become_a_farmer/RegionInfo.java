package com.android.become_a_farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RatingBar;

import com.android.become_a_farmer.databinding.ActivityRegionInfoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegionInfo extends AppCompatActivity {
    private ActivityRegionInfoBinding binding;
    private String URL = "https://farming-mate.web.app";
    private FirebaseFirestore db;
    private String regionName;
    private float regionRating;
    private boolean changed = false;
    private RecyclerItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegionInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        item = (RecyclerItem) intent.getSerializableExtra("item");

        binding.regionInfoTitle.setText(item.getTitle());
        binding.regionInfoWebview.getSettings().setJavaScriptEnabled(true);
        binding.regionInfoWebview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                binding.regionInfoWebview.loadUrl("javascript:display(\""
                        + item.getTitle().split(" ")[1] + "\")");
            }
        });
        binding.regionInfoWebview.clearCache(true);
        binding.regionInfoWebview.loadUrl(URL);

        //binding.regionInfoWebview.loadUrl("javascript:display(\"" +
         //       item.getTitle().split(" ")[1] + "\")");
        binding.regionInfoWebview.setClickable(false);

        RegionInfoViewPagerAdapter adapter = new RegionInfoViewPagerAdapter(item);
        binding.regionInfoViewpager.setAdapter(adapter);
        binding.dotsIndicator.attachTo(binding.regionInfoViewpager);

        regionName = item.getTitle();
        binding.regionInfoRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                regionRating = rating;
                changed = true;
            }
        });
        binding.regionInfoBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.regionInfoBtnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (changed){
            storeRating(regionName, regionRating);
        }
    }

    // 사용자가 ui에서 클릭한 rating 데이터를 forestore에 저장
    public void storeRating(String regionName, float rating){
        String email = getUserEmail();
        db = FirebaseFirestore.getInstance();

        DocumentReference ratingsRef = db.collection("ratings").document(email);
        ratingsRef.update(regionName, rating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }})
                // 사용자가 한 번도 별점을 남기지 않았을 때 -> 문서 새로 생성해야 함
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Map<String, Object> data = new HashMap<>();
                        data.put(regionName, rating);
                        db.collection("ratings").document(email).set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                        Log.w("add rating data", "Error writing document", e);
                                    }
                                });
                    }
                });
    }

    public String getUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            return user.getEmail();
        }
        return null;
    }
}