package hu.kleatech.infrendsz.model;

import java.util.Map;

public interface StorageUnit {
    Map<StorageUnit, Integer> getComponents();
    String toMinimalString();
    String getName();
}
