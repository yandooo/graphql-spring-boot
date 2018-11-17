package com.oembedler.moon.graphql.boot;

import graphql.schema.idl.SchemaDirectiveWiring;

class SchemaDirectiveImpl implements SchemaDirective {

    private final String name;
    private final SchemaDirectiveWiring directive;

    SchemaDirectiveImpl(String name, SchemaDirectiveWiring directive) {
        this.name = name;
        this.directive = directive;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SchemaDirectiveWiring getDirective() {
        return directive;
    }

}
