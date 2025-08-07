package com.ivan.pronin.job.hunt.senior.box;

import java.nio.charset.StandardCharsets;

/**
 * @author Ivan Pronin
 * @since 06.08.2025
 */
public class BitPrinter {

    public void printBits(Object obj){
        if (obj == null) {
            System.out.println("null");
            return;
        }

        if (obj instanceof Byte) {
            printBinary((Byte) obj, 8);
        } else if (obj instanceof Short) {
            printBinary((Short) obj, 16);
        } else if (obj instanceof Integer) {
            printBinary((Integer) obj, 32);
        } else if (obj instanceof Long) {
            printBinary((Long) obj, 64);
        } else if (obj instanceof Character) {
            printBinary((char) obj, 16);
        } else if (obj instanceof Float) {
            int bits = Float.floatToIntBits((Float) obj);
            printBinary(bits, 32);
        } else if (obj instanceof Double) {
            long bits = Double.doubleToLongBits((Double) obj);
            printBinary(bits, 64);
        } else if (obj instanceof Boolean) {
            printBinary(((Boolean) obj) ? 1 : 0, 1);
        } else if (obj instanceof String) {
            byte[] bytes = ((String) obj).getBytes(StandardCharsets.UTF_8);
            System.out.println("String as bytes:");
            for (byte b : bytes) {
                printBinary(b, 8);
            }
        } else {
            System.out.println("Unsupported type: " + obj.getClass().getName());
        }
    }

    private static void printBinary(long value, int width) {
        String binary = Long.toBinaryString(value);
        binary = String.format("%" + width + "s", binary).replace(' ', '0');
        System.out.println(binary);
    }

}
