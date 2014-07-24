/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE
 * CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT.
 * PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY
 * DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE
 * AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE"
 * BUTTON AT THE BOTTOM OF THIS PAGE. Specification: JSR-354 Money and Currency
 * API ("Specification") Copyright (c) 2012-2013, Credit Suisse All rights
 * reserved.
 */
package javax.money;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;

/**
 * Represents a general context of data targeting an item of type {@code Q}. Contexts are used to add arbitrary
 * data that cannot be be mapped in a atandard way to the money API, e.g. use case or customer specific
 * extensions os specialities.<p>
 * Superclasses of this class must be final, immutable, serializable and thread-safe.
 */
public abstract class AbstractContext implements Serializable{

    /** Key for storing the target providers to be queried */
    public static final String PROVIDER = "provider";

    /** Key name for the timestamp attribute. */
    public static final String TIMESTAMP = "timestamp";

    /**
     * The data map containing all values.
     */
    private final Map<Class<?>,Map<Object,Object>> data = new HashMap<>();

    /**
     * Private constructor, used by {@link AbstractContextBuilder}.
     *
     * @param builder the Builder.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected AbstractContext(AbstractContextBuilder<?,?> builder){
        for(Map.Entry<Class,Map<Object,Object>> en : ((Map<Class,Map<Object,Object>>) builder.data).entrySet()){
            Map<Object,Object> presentMap = (Map<Object,Object>) this.data.get(en.getKey());
            if(presentMap == null){
                presentMap = new HashMap<>(en.getValue());
                this.data.put(en.getKey(), presentMap);
            }else{
                presentMap.putAll(en.getValue());
            }
        }
    }

    /**
     * Get the present keys for a given attribute type.
     *
     * @param type The attribute type, not null.
     * @return all present keys of attributes of the (exact) given type, never null.
     */
    public Set<Object> getKeys(Class<?> type){
        Map<Object,Object> values = this.data.get(type);
        if(values != null){
            return values.keySet();
        }
        return Collections.emptySet();
    }

    /**
     * Get all currently present attribute types.
     *
     * @return all currently present attribute types, never null.
     */
    public Set<Class<?>> getTypes(){
        return this.data.keySet();
    }

    /**
     * Access an attribute.
     *
     * @param type the attribute's type, not {@code null}
     * @param key  the attribute's key, not {@code null}
     * @return the attribute value, or {@code null}.
     */
    // Type safe cast
    @SuppressWarnings("unchecked")
    public <T> T getAny(Object key, Class<T> type, T defaultValue){
        Map<Object,Object> values = this.data.get(type);
        Object value = null;
        if(values != null){
            value = values.get(key);
        }
        if(value != null){
            return (T) value;
        }
        return defaultValue;
    }

    /**
     * Access an attribute.
     *
     * @param type the attribute's type, not {@code null}
     * @param key  the attribute's key, not {@code null}
     * @return the attribute value, or {@code null}.
     */
    public <T> T getAny(Object key, Class<T> type){
        return getAny(key, type, null);
    }

    /**
     * Access an attribute, hereby using the class name as key.
     *
     * @param type the attribute's type, not {@code null}
     * @return the attribute value, or {@code null}.
     */
    public <T> T get(Class<T> type){
        return getAny(type, type);
    }

    /**
     * Access a single attribute, also providing a default value.
     *
     * @param type         the attribute's type, not {@code null}.
     * @param defaultValue the default value, can also be {@code null}.
     * @return the attribute's value, or the {@code defaultValue} passed, if no
     * such attribute is present.
     */
    public <T> T get(Class<T> type, T defaultValue){
        return Optional.ofNullable(get(type)).orElse(defaultValue);
    }


