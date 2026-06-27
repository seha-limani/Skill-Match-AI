from app.services.matching_service import match_resume_to_job


def recommend_jobs(resume_text: str, jobs: list[dict]) -> list[dict]:
    ranked = []
    for job in jobs:
        description = " ".join(
            str(job.get(key, "")) for key in ("title", "description", "requiredSkills", "location")
        )
        match = match_resume_to_job(resume_text, description)
        ranked.append({**job, "matchScore": match.matchScore, "matchedSkills": match.matchedSkills})

    return sorted(ranked, key=lambda item: item["matchScore"], reverse=True)
