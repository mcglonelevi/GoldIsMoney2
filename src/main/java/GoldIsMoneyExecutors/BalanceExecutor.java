package GoldIsMoneyExecutors;

import GoldIsMoney2.GoldCurrency;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

public class BalanceExecutor implements CommandExecutor{

    private EconomyService economyService;

    public BalanceExecutor (EconomyService economyService) {
        this.economyService = economyService;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player) {
            Player player = (Player) src;
            player.sendMessage(Text.of(economyService.getAccount(player.getUniqueId()).get().getBalance(new GoldCurrency())));
        } else
            src.sendMessage(Text.of("Must be a player"));

        return CommandResult.success();
    }
}
