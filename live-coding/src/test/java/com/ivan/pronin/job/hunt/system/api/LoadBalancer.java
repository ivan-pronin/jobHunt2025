package com.ivan.pronin.job.hunt.system.api;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public interface LoadBalancer {

    void addServer(String id, int weight);

    void removeServer(String id);

    String getServer();

    void reportLoad(String id, int currentLoad); // for LEAST_LOAD strategy

}
