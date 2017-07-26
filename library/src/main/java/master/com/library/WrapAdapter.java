package master.com.library;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pankaj Sharma on 17/7/17.
 */

public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int VIEW_TYPE_TOP = 1000;
    protected static final int VIEW_TYPE_NORMAL = 1001;
    protected static final int VIEW_TYPE_BOTTOM = 1002;
    private RecyclerView.Adapter adapter;
    private boolean isLoading = false;

    public WrapAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TOP) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progress, parent, false));
        } else if (viewType == VIEW_TYPE_BOTTOM) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progress, parent, false));
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
        } else {
            adapter.onBindViewHolder(holder, (isLoading && loadMoreFromTop) ? position - 1 : position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoading && loadMoreFromTop && position == 0) return VIEW_TYPE_TOP;
        if (isLoading && loadMoreFromBottom && position == getItemCount() - 1)
            return VIEW_TYPE_BOTTOM;
        return adapter.getItemViewType((isLoading && loadMoreFromTop) ? position - 1 : position);
    }

    @Override
    public int getItemCount() {
        return ((isLoading && loadMoreFromTop) ? 1 : 0) + adapter.getItemCount() + ((isLoading && loadMoreFromBottom) ? 1 : 0);
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
        /*if (getItemCount() > 0) {
            notifyItemChanged(0);
            notifyItemChanged(getItemCount() - 1);
        }*/
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    boolean loadMoreFromTop = false;

    public void setLoadMoreFromTop(boolean isEnable) {
        loadMoreFromTop = isEnable;
    }

    boolean loadMoreFromBottom = false;

    public void setLoadMoreFromBottom(boolean isEnable) {
        loadMoreFromBottom = isEnable;
    }
}
