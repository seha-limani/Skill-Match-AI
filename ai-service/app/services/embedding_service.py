from functools import lru_cache

import numpy as np


@lru_cache(maxsize=1)
def _load_model():
    try:
        from sentence_transformers import SentenceTransformer

        return SentenceTransformer("all-MiniLM-L6-v2")
    except Exception:
        return None


def cosine_similarity(left: str, right: str) -> float | None:
    model = _load_model()
    if model is None:
        return None

    vectors = model.encode([left, right])
    a = np.array(vectors[0])
    b = np.array(vectors[1])
    denom = np.linalg.norm(a) * np.linalg.norm(b)
    if denom == 0:
        return 0.0
    return float(np.dot(a, b) / denom)
