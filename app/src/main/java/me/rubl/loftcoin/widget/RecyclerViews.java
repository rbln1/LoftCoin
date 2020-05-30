package me.rubl.loftcoin.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import io.reactivex.Observable;
import io.reactivex.android.MainThreadDisposable;

public class RecyclerViews {

    @NonNull
    public static Observable<Integer> onSnap(@NonNull RecyclerView recyclerView, @NonNull SnapHelper snapHelper) {
        return Observable.create(emitter -> {
            MainThreadDisposable.verifyMainThread();
            final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    final View snapView = snapHelper.findSnapView(recyclerView.getLayoutManager());
                    if (snapView != null) {
                        final RecyclerView.ViewHolder holder = recyclerView.findContainingViewHolder(snapView);
                        if (holder != null) {
                            emitter.onNext(holder.getAdapterPosition());
                        }
                    }
                }
                }
            };
            emitter.setCancellable(() -> recyclerView.removeOnScrollListener(onScrollListener));
            recyclerView.addOnScrollListener(onScrollListener);
        });
    }

    @NonNull
    public static Observable<Integer> onClick(@NonNull RecyclerView recyclerView) {
        return Observable.create(emitter -> {
            MainThreadDisposable.verifyMainThread();

            final RecyclerView.OnItemTouchListener listener = new OnItemClick((v) -> {
                final RecyclerView.ViewHolder holder = recyclerView.findContainingViewHolder(v);
                if (holder != null) {
                    emitter.onNext(holder.getAdapterPosition());
                }
            });

            emitter.setCancellable(() -> recyclerView.removeOnItemTouchListener(listener));
            recyclerView.addOnItemTouchListener(listener);
        });
    }
}
