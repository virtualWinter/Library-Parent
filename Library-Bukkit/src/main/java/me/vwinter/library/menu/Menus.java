package me.vwinter.library.menu;

import com.google.common.collect.Table;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Menus {

    /**
     * Easily fill and decorate an {@link Table} with {@link ItemStack}
     *
     * @param table     the table.
     * @param itemStack the itemStack.
     * @param rows      the amount of rows.
     * @param rowTypes  the row types.
     */
    public static void fill(final Table<Integer, ItemStack, MenuAction> table, final ItemStack itemStack, final int rows, RowType... rowTypes) {
        Arrays.stream(rowTypes).forEach(rowType -> {
            switch (rowType) {
                case TOP: {
                    IntStream.range(0, 9).forEach(i -> table.put(i, itemStack, clickType -> true));
                }

                case BOTTOM: {
                    IntStream.range(rows * 9 - 9, rows * 9).forEach(i -> table.put(i, itemStack, clickType -> true));
                    break;
                }

                case RIGHT: {
                    for (int i = 8; i < rows * 9; i += 9) {
                        table.put(i, itemStack, clickType -> true);
                    }
                    break;
                }

                case LEFT: {
                    for (int i = 0; i < rows * 9; i += 9) {
                        table.put(i, itemStack, clickType -> true);
                    }
                    break;
                }

                case AROUND: {
                    IntStream.range(0, 9).forEach(i -> table.put(i, itemStack, clickType -> true));
                    IntStream.range(rows * 9 - 9, rows * 9).forEach(i -> table.put(i, itemStack, clickType -> true));
                    for (int i = 8; i < rows * 9; i += 9) {
                        table.put(i, itemStack, clickType -> true);
                    }
                    for (int i = 0; i < rows * 9; i += 9) {
                        table.put(i, itemStack, clickType -> true);
                    }
                    break;
                }
            }
        });

    }

}
