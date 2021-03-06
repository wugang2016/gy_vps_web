/**
 * 
 */
package com.bj.pojo;

/**
 * @author LQK
 *
 */
public enum SubTaskStatus {
    PENDING("待运行"),
    IN_PROGRESS("运行中"),
    SUCCESS("运行完成"),
    FAILED("运行失败");

    private final String text;

    SubTaskStatus(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public int index() {
    	return this.ordinal();
    }
	
	public boolean isFail() {
		return this.compareTo(FAILED) == 0;
	}
	
	public boolean allowDelete() {
		return (this.compareTo(SUCCESS) == 0 || this.compareTo(FAILED) == 0);
	}
	
	public boolean isOver() {
		return (this.compareTo(SUCCESS) == 0 || this.compareTo(FAILED) == 0);
	}
	
	public boolean allowDispatch() {
		return this.compareTo(SUCCESS) == 0;
	}
}
