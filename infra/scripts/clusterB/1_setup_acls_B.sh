#!/bin/bash
set -euo pipefail
BOOTSTRAP_B="${BOOTSTRAP_B:-kafka-b-1:9093,kafka-b-2:9093,kafka-b-3:9093}"
CONF_B="${CONF_B:-/etc/kafka/scripts_init/admin-B.properties}"

echo "==> [B] Applying ACLs on ${BOOTSTRAP_B} using ${CONF_B}"

#kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:mm2" --producer --topic "shop.products.filtered" --topic "client.requests" --topic "client.recommendations"

kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:reco" --consumer --group "analytics" --topic "shop.products.filtered" --topic "A.shop.products.filtered" --topic "A.client.requests"

kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:reco" --producer "analytics" --topic "shop.products.filtered" --topic "client.recommendations"

#kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:mm2" --producer --topic "shop.products.filtered" --topic "client.requests" --topic "client.recommendations"
#
#kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:mm2" --operation Create --cluster
#
#kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:mm2" --operation Create --topic "_mm2-" --resource-pattern-type prefixed
#
#kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:mm2" --operation Write --topic "_mm2-" --resource-pattern-type prefixed
#
#kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:mm2"  --operation Create --operation Write --operation Read --operation Describe --topic "A.shop.products.filtered" --topic "A.client.requests"
#
#kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:reco"  --operation Create --operation Write --operation Read --operation Describe --topic "A.shop.products.filtered" --topic "A.client.requests" --topic "client.recommendations"
#
#kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:mm2" --operation Create --operation Write --operation Read --operation Describe --topic mm2*
#
#kafka-acls --bootstrap-server "$BOOTSTRAP_B" --command-config "$CONF_B" --add   --allow-principal "User:mm2" --cluster --operation Create --operation DescribeConfigs --operation AlterConfigs

echo "==> [B] ACLs applied."
