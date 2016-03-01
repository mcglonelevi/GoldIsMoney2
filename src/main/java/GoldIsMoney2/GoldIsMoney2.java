package GoldIsMoney2;

import GoldIsMoneyExecutors.BalanceExecutor;
import GoldIsMoneyExecutors.GiveExecutor;
import GoldIsMoneyExecutors.ResetBalanceExecutor;
import GoldIsMoneyExecutors.SetBalanceExecutor;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

@Plugin(id = "goldismoney2", name = "GoldIsMoney2", version = "1.0")
public class GoldIsMoney2 {

    @Inject
    Game game;

    @Inject
    Logger logger;

    public GoldEconomyService goldIsMoneyService;

    @Listener
    public void onInit (GameInitializationEvent e) {
        goldIsMoneyService = new GoldEconomyService(game);

        CommandSpec balSpec = CommandSpec.builder()
                .description(Text.of("Gets a player's balance."))
                .permission("goldismoney2.bal")
                .executor(new BalanceExecutor(goldIsMoneyService))
                .build();
        CommandSpec setSpec = CommandSpec.builder()
                .description(Text.of("Sets a player's balance."))
                .permission("goldismoney2.setbalance")
                .arguments(GenericArguments.integer(Text.of("amount")), GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new SetBalanceExecutor(goldIsMoneyService))
                .build();
        CommandSpec resetSpec = CommandSpec.builder()
                .description(Text.of("Resets a player's balance."))
                .permission("goldismoney2.resetbalance")
                .arguments(GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new ResetBalanceExecutor(goldIsMoneyService))
                .build();
        CommandSpec paySpec = CommandSpec.builder()
                .description(Text.of("Transfers money from one player to another."))
                .permission("goldismoney2.pay")
                .arguments(GenericArguments.player(Text.of("receiver")), GenericArguments.integer(Text.of("amount")))
                .executor(new GiveExecutor(goldIsMoneyService))
                .build();

        game.getCommandManager().register(this, balSpec, "bal", "balance");
        game.getCommandManager().register(this, setSpec, "setbalance", "setbal");
        game.getCommandManager().register(this, resetSpec, "resetbalance", "resetbal");
        game.getCommandManager().register(this, paySpec, "pay");

        game.getServiceManager().setProvider(this, EconomyService.class, new GoldEconomyService(game));
    }

}