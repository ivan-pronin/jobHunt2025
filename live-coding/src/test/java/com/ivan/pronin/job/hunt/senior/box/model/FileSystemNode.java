package com.ivan.pronin.job.hunt.senior.box.model;

import java.util.Map;

/**
 * @author Ivan Pronin
 * @since 05.08.2025
 */
public record FileSystemNode(
    String name,
    Map<String, FileSystemNode> children
) {

}
