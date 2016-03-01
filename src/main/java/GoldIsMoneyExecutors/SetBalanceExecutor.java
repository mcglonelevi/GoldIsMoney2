package GoldIsMoneyExecutors;

import GoldIsMoney2.GoldCurrency;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

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

            if (numBars < 0)
                player.sendMessage(Text.of("Amount must be >= 0"));
            else
                economyService.getAccount(player.getUniqueId()).get().setBalance(new GoldCurrency(), BigDecimal.valueOf(numBars), null);

        }
        else
            src.sendMessage(Text.of("Must be a player"));

        return CommandResult.success();
    }
}
