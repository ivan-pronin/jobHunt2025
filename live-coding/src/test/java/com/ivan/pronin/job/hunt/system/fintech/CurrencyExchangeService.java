package com.ivan.pronin.job.hunt.system.fintech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Ivan Pronin
 * @since 17.07.2025
 */
public class CurrencyExchangeService {

    /*
     * --- Implementation: Currency Exchange Service ---
     * This service provides currency exchange rates and allows conversion between currencies.
     *
     */
    /*
     * The CurrencyExchangeDFS class implements a depth-first search (DFS) algorithm to find the conversion rate
     * between two currencies. It builds a graph of currency rates and uses DFS to traverse the graph
     * to find the conversion path.
     *
     */
    private static class CurrencyExchangeDFS {

        private final Map<String, Map<String, Double>> graph = new HashMap<>();

        public void addRate(String from, String to, double rate) {
            graph.putIfAbsent(from, new HashMap<>());
            graph.get(from).put(to, rate);
        }

        public double getRate(String from, String to) {
            Set<String> visited = new HashSet<>();
            Double rate = dfs(from, to, 1.0, visited);
            if (rate == null) throw new IllegalArgumentException("No conversion path");
            return rate;
        }

        private Double dfs(String current, String target, double accRate, Set<String> visited) {
            if (current.equals(target)) return accRate;
            visited.add(current);
            List<Integer> ll = new ArrayList<>();
            ll.remove(2);
            Map<String, Double> neighbors = graph.getOrDefault(current, Collections.emptyMap());
            for (Map.Entry<String, Double> entry : neighbors.entrySet()) {
                if (!visited.contains(entry.getKey())) {
                    Double res = dfs(entry.getKey(), target, accRate * entry.getValue(), visited);
                    if (res != null) return res;
                }
            }
            return null;
        }

    }

    @Test
    public void testExchange() {
        CurrencyExchangeDFS exchange = new CurrencyExchangeDFS();
        exchange.addRate("USD", "EUR", 0.9);
        exchange.addRate("EUR", "GBP", 0.8);

        assertEquals(0.72, exchange.getRate("USD", "GBP"), 1e-6);
        assertThrows(IllegalArgumentException.class, () -> exchange.getRate("USD", "INR"));
    }

}
