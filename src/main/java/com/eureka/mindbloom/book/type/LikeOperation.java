package com.eureka.mindbloom.book.type;

public enum LikeOperation {
    ADD {
        @Override
        public long calculateCount(long currentCount) {
            return currentCount + 1;
        }
    },
    REMOVE {
        @Override
        public long calculateCount(long currentCount) {
            return Math.max(0, currentCount - 1);
        }
    };

    public abstract long calculateCount(long currentCount);
}
