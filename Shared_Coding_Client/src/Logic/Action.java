package Logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Action {
	private String type;
	private String elementKey;
	private Date timeStamp;
	private String user;
	private Map<String, Object> properties;
	
	public Action(String type, String elementKey, String user) {
		super();
		this.type = type;
		this.elementKey = elementKey;
		this.timeStamp = new Date();
		this.user = user;
		this.properties = new HashMap<>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getElementKey() {
		return elementKey;
	}

	public void setElementKey(String elementKey) {
		this.elementKey = elementKey;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "Action [type=" + type + ", elementKey=" + elementKey + ", timeStamp=" + timeStamp + ", user=" + user
				+ ", properties=" + properties + "]";
	}
	
	
}
