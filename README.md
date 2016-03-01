# GoldIsMoney2

## Introduction

This is a plugin for the Sponge Minecraft server.  It turns gold nuggets, gold ingots, and gold blocks into money!  This is a rough port of Flobi's GoldIsMoney plugin for bukkit:

http://dev.bukkit.org/bukkit-plugins/goldismoney/

I will be adding more features to make the plugin compatable with Sponge's Economy API.

The values are as follows:

Gold Nugget = $1

Gold Ingot = $9

Gold Block = $81

---------

## Commands

There are a few optional commands to get you started:

| Command                     | Aliases   | Description                                                                                                                                       | Permission                |
|-----------------------------|-----------|---------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------|
| /balance                    | /bal      | Check your balance                                                                                                                                | goldismoney2.bal          |
| /setbalance amount [player] | /setbal   | Set your/another player'sbalance  .If no name is specified, it will set your balance.,If another name is specified, it will set their balance.    | goldismoney2.setbalance   |
| /resetbalance [player]      | /resetbal | Reset your/another player's balance  If no name is specified, it will set your balance.  If another name is specified, it will set their balance. | goldismoney2.resetbalance |
| /pay player amount          |           | Pay another play a set amount                                                                                                                     | goldismoney2.pay          |
