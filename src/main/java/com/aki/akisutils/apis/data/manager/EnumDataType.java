package com.aki.akisutils.apis.data.manager;

import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;

public enum EnumDataType {
    INT("int"),
    DOUBLE("double"),
    FLOAT("float"),
    LONG("long"),
    BOOLEAN("boolean"),
    BYTE("byte"),
    STRING("string"),
    STACK("stack"),
    ENTITY("entity"),
    ARRAYLIST("arraylist"),
    NONNULLLIST("nonnulllist"),
    COMPOUND("compound");

    public String name = "";
    private static final Map<String, EnumDataType> NAME_LOOKUP = Maps.<String, EnumDataType>newHashMap();

    static
    {
        for (EnumDataType dataType : values())
        {
            NAME_LOOKUP.put(dataType.name.toLowerCase(Locale.ROOT), dataType);
        }
    }

    EnumDataType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static EnumDataType byName(String name) {
        return name == null ? null : (EnumDataType) NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }
}
