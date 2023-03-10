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

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.confluent.ksql.execution.expression.tree.Expression;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Immutable
public class TableGroupByV1<K> implements ExecutionStep<KGroupedTableHolder> {
  private final ExecutionStepPropertiesV1 properties;
  private final ExecutionStep<KTableHolder<K>> source;
  private final Formats internalFormats;
  private final ImmutableList<Expression> groupByExpressions;

  public TableGroupByV1(
      @JsonProperty(value = "properties", required = true) final ExecutionStepPropertiesV1 props,
      @JsonProperty(value = "source", required = true) final ExecutionStep<KTableHolder<K>> source,
      @JsonProperty(value = "internalFormats", required = true) final Formats internalFormats,
      @JsonProperty(value = "groupByExpressions", required = true) final List<Expression> groupBys
  ) {
    this.properties = requireNonNull(props, "props");
    this.source = requireNonNull(source, "source");
    this.internalFormats = requireNonNull(internalFormats, "internalFormats");
    this.groupByExpressions = ImmutableList.copyOf(requireNonNull(groupBys, "groupBys"));

    if (groupByExpressions.isEmpty()) {
      throw new IllegalArgumentException("Need at least one grouping expression");
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

  public Formats getInternalFormats() {
    return internalFormats;
  }

  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification = "groupByExpressions is ImmutableList"
  )
  public List<Expression> getGroupByExpressions() {
    return groupByExpressions;
  }

  public ExecutionStep<KTableHolder<K>> getSource() {
    return source;
  }

  @Override
  public KGroupedTableHolder build(final PlanBuilder builder, final PlanInfo info) {
    return builder.visitTableGroupBy(this, info);
  }

  @Override
  public PlanInfo extractPlanInfo(final PlanInfoExtractor extractor) {
    return extractor.visitTableGroupBy(this);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final TableGroupByV1<?> that = (TableGroupByV1<?>) o;
    return Objects.equals(properties, that.properties)
        && Objects.equals(source, that.source)
        && Objects.equals(internalFormats, that.internalFormats)
        && Objects.equals(groupByExpressions, that.groupByExpressions);
  }

  @Override
  public int hashCode() {

    return Objects.hash(properties, source, internalFormats, groupByExpressions);
  }
}
