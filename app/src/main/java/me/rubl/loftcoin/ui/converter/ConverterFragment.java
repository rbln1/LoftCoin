package me.rubl.loftcoin.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import me.rubl.loftcoin.BaseComponent;
import me.rubl.loftcoin.BuildConfig;
import me.rubl.loftcoin.R;
import me.rubl.loftcoin.databinding.FragmentConverterBinding;
import me.rubl.loftcoin.util.ImageLoader;
import me.rubl.loftcoin.widget.CircleViewOutlineProvider;

public class ConverterFragment extends Fragment {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final ConverterComponent component;

    private final ImageLoader imageLoader;

    private FragmentConverterBinding binding;

    private ConverterViewModel viewModel;

    @Inject
    ConverterFragment(BaseComponent baseComponent, ImageLoader imageLoader) {

        component = DaggerConverterComponent.builder()
            .baseComponent(baseComponent)
            .build();
        this.imageLoader = imageLoader;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireParentFragment(), component.viewModelFactory())
            .get(ConverterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentConverterBinding.bind(view);

        final NavController navController = NavHostFragment.findNavController(this);

        CircleViewOutlineProvider.apply(binding.fragmentConverterStartCoinImage);
        CircleViewOutlineProvider.apply(binding.fragmentConverterEndCoinImage);

        disposable.add(viewModel.topCoins().subscribe());

        disposable.add(RxView.clicks(binding.fragmentConverterStartCoinSelector).subscribe(v -> {
            final Bundle args = new Bundle();
            args.putInt(CoinsSheet.KEY_MODE, CoinsSheet.MODE_FROM);
            navController.navigate(R.id.coins_sheet, args);
        }));

        disposable.add(RxView.clicks(binding.fragmentConverterEndCoinSelector).subscribe(v -> {
            final Bundle args = new Bundle();
            args.putInt(CoinsSheet.KEY_MODE, CoinsSheet.MODE_TO);
            navController.navigate(R.id.coins_sheet, args);
        }));

        disposable.add(viewModel.startCoin().subscribe(
            coin -> {
                binding.fragmentConverterStartCoinName.setText(coin.symbol());
                imageLoader.load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png")
                    .into(binding.fragmentConverterStartCoinImage);
            }
        ));
        disposable.add(viewModel.endCoin().subscribe(
            coin -> {
                binding.fragmentConverterEndCoinName.setText(coin.symbol());
                imageLoader.load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png")
                    .into(binding.fragmentConverterEndCoinImage);
            }
        ));

        disposable.add(RxTextView.textChanges(binding.fragmentConverterStartCoinValue).subscribe(viewModel::startCoinValue));
        disposable.add(RxTextView.textChanges(binding.fragmentConverterEndCoinValue).subscribe(viewModel::endCoinValue));

        disposable.add(viewModel.startCoinValue()
                .distinctUntilChanged()
                .subscribe(text -> {
                    binding.fragmentConverterStartCoinValue.setText(text);
                    binding.fragmentConverterStartCoinValue.setSelection(text.length());
                }));
        disposable.add(viewModel.endCoinValue()
                .distinctUntilChanged()
                .subscribe(text -> {
                    binding.fragmentConverterEndCoinValue.setText(text);
                    binding.fragmentConverterEndCoinValue.setSelection(text.length());
                }));
    }

    @Override
    public void onDestroyView() {
        disposable.clear();
        super.onDestroyView();
    }
}