/**
 * 
 */
package com.bj.pojo;

/**
 * @author LQK
 *
 */
public enum PlayStatus {
    PLAYING("播放中"),
    PAUSE("暂停"),
    DONE("播放完成"),
    FAILED("播放失败");

    private final String text;

    PlayStatus(String text) {
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
		return this.compareTo(FAILED) == 0;
	}

	public boolean allowStop() {
		return this.compareTo(PLAYING) == 0;
	}

	public boolean allowReplay() {
		return (this.compareTo(DONE) == 0 || this.compareTo(FAILED) == 0);
	}
}
