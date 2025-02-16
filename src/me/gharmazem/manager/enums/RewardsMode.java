package me.gharmazem.manager.enums;

public enum RewardsMode {

    SORTER,
    ALL_BELOW;

    public static RewardsMode mode(String mode) {
        try {
            return RewardsMode.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SORTER;
        }
    }
}
