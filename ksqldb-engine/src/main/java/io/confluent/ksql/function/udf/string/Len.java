/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the
 * License.
 */

package io.confluent.ksql.function.udf.string;

import io.confluent.ksql.function.FunctionCategory;
import io.confluent.ksql.function.udf.Udf;
import io.confluent.ksql.function.udf.UdfDescription;
import io.confluent.ksql.function.udf.UdfParameter;
import java.nio.ByteBuffer;

@UdfDescription(
    name = "len",
    category = FunctionCategory.STRING,
    description = "Returns the length of the input string or byte array.")
public class Len {

  @Udf
  public Integer len(
      @UdfParameter(description = "The input string") final String input) {
    if (input == null) {
      return null;
    }
    return input.length();
  }

  @Udf
  public Integer len(
      @UdfParameter(description = "The input byte array") final ByteBuffer input) {
    if (input == null) {
      return null;
    }
    return input.capacity();
  }
}
