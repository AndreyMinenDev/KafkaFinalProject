#!/bin/bash
set -euo pipefail
BOOTSTRAP_A="${BOOTSTRAP_A:-kafka-a-1:9093,kafka-a-2:9093,kafka-a-3:9093}"
CONF_A="${CONF_A:-/etc/kafka/scripts_init/admin-A.properties}"

echo "==> [A] Applying ACLs on ${BOOTSTRAP_A} using ${CONF_A}"

kafka-acls --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --add   --allow-principal "User:shop" --producer --topic shop.products

kafka-acls --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --add   --allow-principal "User:streams" --consumer --group "streams-app" --topic shop.products --topic forbidden.skus

kafka-acls --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --add   --allow-principal "User:streams" --producer --topic shop.products.filtered --topic products.error

kafka-acls --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --add   --allow-principal "User:client" --producer --topic client.requests --topic products.error

kafka-acls --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --add   --allow-principal "User:client" --consumer --group "saver-*" --topic shop.products.filtered

kafka-acls --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --add   --allow-principal "User:reco" --producer --topic client.recommendations

kafka-acls --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --add   --allow-principal "User:connect" --consumer --group "connect-*" --topic shop.products.filtered

kafka-acls --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --add   --allow-principal "User:mm2" --consumer --group "*mm2*" --topic shop.products.filtered --topic client.requests --topic client.recommendations

kafka-acls --bootstrap-server "$BOOTSTRAP_A" --command-config "$CONF_A" --add   --allow-principal "User:mm2" --operation Create --operation Write --operation Read --operation Describe --topic shop.products.filtered --topic client.requests --topic client.recommendations

echo "==> [A] ACLs applied."
