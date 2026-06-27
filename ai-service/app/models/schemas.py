from pydantic import BaseModel, Field


class ResumeParseRequest(BaseModel):
    file_url: str | None = None
    text: str | None = None


class ResumeParseResponse(BaseModel):
    extracted_text: str
    skills: list[str] = Field(default_factory=list)
    email: str | None = None
    phone: str | None = None


class MatchRequest(BaseModel):
    resumeText: str
    jobDescription: str


class MatchResponse(BaseModel):
    matchScore: float
    matchedSkills: list[str]
    missingSkills: list[str]
    summary: str


class RecommendationRequest(BaseModel):
    resumeText: str
    jobs: list[dict]
