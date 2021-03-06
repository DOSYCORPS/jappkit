package codr7.jappkit;

import java.nio.channels.SeekableByteChannel;

public abstract class Type<ValueT> {
    public final String name;

    public Type(String name) { this.name = name; }
    public abstract ValueT init();

    public boolean isa(Type parent) { return parent == this; }

    public ValueT clone(ValueT it) { return it; }
    public Object cloneObject(Object it) { return clone((ValueT) it); }

    public ValueT get(ValueT it) { return it; }
    public ValueT set(ValueT it) { return it; }

    public abstract Object load(SeekableByteChannel in);
    public abstract void store(Object it, SeekableByteChannel out);

    public abstract Cmp cmp(ValueT x, ValueT y);
}