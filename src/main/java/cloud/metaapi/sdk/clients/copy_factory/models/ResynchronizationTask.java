package cloud.metaapi.sdk.clients.copy_factory.models;

import cloud.metaapi.sdk.clients.models.IsoTime;

/**
 * Resynchronization task
 */
public class ResynchronizationTask {
    
    /**
     * Task type enum
     */
    public enum TaskType { CREATE_ACCOUNT, CREATE_STRATEGY, UPDATE_STRATEGY, REMOVE_STRATEGY }
    /**
     * Task status enum
     */
    public enum TaskStatus { PLANNED, EXECUTING, SYNCHRONIZING }
    
    /**
     * Task unique id
     */
    public String _id;
    /**
     * Task type
     */
    public TaskType type;
    /**
     * The time task was created at
     */
    public IsoTime createdAt;
    /**
     * Task status
     */
    public TaskStatus status;
}