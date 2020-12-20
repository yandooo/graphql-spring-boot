package graphql.kickstart.graphql.annotations.test.interfaces;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLNonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car implements Vehicle {

  /**
   * Note that you have to repeat the annotations from the interface method!
   */
  @GraphQLField
  @GraphQLNonNull
  private String registrationNumber;

  @GraphQLField
  @GraphQLNonNull
  private int numberOfSeats;
}
