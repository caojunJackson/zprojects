package com.android.entity;



public class PlayMessage {
	private String name;
	private String path;
	private long duration;
	private long size;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public PlayMessage(String name, String path, long duration, long size) {
		super();
		this.name = name;
		this.path = path;
		this.duration = duration;
		this.size = size;
	}
	public PlayMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
