package GoldIsMoney2;

import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.VirtualAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransferResult;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * Created by Levi on 2/29/2016.
 */
public class GoldVirtualAccount implements VirtualAccount {
    @Override
    public Text getDisplayName() {
        return null;
    }

    @Override
    public BigDecimal getDefaultBalance(Currency currency) {
        return null;
    }

    @Override
    public boolean hasBalance(Currency currency, Set<Context> set) {
        return false;
    }

    @Override
    public BigDecimal getBalance(Currency currency, Set<Context> set) {
        return null;
    }

    @Override
    public Map<Currency, BigDecimal> getBalances(Set<Context> set) {
        return null;
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        return null;
    }

    @Override
    public TransactionResult resetBalances(Cause cause, Set<Context> set) {
        return null;
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause, Set<Context> set) {
        return null;
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        return null;
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        return null;
    }

    @Override
    public TransferResult transfer(Account account, Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        return null;
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public Set<Context> getActiveContexts() {
        return null;
    }
}
