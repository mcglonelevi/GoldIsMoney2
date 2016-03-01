package GoldIsMoney2;

import com.google.common.collect.Iterators;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.*;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GoldUniqueAccount implements UniqueAccount{

    public Player player;

    public GoldUniqueAccount (Player player) {
        this.player = player;
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public Text getDisplayName() {
        return Text.of(player.getName());
    }

    @Override
    public BigDecimal getDefaultBalance(Currency currency) {
        return BigDecimal.ZERO;
    }

    @Override
    public boolean hasBalance(Currency currency, Set<Context> set) {
        return true;
    }

    @Override
    public BigDecimal getBalance(Currency currency, Set<Context> set) {
        int numItems = 0;
        Inventory inventory = player.getInventory().query(ItemTypes.GOLD_INGOT);
        //First Slot check
        numItems += inventory.totalItems();
        //Deal with rest
        for (Inventory item : inventory) {
            numItems += item.totalItems();
        }
        return BigDecimal.valueOf(numItems);
    }

    @Override
    public Map<Currency, BigDecimal> getBalances(Set<Context> set) {
        return null;
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        //Get Play inventory
        Inventory inventory = player.getInventory().query(ItemTypes.GOLD_INGOT);
        //Clear first slot
        inventory.clear();
        //Clear Rest
        for (Inventory item : inventory) {
            item.clear();
        }

        // Put # of items into inventory
        // Get number of gold bars as int
        int goldBars = bigDecimal.intValue();
        int fullStacks = goldBars / 64;
        int remainder = goldBars % 64;

        for (int i = 0; i < fullStacks; i++) {
            player.getInventory().offer(ItemStack.of(ItemTypes.GOLD_INGOT, 64));
        }
        if (remainder > 0)
            player.getInventory().offer(ItemStack.of(ItemTypes.GOLD_INGOT,remainder));

        return new GoldTransactionResult(new GoldUniqueAccount(player), currency, bigDecimal, set, ResultType.SUCCESS, null);
    }

    @Override
    public TransactionResult resetBalances(Cause cause, Set<Context> set) {
        return null;
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause, Set<Context> set) {
        setBalance(new GoldCurrency(), BigDecimal.ZERO, null, null);
        return null;
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        setBalance(currency, BigDecimal.valueOf(getBalance(currency).intValue() + bigDecimal.intValue()), null);
        return new GoldTransactionResult(this, currency, bigDecimal, set, ResultType.SUCCESS, TransactionTypes.DEPOSIT);
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        if (getBalance(currency).intValue() >= bigDecimal.intValue()) {
            setBalance(currency, BigDecimal.valueOf(getBalance(currency).intValue() - bigDecimal.intValue()), null);
            return new GoldTransactionResult(this, currency, bigDecimal, set, ResultType.SUCCESS, TransactionTypes.WITHDRAW);
        } else {
            return new GoldTransactionResult(this, currency, bigDecimal, set, ResultType.ACCOUNT_NO_FUNDS, TransactionTypes.WITHDRAW);
        }
    }

    @Override
    public TransferResult transfer(Account account, Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        TransactionResult result = withdraw(currency, bigDecimal, null);
        if (result.getResult().equals(ResultType.SUCCESS)) {
            account.deposit(currency,bigDecimal,null);
        }
        return new GoldTransferResult(account, this, currency, bigDecimal, set, result.getResult(), TransactionTypes.TRANSFER);
    }

    @Override
    public String getIdentifier() {
        return player.getIdentifier();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return null;
    }
}
