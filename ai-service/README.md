# SkillMatch AI Service

FastAPI service for resume parsing, resume/job matching, and recommendations.

## Run

```bash
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

The Spring Boot API calls this service through `AI_SERVICE_URL`, defaulting to `http://localhost:8000`.
