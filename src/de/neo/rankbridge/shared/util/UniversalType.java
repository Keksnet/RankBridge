package de.neo.rankbridge.shared.util;

public class UniversalType<T> {
	
	private T object;
	
	public UniversalType(T object) {
		this.object = object;
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getObjectClass() {
		return (Class<T>) this.object.getClass();
	}
	
	public T getAsOriginal() {
		return this.object;
	}
	
	public Object getAsObject() {
		return this.object;
	}
	
	public String getAsString() {
		return String.valueOf(this.object);
	}
	
	public byte[] getAsBytes() {
		return getAsString().getBytes();
	}
	
	public Boolean getAsBoolean() {
		return Boolean.valueOf(getAsString());
	}
	
	public Integer getAsInteger() {
		return Integer.getInteger(getAsString());
	}
	
	public Double getAsDouble() {
		return Double.valueOf(getAsString());
	}
	
	public Long getAsLong() {
		return Long.valueOf(getAsString());
	}
	
	public Float getAsFloat() {
		return Float.valueOf(getAsString());
	}
	
	public <V> V getAsGeneric(Class<V> type) {
		return type.cast(this.object);
	}
}
