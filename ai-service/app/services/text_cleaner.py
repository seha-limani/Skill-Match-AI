import re


STOP_WORDS = {
    "and",
    "the",
    "with",
    "for",
    "you",
    "are",
    "our",
    "this",
    "that",
    "from",
    "have",
    "will",
    "your",
    "role",
    "work",
}


def clean_text(text: str) -> str:
    text = re.sub(r"\s+", " ", text or "")
    return text.strip()


def tokens(text: str) -> set[str]:
    raw = re.split(r"[^a-zA-Z0-9+#.]+", text.lower())
    return {token for token in raw if len(token) > 2 and token not in STOP_WORDS}


def extract_email(text: str) -> str | None:
    match = re.search(r"[\w.+-]+@[\w-]+\.[\w.-]+", text)
    return match.group(0) if match else None


def extract_phone(text: str) -> str | None:
    match = re.search(r"(?:\+?\d[\s-]?){9,14}\d", text)
    return match.group(0) if match else None
