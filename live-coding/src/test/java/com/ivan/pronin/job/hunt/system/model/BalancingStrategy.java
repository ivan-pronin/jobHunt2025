package com.ivan.pronin.job.hunt.system.model;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public enum BalancingStrategy {
    ROUND_ROBIN,
    WEIGHTED,
    LEAST_LOAD,
    CONSISTENT_HASHING
}
