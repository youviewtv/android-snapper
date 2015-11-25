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

import android.support.v7.widget.RecyclerView;

/**
 * <p>Implementation of {@link RecyclerView.Adapter} to work with a {@link CentreSnapRecyclerView}
 * that exposes extra information, allowing the {@code RecyclerView} to keep its centred state, and
 * for changes in this {@code Adapter}'s current item to be propagated.
 */
public abstract class SnapRecyclerAdapter extends RecyclerView.Adapter {
    protected int mCurrentPosition;

    /**
     * <p>Informs this {@code Adapter} that the selected item has changed.</p>
     *
     * @param position The adapter position of the newly-selected item.
     */
    public void onItemSelected(int position) {
        mCurrentPosition = position;
    }

    /**
     * <p>Gets the adapter position of the current item. It is necessary to expose this so we can
     * maintain the associated view's visible position in the centre of the
     * {@code RecyclerView}.</p>
     *
     * @return This {@code Adapter}'s current position.
     */
    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
