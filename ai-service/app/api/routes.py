from fastapi import APIRouter

from app.models.schemas import MatchRequest, MatchResponse, RecommendationRequest, ResumeParseRequest, ResumeParseResponse
from app.services.matching_service import match_resume_to_job
from app.services.recommendation_service import recommend_jobs
from app.services.resume_parser import parse_resume_text

router = APIRouter(prefix="/ai", tags=["ai"])


@router.post("/parse-resume", response_model=ResumeParseResponse)
def parse_resume(request: ResumeParseRequest):
    return parse_resume_text(request.text or "")


@router.post("/match-resume-job", response_model=MatchResponse)
def match_resume_job(request: MatchRequest):
    return match_resume_to_job(request.resumeText, request.jobDescription)


@router.post("/recommend-jobs")
def recommend(request: RecommendationRequest):
    return {"jobs": recommend_jobs(request.resumeText, request.jobs)}


@router.post("/recommend-candidates")
def recommend_candidates(payload: dict):
    job_description = payload.get("jobDescription", "")
    candidates = payload.get("candidates", [])
    ranked = []
    for candidate in candidates:
        match = match_resume_to_job(candidate.get("resumeText", ""), job_description)
        ranked.append({**candidate, "matchScore": match.matchScore, "summary": match.summary})
    return {"candidates": sorted(ranked, key=lambda item: item["matchScore"], reverse=True)}
