package com.ki11erwolf.resynth.util;

import com.google.gson.*;
import com.ki11erwolf.resynth.ResynthMod;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

/**
 * The JSerializer is a set of classes that simplifies the creation of objects
 * built to be serialized and deserialized to and from Json. The JSerializer
 * includes the complete set of classes needed to create a serializable object
 * and a serializer to handle the object.
 *
 * <p/> Beginning with the {@link JSerializable} interface, a simple interface
 * which needs to be implemented by the serializable object which stores the
 * data. Next is the {@link JSerializer} class, which needs to be inherited by
 * the class responsible for handling the logic of serializing and deserializing
 * the data held by the object instance to and from Json format. It's suggested
 * that both classes, JSerializer and JSerializable object, be stored in the same
 * java file, with the JSerializer as a private inner class contained in
 * JSerializable as a top level class. The JSerializer class should be a publicly
 * accessible singleton instance.
 *
 * <p/> In addition to the {@link JSerializable} and {@link JSerializer} classes
 * which are designed for inheritance. The set of classes also includes the {@link
 * JSerialData} and {@link JSerialDataIO} instance classes, which together make
 * up the serialized Json container used to store the data of a JSerializable object
 * and pass it around. All {@link JSerializable} objects are serialized to {@link
 * JSerialData} objects, and vice versa. The {@link JSerialDataIO} is given to
 * implementations of JSerializers that allows reading and writing Json data to a
 * {@link JSerialData} object.
 *
 * @param <T> the specific {@link JSerializable} class implementation that this
 *           JSerializer was designed to serialize and deserialize.
 */
public abstract class JSerializer<T extends JSerializer.JSerializable<T>> {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    /**
     * The String name that identifies this JSerializer with the
     * specific {@link JSerialData} it serializes and deserializes.
     * A {@link JSerialData} object and a {@link JSerializer}
     * are linked using this identifier. <b>If the identifying
     * name of the JSerializer changes, it won't be able to read
     * old JSerialData.</b>
     */
    private final String identification;

    /**
     * The version number of this specific implementation of JSerializer,
     * allowing different and suttle implementations of this specific
     * JSerializer to exist. The version number also marks the serialized
     * data written with the version number, allowing data to be identified.
     */
    private final int version;

    /**
     * Creates a new JSerializer, or rather, creates a specific child class
     * implementation for serializing and deserializing a specific {@link
     * JSerializable}. The name of the JSerializer identifies its serialized
     * Json, so be careful when changing it. The version of this JSerializer
     * is always {@code 1}.
     *
     * @param identification The String name that identifies this JSerializer
     * with the specific {@link JSerialData} it serializes and deserializes
     * to and from Json. The name is used to match Json data and JSerializers.
     */
    public JSerializer(String identification) {
        this(identification, 1);
    }

    /**
     * Creates a new JSerializer, or rather, creates a specific child class
     * implementation for serializing and deserializing a specific {@link
     * JSerializable}. The name of the JSerializer identifies its serialized
     * Json, so be careful when changing it.
     *
     * @param identification The String name that identifies this JSerializer
     * with the specific {@link JSerialData} it serializes and deserializes
     * to and from Json. The name is used to match Json data and JSerializers.
     * @param version the version number of this specific version of the
     * JSerializer implementation. If the data handled by this JSerializer
     * ever changes, it's wise to increment the version number.
     */
    public JSerializer(String identification, int version) {
        if(Objects.requireNonNull(identification).isEmpty())
            throw new IllegalArgumentException("An empty String is not valid as JSerializer identification.");

        this.identification = identification;
        this.version = version > 0 ? version : 1;
    }

    // Own Data

    /**
     * @return The String name that identifies this JSerializer with the
     * specific {@link JSerialData} it serializes and deserializes.
     * A {@link JSerialData} object and a {@link JSerializer}
     * are linked using this identifier. <b>If the identifying
     * name of the JSerializer changes, it won't be able to read
     *  old JSerialData.</b>
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * @return The version number of this specific implementation of JSerializer,
     * allowing different and suttle implementations of this specific
     * JSerializer to exist. The version number also marks the serialized
     * data written with the version number, allowing data to be identified.
     */
    public int getVersion() {
        return version;
    }

