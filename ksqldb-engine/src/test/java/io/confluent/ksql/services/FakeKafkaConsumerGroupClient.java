package io.confluent.ksql.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.confluent.ksql.exception.KafkaResponseGetFailedException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class FakeKafkaConsumerGroupClient implements KafkaConsumerGroupClient {

  private static final ImmutableList<String> groups = ImmutableList.of("cg1", "cg2");

  private final Set<String> deletedConsumerGroups = new HashSet<>();

  @Override
  @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "groups is ImmutableList")
  public List<String> listGroups() {
    return groups;
  }

  @Override
  public ConsumerGroupSummary describeConsumerGroup(final String group) {
    if (groups.contains(group)) {
      Set<ConsumerSummary> instances = ImmutableSet.of(
          new ConsumerSummary(group + "-1"),
          new ConsumerSummary(group + "-2")
      );
      return new ConsumerGroupSummary(instances);
    } else {
      throw new KafkaResponseGetFailedException(
          "Failed to retrieve Kafka consumer group",
          new RuntimeException()
      );
    }
  }

  @Override
  public Map<TopicPartition, OffsetAndMetadata> listConsumerGroupOffsets(final String group) {
    if (groups.contains(group)) {
      Map<TopicPartition, OffsetAndMetadata> offsets = new LinkedHashMap<>();
      offsets.put(new TopicPartition("topic1", 0), new OffsetAndMetadata(10));
      offsets.put(new TopicPartition("topic1", 1), new OffsetAndMetadata(11));
      return offsets;
    } else {
      throw new KafkaResponseGetFailedException(
          "Failed to list Kafka consumer groups",
          new RuntimeException()
      );
    }
  }

  @Override
  public void deleteConsumerGroups(final Set<String> groups) {
    deletedConsumerGroups.addAll(groups);
  }

  public Set<String> getDeletedConsumerGroups() {
    return ImmutableSet.copyOf(deletedConsumerGroups);
  }
}
