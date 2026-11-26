package com.finalProject.Ui;
import java.util.UUID;
import com.finalProject.Settings.AssignmentRegistry;

public record AssignmentRow(UUID id, AssignmentRegistry.Entry entry) {}
