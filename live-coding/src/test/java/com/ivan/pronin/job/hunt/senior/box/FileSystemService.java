package com.ivan.pronin.job.hunt.senior.box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ivan.pronin.job.hunt.senior.box.model.FileSystemNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 05.08.2025
 */
public class FileSystemService {

    private static final String PATH_START_MARKER = "/";

    private final FileSystemNode root = new FileSystemNode("", new HashMap<>());

    FileSystemNode create(String path) {
        if (!path.startsWith(PATH_START_MARKER)) {
            System.err.println("Path: " + path + " doesn't start from /. Ignoring this path.");
            return null;
        }
        return createPath(path, root.children());
    }

    FileSystemNode delete(String path) {
        if (!path.startsWith(PATH_START_MARKER)) {
            System.err.println("Path: " + path + " doesn't start from /. Ignoring this path.");
            return null;
        }
        return deletePath(path, root.children());
    }

    void move(String sourcePath, String targetPath) {
        var deletedNode = delete(sourcePath);
        var createdNode = create(targetPath);
        createdNode.children().putAll(deletedNode.children());
    }

    List<String> list() {
        List<String> result = new ArrayList<>();
        dfs(root, new StringBuilder(), result);
        return result;
    }

    private void dfs(FileSystemNode node, StringBuilder paths, List<String> result) {
        int prevLength = paths.length();
        if (node != root) {
            paths.append(node.name());
            result.add(paths.toString());
        }
        for (FileSystemNode n : node.children().values()) {
            dfs(n, paths, result);
        }
        paths.setLength(prevLength);
    }

    private FileSystemNode createPath(String path, Map<String, FileSystemNode> children) {
        var paths = path.split(PATH_START_MARKER);
        if (paths.length == 2) {
            var newPath = PATH_START_MARKER + paths[1];
            return children.computeIfAbsent(newPath, key -> new FileSystemNode(newPath, new HashMap<>()));
        }
        var nextRoot = children.computeIfAbsent(PATH_START_MARKER + paths[1], key -> new FileSystemNode(paths[1], new HashMap<>())).children();
        return createPath(path.substring(paths[1].length() + 1), nextRoot);
    }

    private FileSystemNode deletePath(String path, Map<String, FileSystemNode> children) {
        var paths = path.split(PATH_START_MARKER);
        if (paths.length == 2) {
            return children.remove(PATH_START_MARKER + paths[1]);
        }
        var nextRoot = children.get(PATH_START_MARKER + paths[1]);
        if (nextRoot != null) {
            return deletePath(path.substring(paths[1].length() + 1), nextRoot.children());
        }
        return null;
    }

    @Test
    public void test() {
        create("/home");
        create("/home/user");
        create("/home/user/file.txt");
        create("/tmp");
        move("/home/user", "/tmp/user");
        delete("/home");
        assertEquals(List.of("/tmp", "/tmp/user", "/tmp/user/file.txt"), list());
    }

}
