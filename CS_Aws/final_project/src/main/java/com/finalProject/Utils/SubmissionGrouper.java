package com.finalProject.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import com.finalProject.PlagDetect.LocalPlagDetect.BestMatch;

public class SubmissionGrouper {
    private record Edge(String from, String to, double score) {}

    public static List<List<String>> groupSubmissions(Map<String, BestMatch> submissions, double threshold) {
        List<Edge> edges = new ArrayList<>();
        
        for(var entry : submissions.entrySet()) {
            edges.add(new Edge(entry.getKey(), entry.getValue().submissionName(), entry.getValue().score()));
        }

        Map<String, Set<String>> adjacencyList = new HashMap<>();
        for(var edge : edges) {
            if(edge.score() >= threshold) {
                adjacencyList.computeIfAbsent(edge.from(), k -> new HashSet<>()).add(edge.to());
                adjacencyList.computeIfAbsent(edge.to(), k -> new HashSet<>()).add(edge.from());
            }
        }

        List<List<String>> groups = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for(String node : adjacencyList.keySet()) {
            if(!visited.contains(node)) {
                List<String> group = new ArrayList<>();
                dfs(node, adjacencyList, visited, group);
                groups.add(group);
            }
        }
        return groups;
    }
    
    private static void dfs(String node, Map<String, Set<String>> adjacencyList, Set<String> visited, List<String> group) {
        visited.add(node);
        group.add(node);
        for(String neighbor : adjacencyList.getOrDefault(node, Set.of())) {
            if(!visited.contains(neighbor)) {
                dfs(neighbor, adjacencyList, visited, group);
            }
        }
    }
}
