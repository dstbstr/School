package com.randalld.Store;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.transfer.s3.model.DownloadFileRequest;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.TransferListener;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.progress.TransferProgressSnapshot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.randalld.ProgressReport;

public class S3FileStore implements FileStore {
    private final S3AsyncClient s3;
    private final S3TransferManager transferManager;

    private class S3ProgressListener implements TransferListener {
        private final ProgressReport report;
        S3ProgressListener(ProgressReport report) {
            this.report = report;
        }

        @Override
        public void bytesTransferred(Context.BytesTransferred context) {
            TransferProgressSnapshot snapshot = context.progressSnapshot();
            long current = snapshot.transferredBytes();
            OptionalLong opTotal = snapshot.totalBytes();
            if(opTotal.isPresent()) {
                report.update(current, opTotal.getAsLong());
            }
        }
    }

    public S3FileStore() {
        s3 = S3AsyncClient
            .builder()
            .region(Region.US_EAST_1)
            .build();
        transferManager = S3TransferManager.builder().s3Client(s3).build();
    }

    @Override
    public CompletableFuture<List<FileNode>> listBuckets() {
        try {
            return s3.listBuckets().thenApply(result -> 
                result.buckets()
                .stream()
                .map(b -> new FileNode(b.name(), "", true))
                .toList()
            );
        } catch (S3Exception e) {
            throw new CompletionException("Failed to list buckets", e);
        }
    }

    @Override
    public CompletableFuture<List<FileNode>> listChildren(FileNode parent) {
        ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(parent.bucket()).prefix(parent.key()).delimiter("/").build();

        return s3.listObjectsV2(request).thenApply(response -> {
            List<FileNode> result = new ArrayList<>();
            response.commonPrefixes().forEach(p -> result.add(new FileNode(parent.bucket(), p.prefix(), true)));
            response.contents().forEach(f -> {
                if(!f.key().equals(parent.key())) {
                    result.add(new FileNode(parent.bucket(), f.key(), false));
                }
            });
            return result;
        });
    }

    @Override
    public CompletableFuture<Void> upload(Path src, FileNode dest, ProgressReport report) {
        if(!Files.exists(src)) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Source file does not exist"));
        }

        UploadFileRequest request = UploadFileRequest.builder()
                .putObjectRequest(r -> r.bucket(dest.bucket()).key(dest.key()).build())
                .source(src)
                .addTransferListener(new S3ProgressListener(report))
                .build();
        return transferManager.uploadFile(request)
            .completionFuture()
            .thenApply(r -> null);
    }

    @Override
    public CompletableFuture<Void> download(FileStore.FileNode src, Path dest, ProgressReport report) {
        if(Files.exists(dest)) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Destination file already exists"));
        }
        DownloadFileRequest request = DownloadFileRequest.builder()
                .getObjectRequest(GetObjectRequest.builder().bucket(src.bucket()).key(src.key()).build())
                .destination(dest)
                .addTransferListener(new S3ProgressListener(report))
                .build();
        return transferManager.downloadFile(request)
            .completionFuture()
            .thenApply(r -> null);
    }

    @Override
    public CompletableFuture<Boolean> delete(FileNode node) {
        DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(node.bucket()).key(node.key()).build();
        return s3.deleteObject(request)
            .thenApply(r -> true);
    }

    @Override
    public CompletableFuture<Boolean> deleteBucket(String name) {
        DeleteBucketRequest request = DeleteBucketRequest.builder().bucket(name).build();
        return s3.deleteBucket(request)
            .thenApply(r -> true);
    }

    @Override
    public CompletableFuture<FileNode> createBucket(String name) {
        CreateBucketRequest request = CreateBucketRequest.builder().bucket(name).build();
        return s3.createBucket(request)
            .thenApply(response -> new FileNode(name, "", true));
    }
}
