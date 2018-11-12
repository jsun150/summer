package com.summer.service.Hystrix;

/**
 * @author Jook
 * @create 2018-11-07 10:28
 **/
public class HystirxConfig {

    private String groupKey;
    private String commandKey;
    private String poolName;
    private Integer executionTimeoutInMilliseconds = 10000;
    private Integer coreSize = 10;
    private Integer queueSizeRejectionThreshold = 500;

    public HystirxConfig (String application) {
        this.groupKey = application;
        this.commandKey = application;
        this.poolName = application;
    }

    public Integer getQueueSizeRejectionThreshold() {
        return queueSizeRejectionThreshold;
    }

    public void setQueueSizeRejectionThreshold(Integer queueSizeRejectionThreshold) {
        this.queueSizeRejectionThreshold = queueSizeRejectionThreshold;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getCommandKey() {
        return commandKey;
    }

    public void setCommandKey(String commandKey) {
        this.commandKey = commandKey;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public Integer getExecutionTimeoutInMilliseconds() {
        return executionTimeoutInMilliseconds;
    }

    public void setExecutionTimeoutInMilliseconds(Integer executionTimeoutInMilliseconds) {
        this.executionTimeoutInMilliseconds = executionTimeoutInMilliseconds;
    }

    public Integer getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(Integer coreSize) {
        this.coreSize = coreSize;
    }
}
