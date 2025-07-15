package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.List;

import com.ivan.pronin.job.hunt.senior.model.FileNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 14.07.2025
 */
public class DesignInMemoryFileSystem588 {

    private static class TrieLikeSolution {

        private final FileNode root;

        public TrieLikeSolution() {
            root = new FileNode();
        }

        private FileNode traverse(String path) {
            String[] parts = path.split("/");
            FileNode node = root;
            for (int i = 1; i < parts.length; i++) {
                node.children.putIfAbsent(parts[i], new FileNode());
                node = node.children.get(parts[i]);
            }
            return node;
        }

        public List<String> ls(String path) {
            FileNode node = root;
            List<String> res = new ArrayList<>();
            if (!path.equals("/")) {
                String[] parts = path.split("/");
                for (int i = 1; i < parts.length; i++) {
                    node = node.children.get(parts[i]);
                }
                if (node.isFile) {
                    res.add(parts[parts.length - 1]);
                    return res;
                }
            }
            res.addAll(node.children.keySet());
            return res;
        }

        public void mkdir(String path) {
            traverse(path);
        }

        public void addContentToFile(String filePath, String content) {
            FileNode node = traverse(filePath);
            node.isFile = true;
            node.content.append(content);
        }

        public String readContentFromFile(String filePath) {
            FileNode node = traverse(filePath);
            return node.content.toString();
        }

    }

    @Test
    public void test() {
        var fs = new TrieLikeSolution();

        assertTrue(fs.ls("/").isEmpty());

        fs.mkdir("/a/b/c");
        fs.addContentToFile("/a/b/c/d", "hello");
        assertEquals(List.of("a"), fs.ls("/"));
        assertEquals(List.of("d"), fs.ls("/a/b/c/d"));
        assertEquals("hello", fs.readContentFromFile("/a/b/c/d"));

        fs.addContentToFile("/a/b/c/d", " world");
        assertEquals("hello world", fs.readContentFromFile("/a/b/c/d"));
        System.out.println("All tests passed.");
    }

}
