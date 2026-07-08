const API_BASE = '/api'

export async function getStatus() {
  const res = await fetch(`${API_BASE}/status`)
  return res.json()
}

export async function executeCommand(cmd) {
  const res = await fetch(`${API_BASE}/execute`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: `cmd=${encodeURIComponent(cmd)}`,
  })
  const text = await res.text()
  return text
}