    /**
     * @return a string containing the name of the class, as well as {@link
     * #identification} and {@link #version}.
     */
    @Override
    public String toString() {
        return String.format("JSerializer[identification=%s, version=%s]", identification, version);
    }

    // Serialize & Deserialize API

    /**
     * The serialize method for this specific JSerializer implementation, used
     * to serialize, or convert, the {@code object} passed in into a {@link
     * JSerialData} object containing the given objects data as a {@link
     * JsonObject} which can then be saved to file, sent over the network, or
     * whatever else.
     *
     * <p/> The given {@code object} to be serialized must be an implementation
     * of {@link JSerializable} and it must be the specific implementation type
     * that this JSerializer was designed to serialize & deserialize.
     *
     * @param object the JSerializable implementation, for this specific
     * JSerializer, to be serialized.
     * @return a {@link JSerialData} object containing all the data of the given
     * object to serialize, which can then be used to recreate the object as it was.
     * @throws SerializeException if for any reason the given object cannot be
     * serialized. If an exception is the cause, it will be included in the
     * JSerializeException.
     */
    public JSerialData serializeObject(T object) throws SerializeException {
        return iserialize(object);
    }

    /**
     * Deserializes an object instance of the {@link JSerializable} implementation made
     * for this JSerializer, from a {@link JSerialData} object instance of a previously
     * serialized object, reconstructed into a new object containing the same data.
     *
     * <p/> The {@link JSerialData} serialized object provided must match this JSerializers
     * {@link #identification} <b>and</b> {@link #version}. If the identification of the
     * JSerialData does not match this JSerializers, the operation will fail. Additionally,
     * the version numbers must match as well, or the implementations {@link
     * #fixVersionMissmatch(JSerialDataIO, int, int)} will be called in an attempt to fix
     * any data compatability issues. Care should be taken as {@link
     * #fixVersionMissmatch(JSerialDataIO, int, int)} throws a {@link UnsupportedOperationException}
     * by default.
     *
     * <p/> <b>Use with care: {@code suggestedInstance} object.</b> This specific deserialize
     * method implementation allows suggesting that the JSerializer implementation use the
     * provided object instance instead of creating a brand new one. <b>However this is merely
     * a suggestion</b> and the specific implementation is <b>allowed to freely ignore the
     * suggestion.</b> This means that the suggested instance should not be expected to have
     * been changed or modified in any way. The specific JSerializer implementation should be
     * consulted in such situations. <b>Perfer the returned object instance to the suggested
     * object instance!</b>
     *
     * @param data the {@link JSerialData} object holding the data of a previously serialized
     * object instance, which is to be reconstructed into a new object instance, replicating
     * the old object.
     * @param suggestedInstance an instance of the object type to be created from deserialization,
     * which is suggested to be used by the specific implementation, if it so wishes.
     * @return the {@link JSerialData} serialized data reconstructed into an object instance. This
     * object instance could either be a new object instance or it may the {@code suggestedInstance}
     * provided.
     * @throws DataMissmatchException if the given {@link JSerialData} serialized data does not match
     * the serialized data created by this JSerializer. This happens when the {@link #identification}
     * of the JSerializer and the {@link JSerialData} serialized data do not match.
     * @throws SerializeException if for any reason the given object cannot be  deserialized. If an
     * exception is the cause, it will be included in the JSerializeException.
     */
    public T deserializeData(JSerialData data, T suggestedInstance) throws DataMissmatchException, SerializeException {
        return ideserialize(data, suggestedInstance);
    }

