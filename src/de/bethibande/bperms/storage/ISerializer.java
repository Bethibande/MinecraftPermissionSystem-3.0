package de.bethibande.bperms.storage;

public interface ISerializer<T> {

    String[] serialize(T object);

    T deserialize(String[] s);

}
