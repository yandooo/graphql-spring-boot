package com.oembedler.moon.graphql.boot;

import graphql.schema.idl.SchemaDirectiveWiring;

public interface SchemaDirective {

    String getName();

    SchemaDirectiveWiring getDirective();

    static SchemaDirective create(String name, SchemaDirectiveWiring directive) {
        return new SchemaDirectiveImpl(name, directive);
    }

}
