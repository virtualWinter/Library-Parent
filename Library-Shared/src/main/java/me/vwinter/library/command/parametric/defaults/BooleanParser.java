package me.vwinter.library.command.parametric.defaults;

import com.google.common.collect.Lists;
import me.vwinter.library.command.WrappedSender;
import me.vwinter.library.command.node.CommandContext;
import me.vwinter.library.command.parametric.argument.ArgumentParser;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BooleanParser implements ArgumentParser<Boolean> {

    private final List<String> matches;

    public BooleanParser() {
        Random random = new Random();
        this.matches = Arrays.asList("true", "yes", "false", "no");
    }

    @Override
    public <C> Boolean parse(WrappedSender sender, CommandContext<C> context, String argument) {
        if (argument.equalsIgnoreCase("true") || argument.equalsIgnoreCase("yes")) {
            return true;
        }

        if (argument.equalsIgnoreCase("false") || argument.equalsIgnoreCase("no")) {
            return false;
        }

        return null;
    }

    @Override
    public String getParseMessage(WrappedSender sender, String invalid) {
        return String.format("&c%s is not a valid boolean. ", invalid);
    }

    @Override
    public <C> List<String> getSuggestions(WrappedSender sender, CommandContext<C> context, String lastWord) {
        if (lastWord.isEmpty()) {
            return this.matches;
        }

        List<String> list = Lists.newArrayList();

        for (String string : this.matches) {
            if (StringUtil.startsWithIgnoreCase(string, lastWord)) {
                list.add(string);
            }
        }

        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;
    }
}
