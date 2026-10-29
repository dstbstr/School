package com.randalld.Commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import com.randalld.ProgressReport;
import com.randalld.Store.FileStore;
import com.randalld.Store.LocalFileStore;
import com.randalld.Store.S3FileStore;
import com.randalld.Store.FileStore.FileNode;

public class CliCommands {
    private final FileStore store;
    private final String cmd;
    private final String[] args;

    public CliCommands(String env, String cmd, String... args) {
        this.cmd = cmd.toLowerCase();
        this.args = args;
        store = env.equalsIgnoreCase("s3") ? new S3FileStore() : new LocalFileStore();
    }

    public void run() {
        switch(cmd) {
            case "list" -> list();
            case "create" -> create();
            case "delete" -> delete();
            case "upload" -> upload();
            case "download" -> download();
            default -> throw new RuntimeException("Unknown command: " + cmd);
        }
    }

    private void list() {
        String prefix = args.length > 0 ? args[0] : null;
        List<FileNode> entries;
        if(prefix == null) {
            entries = store.listBuckets().join();
        } else {
            FileNode node = toNode(prefix);
            entries = store.listChildren(node).join();
        }
        entries.stream()
            .map(e -> String.join("/", e.bucket(), e.key()))
            .forEach(System.out::println);
    }
    private void create() {
        if (args.length < 1) {
            throw new RuntimeException("Expected 'create bucket'");
        }
        String name = args[0];
        store.createBucket(name).join();
    }

    private void delete() {
        if(args.length < 1) {
            throw new RuntimeException("Expected 'delete <path>'");
        }
        FileNode node = toNode(args[0]);
        if(node.key().isEmpty()) {
            store.deleteBucket(node.bucket()).join();
        } else {
            store.delete(node).join();
        }
    }
    private void upload() {
        if(args.length < 2) {
            throw new RuntimeException("Expected upload <src> <dest>");
        }
        ProgressReport report = new ProgressReport(this::displayProgress);
        Path src = Path.of(args[0]);
        FileNode dest = toNode(args[1]);
        store.upload(src, dest, report).join();
    }

    private void download() {
        if(args.length < 2) {
            throw new RuntimeException("Expected download <src> <dest>");
        }
        ProgressReport report = new ProgressReport(this::displayProgress);
        FileNode src = toNode(args[0]);
        Path dest = Path.of(args[1]);
        store.download(src, dest, report).join();
    }

    private FileNode toNode(String path) {
        String[] parts = path.split("/");
        String bucket = parts[0];
        String[] rest = Arrays.copyOfRange(parts, 1, parts.length);
        String key = String.join("/", rest);
        boolean isFolder = Files.isDirectory(Path.of(path));
        return new FileNode(bucket, key, isFolder);
    }

     public void displayProgress(double progress) {
        int width = 40;
        int filled = (int)(progress * width);
        String bar = "[" + "=".repeat(filled) + " ".repeat(width - filled) + "]";
        System.out.printf("\r%s %5.1f%%", bar, progress * 100);
    }
}
