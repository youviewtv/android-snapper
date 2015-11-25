/*
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <p>Implementation of a {@link LinearLayoutManager} to allow for centre-aligned scrolls, i.e. when
 * a scroll is requested to a {@code position}, the View at that position is centred within the
 * {@link RecyclerView}.</p>
 *
 * <p>The overrides of {@link #scrollToPosition(int)} and
 * {@link #smoothScrollToPosition(RecyclerView, RecyclerView.State, int)} will attempt a
 * centre-aligned scroll if possible, but revert to default behaviour if the required measurements
 * aren't present (as provided by {@link #setNewMeasurements(int, int)}).</p>
 */
public class CentreScrollingLinearLayoutManager extends LinearLayoutManager {
    // We guard against centre-align scrolls happening before we've been updated about the new
    // sizes, though this shouldn't happen.
    private boolean mMeasurementsValid;
    private int mWidth;
    private int mChildWidth;

    public CentreScrollingLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * <p>In case this method is called directly (the associated {@link CentreSnapRecyclerView}
     * routes all scrolls through
     * {@link #smoothScrollToPosition(RecyclerView, RecyclerView.State, int)}), we attempt an
     * <i>approximation</i> scroll if possible, else fall back on the default
     * implementation.</p>
     * 
     * @param position {@inheritDoc}
     * @see #smoothScrollToPosition(RecyclerView, RecyclerView.State, int)
     */
    @Override
    public void scrollToPosition(int position) {
        if (mMeasurementsValid) {
            int centreOffset = (mWidth - mChildWidth) / 2;
            super.scrollToPositionWithOffset(position, centreOffset);
        } else {
            super.scrollToPosition(position);
        }
    }

    /**
     * <p>This scrolling mechanism attempts to scroll in three ways of decreasing accuracy:</p>
     * <ul>
     *     <li>
     *         <strong>Fine-grained</strong><br />
     *         <p>If the provided {@code recyclerView} can find a child {@link View} at the given
     *         {@code position}, we calculate the difference between its current {@code left} and
     *         what it would be if it was centre-aligned, and scroll by that offset.</p>
     *     </li>
     *     <li>
     *         <strong>Approximation</strong>
     *         <p>If no such {@link View} can be found (usually because the {@code recyclerView} has
     *         not yet been laid out), we use our average {@link #mChildWidth} to work out where the
     *         child at {@code position} should be. Given the children can and do vary substantially
     *         in width, this can be slightly off.</p>
     *     </li>
     *     <li>
     *         <strong>Default</strong>
     *         <p>If we have no information about the measurements of {@code recyclerView}, we
     *         fall back on the default implementation, which will not be central. This should never
     *         happen.</p>
     *     </li>
     * </ul>
     * <p>In the vast majority of cases, the first condition will be met, and the accurate scroll
     * will take place.</p>
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        View newCentreChild = findViewByPosition(position);
        if (newCentreChild != null) {
            int currentLeft = newCentreChild.getLeft();
            int newLeft = (mWidth - newCentreChild.getMeasuredWidth()) / 2;
            int offset = currentLeft - newLeft;
            recyclerView.smoothScrollBy(offset, 0);
        } else if (mMeasurementsValid) {
            int centreOffset = (mWidth - mChildWidth) / 2;
            super.scrollToPositionWithOffset(position, centreOffset);
        } else {
            super.scrollToPosition(position);
        }
    }

    /**
     * <p>Informs this {@code LayoutManager} that its stored measurements are no longer valid, and
     * measurement-aware scrolls cannot be relied upon.</p>
     */
    public void invalidateMeasurements() {
        mMeasurementsValid = false;
    }

    /**
     * <p>Informs this {@code LayoutManager} of new measurements, allowing it to perform
     * measurement-aware scrolls correctly.</p>
     *
     * @param width The width of the associated {@link RecyclerView}.
     * @param childWidth The width of each child {@link View} in the {@code RecyclerView}.
     */
    public void setNewMeasurements(int width, int childWidth) {
        mWidth = width;
        mChildWidth = childWidth;
        mMeasurementsValid = true;
    }
}
