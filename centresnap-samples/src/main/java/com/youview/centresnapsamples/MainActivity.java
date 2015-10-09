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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.youview.centresnaprecyclerview.SnapRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectionChangedListener<String> {
    private static final int NUMBER_OF_ITEMS = 16;
    private static final String BUNDLE_CURRENT_POSITION = "position";

    private SampleCenterSnapView mRecyclerView;
    private TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mRecyclerView = (SampleCenterSnapView) findViewById(R.id.recycler);
        mContent = (TextView) findViewById(R.id.content);

        List<DataItem> data = new ArrayList<>(NUMBER_OF_ITEMS);
        String base = getString(R.string.item_content_base);
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            data.add(new DataItem(Integer.toString(i), String.format(base, i)));
        }

        int startPosition = 0;
        if (savedInstanceState != null) {
            startPosition = savedInstanceState.getInt(BUNDLE_CURRENT_POSITION, startPosition);
        }

        mRecyclerView.setAdapter(new SampleSnapAdapter(data, startPosition));
        mRecyclerView.setOnSelectionChangedListener(this);
        onSelectionChanged(data.get(startPosition).getContent());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SnapRecyclerAdapter adapter = (SnapRecyclerAdapter) mRecyclerView.getAdapter();
        outState.putInt(BUNDLE_CURRENT_POSITION, adapter.getCurrentPosition());
    }

    @Override
    public void onSelectionChanged(String newData) {
        mContent.setText(newData);
    }
}