    /**
     * Deserializes an object instance of the {@link JSerializable} implementation made
     * for this JSerializer, from a {@link JSerialData} object instance of a previously
     * serialized object, reconstructed into a new object containing the same data.
     *
     * <p/> The {@link JSerialData} serialized object provided must match this JSerializers
     * {@link #identification} <b>and</b> {@link #version}. If the identification of the
     * JSerialData does not match this JSerializers, the operation will fail. Additionally,
     * the version numbers must match as well, or the implementations {@link
     * #fixVersionMissmatch(JSerialDataIO, int, int)} will be called in an attempt to fix
     * any data compatability issues. Care should be taken as {@link
     * #fixVersionMissmatch(JSerialDataIO, int, int)} throws a {@link UnsupportedOperationException}
     * by default.
     *
     * @param data the {@link JSerialData} object holding the data of a previously serialized
     * object instance, which is to be reconstructed into a new object instance, replicating
     * the old object.
     * @return the {@link JSerialData} serialized data reconstructed into a newly constructed
     * object instance.
     * @throws DataMissmatchException if the given {@link JSerialData} serialized data does not match
     * the serialized data created by this JSerializer. This happens when the {@link #identification}
     * of the JSerializer and the {@link JSerialData} serialized data do not match.
     * @throws SerializeException if for any reason the given object cannot be deserialized. If an
     * exception is the cause, it will be included in the JSerializeException.
     */
    public T deserializeData(JSerialData data) throws DataMissmatchException, SerializeException {
        T newObject;

        try {
            if((newObject = createInstance()) == null)
                throw new NullPointerException(toString() + " method 'createInstance()' gave a null JSerializable object .");
        } catch (Exception cause) {
            throw new SerializeException("Failed to create new JSerializable during deserialization.", cause);
        }

        return ideserialize(data, newObject);
    }

    // Abstract/Overridable methods for inheriting implementations.

    /**
     * Copies the data from the given JSerializable object instance, meant for this
     * specific JSerializer, from the objects variables and stores it in a new {@link
     * JSerialData} object instance which contains all the data in Json format -
     * effectively serializing the object.
     *
     * <p/> This is an abstract method meant to be overriden and implemented by more
     * specialized JSerializers that handle specific {@link JSerializable}
     * implementations. Delegates the responsibility of copying the data from the object
     * instance into a serialized {@link JSerialData} object - letting the specialized
     * JSerializer implementation handle how it serializes its data and the logic of it all.
     *
     * <p/> Object instance data is copied over to a {@link JSerialData} object through
     * the use of a {@link JSerialDataIO} object, which provides the means and methods
     * required to read from & write to the specific JSerialData object.
     *
     * <p/> In the event of failure, corrupt or missing data, or any other reason where
     * the object cannot be serialized, an appropriate and detailed Exception should be
     * raised. It will be wrapped and passed up the stack where it can be handled
     * correctly. It's better to have the serialization fail-fast with a detailed error
     * message, rather than let incomplete or otherwise invalid serialized data be allowed
     * to cause untold havoc elsewhere later down the line.
     *
     * @param object the JSerializable object instance, capable of being serialized by
     * this JSerializer, to be serialized.
     * @param dataIO the {@link JSerialDataIO} object which provides the means & methods
     * needed to read data from and write data to the {@link JSerialData} object containing
     * the serialized object data.
     * @throws Exception if an issue arrised which prevented the object from being serialized
     * correctly. If an Exception is raised, the objects data {@link JSerialData} should be
     * considered invalid and unfit for use.
     */
    protected abstract void objectToData(T object, JSerialDataIO dataIO) throws Exception;

