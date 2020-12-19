package com.graphql.spring.boot.test.assertions;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestError;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.Getter;
import org.assertj.core.api.AbstractIntegerAssert;

import java.util.List;

import static com.graphql.spring.boot.test.helper.GraphQLTestConstantsHelper.ERRORS_PATH;
import static java.util.Objects.nonNull;

public class NumberOfErrorsAssertion
    extends AbstractIntegerAssert<NumberOfErrorsAssertion>
    implements GraphQLResponseAssertion {

    private final GraphQLResponse graphQLResponse;

    public NumberOfErrorsAssertion(final GraphQLResponse response) {
        super(getNumberOfErrors(response), NumberOfErrorsAssertion.class);
        graphQLResponse = response;
    }

    @Override
    public GraphQLResponse and() {
        return graphQLResponse;
    }

    private static Integer getNumberOfErrors(final GraphQLResponse response) {
        int numErrors = 0;
        try {
            final List<GraphQLTestError> errorList = response.getList(ERRORS_PATH, GraphQLTestError.class);
            if(nonNull(errorList)) {
                numErrors = errorList.size();
            }
        } catch (PathNotFoundException e) {
            // do nothing, number of errors is zero
        }
        return numErrors;
    }
}
