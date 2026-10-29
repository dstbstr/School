package com.randalld.Store;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

import com.randalld.ProgressReport;

public class LocalFileStore implements FileStore {
    private static final String RootDir = "Z:/Temp";
    private static final Executor IO_EXECUTOR = Executors.newCachedThreadPool();

    @Override
    public CompletableFuture<List<FileNode>> listBuckets() {
        return CompletableFuture.supplyAsync(() -> {
            try (Stream<Path> paths = Files.list(Path.of(RootDir))) {
                return paths
                .filter(Files::isDirectory)
                .map(p -> new FileNode(p.getFileName().toString(), "", true))
                .toList();
            } catch(IOException ex) {
                throw new CompletionException("List failed", ex);
            }
        }, IO_EXECUTOR);
    }

    @Override
    public CompletableFuture<List<FileNode>> listChildren(FileNode parent) {
        return CompletableFuture.supplyAsync(() -> {
            Path bucketRoot = Path.of(RootDir, parent.bucket());
            Path dir = Path.of(RootDir, parent.bucket(), parent.key());
            try(Stream<Path> paths = Files.list(dir)) {
                return paths.map(p -> {
                    String key = bucketRoot.relativize(p).toString().replace('\\', '/');
                    return new FileNode(parent.bucket(), key, Files.isDirectory((p)));
                })
                .toList();
            } catch(IOException ex) {
                throw new CompletionException("List children failed", ex);
            }
        }, IO_EXECUTOR);
    }

    @Override
    public CompletableFuture<Void> upload(Path src, FileNode dest, ProgressReport report) {
        Path to = toPath(dest);
        
        return CompletableFuture.runAsync(() -> {
            if(Files.exists(to) || !Files.exists(src)) {
                throw new CompletionException("Upload failed: destination exists or source does not exist", null);
            }
            try {
                Path parent = to.getParent();
                if(parent != null) {
                    Files.createDirectories(parent);
                }
                slowCopy(src, to, report);
            } catch(IOException e) {
                throw new CompletionException("Upload failed", e);
            }
        }, IO_EXECUTOR);
    }

    @Override
    public CompletableFuture<Void> download(FileNode src, Path dest, ProgressReport report) {
        Path from = toPath(src);
        return CompletableFuture.runAsync(() -> {
            if(Files.exists(dest) || !Files.exists(from) || src.isFolder()) {
                throw new CompletionException("Download failed: destination exists, source does not exist, or source is a folder", null);
            }
            try {
                Path parent = dest.getParent();
                if(parent != null) {
                    Files.createDirectories(parent);
                }

                slowCopy(from, dest, report);
            } catch(IOException e) {
                throw new CompletionException("Download failed", e);
            }
        }, IO_EXECUTOR);
    }

    @Override
    public CompletableFuture<Boolean> delete(FileNode node) {
        Path path = toPath(node);
        return CompletableFuture.supplyAsync(() -> {
            if(!Files.exists(path)) {
                return false;
            }
            try {
                Files.delete(path);
                return true;
            } catch(IOException ex) {
                throw new CompletionException("Delete failed", ex);
            }
        }, IO_EXECUTOR);
    }

    @Override
    public CompletableFuture<Boolean> deleteBucket(String name) {
        Path path = Path.of(RootDir, name);
        return CompletableFuture.supplyAsync(() -> {
            try {
                deleteRecursive(path);
                return true;
            } catch(IOException ex) {
                throw new CompletionException("Delete bucket failed", ex);
            }
        }, IO_EXECUTOR);
    }

    @Override
    public CompletableFuture<FileNode> createBucket(String name) {
        Path path = Path.of(RootDir, name);
        return CompletableFuture.supplyAsync(() -> {
            try {
                Files.createDirectory(path);
                return new FileNode(name, "", true);
            } catch(IOException ex) {
                throw new CompletionException("Create bucket failed", ex);
            }
        }, IO_EXECUTOR);
    }

    private Path toPath(FileNode node) {
        return Path.of(RootDir, node.bucket(), node.key());
    }

    private void slowCopy(Path src, Path dest, ProgressReport report) throws IOException {
        long totalBytes = Files.size(src);
        long progress = 0;
        byte[] buffer = new byte[128];
        try(
            InputStream in = Files.newInputStream(src);
            OutputStream out = Files.newOutputStream(dest);
        ) {
            int bytesRead;
            while((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                progress += bytesRead;
                report.update(progress, totalBytes);
                try {
                    Thread.sleep(50);
                } catch(InterruptedException ignore) {
                    Thread.currentThread().interrupt();
                    throw new InterruptedIOException("Copy interrupted");
                }
            }
        }
    }

    private void deleteRecursive(Path root) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if(exc != null) throw exc;
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
