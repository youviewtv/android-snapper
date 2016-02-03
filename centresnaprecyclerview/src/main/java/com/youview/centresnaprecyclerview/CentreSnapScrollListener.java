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

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <p>Implementation of a {@link RecyclerView.OnScrollListener} that snaps the central item in the
 * {@link RecyclerView} to the centre of the view once the scroll has completed.</p>
 */
public class CentreSnapScrollListener extends RecyclerView.OnScrollListener {
    private static final int SNAP_THRESHOLD_PIXELS = 1;

    private int mPreviousScrollState = Integer.MIN_VALUE;

    /**
     * {@inheritDoc}
     * <p>Once a scroll has finished (i.e. {@code newState} <i>becomes</i>
     * {@link RecyclerView#SCROLL_STATE_IDLE}), calculates the delta between the centre of the
     * RecyclerView and the centre child view, and scrolls by that amount to centre the child.</p>
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && newState != mPreviousScrollState) {
            int centreX = recyclerView.getWidth() / 2;
            int centreY = recyclerView.getHeight() / 2;
            View centreChild = recyclerView.findChildViewUnder(centreX, centreY);

            SnapRecyclerAdapter adapter = (SnapRecyclerAdapter) recyclerView.getAdapter();
            int childPosition = recyclerView.getChildLayoutPosition(centreChild);
            adapter.onItemSelected(childPosition);

            int delta = calculateDelta(recyclerView, centreChild, centreX, centreY);
            // Avoid infinite scrolls where the parity of the screen width and the view width are
            // different.
            if (Math.abs(delta) > SNAP_THRESHOLD_PIXELS) {
                recyclerView.smoothScrollBy(delta, delta);
            }
        } else {
            super.onScrollStateChanged(recyclerView, newState);
        }

        mPreviousScrollState = newState;
    }

    /**
     * <p>TODO</p>
     * @param recyclerView
     * @param centreChild
     * @return
     */
    private int calculateDelta(RecyclerView recyclerView, View centreChild, int centreX, int centreY) {
        boolean isHorizontal = ((CentreSnapRecyclerView) recyclerView).getOrientation() == RecyclerView.HORIZONTAL;
        // Start is the side closest to 0 scroll (i.e. left or top), end is the appropriate opposite.
        int startFromCentre, endFromCentre;
        if (isHorizontal) {
            int left = centreChild.getLeft();
            int right = centreChild.getRight();
            startFromCentre = centreX - left;
            endFromCentre = right - centreX;
        } else {
            int top = centreChild.getTop();
            int bottom = centreChild.getBottom();
            startFromCentre = centreY - top;
            endFromCentre = bottom - centreY;
        }

        return (endFromCentre - startFromCentre) / 2;
    }
}