    /**
     * Reads the data for the given {@link JSerializable} object instance, meant for this
     * specific {@link JSerializer}, from the given {@link JSerialData} object instance,
     * that stores serialized object data in known format from a previously serialized object,
     * as a {@link JSerialDataIO} object, and copies it to an object instance of the specific
     * JSerializable implementation - effectively deserializing the object. The deserialized
     * object instance may either be a brand new constructed object, or the object suggested
     * with the {@code suggestedObject} paramater. The choice is up to the specific {@link
     * JSerializer} implementation to make.
     *
     * <p/> This is an abstract method meant to be overriden and implemented by more
     * specialized JSerializers that handle specific {@link JSerializable} implementations.
     * Delegates the responsibility of copying the serialized data from the {@link JSerialData}
     * object instance into the into the {@link JSerializable} object instance - letting the
     * specialized JSerializer implementation handle how it deserializes its data and the logic
     * of it all.
     *
     * <p/> Object instance data is read from a {@link JSerialData} object through the use of a
     * {@link JSerialDataIO} object, which provides the means and methods required to read from
     * & write to the specific JSerialData object.
     *
     * <p/> In the event of failure, corrupt or missing data, or any other reason where the object
     * cannot be deserialized, an appropriate and detailed Exception should be raised. It will be
     * wrapped and passed up the stack where it can be handled correctly. It's better to have the
     * deserialization fail-fast with a detailed error message, rather than let an incomplete or
     * otherwise invalid deserialized object be allowed to cause untold havoc elsewhere later down
     * the line.
     *
     * @param suggestedObject an object instance, of the specific {@link JSerializable}
     * implementation, which can be used to store the deserialized data, if the specific
     * implementation so chooses.
     * @param dataIO the {@link JSerialDataIO} object which provides the means & methods
     * needed to read data from and write data to the {@link JSerialData} object meant to
     * store the serialized object data.
     * @throws Exception if an issue arrised which prevented the object from being serialized
     * correctly. If an Exception is raised, the objects data {@link JSerialData} should be
     * considered invalid and unfit for use.
     * @return the specific {@link JSerializable} implementation object instance, that contains
     * all the serialized data from the {@link JSerialData} object. If the data was written to
     * the {@code suggestedObject} passed in as a paramater, return the same object, otherwise,
     * if a new object instance was used, return that object instance. If {@code null} is returned,
     * the {@code suggestedObject} passed in as a paramater will be used as the deserialized object.
     */
    protected abstract T dataToObject(T suggestedObject, JSerialDataIO dataIO) throws Exception;

    /**
     * Provided so that specialized implementations of JSerializers can define the exact {@link
     * JSerializable} object implementation they serialize and deserialize. All inheriting class
     * must provide an object instance.
     *
     * @return a new object instance of the specific {@link JSerializable} implementation
     * for this JSerializer. The given object instance can then be used to when deserializing.
     * This can also tell us exactly what JSerializable instance is used by this class instance.
     */
    protected abstract T createInstance();

    /**
     * Called whenever this {@link JSerializer} implementation tries to deserialize an object
     * using {@link JSerialData} that was created using a different version of the JSerializer,
     * so that the specific {@link JSerializer} implementation can fix the given {@link
     * JSerialData} by modifying it so that this specific {@link JSerializer} version can read it.
     *
     * <p/> The specific implementing {@link JSerializer} is expected to override this method, if
     * it has multiple {@link #version}s or is expected to deal with multiple versions. By default,
     * a {@link UnsupportedOperationException} is thrown, stating that this {@link JSerializer}
     * does not support multiple versions.
     *
     * @param dataIO the {@link JSerialDataIO} object for the {@link JSerialData} object that holds
     * serialized data from an older/different version of this {@link JSerializer}, which needs to
     * be fixed.
     * @param version the {@link #version} of this specific {@link JSerializer} implementation.
     * @param dataVersion the {@link JSerialData#version} of the {@link JSerialData} which is also
     * the version of the {@link JSerializer} that created it.
     * @throws Exception if the specific {@link JSerializer} implementation could not fix the {@link
     * JSerialData} version differences. If no exception is thrown, the call to this method is
     * considered a success. By default, a {@link UnsupportedOperationException} is thrown, stating
     * that this JSerializer implementation only supports a single version.
     */
    @SuppressWarnings("RedundantThrows") // Allow implementations that override this to throw exceptions.
    protected void fixVersionMissmatch(JSerialDataIO dataIO, int version, int dataVersion) throws Exception {
        throw new UnsupportedOperationException("This JSerializer only supports a single version: Version " + version + ".");
    }

    // Internal

