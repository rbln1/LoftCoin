package me.rubl.loftcoin.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Observer;

public class RecyclerViews {

    public static void onItemClick(@NonNull RecyclerView recyclerView, @NonNull View.OnClickListener listener) {
        recyclerView.addOnItemTouchListener(new ClickHelper(recyclerView.getContext(), listener));
    }

    private static class ClickHelper implements RecyclerView.OnItemTouchListener {

        private final GestureDetectorCompat gestureDetector;
        private final View.OnClickListener listener;

        public ClickHelper(@NonNull Context context, @NonNull View.OnClickListener listener) {
            this.gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
            this.listener = listener;
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            final View view = rv.findChildViewUnder(e.getX(), e.getY());

            if (view != null && gestureDetector.onTouchEvent(e)) {
                listener.onClick(view);
                return true;
            }

            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
