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

package com.youview.centresnapsamples;

import android.content.Context;
import android.util.AttributeSet;

import com.youview.centresnaprecyclerview.CentreSnapRecyclerView;

public class SampleCenterSnapView extends CentreSnapRecyclerView {
    private OnSelectionChangedListener<String> mSelectionChangedListener;
    private int mChildWidth;

    public SampleCenterSnapView(Context context) {
        super(context);
    }

    public SampleCenterSnapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SampleCenterSnapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mChildWidth = getContext().getResources().getDimensionPixelSize(R.dimen.fixed_item_width);
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener<String> listener) {
        mSelectionChangedListener = listener;
    }

    public void updateData(DataItem dataItem) {
        if (mSelectionChangedListener != null) {
            mSelectionChangedListener.onSelectionChanged(dataItem.getContent());
        }
    }

    @Override
    protected int getChildWidth() {
        return mChildWidth;
    }
}
