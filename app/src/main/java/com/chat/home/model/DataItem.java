package com.chat.home.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("unseenapi")
	private List<Object> unseenapi;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("departements_user_name")
	private DepartementsUserName departementsUserName;

	@SerializedName("_id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("type")
	private String type;

	@SerializedName("user")
	private User user;

	@SerializedName("departements_user")
	private String departementsUser;

	@SerializedName("desc")
	private String desc;

	@SerializedName("lastaction")
	private int lastaction;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setUnseenapi(List<Object> unseenapi){
		this.unseenapi = unseenapi;
	}

	public List<Object> getUnseenapi(){
		return unseenapi;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setDepartementsUserName(DepartementsUserName departementsUserName){
		this.departementsUserName = departementsUserName;
	}

	public DepartementsUserName getDepartementsUserName(){
		return departementsUserName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setDepartementsUser(String departementsUser){
		this.departementsUser = departementsUser;
	}

	public String getDepartementsUser(){
		return departementsUser;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}

	public void setLastaction(int lastaction){
		this.lastaction = lastaction;
	}

	public int getLastaction(){
		return lastaction;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",unseenapi = '" + unseenapi + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",departements_user_name = '" + departementsUserName + '\'' + 
			",_id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",type = '" + type + '\'' + 
			",user = '" + user + '\'' + 
			",departements_user = '" + departementsUser + '\'' + 
			",desc = '" + desc + '\'' + 
			",lastaction = '" + lastaction + '\'' + 
			"}";
		}
}