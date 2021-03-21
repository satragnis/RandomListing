package com.satragni.randomlist.listDetails;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.satragni.randomlist.ImageDetailBinding;
import com.satragni.randomlist.MainActivity;
import com.satragni.randomlist.base.BaseFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class ImageDetailFragment extends BaseFragment {

    private NavController navController;

    @Inject
    ViewModelProvider.Factory factory;
    private ImageDetailBinding imageDetailBinding;
    private ImageDetailViewModel imageDetailViewModel;
    private String imageUrl = "";
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        imageDetailBinding = ImageDetailBinding.inflate(inflater, container, false);
        return imageDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageDetailViewModel = new ViewModelProvider(this, factory).get(ImageDetailViewModel.class);
        imageDetailBinding.setViewModel(imageDetailViewModel);
        mContext = getContext();
        navController = NavHostFragment.findNavController(this);
        String title = "";

        if (getArguments() != null) {
            imageUrl = getArguments().getString("imageUrl");
            String imageId = getArguments().getString("imageId");
            title = getArguments().getString("title");
            imageDetailViewModel.setImageData(imageId, imageUrl);
        }
        setupActionbar(title);
        setPostCommentButtonListener();
        setShareImageListener();
        setupCommentList();
        hideKeyboard(imageDetailBinding.commentEditText);
    }

    private void setupCommentList() {
        CommentsAdapter commentsAdapter = new CommentsAdapter();
        imageDetailBinding.commentsRecyclerView.setAdapter(commentsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        imageDetailBinding.commentsRecyclerView.setLayoutManager(linearLayoutManager);


        imageDetailBinding.commentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // only when scrolling up
                    int visibleThreshold = 8;
                    int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int currentTotalCount = linearLayoutManager.getItemCount();
                    if (currentTotalCount <= lastItem + visibleThreshold) {
                        // load more called
                        imageDetailViewModel.getCommentsFromDb();
                    }
                }
            }
        });

        imageDetailViewModel.getComments().observe(getViewLifecycleOwner(), commentResponses -> {
            linearLayoutManager.scrollToPosition(0);
            commentsAdapter.setComments(commentResponses);
        });


    }

    private void setupActionbar(String title) {
        ((MainActivity) requireActivity()).setSupportActionBar(imageDetailBinding.toolbarDetail);
        ActionBar supportActionBar = ((MainActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        imageDetailBinding.toolbarDetail.setNavigationOnClickListener(v -> navController.navigateUp());

        imageDetailBinding.toolbarDetail.setTitle(title);
    }

    private void setPostCommentButtonListener() {
        imageDetailBinding.postCommentButton.setOnClickListener(v -> {
            imageDetailViewModel.postComment(
                    imageDetailBinding.commentEditText.getText()
                            .toString()
                            .trim()
            );
            //clear comment box once it is posted
            imageDetailBinding.commentEditText.setText("");
        });
    }

    private void setShareImageListener() {
        imageDetailBinding.shareImage.setOnClickListener(v -> {
            checkPermissionBeforeShare();
        });
    }


    void checkPermissionBeforeShare(){
        Dexter.withContext(mContext)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(report.areAllPermissionsGranted()){
                    shareImage();
                }
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
    }

    private void shareImage(){
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            // Use methods on Context to access package-specific directories on external storage.
                            // This way, you don't need to request external read/write permission.
                            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
                            File file =  new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share" + System.currentTimeMillis() + ".png");
                            FileOutputStream out = new FileOutputStream(file);
                            resource.compress(Bitmap.CompressFormat.PNG, 90, out);
                            out.close();
                            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
                            Uri bmpUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);

                            if (bmpUri != null) {
                                // Construct a ShareIntent with link to image
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                shareIntent.setType("image/*");
                                // Launch sharing dialog for image
                                startActivity(Intent.createChooser(shareIntent, "Share Image"));
                            } else {
                                // ...sharing failed, handle error
                                Log.d(">>>>", "onShareItem: fail");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void hideKeyboard(EditText commentEditText) {
        try {
            // use application level context to avoid unnecessary leaks.
            InputMethodManager inputManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(commentEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
