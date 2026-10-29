package com.randalld.Store;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.randalld.ProgressReport;

public interface FileStore {
    public record FileNode(String bucket, String key, boolean isFolder) {
        @Override
        public String toString() {
            if(key.isEmpty()) {
                return bucket;
            } else {
                return key;
            }
        }
    }

    CompletableFuture<List<FileNode>> listBuckets();
    CompletableFuture<List<FileNode>> listChildren(FileNode parent);
    CompletableFuture<Void> upload(Path src, FileNode dest, ProgressReport report);
    CompletableFuture<Void> download(FileNode src, Path dest, ProgressReport report);
    CompletableFuture<Boolean> delete(FileNode node);
    CompletableFuture<Boolean> deleteBucket(String bucketName);
    CompletableFuture<FileNode> createBucket(String name);
}
