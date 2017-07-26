package master.com.library;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Pankaj Sharma on 17/7/17.
 */

public class MRecyclerView extends RecyclerView {
    private static final String TAG = "MRecyclerView";
    private WrapAdapter mAdapter;

    public MRecyclerView(Context context) {
        super(context);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setLayoutManager(final LayoutManager layoutManager) {
        LayoutManager myLayoutManager = layoutManager;
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) myLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapter != null) {
                        if (mAdapter.getItemViewType(position) == WrapAdapter.VIEW_TYPE_TOP)
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        if (mAdapter.getItemViewType(position) == WrapAdapter.VIEW_TYPE_BOTTOM)
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    Log.d(TAG, "getSpanSize() called with: position = [" + position + "]");
                    return 1;
                }
            });
        }
        super.setLayoutManager(myLayoutManager);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = new WrapAdapter(adapter);
        mAdapter.setLoadMoreFromTop(loadMoreFromTop);
        mAdapter.setLoadMoreFromBottom(loadMoreFromBottom);
        super.setAdapter(mAdapter);
    }

    boolean loadMoreFromTop = false;

    public void setLoadMoreFromTop(boolean isEnable, OnLoadMoreListener onLoadMoreListener) {
        loadMoreFromTop = isEnable;
        if (mAdapter != null) {
            mAdapter.setLoadMoreFromTop(isEnable);
        }
        this.onLoadMoreListener = onLoadMoreListener;
        setScrollListener(isEnable);
    }

    boolean loadMoreFromBottom = false;
    OnLoadMoreListener onLoadMoreListener;

    public void setLoadMoreFromBottom(boolean isEnable, OnLoadMoreListener onLoadMoreListener) {
        loadMoreFromBottom = isEnable;
        if (mAdapter != null) {
            mAdapter.setLoadMoreFromBottom(isEnable);
        }
        this.onLoadMoreListener = onLoadMoreListener;
        setScrollListener(isEnable);
    }

    public void setLoadMoreDone() {
        mAdapter.setIsLoading(false);
        loading = false;
    }

    private void setScrollListener(boolean isSet) {
        if (isSet) {
            addOnScrollListener(scrollListener);
        } else {
            removeOnScrollListener(scrollListener);
        }
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    public int getFirstVisibleItem(int[] lastVisibleItemPositions) {
        int minSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                minSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] < minSize) {
                minSize = lastVisibleItemPositions[i];
            }
        }
        return minSize;
    }

    int totalItemCount, lastVisibleItem, firstVisibleItem;
    boolean loading = false;
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView,
                               int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            totalItemCount = getLayoutManager().getItemCount();

            if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) getLayoutManager()).findLastVisibleItemPositions(null);
                // get maximum element within the list
                lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions);
                firstVisibleItem = getFirstVisibleItem(((StaggeredGridLayoutManager) getLayoutManager()).findFirstVisibleItemPositions(null));
            } else if (getLayoutManager() instanceof GridLayoutManager) {
                lastVisibleItem = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                firstVisibleItem = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            } else if (getLayoutManager() instanceof LinearLayoutManager) {
                lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
                firstVisibleItem = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            }

            if (!loading) {
                if (loadMoreFromBottom && totalItemCount <= (lastVisibleItem + 5)) {
                    if (onLoadMoreListener != null) {
                        if (mAdapter != null) mAdapter.setIsLoading(true);
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                } else if (loadMoreFromTop && firstVisibleItem <= 0) {
                    if (onLoadMoreListener != null) {
                        if (mAdapter != null) mAdapter.setIsLoading(true);
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        }
    };
}