    /**
     * Internal {@link #serializeObject(JSerializable)}} method.
     *
     * <p/> The serialize method for this specific JSerializer implementation,
     * used to serialize, or convert, the {@code object} passed in into a {@link
     * JSerialData} object containing the given objects data as a {@link
     * JsonObject} which can then be saved to file, sent over the network, or
     * whatever else.
     *
     * <p/> The given {@code object} to be serialized must be an implementation
     * of {@link JSerializable} and it must be the specific implementation type
     * that this JSerializer was designed to serialize & deserialize.
     *
     * @param object the JSerializable implementation, for this specific
     * JSerializer, to be serialized.
     * @return a {@link JSerialData} object containing all the data of the given
     * object to serialize, which can then be used to recreate the object as it was.
     * @throws SerializeException if for any reason the given object cannot be
     * serialized. If an exception is the cause, it will be included in the
     * JSerializeException.
     */
    private JSerialData iserialize(T object) throws SerializeException {
        JSerialDataIO dataIO = getNewJSerialDataIO();

        try{
            objectToData(object, dataIO);
        } catch (Exception cause) {
            SerializeException toThrow = new SerializeException(
                    "JSerializer " + getIdentification() + "' failed to covert JSerializable object to data", cause
            );
            LOG.error(toThrow.getMessage(), cause);
            throw toThrow;
        }

        return dataIO.getJSerialData();
    }

    /**
     * Internal {@link #deserializeData(JSerialData, JSerializable)}} method.
     *
     * <p/> Deserializes an object instance of the {@link JSerializable} implementation made
     * for this JSerializer, from a {@link JSerialData} object instance of a previously
     * serialized object, reconstructed into a new object containing the same data.
     *
     * <p/> The {@link JSerialData} serialized object provided must match this JSerializers
     * {@link #identification} <b>and</b> {@link #version}. If the identification of the
     * JSerialData does not match this JSerializers, the operation will fail. Additionally,
     * the version numbers must match as well, or the implementations {@link
     * #fixVersionMissmatch(JSerialDataIO, int, int)} will be called in an attempt to fix
     * any data compatability issues. Care should be taken as {@link
     * #fixVersionMissmatch(JSerialDataIO, int, int)} throws a {@link UnsupportedOperationException}
     * by default.
     *
     * <p/> <b>Use with care: {@code suggestedInstance} object.</b> This specific deserialize
     * method implementation allows suggesting that the JSerializer implementation use the
     * provided object instance instead of creating a brand new one. <b>However this is merely
     * a suggestion</b> and the specific implementation is <b>allowed to freely ignore the
     * suggestion.</b> This means that the suggested instance should not be expected to have
     * been changed or modified in any way. The specific JSerializer implementation should be
     * consulted in such situations. <b>Perfer the returned object instance to the suggested
     * object instance!</b>
     *
     * @param data the {@link JSerialData} object holding the data of a previously serialized
     * object instance, which is to be reconstructed into a new object instance, replicating
     * the old object.
     * @param newObject an instance of the object type to be created from deserialization,
     * which is suggested to be used by the specific implementation, if it so wishes.
     * @return the {@link JSerialData} serialized data reconstructed into an object instance. This
     * object instance could either be a new object instance or it may the {@code suggestedInstance}
     * provided.
     * @throws DataMissmatchException if the given {@link JSerialData} serialized data does not match
     * the serialized data created by this JSerializer. This happens when the {@link #identification}
     * of the JSerializer and the {@link JSerialData} serialized data do not match.
     * @throws SerializeException if for any reason the given object cannot be  deserialized. If an
     * exception is the cause, it will be included in the JSerializeException.
     */
    private T ideserialize(JSerialData data, T newObject) throws DataMissmatchException {
        matchData(data);
        T providedObject;

        try{
            providedObject = dataToObject(newObject, data.io);
        } catch (Exception cause) {
            SerializeException toThrow = new SerializeException(
                    "JSerializer " + getIdentification() + "' failed to convert data to JSerializable object", cause
            );
            LOG.error(toThrow.getMessage(), cause);
            throw toThrow;
        }

        return providedObject == null ? newObject : providedObject;
    }

    /**
     * Creates a brand new {@link JSerialData} object, constucted
     * for this specific {@link JSerializer} implementation, and
     * returns the {@link JSerialDataIO} object used to modify the
     * data of the {@link JSerialData} object.
     *
     * @return the {@link JSerialDataIO} object of a brand new
     * {@link JSerialData} object instance, constucted for this
     * specific {@link JSerializer} implementation
     */
    private JSerialDataIO getNewJSerialDataIO() {
        JSerialData data = new JSerialData(this);
        return data.io;
    }

