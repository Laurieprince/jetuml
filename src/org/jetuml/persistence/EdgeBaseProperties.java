package org.jetuml.persistence;

public enum EdgeBaseProperties {
	
	START_NODE("start", "integer"), END_NODE("end", "integer"), EDGE_TYPE("type", "string");
	
	private final String aLabel;
	private final String aType;
	
	EdgeBaseProperties(String pLabel, String pType)
	{ 
		aLabel = pLabel; 
		aType = pType;
	}
	
	public String getLabel()
	{ return aLabel; }
	
	public String getType()
	{ return aType; }
}
