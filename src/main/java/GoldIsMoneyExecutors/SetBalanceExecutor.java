package GoldIsMoneyExecutors;

import GoldIsMoney2.GoldCurrency;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Optional;

public class SetBalanceExecutor implements CommandExecutor{

    private EconomyService economyService;

    public SetBalanceExecutor (EconomyService economyService) {
        this.economyService = economyService;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player) {

            Player player = (Player) src;
            int numBars = args.<Integer>getOne("amount").get();
            Optional<Player> playerOptional = args.<Player>getOne("player");

            if (playerOptional.isPresent()) {
                player = playerOptional.get();
            }

            if (numBars < 0)
                src.sendMessage(Text.of("Amount must be >= 0"));
            else {
                TransactionResult result = economyService.getAccount(player.getUniqueId()).get().setBalance(new GoldCurrency(), BigDecimal.valueOf(numBars), null);
                if (!result.getResult().equals(ResultType.SUCCESS)) {
                    src.sendMessage(Text.of("There was an issue transferring funds.  (Lack of inventory space.)"));
                } else {
                    src.sendMessage(Text.of("Funds set."));
                    player.sendMessage(Text.of("Your new balance is " + economyService.getAccount(player.getUniqueId()).get().getBalance(new GoldCurrency())));
                }

            }
        }
        else
            src.sendMessage(Text.of("Must be a player to use this command."));

        return CommandResult.success();
    }
}
