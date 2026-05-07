#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_DIR="$SCRIPT_DIR/config"

mkdir -p "$CONFIG_DIR"

echo "Downloading sample-scd-full.scd ..."
curl -fSL -o "$CONFIG_DIR/sample-scd-full.scd" \
  "https://raw.githubusercontent.com/gillerman/IEC_61850_SCL_2_Nodeset/master/Example61850TestSCD.xml"
echo "  OK"

echo "Downloading sample-scd-relay.scd ..."
curl -fSL -o "$CONFIG_DIR/sample-scd-relay.scd" \
  "https://raw.githubusercontent.com/robidev/iec61850_open_server/master/scd/protection_relay.scd"
echo "  OK"

echo ""
echo "Done! Files saved to: $CONFIG_DIR"