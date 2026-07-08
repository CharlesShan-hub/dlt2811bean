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
  // Strip ANSI escape codes, then find the JSON part
  const clean = text.replace(/\x1b\[\d+m/g, '').trim()
  const jsonStart = clean.indexOf('{')
  if (jsonStart >= 0) {
    try {
      return JSON.parse(clean.slice(jsonStart))
    } catch {
      // fall through
    }
  }
  return { success: false, data: [], error: clean }
}
