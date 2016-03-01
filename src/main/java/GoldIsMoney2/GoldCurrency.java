package GoldIsMoney2;

import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

public class GoldCurrency implements Currency {
    @Override
    public Text getDisplayName() {
        return Text.of("Gold Ingot");
    }

    @Override
    public Text getPluralDisplayName() {
        return Text.of("Gold Ingots");
    }

    @Override
    public Text getSymbol() {
        return Text.of("$");
    }

    @Override
    public Text format(BigDecimal bigDecimal, int i) {
        return Text.of(bigDecimal);
    }

    @Override
    public int getDefaultFractionDigits() {
        return 0;
    }

    @Override
    public boolean isDefault() {
        return true;
    }
}
