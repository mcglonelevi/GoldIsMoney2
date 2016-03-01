package GoldIsMoneyExecutors;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransferResult;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Optional;

public class GiveExecutor implements CommandExecutor {

    private EconomyService economyService;

    public GiveExecutor (EconomyService economyService) {
        this.economyService = economyService;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player) {
            Player player = (Player) src;
            Optional<UniqueAccount> accountOptional = economyService.getAccount(args.<Player>getOne("receiver").get().getUniqueId());
            if (accountOptional.isPresent()) {
                TransferResult result = economyService.getAccount(player.getUniqueId()).get().transfer(accountOptional.get(), economyService.getDefaultCurrency(), BigDecimal.valueOf(args.<Integer>getOne("amount").get()), null);
                if (result.getResult().equals(ResultType.SUCCESS)) {
                    src.sendMessage(Text.of("Gold Transferred"));
                    args.<Player>getOne("receiver").get().sendMessage(Text.of("You have received gold!"));
                } else {
                    src.sendMessage(Text.of("Could not send money! Not enough funds!"));
                }
            }
        } else
            src.sendMessage(Text.of("Must be a player"));
        return CommandResult.success();
    }
}
