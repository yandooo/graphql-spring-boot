package com.oembedler.moon.graphql.boot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Andrew Potter
 */
public class SchemaParserDictionary {
    private BiMap<String, Class<?>> dictionary = HashBiMap.create();

    /**
     * Add arbitrary classes to the parser's dictionary.
     */
    public SchemaParserDictionary dictionary(String name, Class<?> clazz) {
        this.dictionary.put(name, clazz);
        return this;
    }

    /**
     * Add arbitrary classes to the parser's dictionary.
     */
    public SchemaParserDictionary dictionary(Map<String, Class<?>> dictionary) {
        this.dictionary.putAll(dictionary);
        return this;
    }

    /**
     * Add arbitrary classes to the parser's dictionary.
     */
    public SchemaParserDictionary dictionary(Class<?> clazz) {
        this.dictionary(clazz.getSimpleName(), clazz);
        return this;
    }

    /**
     * Add arbitrary classes to the parser's dictionary.
     */
    public SchemaParserDictionary dictionary(Class<?> ... dictionary) {
        Arrays.stream(dictionary).forEach(this::dictionary);
        return this;
    }

    /**
     * Add arbitrary classes to the parser's dictionary.
     */
    public SchemaParserDictionary dictionary(List<Class<?>> dictionary) {
        dictionary.forEach(this::dictionary);
        return this;
    }

    public Map<String, Class<?>> getDictionary() {
        return Collections.unmodifiableMap(dictionary);
    }
}
