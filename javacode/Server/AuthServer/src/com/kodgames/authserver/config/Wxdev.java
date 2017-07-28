package com.kodgames.authserver.config;

import java.util.ArrayList;
import java.util.List;


public class Wxdev {
	String table;
	String type;
	String status;
	
	public List<Wxapp> wxapps = new ArrayList<Wxapp>();

	public boolean isContains(int appcode) {
		for (Wxapp wxapp : wxapps) { 
			if (wxapp != null && wxapp.getAppcode() == appcode)
			{
				return true;
			}
		}
		return false;
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    @Override
    public String toString() {
        return "Wxdev{" +
                "type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", wxapps=" + wxapps +
                '}';
    }

}