    /**
     * @param data the {@link JSerialData} object instance to check.
     * @return {@code true} if the {@link JSerialData#identification}
     * of the given {@link JSerialData}, {@code data}, matches the
     * {@link #identification} of this specific {@link JSerializer}
     * implementation.
     */
    private boolean doesDataIdentificationMatch(JSerialData data) {
        return data.io.getIdentification().equals(identification);
    }

    /**
     * @param data the {@link JSerialData} object instance to check.
     * @return {@code true} if the {@link JSerialData#version} of the
     * given {@link JSerialData}, {@code data}, matches the {@link
     * #version} of this specific {@link JSerializer} implementation.
     */
    private boolean doesDataVersionMatch(JSerialData data) {
        return data.io.getVersion() == version;
    }

    /**
     * Checks the given {@link JSerialData}, {@code data}, to make sure
     * the serialized data it holds matchs the {@link #identification}
     * and {@link #version} of this specific {@link JSerializer}
     * implementation. If the versions don't match, {@link
     * #fixVersionMissmatch(JSerialDataIO, int, int)} will be called
     * in an attempt to fix it.
     *
     * @param data the {@link JSerialData} object to check.
     * @throws DataMissmatchException if the {@link JSerialData#identification}
     * doesn't match this {@link JSerializer}s {@link #identification}, or, if
     * the {@link JSerialData#version} doesn't oesn't match this {@link
     * JSerializer}s {@link #version} and it cannot be fixed using {@link
     * #fixVersionMissmatch(JSerialDataIO, int, int)}.
     */
    private void matchData(JSerialData data) throws DataMissmatchException {
        boolean missmatch = false;

        if(!doesDataIdentificationMatch(data))
            missmatch = true;

        if(!doesDataVersionMatch(data))
            try {
                fixVersionMissmatch(data.io, this.getVersion(), data.io.getVersion());
            } catch (Exception cause) {
                missmatch = true;
            }

        if(missmatch) {
            throw new DataMissmatchException(
                    "Cannot deserialize " + data.toString() + " with " + this.toString()
            );
        }
    }

    // ###############
    // Helping classes
    // ###############

    public interface JSerializable<T extends JSerializable<T>> {

        JSerializer<T> getSerializer();

    }

    private enum SerialDataKeys {

        METADATA, DATA, IDENTIFICATION, VERSION;

        @Override
        public String toString(){
            return this.name().toLowerCase();
        }
    }

    public static class JSerialData {

        private static final Gson INTERNAL_GSON_INSTANCE
                = new GsonBuilder().setLenient().create();

        private final JSerialDataIO io;

        private final JsonObject metadata;

        private final JsonObject data;

        // Constructors

        private JSerialData() {
            this(new JsonObject(), new JsonObject());
        }

        private JSerialData(JsonObject metadata, JsonObject data) {
            this.metadata = Objects.requireNonNull(metadata);
            this.data = Objects.requireNonNull(data);
            this.io = new JSerialDataIO(this);
        }

        private JSerialData(JSerializer<?> serializer) {
            this(new JsonObject(), new JsonObject());
            this.io.setMetadata(serializer.identification, serializer.getVersion());
        }

        // API

        protected JsonObject getDataJsonObject() {
            JsonObject serialDataContainer = new JsonObject();
            serialDataContainer.add(SerialDataKeys.METADATA.toString(), metadata);
            serialDataContainer.add(SerialDataKeys.DATA.toString(), data);

            return serialDataContainer;
        }

        public String getDataAsJsonString() {
            return getDataAsJsonString(INTERNAL_GSON_INSTANCE);
        }

        public String getDataAsJsonString(Gson gsonBuilder) {
            return Objects.requireNonNull(gsonBuilder).toJson(getDataJsonObject());
        }

        @Override
        public String toString() {
            return String.format("JSerialData[identification=%s, version=%s]", io.getIdentification(), io.getVersion());
        }
    }

    protected static final class JSerialDataIO {

        private final JSerialData dataObject;

        protected JSerialDataIO(JSerialData dataObject){
            this.dataObject = dataObject;
        }

        // Internal

        private static String key(SerialDataKeys key){
            return key.toString();
        }

        private JsonObject getMetadata() {
            return dataObject.metadata;
        }

