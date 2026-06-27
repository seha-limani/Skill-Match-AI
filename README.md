# SkillMatch AI

AI-powered job portal connecting employers and job seekers with smart resume analysis, job matching, applicant tracking, and interview management.

## Included

- Job seeker and employer registration/login with bearer tokens
- Company creation
- Job posting and public job search
- Resume text storage
- Job applications with AI match scores
- Spring Boot proxy endpoint for the AI service
- FastAPI resume parsing, matching, and recommendation endpoints
- React + TypeScript + Vite frontend dashboard

## Run Backend

```bash
cmd /c mvnw.cmd spring-boot:run
```

Backend runs at `http://localhost:8080`.

Local development uses H2 by default. For PostgreSQL, set:

```bash
DATABASE_URL=jdbc:postgresql://host:5432/db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=password
DATABASE_DRIVER=org.postgresql.Driver
```

## Run AI Service

```bash
cd ai-service
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

AI service runs at `http://localhost:8000`.

## Run Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173`.

Set `VITE_API_URL` if the backend is not at `http://localhost:8080/api`.

## Verified

- Backend: `cmd /c mvnw.cmd test`
- Frontend: `cmd /c npm run build`
- AI service syntax: `python -m compileall ai-service\app`
