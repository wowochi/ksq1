/*
 * Copyright 2019 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"; you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.rest.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Immutable
public class ConnectorList extends KsqlEntity {

  private final ImmutableList<SimpleConnectorInfo> connectors;

  @JsonCreator
  public ConnectorList(
      @JsonProperty("statementText")  final String statementText,
      @JsonProperty("warnings")       final List<KsqlWarning> warnings,
      @JsonProperty("connectors")     final List<SimpleConnectorInfo> connectors
  ) {
    super(statementText, warnings);
    this.connectors = ImmutableList.copyOf(Objects.requireNonNull(connectors, "connectors"));
  }

  @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "connectors is ImmutableList")
  public ImmutableList<SimpleConnectorInfo> getConnectors() {
    return connectors;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ConnectorList that = (ConnectorList) o;
    return Objects.equals(connectors, that.connectors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(connectors);
  }
}