        private JsonObject getData() {
            return dataObject.data;
        }

        // Internal IO

        private void setMetadata(String identification, int version) {
            getMetadata().add(key(SerialDataKeys.IDENTIFICATION), new JsonPrimitive(identification));
            getMetadata().add(key(SerialDataKeys.VERSION), new JsonPrimitive(version));
        }

        private String getIdentification() {
            return getMetadata().get(key(SerialDataKeys.IDENTIFICATION)).getAsString();
        }

        private int getVersion() {
            return getMetadata().get(key(SerialDataKeys.VERSION)).getAsInt();
        }

        // API

        public JSerialData getJSerialData() {
            return dataObject;
        }

        public void add(String key, JsonElement value) {
            getData().add(key, value);
        }

        public void add(String key, String value) {
            getData().add(key, new JsonPrimitive(value));
        }

        public void add(String key, boolean value) {
            getData().add(key, new JsonPrimitive(value));
        }

        public void add(String key, Number value) {
            getData().add(key, new JsonPrimitive(value));
        }

        public void add(String key, Character value) {
            getData().add(key, new JsonPrimitive(value));
        }

        public boolean has(String key) {
            return getData().has(key);
        }

        public JsonElement get(String key) {
            return getData().get(key);
        }

        public JsonObject getObject(String key) {
            JsonElement value;

            if(!getData().has(key) || !(value = getData().get(key)).isJsonObject())
                throw new IllegalStateException("Could not get value '" + key + "' as JsonObject from JSerialData.");
            return value.getAsJsonObject();
        }

        public JsonObject getObject(String key, JsonObject defaultValue) {
            JsonElement value = get(key);

            if(value != null && value.isJsonObject()) {
                return value.getAsJsonObject();
            }

            return defaultValue;
        }

        public String getString(String key) {
            JsonElement value;

            if(!getData().has(key) || !(value = getData().get(key)).isJsonPrimitive())
                throw new IllegalStateException("Could not get value '" + key + "' as String from JSerialData.");
            return value.getAsString();
        }

        public String getString(String key, String defaultValue) {
            JsonElement value = get(key);

            if(value != null && value.isJsonPrimitive()) {
                return value.getAsString();
            }

            return defaultValue;
        }

        public int getInteger(String key) {
            JsonElement value;

            if(!getData().has(key) || !(value = getData().get(key)).isJsonPrimitive())
                throw new IllegalStateException("Could not get value '" + key + "' as Integer from JSerialData.");
            return value.getAsInt();
        }

        public int getInteger(String key, int defaultValue) {
            JsonElement value = get(key);

            if(value != null && value.isJsonPrimitive()) {
                return value.getAsInt();
            }

            return defaultValue;
        }

        public boolean getBoolean(String key) {
            JsonElement value;

            if(!getData().has(key) || !(value = getData().get(key)).isJsonPrimitive())
                throw new IllegalStateException("Could not get value '" + key + "' as Boolean from JSerialData.");
            return value.getAsBoolean();
        }

        public boolean getBoolean(String key, boolean defaultValue) {
            JsonElement value = get(key);

            if(value != null && value.isJsonPrimitive()) {
                return value.getAsBoolean();
            }

            return defaultValue;
        }

        public long getLong(String key) {
            JsonElement value;

            if(!getData().has(key) || !(value = getData().get(key)).isJsonPrimitive())
                throw new IllegalStateException("Could not get value '" + key + "' as Long from JSerialData.");
            return value.getAsLong();
        }

        public long getLong(String key, long defaultValue) {
            JsonElement value = get(key);

            if(value != null && value.isJsonPrimitive()) {
                return value.getAsLong();
            }

            return defaultValue;
        }
    }

    /**
     * A specific type of {@link SerializeException} that is thrown instead
     * of a SerializeException, only if and when, an attempt is made at
     * deserializing a specific {@link JSerializable} object implementation
     * with its {@link JSerializer} implementation, using serialized data
     * {@link JSerialData} from a different {@link JSerializer} implementation.
     *
     *
     * <p/> More specifically, this runtime exception is thrown during
     * deserialization to indicate that the serialized object data {@link
     * JSerialData} given to the {@link JSerializer} was not created by the
     * specific JSerializer and is therefore invalid, as it contains unusable
     * data for a different object in an unknown format. This happens when the
     * identification name of the JSerializer doesn't match the one from the
     * JSerialData.
     */
    public static final class DataMissmatchException extends SerializeException {

