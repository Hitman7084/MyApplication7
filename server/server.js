// /server/server.js
import express from "express";
import cors from "cors";
import "dotenv/config";

const app = express();
app.use(cors());
app.use(express.json({ limit: "1mb" }));

const SYSTEM_PROMPT = `
You are ACME Assistant. Stay strictly on topic for ACME app help,
answer concisely in bullet points, and refuse unrelated questions.
`;

const MODEL = process.env.OPENAI_MODEL || "gpt-4o-mini"; // change if needed
const OPENAI_URL = "https://api.openai.com/v1/chat/completions";
const OPENAI_API_KEY = process.env.OPENAI_API_KEY;

if (!OPENAI_API_KEY) {
  console.error("❌ Missing OPENAI_API_KEY in .env");
}

app.post("/chat", async (req, res) => {
  try {
    const messages = Array.isArray(req.body?.messages) ? req.body.messages : [];
    // Basic validation
    const sane = messages
      .filter(m => m && typeof m.role === "string" && typeof m.content === "string")
      .map(m => ({ role: m.role === "assistant" ? "assistant" : "user", content: m.content }));

    const body = {
      model: MODEL,
      messages: [{ role: "system", content: SYSTEM_PROMPT }, ...sane],
      temperature: 0.3
    };

    const r = await fetch(OPENAI_URL, {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${OPENAI_API_KEY}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body)
    });

    const text = await r.text(); // read raw first for easier logging
    let json;
    try { json = JSON.parse(text); } catch { /* not JSON */ }

    if (!r.ok) {
      console.error("❌ OpenAI error", r.status, text);
      return res.status(r.status).json({
        error: json?.error?.message || `OpenAI ${r.status}`,
        provider_status: r.status,
        provider_body: json || text
      });
    }

    const reply = json?.choices?.[0]?.message?.content;
    if (!reply) {
      console.error("⚠️ Unexpected OpenAI payload", json);
      return res.status(502).json({ error: "No content from model", provider_body: json });
    }

    return res.json({ text: reply });
  } catch (e) {
    console.error("💥 Server exception:", e);
    return res.status(500).json({ error: e?.message || "Server error" });
  }
});

const port = Number(process.env.PORT || 8787);
app.listen(port, () => console.log(`API on http://localhost:${port}`));
