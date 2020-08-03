package com.graphql.spring.boot.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GraphQLFieldAssertTestBase {

    protected static final String MOCK_PATH = "test.path";
    protected static final String NON_NULL_VALUE = "non-null";

    @Mock
    protected GraphQLResponse graphQLResponse;
}
