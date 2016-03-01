package GoldIsMoneyExecutors;

import GoldIsMoney2.GoldCurrency;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class ResetBalanceExecutor implements CommandExecutor{

    private EconomyService economyService;

    public ResetBalanceExecutor (EconomyService economyService) {
        this.economyService = economyService;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player) {
            Player player = (Player) src;

            Optional<Player> playerOptional = args.<Player>getOne("player");
            if (playerOptional.isPresent()) {
                player = playerOptional.get();
            }

            UniqueAccount account = economyService.getAccount(player.getUniqueId()).get();
            Currency currency = new GoldCurrency();
            account.resetBalance(currency, Cause.of(player), null);
            src.sendMessage(Text.of(player.getName() + "'s Balance reset."));
            player.sendMessage(Text.of("Your balance has been reset to " + currency.getSymbol().toPlain() + account.getBalance(currency)));
        } else {
            src.sendMessage(Text.of("Must be a player"));
        }

        return CommandResult.success();
    }
}
