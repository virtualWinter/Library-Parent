package me.vwinter.library.command.parametric.defaults;

import me.vwinter.library.command.WrappedSender;
import me.vwinter.library.command.node.CommandContext;
import me.vwinter.library.command.parametric.argument.ArgumentParser;

import java.util.Collections;
import java.util.List;

public class IntegerParser implements ArgumentParser<Integer> {

    @Override
    public <C> Integer parse(WrappedSender sender, CommandContext<C> context, String argument) {
        int integer;

        try {
            integer = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            return null;
        }

        return integer;
    }

    @Override
    public String getParseMessage(WrappedSender sender, String invalid) {
        return String.format("&c%s is not a valid integer.", invalid);
    }

    @Override
    public <C> List<String> getSuggestions(WrappedSender sender, CommandContext<C> context, String lastWord) {
        if (lastWord.isEmpty()) {
            return Collections.singletonList("0");
        }

        int lastInteger;

        try {
            lastInteger = Integer.parseInt(lastWord);
        } catch (NumberFormatException e) {
            return Collections.singletonList("0");
        }

        return Collections.singletonList(lastInteger + 1 + "");
    }
}
