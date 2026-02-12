package elyra.task;

/**
 * Enum representing different types of tasks.
 */
public enum TaskType {
    TODO("T", "[T]"),
    DEADLINE("D", "[D]"),
    EVENT("E", "[E]");

    private final String storageCode;
    private final String uiTag;

    TaskType(String storageCode, String uiTag) {
        this.storageCode = storageCode;
        this.uiTag = uiTag;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public String getUiTag() {
        return uiTag;
    }

    /**
     * Converts a storage code to its corresponding TaskType.
     * @param code Storage code representing a task type.
     * @return Corresponding TaskType.
     */
    public static TaskType fromStorageCode(String code) {
        for (TaskType taskType : values()) {
            if (taskType.storageCode.equals(code)) {
                return taskType;
            }
        }
        throw new IllegalArgumentException("Unknown task type code: " + code);
    }
}
