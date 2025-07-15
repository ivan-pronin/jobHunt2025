package com.ivan.pronin.job.hunt.senior.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Ivan Pronin
 * @since 14.07.2025
 */
public class FileNode {

    public boolean isFile = false;

    public StringBuilder content = new StringBuilder();

    public Map<String, FileNode> children = new TreeMap<>(); // TreeMap для лексикографического ls()

}
