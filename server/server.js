import express from "express";
import cors from "cors";
import "dotenv/config";
import fetch from "node-fetch";

const app = express();
app.use(cors());
app.use(express.json());

const SYSTEM_PROMPT = `
You are ACME Assistant. Stay strictly on topic for ACME app help,
answer concisely in bullet points, and refuse unrelated questions.
`;

app.post("/chat", async (req, res) => {
  try {
    const { messages = [] } = req.body ?? {};
    const body = {
      model: "gpt-4o-mini",
      messages: [{ role: "system", content: SYSTEM_PROMPT }, ...messages],
      temperature: 0.3
    };
    const r = await fetch("https://api.openai.com/v1/chat/completions", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${process.env.OPENAI_API_KEY}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body)
    });
    const j = await r.json();
    res.json({ text: j.choices?.[0]?.message?.content ?? "Sorry, no reply." });
  } catch (e) {
    res.status(500).json({ error: e?.message || "Server error" });
  }
});

const port = process.env.PORT || 8787;
app.listen(port, () => console.log(`API on http://localhost:${port}`));
