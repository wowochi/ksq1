/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.google.common.collect.ImmutableList;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({})
public class KafkaTopicInfo {

  private final String name;
  private final ImmutableList<Integer> replicaInfo;

  @JsonCreator
  public KafkaTopicInfo(
      @JsonProperty("name") final String name,
      @JsonProperty("replicaInfo") final List<Integer> replicaInfo
  ) {
    this.name = name;
    this.replicaInfo = ImmutableList.copyOf(replicaInfo);
  }

  public String getName() {
    return name;
  }

  @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "replicaInfo is ImmutableList")
  public List<Integer> getReplicaInfo() {
    return replicaInfo;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final KafkaTopicInfo that = (KafkaTopicInfo) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
