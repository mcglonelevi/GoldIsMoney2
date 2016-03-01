package GoldIsMoney2;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

import java.util.*;

public class GoldEconomyService implements EconomyService {

    private Game game;

    public GoldEconomyService (Game game) {
        this.game = game;
    }

    /*
    Currency will always be gold.
     */
    @Override
    public Currency getDefaultCurrency() {
        return new GoldCurrency();
    }

    @Override
    public Set<Currency> getCurrencies() {
        Currency[] currencies = new Currency[1];
        currencies[0] = new GoldCurrency();
        return new HashSet<Currency>(Arrays.asList(currencies));
    }

    /*
    Method will get an account associated with the number of gold bars in their inventory via their UUID.
     */
    @Override
    public Optional<UniqueAccount> getAccount(UUID uuid) {

        Player player = null;
        for (Player playerTest : game.getServer().getOnlinePlayers()) {
            if (playerTest.getUniqueId().equals(uuid)) {
                player = playerTest;
            }
        }
        if (player != null) {
            return Optional.of(new GoldUniqueAccount(player));
        } else {
            return Optional.empty();
        }

    }

    /*
    Method will get the number of gold bars in their inventory via their name.
     */
    @Override
    public Optional<Account> getAccount(String s) {
        Player player = null;
        for (Player playerTest : game.getServer().getOnlinePlayers()) {
            if (playerTest.getName().toLowerCase().equals(s.toLowerCase())) {
                player = playerTest;
            }
        }
        if (player != null) {
            return Optional.of(new GoldUniqueAccount(player));
        } else {
            return Optional.empty();
        }
    }

    /*
    Unique accounts are linked to player's inventories.  They cannot be created.
     */
    @Override
    public Optional<UniqueAccount> createAccount(UUID uuid) {
        return Optional.empty();
    }

    /*
    Virtual accounts are not implemented at this time.
     */
    @Override
    public Optional<VirtualAccount> createVirtualAccount(String s) {
        return Optional.empty();
    }

    @Override
    public void registerContextCalculator(ContextCalculator<Account> contextCalculator) {

    }
}
