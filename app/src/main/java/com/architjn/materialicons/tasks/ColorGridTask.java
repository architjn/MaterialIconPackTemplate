package com.architjn.materialicons.tasks;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;

import com.afollestad.async.Action;
import com.architjn.materialicons.adapters.WallAdapter;

/**
 * Created by architjn on 26/06/15.
 */
public class ColorGridTask extends Action {

    private Context context;
    private Bitmap art;
    WallAdapter.SimpleItemViewHolder holder;
    private ValueAnimator colorAnimation;

    public ColorGridTask(Context context, Bitmap art, WallAdapter.SimpleItemViewHolder holder) {
        this.context = context;
        this.art = art;
        this.holder = holder;
    }

    @NonNull
    @Override
    public String id() {
        return ColorGridTask.class.getName();
    }

    @Nullable
    @Override
    protected Object run() throws InterruptedException {
        Palette.from(art).generate(
                new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(final Palette palette) {
                        Integer colorFrom = ContextCompat.getColor(context, android.R.color.white);
                        Integer colorTo = palette.getVibrantColor(ContextCompat.getColor(context, android.R.color.white));
                        colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                holder.realBackground.setBackgroundColor((Integer) animator.getAnimatedValue());
                            }

                        });
                        colorAnimation.start();
                        try {
                            Integer colorFrom1 = holder.name.getCurrentTextColor();
                            Integer colorTo1 = palette.getVibrantSwatch().getBodyTextColor();
                            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom1, colorTo1);
                            colorAnimation.setDuration(800);
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    holder.name.setTextColor((Integer) animator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();
                            Integer colorFrom2 = holder.author.getCurrentTextColor();
                            Integer colorTo2 = palette.getVibrantSwatch().getTitleTextColor();
                            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom2, colorTo2);
                            colorAnimation.setDuration(800);
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    holder.author.setTextColor((Integer) animator.getAnimatedValue());
                                }
                            });
                            colorAnimation.start();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return null;
    }
}
