package de.neo.rankbridge.shared.util;

/**
 * UniversalType could be anything.
 * 
 * @author Neo8
 * @version 1.0
 * @param <T> The OriginalType
 */
public class UniversalType<T> {
	
	private T object;
	
	/**
	 * new Instance.
	 * 
	 * @param object the OriginalObject.
	 */
	public UniversalType(T object) {
		this.object = object;
	}
	
	/**
	 * Returns the Class of the Object
	 * 
	 * @return the Class of the Object.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getObjectClass() {
		return (Class<T>) this.object.getClass();
	}
	
	/**
	 * Returns the Object as Original.
	 * 
	 * @return the Original.
	 */
	public T getAsOriginal() {
		return this.object;
	}
	
	/**
	 * Returns the Object as Object.
	 * 
	 * @return the Object.
	 */
	public Object getAsObject() {
		return this.object;
	}
	
	/**
	 * Returns the Object as String.
	 * 
	 * @return the String.
	 */
	public String getAsString() {
		return String.valueOf(this.object);
	}
	
	/**
	 * Returns the Object as ByteArray.
	 * 
	 * @return the ByteArray.
	 */
	public byte[] getAsBytes() {
		return getAsString().getBytes();
	}
	
	/**
	 * Returns the Object as Boolean.
	 * 
	 * @return the Boolean.
	 */
	public Boolean getAsBoolean() {
		return Boolean.valueOf(getAsString());
	}
	
	/**
	 * Returns the Object as Integer.
	 * 
	 * @return the Integer.
	 */
	public Integer getAsInteger() {
		return Integer.getInteger(getAsString());
	}
	
	/**
	 * Returns the Object as Double.
	 * 
	 * @return the Double.
	 */
	public Double getAsDouble() {
		return Double.valueOf(getAsString());
	}
	
	/**
	 * Returns the Object as Long.
	 * 
	 * @return the Long.
	 */
	public Long getAsLong() {
		return Long.valueOf(getAsString());
	}
	
	/**
	 * Returns the Object as Float.
	 * 
	 * @return the Float.
	 */
	public Float getAsFloat() {
		return Float.valueOf(getAsString());
	}
	
	/**
	 * Returns the Object as you wish.
	 * 
	 * @param <V> the type of the object.
	 * @param type the type to get the original.
	 * @return the object as you wish.
	 */
	public <V> V getAsGeneric(Class<V> type) {
		return type.cast(this.object);
	}
}
