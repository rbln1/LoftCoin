package me.rubl.loftcoin.util;

import android.content.Context;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.NumberFormat;
import java.util.Locale;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class PriceFormatterTest {

    private Context context;

    private PriceFormatter formatter;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        formatter = new PriceFormatter(context);
    }

    @Test
    public void format_EUR() {
        final NumberFormat germanyNumberFormat =
                NumberFormat.getCurrencyInstance(Locale.GERMANY);

        assertThat(
                formatter.format("EUR", 2.34)
        ).isEqualTo(germanyNumberFormat.format(2.34) );
    }

    @Test
    public void format_RUB() {
        final NumberFormat russiaNumberFormat =
                NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));

        assertThat(
                formatter.format("RUB", 2.34)
        ).isEqualTo(russiaNumberFormat.format(2.34) );
    }

    @Test
    public void format_default() {

        final LocaleListCompat locales = ConfigurationCompat
                .getLocales(context.getResources().getConfiguration());

        assertThat(
                formatter.format("CAD", 2.34)
        ).isEqualTo(NumberFormat.getCurrencyInstance(locales.get(0)).format(2.34));
    }
}