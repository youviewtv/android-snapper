/*
 * The MIT License (MIT)
 * Copyright (c) 2015 YouView TV Limited.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.youview.centresnaprecyclerview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>Abstract implementation of a {@link RecyclerView} that provides <i>most</i> of the work for
 * a centre-aligned and centre-snapping {@code RecyclerView}. Sub-classes must implement
 * {@link #getChildWidth()} to provide the width of child {@code View}s, as the behaviours
 * that make centring possible are very measurement-aware.</p>
 */
public abstract class CentreSnapRecyclerView extends RecyclerView {
    CentreScrollingLinearLayoutManager mLayoutManager;

    // State variables
    private int mMeasuredWidth;
    private boolean mMeasurementsValid;

    public CentreSnapRecyclerView(Context context) {
        super(context);
        init();
    }

    public CentreSnapRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CentreSnapRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * <p>Sets up initial state of this {@code RecyclerView}.</p>
     */
    protected void init() {
        mLayoutManager = new CentreScrollingLinearLayoutManager(getContext(), HORIZONTAL, false);
        setLayoutManager(mLayoutManager);
        addOnScrollListener(new CentreSnapScrollListener());

        ItemDecoration defaultItemDecoration = getDefaultItemDecoration();
        if (defaultItemDecoration != null) {
            addItemDecoration(getDefaultItemDecoration());
        }
    }

    /**
     * <p>Implementation-specific method to retrieve the width of each child shown in this
     * {@link RecyclerView}. Fixed-width works best, but if there is a marginal variance in width,
     * an average tends to do the trick.</p>
     *
     * @return The width (fixed or average) of children of this {@link RecyclerView}.
     */
    protected abstract int getChildWidth();

    /**
     * <p>Provides a {@link RecyclerView.ItemDecoration} that gets added by default to this
     * {@code CentreSnapRecyclerView}. This base implementation creates empty space at the beginning
     * and end of the view such that the first and last elements can reach the centre.</p>
     *
     * <p>It is safe to return {@code null} from this method if this is not desired.</p>
     *
     * @return An {@link RecyclerView.ItemDecoration} that adds empty space to the start and end of
     *         the {@code RecyclerView} to allow all children to be centred.
     */
    @Nullable
    protected ItemDecoration getDefaultItemDecoration() {
        return new ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                int parentWidth = parent.getWidth();
                int spacingSize = (parentWidth - getChildWidth()) / 2;
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.left = spacingSize;
                } else if (position == parent.getAdapter().getItemCount() - 1) {
                    outRect.right = spacingSize;
                }
            }
        };
    }

    /**
     * <p>Overrides {@link RecyclerView#onMeasure(int, int)} not to change the measurement logic,
     * but to calculate/retrieve the width of its children, and update other measurement-aware
     * components.</p>
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int measuredWidth = getMeasuredWidth();
        if (measuredWidth != mMeasuredWidth) {
            mMeasuredWidth = measuredWidth;
            onWidthChanged();
        }

        // Guard code that is potentially expensive.
        if (!mMeasurementsValid) {
            mMeasurementsValid = true;
            int childWidth = getChildWidth();
            mLayoutManager.setNewMeasurements(mMeasuredWidth, childWidth);
        }
    }

    /**
     * {@inheritDoc}
     * <p>When a change in layout happens, this {@code RecyclerView} automatically scrolls again to
     * the current item, to ensure it remains in the centre.</p>
     * <p><strong>Note:</strong> this means the {@link CentreSnapScrollListener} will detect a
     * scroll and notify the {@link SnapRecyclerAdapter} accordingly.</p>
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            SnapRecyclerAdapter adapter = (SnapRecyclerAdapter) getAdapter();
            scrollToPosition(adapter.getCurrentPosition());
        }
    }

    @Override
    public void scrollToPosition(int position) {
        // Our scrolling algorithm means all scrolls are smooth.
        smoothScrollToPosition(position);
    }

    /**
     * <p>Responds to a change in dimensions and resets state variables as appropriate.</p>
     */
    private void onWidthChanged() {
        mMeasurementsValid = false;
        mLayoutManager.invalidateMeasurements();
    }

    /**
     * {@inheritDoc}
     * <p>This class must be used with an implementation of {@link SnapRecyclerAdapter}.</p>
     */
    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof SnapRecyclerAdapter)) {
            throw new IllegalArgumentException(getClass().getSimpleName() + " must be used with a "
                    + SnapRecyclerAdapter.class.getSimpleName() + " instance.");
        }
        super.setAdapter(adapter);
    }
}
