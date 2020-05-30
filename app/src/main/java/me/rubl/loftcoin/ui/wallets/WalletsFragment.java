package me.rubl.loftcoin.ui.wallets;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import me.rubl.loftcoin.BaseComponent;
import me.rubl.loftcoin.R;
import me.rubl.loftcoin.databinding.FragmentWalletsBinding;
import me.rubl.loftcoin.widget.RecyclerViews;

public class WalletsFragment extends Fragment {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final WalletsComponent component;

    private FragmentWalletsBinding binding;

    private WalletsViewModel viewModel;

    private SnapHelper walletsSnapHelper;

    private WalletsAdapter adapter;
    private TransactionsAdapter transactionsAdapter;

    @Inject
    WalletsFragment(BaseComponent baseComponent) {
        component = DaggerWalletsComponent.builder()
            .baseComponent(baseComponent)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, component.viewModelFactory())
            .get(WalletsViewModel.class);
        adapter = component.walletsAdapter();
        transactionsAdapter = component.transactionsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fragment contain menu
        setHasOptionsMenu(true);

        binding = FragmentWalletsBinding.bind(view);

        walletsSnapHelper = new PagerSnapHelper();
        walletsSnapHelper.attachToRecyclerView(binding.fragmentWalletsRecycler);

        final TypedValue value = new TypedValue();
        view.getContext().getTheme().resolveAttribute(R.attr.walletCardWidth, value, true);
        final DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        final int padding = (int) (displayMetrics.widthPixels - value.getDimension(displayMetrics)) / 2;
        binding.fragmentWalletsRecycler.setPadding(padding, 0, padding, 0);
        binding.fragmentWalletsRecycler.setClipToPadding(false);

        // Add carousel listener
        binding.fragmentWalletsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        binding.fragmentWalletsRecycler.addOnScrollListener(new CarouselScroller());

        binding.fragmentWalletsRecycler.setAdapter(adapter);

        disposable.add(viewModel.wallets().subscribe(adapter::submitList));
        disposable.add(viewModel.wallets().map(List::isEmpty).subscribe((isEmpty) -> {
            binding.fragmentWalletsCard.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            binding.fragmentWalletsRecycler.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }));

        binding.fragmentWalletsTransactions.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.fragmentWalletsTransactions.setAdapter(transactionsAdapter);
        binding.fragmentWalletsTransactions.setHasFixedSize(true);

        disposable.add(RecyclerViews.onSnap(binding.fragmentWalletsRecycler, walletsSnapHelper).subscribe(viewModel::changeWallet));

        disposable.add(viewModel.transactions().subscribe(transactionsAdapter::submitList));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.wallets, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.add == item.getItemId()) {
            disposable.add(viewModel.addWallet().subscribe());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        walletsSnapHelper.attachToRecyclerView(null);
        binding.fragmentWalletsRecycler.setAdapter(null);
        disposable.clear();
        super.onDestroyView();
    }

    private static class CarouselScroller extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            final int centerX = (recyclerView.getLeft() + recyclerView.getRight()) / 2;

            for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                final View child = recyclerView.getChildAt(i);
                 final int childCenterX = (child.getLeft() + child.getRight()) / 2;
                 final float  childOffset = Math.abs(centerX - childCenterX) / (float ) centerX;
                 float factor = (float) (Math.pow(0.85, childOffset));
                 child.setScaleX(factor);
                 child.setScaleY(factor);
            }
        }
    }
}