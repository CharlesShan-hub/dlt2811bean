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
  return res.text()
}

export async function executeJson(cmd) {
  const text = await executeCommand(cmd)
  try {
    return JSON.parse(text)
  } catch {
    return { success: false, data: [], error: text }
  }
}
