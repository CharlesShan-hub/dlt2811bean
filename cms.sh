#!/usr/bin/env bash
set -euo pipefail

HOST="http://127.0.0.1"
PORT=7899
ARGS=()

while [[ $# -gt 0 ]]; do
    case "$1" in
        --install)
            SCRIPT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/$(basename "${BASH_SOURCE[0]}")"
            TARGET="/usr/local/bin/cms"
            if [[ -L "$TARGET" ]] && [[ "$(readlink "$TARGET")" == "$SCRIPT" ]]; then
                echo "cms already installed at $TARGET"
            else
                sudo ln -sf "$SCRIPT" "$TARGET"
                echo "Installed cms -> $SCRIPT"
            fi
            exit 0
            ;;
        --port)
            if [[ -z "${2:-}" ]]; then
                echo "Error: --port requires a value" >&2
                exit 1
            fi
            PORT="$2"
            shift 2
            ;;
        --status)
            URL="${HOST}:${PORT}/api/status"
            if ! response=$(curl -sf --max-time 10 "$URL" 2>/dev/null); then
                echo "Error: Cannot connect to CMS CLI API server at ${HOST}:${PORT}" >&2
                echo "Make sure the CMS CLI is running with API server enabled." >&2
                exit 1
            fi
            echo "$response"
            exit 0
            ;;
        *)
            ARGS+=("$1")
            ;;
    esac
    shift
done

if [[ ${#ARGS[@]} -eq 0 ]]; then
    echo "Usage: ./cms.sh <command> [args...]"
    echo "       ./cms.sh --status"
    echo "       ./cms.sh --port <port> <command> [args...]"
    echo "       ./cms.sh --install"
    exit 1
fi

# Build command line from remaining args
CMD_LINE="${ARGS[*]}"

# Send command to CMS CLI API server
URL="${HOST}:${PORT}/api/execute"
if ! response=$(curl -sf --max-time 30 -X POST "$URL" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "cmd=$CMD_LINE" 2>/dev/null); then
    echo "Error: Cannot connect to CMS CLI API server at ${HOST}:${PORT}" >&2
    echo "Make sure the CMS CLI is running with API server enabled." >&2
    exit 1
fi

echo "$response"