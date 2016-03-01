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

public class ResetBalanceExecutor implements CommandExecutor{

    private EconomyService economyService;

    public ResetBalanceExecutor (EconomyService economyService) {
        this.economyService = economyService;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if(src instanceof Player) {
            Player player = (Player) src;
            economyService.getAccount(player.getUniqueId()).get().resetBalance(new GoldCurrency(), null, null);
            player.sendMessage(Text.of("Your balance has been reset."));
        } else {
            src.sendMessage(Text.of("Must be a player"));
        }

        return CommandResult.success();
    }
}
