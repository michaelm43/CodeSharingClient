package Logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Action {
	private String type;
	private Key element;
	private Date timeStamp;
	private String user;
	private Map<String, Object> properties;
	
	public Action(String type, String creator, String name, String user) {
		super();
		this.type = type;
		this.element = new Key();
		this.element.setUser(creator);
		this.element.setId(name);
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

	public Key getElementKey() {
		return element;
	}

	public void setElementKey(Key elementKey) {
		this.element = elementKey;
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
		return "Action [type=" + type + ", elementKey=" + element + ", timeStamp=" + timeStamp + ", user=" + user
				+ ", properties=" + properties + "]";
	}
	
	
}
