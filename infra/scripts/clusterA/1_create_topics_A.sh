#!/bin/bash
set -euo pipefail
BOOTSTRAP_A="${BOOTSTRAP_A:-kafka-a-1:9093,kafka-a-2:9093,kafka-a-3:9093}"
CONF_A="${CONF_A:-/etc/kafka/scripts_init/admin-A.properties}"
PARTITIONS="${PARTITIONS:-6}"
RF="${RF:-3}"

echo "==> [A] Creating topics on ${BOOTSTRAP_A} using ${CONF_A}"

kafka-topics --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --create --if-not-exists   --topic shop.products --replication-factor "$RF" --partitions "$PARTITIONS"

kafka-topics --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --create --if-not-exists   --topic products.error --replication-factor "$RF" --partitions "$PARTITIONS"

kafka-topics --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --create --if-not-exists   --topic shop.products.filtered --replication-factor "$RF" --partitions "$PARTITIONS"

kafka-topics --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --create --if-not-exists   --topic client.requests --replication-factor "$RF" --partitions "$PARTITIONS"

kafka-topics --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --create --if-not-exists   --topic client.recommendations --replication-factor "$RF" --partitions "$PARTITIONS"

kafka-topics --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --create --if-not-exists   --topic forbidden.skus --replication-factor "$RF" --partitions 3   --config cleanup.policy=compact --config min.cleanable.dirty.ratio=0.1 --config segment.ms=3600000

echo "==> [A] Topics created (or already existed)."