    /**
     * Access a Long attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public Long getLong(Object key){
        return getLong(key, null);
    }

    /**
     * Access a Long attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public Long getLong(Object key, Long defaultValue){
        return getAny(key, Long.class, defaultValue);
    }


    /**
     * Access a Float attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public Float getFloat(Object key){
        return getFloat(key, null);
    }

    /**
     * Access a Float attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public Float getFloat(Object key, Float defaultValue){
        return getAny(key, Float.class, defaultValue);
    }

    /**
     * Access an Integer attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public Integer getInt(Object key){
        return getInt(key, null);
    }

    /**
     * Access an Integer attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public Integer getInt(Object key, Integer defaultValue){
        return getAny(key, Integer.class, defaultValue);
    }

    /**
     * Access a Boolean attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public Boolean getBoolean(Object key){
        return getBoolean(key, null);
    }

    /**
     * Access a Boolean attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public Boolean getBoolean(Object key, Boolean defaultValue){
        return getAny(key, Boolean.class, defaultValue);
    }

    /**
     * Access a Double attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public Double getDouble(Object key){
        return getDouble(key, null);
    }

    /**
     * Access an Double attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public Double getDouble(Object key, Double defaultValue){
        return getAny(key, Double.class, defaultValue);
    }

    /**
     * Access a String attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public String getText(Object key){
        return getText(key, null);
    }

    /**
     * Access a String attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public String getText(Object key, String defaultValue){
        return getAny(key, String.class, defaultValue);
    }


    /**
     * Access a String attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public Character getChar(Object key){
        return getChar(key, null);
    }

    /**
     * Access a Character attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public Character getChar(Object key, Character defaultValue){
        return getAny(key, Character.class, defaultValue);
    }

    /**
     * Access a Collection attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public Collection<?> getCollection(Object key){
        return getCollection(key, null);
    }

    /**
     * Access a Collection attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public <T> Collection<T> getCollection(Object key, Collection<T> defaultValue){
        return getAny(key, Collection.class, defaultValue);
    }

    /**
     * Access a List attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public List<?> getList(Object key){
        return getList(key, null);
    }

    /**
     * Access a Collection attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public <T> List<T> getList(Object key, List<T> defaultValue){
        return getAny(key, List.class, defaultValue);
    }

    /**
     * Access a Set attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public Set<?> getSet(Object key){
        return getSet(key, null);
    }

    /**
     * Access a Set attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public <T> Set<T> getSet(Object key, Set<T> defaultValue){
        return getAny(key, Set.class, defaultValue);
    }

    /**
     * Access a Map attribute.
     *
     * @param key the attribute's key, not null.
     * @return the value, or null.
     */
    public Map<?,?> getMap(Object key){
        return getMap(key, null);
    }

    /**
     * Access a Map attribute.
     *
     * @param key          the attribute's key, not null.
     * @param defaultValue the default value returned, if the attribute is not present.
     * @return the value, or default value.
     */
    public <K, V> Map<K,V> getMap(Object key, Map<K,V> defaultValue){
        return getAny(key, Map.class, defaultValue);
    }

    /**
     * Get the provider name of this context.
     *
     * @return the provider name, or null.
     */
    public String getProvider(){
        return getText(PROVIDER, null);
    }

    /**
     * Get the current target timestamp of the query in UTC milliseconds.  If not set it tries to create an
     * UTC timestamp from #getTimestamp(). This allows to select historical roundings that were valid in the
     * past. Its implementation specific, to what extend historical roundings are available. By default if this
     * property is not set always current {@link  javax.money.MonetaryRounding} instances are provided.
     *
     * @return the timestamp in millis, or null.
     */
    public Long getTimestampMillis(){
        Long value = getAny(TIMESTAMP, Long.class, null);
        if(Objects.isNull(value)){
            TemporalAccessor acc = getTimestamp();
            if(Objects.nonNull(acc)){
                return (acc.getLong(ChronoField.INSTANT_SECONDS) * 1000L) + acc.getLong(ChronoField.MILLI_OF_SECOND);
            }
        }
        return value;
    }

    /**
     * Get the current target timestamp of the query. If not set it tries to create an Instant from
     * #getTimestampMillis(). This allows to select historical roundings that were valid in the
     * past. Its implementation specific, to what extend historical roundings are available. By default if this
     * property is not set always current {@link  javax.money.MonetaryRounding} instances are provided.
     *
     * @return the current timestamp, or null.
     */
    public TemporalAccessor getTimestamp(){
        TemporalAccessor acc = getAny(TIMESTAMP, TemporalAccessor.class, null);
        if(Objects.isNull(acc)){
            Long value = getAny(TIMESTAMP, Long.class, null);
            if(Objects.nonNull(value)){
                acc = Instant.ofEpochMilli(value);
            }
        }
        return acc;
    }