        /**
         * Creates a new UnsupportedDataException exception with the given
         * detailed message.
         *
         * @param message the details of the exception.
         */
        private DataMissmatchException(String message) {
            super(message);
        }

        /**
         * Creates a new UnsupportedDataException exception with the given
         * detailed message and reason for being thrown.
         *
         * @param message the details of the exception.
         * @param cause the exception that caused this one to be thrown.
         */
        private DataMissmatchException(String message, Exception cause) {
            super(message, cause);
        }

    }

    /**
     * A general serialization exception thrown when a {@link JSerializer} fails
     * to serialize or deserialize a {@link JSerializable}, for whatever reason.
     *
     * <p/> If for any reason a JSerializable object cannot be serialized or
     * deserialized by its JSerializer implementation, such a missing/corrupted
     * data or an error with the JSerializer implementation, a SerializeException
     * will be thrown to indicate failure. A message with a reason and details
     * on the failure will be provided, as well as the exception that caused the
     * failure, if any.
     *
     * <p/> This exception is different from {@link DataMissmatchException} in
     * that - a {@link DataMissmatchException} is thrown instead of a {@link
     * SerializeException} only when deserializing a {@link JSerializable} with
     * the wrong {@link JSerialData}. Any other cause of failure is represented
     * with this exception.
     */
    public static class SerializeException extends RuntimeException {

        /**
         * Creates a new SerializeException exception with the given
         * detailed message.
         *
         * @param message the details of the exception.
         */
        private SerializeException(String message) {
            super(message);
        }

        /**
         * Creates a new SerializeException exception with the given
         * detailed message nd reason for being thrown.
         *
         * @param message the details of the exception.
         * @param cause the exception that caused this one to be thrown.
         */
        private SerializeException(String message, Exception cause) {
            super(message, cause);
        }

    }

    // ##############
    // Static Methods
    // ##############

    public static <T extends JSerializable<T>> JSerialData serialize(T object) {
        JSerializer<T> serializer = object.getSerializer();
        return serializer.serializeObject(object);
    }

    public static <T extends JSerializable<T>> T deserialize(JSerialData serializedData, JSerializer<T> serializer)
            throws DataMissmatchException {
        return serializer.deserializeData(serializedData);
    }

    public static <T extends JSerializable<T>> T deserialize(JSerialData serializedData, Class<? extends T> object)
            throws DataMissmatchException {
        T newInstance = constructSerializableObject(object);
        JSerializer<T> serializer = newInstance.getSerializer();
        return serializer.deserializeData(serializedData, newInstance);
    }

    private static <T extends JSerializable<T>> T constructSerializableObject(Class<? extends T> object) {
        //Get the default (probably private) constructor, or throw IllegalArgumentException
        Constructor<?> objectConstructor = Arrays.stream(object.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterCount() == 0)
                .peek(constructor -> {
                    if(constructor.isAccessible())
                        LOG.warn("Serializable object '" + object.getCanonicalName() + "' has accessable default constructor.");
                    else constructor.setAccessible(true);
                }).findFirst().orElseThrow(() -> new IllegalArgumentException(
                        "Serializable object '" + object.getCanonicalName() + "' has no default constructor available."
                ));

        try {
            // Attempt to create a new object instance from constructor.
            Object objectInstance = objectConstructor.newInstance();

            if(!(objectInstance instanceof JSerializable<?>)){
                throw new IllegalArgumentException(
                        "Serializable object '" + object.getCanonicalName() + "' not of type JSerializable"
                );
            }

            try { // Try cast to correct type, or throw exception
                //noinspection unchecked
                return (T) objectInstance;
            } catch (Throwable e) {
                throw new IllegalArgumentException("Serializable object '" + object.getCanonicalName() + "' cast failed", e);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(
                    "Serializable object '" + object.getCanonicalName() + "' could not be instantiated", e
            );
        }
    }
}
