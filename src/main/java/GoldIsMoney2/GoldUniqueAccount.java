package GoldIsMoney2;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemType;
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
        int balance = 0;

        Inventory goldBlockInventory = player.getInventory().query(ItemTypes.GOLD_BLOCK);
        Inventory goldIngotInventory = player.getInventory().query(ItemTypes.GOLD_INGOT);
        Inventory goldNuggetInventory = player.getInventory().query(ItemTypes.GOLD_NUGGET);

        balance += getItemsInInventory(goldBlockInventory) * 81;
        balance += getItemsInInventory(goldIngotInventory) * 9;
        balance += getItemsInInventory(goldNuggetInventory);

        return BigDecimal.valueOf(balance);
    }

    private int getItemsInInventory(Inventory inventory) {
        int numItems = 0;
        //First Slot check
        numItems += inventory.totalItems();
        //Deal with rest
        for (Inventory item : inventory) {
            numItems += item.totalItems();
        }
        return numItems;
    }

    private void clearItemsInInventory(Inventory inventory) {
        //First Slot check
        inventory.clear();
        //Deal with rest
        for (Inventory item : inventory) {
            item.clear();
        }
    }

    private void offerGold (ItemType itemType, int number) {
        int fullStacks = number / 64;
        int remainder = number % 64;

        for (int i = 0; i < fullStacks; i++) {
            player.getInventory().offer(ItemStack.of(itemType, 64));
        }
        if (remainder > 0)
            player.getInventory().offer(ItemStack.of(itemType,remainder));
    }

    private int getStacks (int number) {
        int stackCount = 0;
        stackCount += number / 64;
        number -= number * stackCount;
        if (number != 0) {
            stackCount++;
        }
        return stackCount;
    }

    @Override
    public Map<Currency, BigDecimal> getBalances(Set<Context> set) {
        return null;
    }

    @Override
    public TransactionResult setBalance(Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {

        if (bigDecimal.intValue() < 0) {
            return new GoldTransactionResult(this, currency, bigDecimal, set, ResultType.FAILED, TransactionTypes.DEPOSIT);
        }

        Inventory goldBlockInventory = player.getInventory().query(ItemTypes.GOLD_BLOCK);
        Inventory goldIngotInventory = player.getInventory().query(ItemTypes.GOLD_INGOT);
        Inventory goldNuggetInventory = player.getInventory().query(ItemTypes.GOLD_NUGGET);

        // Put # of items into inventory
        // Get number of gold bars as int
        int remainder = bigDecimal.intValue();
        int numberGoldBlocks = bigDecimal.intValue() / 81;
        remainder -= numberGoldBlocks * 81;
        int numberGoldIngots =  remainder / 9;
        remainder -= numberGoldIngots * 9;
        int numberGoldNuggets = remainder;

        int numStacks = getStacks(numberGoldBlocks) + getStacks(numberGoldIngots) + getStacks(numberGoldNuggets);
        int emptySlots = player.getInventory().capacity() - player.getInventory().size();

        if (numStacks > emptySlots) {
            return new GoldTransactionResult(new GoldUniqueAccount(player), currency, bigDecimal, set, ResultType.ACCOUNT_NO_SPACE, null);
        }

        clearItemsInInventory(goldBlockInventory);
        clearItemsInInventory(goldIngotInventory);
        clearItemsInInventory(goldNuggetInventory);

        offerGold(ItemTypes.GOLD_BLOCK, numberGoldBlocks);
        offerGold(ItemTypes.GOLD_INGOT, numberGoldIngots);
        offerGold(ItemTypes.GOLD_NUGGET, numberGoldNuggets);

        return new GoldTransactionResult(new GoldUniqueAccount(player), currency, bigDecimal, set, ResultType.SUCCESS, null);
    }

    @Override
    public TransactionResult resetBalances(Cause cause, Set<Context> set) {
        return null;
    }

    @Override
    public TransactionResult resetBalance(Currency currency, Cause cause, Set<Context> set) {
        TransactionResult result = setBalance(currency, this.getDefaultBalance(currency), cause, set);
        return result;
    }

    @Override
    public TransactionResult deposit(Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        if (bigDecimal.intValue() < 0) {
            return new GoldTransactionResult(this, currency, bigDecimal, set, ResultType.FAILED, TransactionTypes.DEPOSIT);
        }
        TransactionResult result = setBalance(currency, BigDecimal.valueOf(getBalance(currency).intValue() + bigDecimal.intValue()), null);
        return new GoldTransactionResult(this, currency, bigDecimal, set, result.getResult(), TransactionTypes.DEPOSIT);
    }

    @Override
    public TransactionResult withdraw(Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {
        if (bigDecimal.intValue() < 0) {
            return new GoldTransactionResult(this, currency, bigDecimal, set, ResultType.FAILED, TransactionTypes.DEPOSIT);
        }
        if (getBalance(currency).intValue() >= bigDecimal.intValue()) {
            TransactionResult result = setBalance(currency, BigDecimal.valueOf(getBalance(currency).intValue() - bigDecimal.intValue()), null);
            return new GoldTransactionResult(this, currency, bigDecimal, set, result.getResult(), TransactionTypes.WITHDRAW);
        } else {
            return new GoldTransactionResult(this, currency, bigDecimal, set, ResultType.ACCOUNT_NO_FUNDS, TransactionTypes.WITHDRAW);
        }
    }

    @Override
    public TransferResult transfer(Account account, Currency currency, BigDecimal bigDecimal, Cause cause, Set<Context> set) {

        ResultType resultType = ResultType.FAILED;

        if (bigDecimal.intValue() < 0) {
            return new GoldTransferResult(account, this, currency, bigDecimal, set, resultType, TransactionTypes.TRANSFER);
        }

        if (getBalance(currency).intValue() >= bigDecimal.intValue()) {
            TransactionResult result = withdraw(currency, bigDecimal, cause, set);
            if (result.getResult().equals(ResultType.SUCCESS)) {
                TransactionResult depResult = account.deposit(currency,bigDecimal,cause,set);
                if (depResult.getResult().equals(ResultType.SUCCESS)) {
                    resultType = ResultType.SUCCESS;
                } else{
                    deposit(currency, bigDecimal, cause, set);
                }
            }
        }

        return new GoldTransferResult(account, this, currency, bigDecimal, set, resultType, TransactionTypes.TRANSFER);
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
