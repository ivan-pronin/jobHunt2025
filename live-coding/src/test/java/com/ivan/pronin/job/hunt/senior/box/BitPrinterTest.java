package com.ivan.pronin.job.hunt.senior.box;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
class BitPrinterTest {

    private BitPrinter printer = new BitPrinter();

    @Test
    void testPrint(){
        printer.printBits((byte) 5);
        printer.printBits((short) 123);
        printer.printBits(42);
        printer.printBits(3.14f);
        printer.printBits(3.14);
        printer.printBits('A');
        printer.printBits(true);
        printer.printBits("Hi!");
    }


}