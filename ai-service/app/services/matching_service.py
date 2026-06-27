from app.models.schemas import MatchResponse
from app.services.embedding_service import cosine_similarity
from app.services.text_cleaner import tokens


def match_resume_to_job(resume_text: str, job_description: str) -> MatchResponse:
    resume_tokens = tokens(resume_text)
    job_tokens = tokens(job_description)

    matched = sorted(job_tokens.intersection(resume_tokens))
    missing = sorted(job_tokens.difference(resume_tokens))

    semantic_score = cosine_similarity(resume_text, job_description)
    lexical_score = len(matched) / max(len(job_tokens), 1)

    if semantic_score is None:
        score = lexical_score
        summary_type = "Lexical fallback"
    else:
        score = (semantic_score * 0.75) + (lexical_score * 0.25)
        summary_type = "Semantic"

    percent = round(max(0.0, min(score, 1.0)) * 100, 1)
    if percent >= 80:
        strength = "strong"
    elif percent >= 60:
        strength = "good"
    elif percent >= 40:
        strength = "moderate"
    else:
        strength = "low"

    return MatchResponse(
        matchScore=percent,
        matchedSkills=matched[:15],
        missingSkills=missing[:12],
        summary=f"{summary_type} analysis found a {strength} candidate/job match.",
    )
