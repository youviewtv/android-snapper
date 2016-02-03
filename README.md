## Android-snapper
Android-snapper is a custom `RecyclerView` that provides 'centre-snap' behaviour:
  - The current item is displayed in the centre of the `RecyclerView`.
  - After a scroll, the item closest to the centre will be automatically scrolled to the centre.

Since `Gallery` was deprecated, there has not been a simple or clean implementation providing a centre-aligned/snapping list-type view outside of using a `ViewPager` with some margin tricks to 'peek' the pages either side (or the closed-source `WearableListView`). Android-snapper gives much more flexibility, allowing multiple `View`s to be visible and actionable on screen.

### Usage
You will need to subclass `CentreSnapRecyclerView`, which provides the abstract method `getChildWidth()`. This should return the width of each child `View` in the `RecyclerView`, as this is used in scrolling and snapping calculations. The `CentreSnapRecyclerView` should be provided with a subclass of `SnapRecyclerAdapter`, which is a `RecyclerView.Adapter` customised to work with Android-snapper.

### Sample
Android-snapper comes with a single sample app (under the `samples` package) which shows how to use the Android-snapper view.

### Version
1.0.0

### Future work
 - Add vertical support.

### License
```
Copyright (c) 2016 YouView TV Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