    /**
     * Checks if the current instance has no attributes set. This is often the cases, when used in default cases.
     *
     * @return true, if no attributes are set.
     */
    public boolean isEmpty(){
        return this.data.isEmpty();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode(){
        return Objects.hash(data);
    }

    /**
     * Access all the values present.
     * @param type the type used.
     * @return
     */
    public Map<Object,Object> getValues(Class type){
        return this.data.get(type);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }
        if(obj instanceof AbstractContext){
            AbstractContext other = (AbstractContext) obj;
            return Objects.equals(data, other.data);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see Object#toString()
     */
    @Override
    public String toString(){
        StringBuilder attrsBuilder = new StringBuilder();
        for(Map.Entry<Class<?>,Map<Object,Object>> en : this.data.entrySet()){
            Map<Object,Object> sortedMap = new TreeMap<>(new Comparator<Object>(){
                @Override
                public int compare(Object o1, Object o2){
                    return o1.toString().compareTo(o2.toString());
                }
            });
            sortedMap.putAll(en.getValue());
            for(Map.Entry<Object,Object> entry : sortedMap.entrySet()){
                Object key = entry.getKey();
                attrsBuilder.append("  ");
                if(key.getClass() == Class.class){
                    attrsBuilder.append(((Class) key).getName());
                }else{
                    attrsBuilder.append(key);
                }
                attrsBuilder.append('[');
                if(en.getKey().getName().startsWith("java.lang.")){
                    attrsBuilder.append(en.getKey().getName().substring("java.lang.".length()));
                }else{
                    attrsBuilder.append(en.getKey().getName());
                }
                attrsBuilder.append("]=");
                attrsBuilder.append(entry.getValue()).append('\n');
            }
        }
        return getClass().getSimpleName() + " (\n" + attrsBuilder.toString() + ')';
    }


    /**
     * This interface defines the common generic parts of a query. Queries are used to pass complex parameters sets
     * to lookup monetary artifacts, e.g. {@link javax.money.MonetaryAmountFactory},
     * {@link javax.money.MonetaryRounding},
     * {@link javax.money.CurrencyUnit}, {@link javax.money.convert.ExchangeRateProvider} and {@link javax.money.convert
     * .CurrencyConversion}.
     * <p>
     * Instances of this class are not thread-safe and not serializable.
     */
    public static abstract class AbstractContextBuilder<B extends AbstractContextBuilder, C extends AbstractContext>{

        /**
         * serialVersionUID.
         */
        private static final long serialVersionUID = 5579720004786348255L;

        /**
         * The data map containing all values.
         */
        private final Map<Class,Map<Object,Object>> data = new HashMap<>();

        /**
         * Apply all attributes on the given context.
         *
         * @param context             the context to be applied, not null.
         * @param overwriteDuplicates flag, if existing entries should be overwritten.
         * @return this Builder, for chaining
         */
        @SuppressWarnings("unchecked")
        public B importContext(AbstractContext context, boolean overwriteDuplicates){
            for(Class<?> type : context.getTypes()){
                Map<Object,Object> values = context.getValues(type);
                Map<Object,Object> presentMap = this.data.get(type);
                if(presentMap == null){
                    this.data.put(type, new HashMap<>(values));
                }else{
                    if(overwriteDuplicates){
                        presentMap.putAll(values);
                    }else{
                        for(Map.Entry<Object,Object> en : values.entrySet()){
                            presentMap.putIfAbsent(en.getKey(), en.getValue());
                        }
                    }
                }
            }
            return (B) this;
        }

        /**
         * Apply all attributes on the given context, hereby existing entries are preserved.
         *
         * @param context the context to be applied, not null.
         * @return this Builder, for chaining
         * @see #importContext(AbstractContext, boolean)
         */
        public B importContext(AbstractContext context){
            Objects.requireNonNull(context);
            return importContext(context, false);
        }

        /**
         * Sets an Integer attribute.
         *
         * @param key   the key, non null.
         * @param value the value
         * @return the Builder, for chaining.
         */
        public B set(Object key, int value){
            Objects.requireNonNull(value);
            return set(key, value, Integer.class);
        }


        /**
         * Sets an Boolean attribute.
         *
         * @param key   the key, non null.
         * @param value the value
         * @return the Builder, for chaining.
         */
        public B set(Object key, boolean value){
            Objects.requireNonNull(value);
            return set(key, value, Boolean.class);
        }


        /**
         * Sets an Long attribute.
         *
         * @param key   the key, non null.
         * @param value the value
         * @return the Builder, for chaining.
         */
        public B set(Object key, long value){
            Objects.requireNonNull(value);
            return set(key, value, Long.class);
        }


        /**
         * Sets an Float attribute.
         *
         * @param key   the key, non null.
         * @param value the value
         * @return the Builder, for chaining.
         */
        public B set(Object key, float value){
            Objects.requireNonNull(value);
            return set(key, value, Float.class);
        }

        /**
         * Sets an Double attribute.
         *
         * @param key   the key, non null.
         * @param value the value
         * @return the Builder, for chaining.
         */
        public B set(Object key, double value){
            Objects.requireNonNull(value);
            return set(key, value, Double.class);
        }


        /**
         * Sets an Character attribute.
         *
         * @param key   the key, non null.
         * @param value the value
         * @return the Builder, for chaining.
         */
        public B set(Object key, char value){
            Objects.requireNonNull(value);
            return set(key, value, Character.class);
        }

        /**
         * Sets an String attribute.
         *
         * @param key   the key, non null.
         * @param value the value
         * @return the Builder, for chaining.
         */
        public B set(Object key, String value){
            Objects.requireNonNull(value);
            return set(key, value, String.class);
        }


        /**
         * Sets an attribute, using {@code attribute.getClass()} as attribute
         * <i>type</i> and {@code attribute.getClass().getName()} as attribute
         * <i>name</i>.
         *
         * @param value the attribute value
         * @return this Builder, for chaining
         */
        public B set(Object value){
            return set(value.getClass(), value, value.getClass());
        }

        /**
         * Sets an attribute, using {@code attribute.getClass()} as attribute
         * <i>type</i> and {@code attribute.getClass().getName()} as attribute
         * <i>name</i>.
         *
         * @param value the attribute value
         *              * @param type
         *              the attribute's type
         * @return this Builder, for chaining
         */
        public <T> B set(T value, Class<? extends T> type){
            Objects.requireNonNull(type);
            Objects.requireNonNull(value);
            return set(type, value, type);
        }


        /**
         * Sets an attribute, using {@code attribute.getClass()} as attribute
         * <i>type</i>.
         *
         * @param value the attribute value
         * @param key   the attribute's key, not {@code null}
         * @return this Builder, for chaining
         */
        public B set(Object key, Object value){
            return set(key, value, value.getClass());
        }

        /**
         * Sets a collection as attribute.
         *
         * @param value the attribute value
         * @param key   the attribute's key, not {@code null}
         * @return this Builder, for chaining
         */
        public B setCollection(Object key, Collection<?> value){
            return set(key, value, Collection.class);
        }

        /**
         * Sets a List as attribute.
         *
         * @param value the attribute value
         * @param key   the attribute's key, not {@code null}
         * @return this Builder, for chaining
         */
        public B setList(Object key, List<?> value){
            return set(key, value, List.class);
        }

        /**
         * Sets a Map as attribute.
         *
         * @param value the attribute value
         * @param key   the attribute's key, not {@code null}
         * @return this Builder, for chaining
         */
        public B setMap(Object key, Map<?,?> value){
            return set(key, value, Map.class);
        }

        /**
         * Sets a Set as attribute.
         *
         * @param value the attribute value
         * @param key   the attribute's key, not {@code null}
         * @return this Builder, for chaining
         */
        public B setSet(Object key, Set<?> value){
            return set(key, value, Set.class);
        }

        /**
         * Sets the provider.
         *
         * @param provider the provider, not null.
         * @return the Builder for chaining
         */
        public B setProvider(String provider){
            Objects.requireNonNull(provider);
            set(PROVIDER, provider);
            return (B) this;
        }

        /**
         * Set the target timestamp in UTC millis. This allows to select historical roundings that were valid in the
         * past. Its implementation specific, to what extend historical roundings are available. By default if this
         * property is not set always current {@link  javax.money.MonetaryRounding} instances are provided.
         *
         * @param timestamp the target timestamp
         * @return this instance for chaining
         * @see #setTimestamp(java.time.temporal.TemporalAccessor)
         */
        public B setTimestampMillis(long timestamp){
            set(TIMESTAMP, timestamp);
            return (B)this;
        }

        /**
         * Set the target timestamp as {@link java.time.temporal.TemporalAccessor}. This allows to select historical
         * roundings that were valid in the past. Its implementation specific, to what extend historical roundings
         * are available. By default if this property is not set always current {@link  javax.money.MonetaryRounding}
         * instances are provided.
         *
         * @param timestamp the target timestamp
         * @return this instance for chaining
         * @see #setTimestampMillis(long)
         */
        public B setTimestamp(TemporalAccessor timestamp){
            Objects.requireNonNull(timestamp);
            set(TIMESTAMP, timestamp, TemporalAccessor.class);
            return (B)this;
        }

        /**
         * Removes an entry of a certain type. This can be useful, when a context is initialized with another
         * existing context, but only subset of the entries should be visible. FOr example {@code removeAttributes
         * (String.class, "a", "b", "c")} removes all textual attributes named 'a','b' and 'c'.
         *
         * @param keys the keys
         * @param type the attribute's type.
         * @return this Builder, for chaining
         */
        public B removeAttributes(Class<?> type, Object... keys){
            Map<Object,Object> values = this.data.get(type);
            if(!Objects.isNull(values)){
                for(Object key : keys){
                    values.remove(key);
                }
            }
            return (B) this;
        }

        /**
         * Removes all entries of a certain type. This can be useful, when a context is initialized with another
         * existing context, but only
         * subset of the entries should be visible.
         *
         * @param type the attribute's type.
         * @return this Builder, for chaining
         */
        public B removeAll(Class<?> type){
            this.data.remove(type);
            return (B) this;
        }

        /**
         * Sets an attribute.
         *
         * @param value the attribute's value
         * @param key   the attribute's key
         * @param type  the attribute's type
         * @return this Builder, for chaining
         */
        @SuppressWarnings("unchecked")
        public <T> B set(Object key, T value, Class<? extends T> type){
            Map<Object,Object> values = this.data.get(type);
            if(Objects.isNull(values)){
                values = new HashMap<>();
                this.data.put(type, values);
            }
            values.put(key, value);
            return (B) this;
        }



        /**
         * Creates a new {@link AbstractContext} with the data from this Builder
         * instance.
         *
         * @return a new {@link AbstractContext}. never {@code null}.
         */
        public abstract C build();

        /*
         * (non-Javadoc)
         *
         * @see Object#toString()
         */
        @Override
        public String toString(){
            StringBuilder attrsBuilder = new StringBuilder();
            for(Map.Entry<Class,Map<Object,Object>> en : ((Map<Class,Map<Object,Object>>) this.data).entrySet()){
                Map<Object,Object> sortedMap = new TreeMap<>(new Comparator<Object>(){
                    @Override
                    public int compare(Object o1, Object o2){
                        return o1.toString().compareTo(o2.toString());
                    }
                });
                sortedMap.putAll(en.getValue());
                for(Map.Entry<Object,Object> entry : sortedMap.entrySet()){
                    Object key = entry.getKey();
                    attrsBuilder.append("  ");
                    if(key.getClass() == Class.class){
                        String className = ((Class) key).getName();
                        if(className.startsWith("java.lang.")){
                            className = className.substring("java.lang.".length());
                        }
                        attrsBuilder.append(className);
                    }else{
                        attrsBuilder.append(key);
                    }
                    attrsBuilder.append('[');
                    String typeName = en.getKey().getName();
                    if(typeName.startsWith("java.lang.")){
                        typeName = typeName.substring("java.lang.".length());
                    }
                    attrsBuilder.append(typeName);
                    attrsBuilder.append("]=");
                    attrsBuilder.append(entry.getValue()).append('\n');
                }
            }
            return getClass().getSimpleName() + " [attributes:\n" + attrsBuilder.toString() + ']';
        }
    }

}
