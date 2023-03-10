/*
 * Copyright 2019 Confluent Inc.
 *
 * Licensed under the Confluent Community License; you may not use this file
 * except in compliance with the License.  You may obtain a copy of the License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.execution.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.confluent.ksql.execution.expression.tree.FunctionCall;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Immutable
public class StreamFlatMap<K> implements ExecutionStep<KStreamHolder<K>> {

  private final ExecutionStepPropertiesV1 properties;
  private final ExecutionStep<KStreamHolder<K>> source;
  private final ImmutableList<FunctionCall> tableFunctions;

  public StreamFlatMap(
      @JsonProperty(value = "properties", required = true) final ExecutionStepPropertiesV1 props,
      @JsonProperty(value = "source", required = true) final ExecutionStep<KStreamHolder<K>> source,
      @JsonProperty(value = "tableFunctions", required = true) final
      List<FunctionCall> tableFunctions
  ) {
    this.properties = Objects.requireNonNull(props, "props");
    this.source = Objects.requireNonNull(source, "source");
    this.tableFunctions = ImmutableList.copyOf(Objects.requireNonNull(tableFunctions));

    if (tableFunctions.isEmpty()) {
      throw new IllegalArgumentException("Need at latest one table function");
    }
  }

  @Override
  public ExecutionStepPropertiesV1 getProperties() {
    return properties;
  }

  @Override
  @JsonIgnore
  public List<ExecutionStep<?>> getSources() {
    return Collections.singletonList(source);
  }

  @Override
  public KStreamHolder<K> build(final PlanBuilder builder, final PlanInfo info) {
    return builder.visitFlatMap(this, info);
  }

  @Override
  public PlanInfo extractPlanInfo(final PlanInfoExtractor extractor) {
    return extractor.visitFlatMap(this);
  }

  @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "tableFunctions is ImmutableList")
  public List<FunctionCall> getTableFunctions() {
    return tableFunctions;
  }

  public ExecutionStep<KStreamHolder<K>> getSource() {
    return source;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final StreamFlatMap<?> that = (StreamFlatMap<?>) o;
    return Objects.equals(properties, that.properties)
        && Objects.equals(source, that.source)
        && Objects.equals(tableFunctions, that.tableFunctions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(properties, source, tableFunctions);
  }
}
