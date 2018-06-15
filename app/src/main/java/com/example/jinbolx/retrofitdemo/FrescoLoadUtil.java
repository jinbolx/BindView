package com.example.jinbolx.retrofitdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class FrescoLoadUtil {
    @FrescoLoadAnnotation(url = "url")
    String url;
    public static void loadDecodedImage(final SimpleDraweeView fbImageView, String url,
            final Context context) {
        loadDecodedImage(fbImageView, url, context, ScaleType.FIT_XY);
    }

    public static void loadDecodedImage(final SimpleDraweeView fbImageView, String url,
                                        final Context context, ScaleType type) {
        boolean isCacheExist = Fresco.getImagePipeline().isInDiskCacheSync(Uri.parse(url));
        if (!isCacheExist && (url.contains("http") || url.contains("https"))) {
            fbImageView.setImageURI(url);
        } else {
            loadUrl(fbImageView, url, context, type);
        }
    }

    public static void loadDecodedImageWithCenterType(final SimpleDraweeView fbImageView,
            String url,
            final Context context) {
        boolean isCacheExist = Fresco.getImagePipeline().isInDiskCacheSync(Uri.parse(url));
        if (!isCacheExist && (url.contains("http") || url.contains("https"))) {
            fbImageView.setImageURI(url);
        } else {
            loadUrl(fbImageView, url, context, ScaleType.FIT_CENTER);
        }
    }

    private static void loadUrl(final SimpleDraweeView fbImageView, String url,
            final Context context,
            final ScaleType scaleType) {
        loadLocalDecodedCache(url, new IResult<Bitmap>() {
            @Override
            public void onResult(Bitmap result) {
                Drawable drawable = new BitmapDrawable(context.getResources(), result);
                GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(
                        context.getResources())
                        .setPlaceholderImage(drawable, scaleType)
                        .build();
                fbImageView.setHierarchy(hierarchy);
            }
        });
    }

    private static void loadLocalDecodedCache(String url, final IResult<Bitmap> loadImageResult) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        // 获取已解码的图片，返回的是Bitmap
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline
                .fetchDecodedImage(imageRequest,
                        ImageRequest.RequestLevel.DISK_CACHE);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (!dataSource.isFinished()) {
                    return;
                }

                CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<CloseableImage> closeableReference = imageReference
                            .clone();
                    try {
                        CloseableImage closeableImage = closeableReference.get();
                        CloseableBitmap closeableBitmap = (CloseableBitmap) closeableImage;
                        Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                        if (bitmap != null && !bitmap.isRecycled()) {
                            // https://github.com/facebook/fresco/issues/648
                            final Bitmap tempBitmap = bitmap.copy(bitmap.getConfig(), false);
                            if (loadImageResult != null) {
                                loadImageResult.onResult(tempBitmap);
                            }
                        }
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (loadImageResult != null) {
                    loadImageResult.onResult(null);
                }

                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("FrescoLoadUtil", "onFailureImpl = " + throwable.toString());
                }
            }
        }, UiThreadImmediateExecutorService.getInstance());
    }

    public interface IResult<T> {

        void onResult(T result);
    }
}
