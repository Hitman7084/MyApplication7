// /server/server.js
import express from "express";
import cors from "cors";
import "dotenv/config";


const app = express();
app.use(cors());
app.use(express.json({ limit: "1mb" }));


const OPENROUTER_KEY   = process.env.OPENROUTER_API_KEY || "";
const MODEL            = process.env.OPENROUTER_MODEL || "mistralai/mistral-7b-instruct:free";
const SYSTEM_PROMPT    = process.env.SYSTEM_PROMPT || "You are ACME Assistant. Answer concisely.";
const APP_URL          = process.env.APP_URL || "";
const APP_NAME         = process.env.APP_NAME || "Chat Server";

if (!OPENROUTER_KEY) {
  console.error("❌ Missing OPENROUTER_API_KEY in /server/.env");
}

app.get("/", (_req, res) => {
  res.json({ ok: true, provider: "openrouter", model: MODEL });
});

app.post("/chat", async (req, res) => {
  try {
    // Expect: { messages: [{role, content}, ...] }
    const input = req.body?.messages;
    const messages = Array.isArray(input) ? input : [];
    const sanitized = messages
      .filter(m => m && typeof m.role === "string" && typeof m.content === "string")
      .map(m => ({ role: m.role === "assistant" ? "assistant" : "user", content: m.content }));

    const body = {
      model: MODEL,
      messages: [{ role: "system", content: SYSTEM_PROMPT }, ...sanitized],
      temperature: 0.3
    };

    const headers = {
      "Authorization": `Bearer ${OPENROUTER_KEY}`,
      "Content-Type": "application/json",
    };
    // OpenRouter recommends these optional headers for attribution/rate-limit friendliness
    if (APP_URL)  headers["HTTP-Referer"] = APP_URL;
    if (APP_NAME) headers["X-Title"]      = APP_NAME;

    const r = await fetch("https://openrouter.ai/api/v1/chat/completions", {
      method: "POST",
      headers,
      body: JSON.stringify(body)
    });

    const raw = await r.text();
    let json;
    try { json = JSON.parse(raw); } catch { /* keep raw for error */ }

    if (!r.ok) {
      console.error("❌ OpenRouter error", r.status, raw);
      return res.status(r.status).json({
        error: json?.error?.message || `OpenRouter ${r.status}`,
        provider_status: r.status,
        provider_body: json || raw
      });
    }

    const text = json?.choices?.[0]?.message?.content;
    if (!text) {
      console.error("⚠️ Unexpected OpenRouter payload", json);
      return res.status(502).json({ error: "No content from model", provider_body: json });
    }

    return res.json({ text });
  } catch (e) {
    console.error("💥 Server exception:", e);
    return res.status(500).json({ error: e?.message || "Server error" });
  }
});

const port = Number(process.env.PORT || 8787);
app.listen(port, () => console.log(`API on http://localhost:${port} (model: ${MODEL})`));
