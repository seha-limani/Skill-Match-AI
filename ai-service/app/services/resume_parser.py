from app.models.schemas import ResumeParseResponse
from app.services.text_cleaner import clean_text, extract_email, extract_phone, tokens


COMMON_SKILLS = {
    "java",
    "spring",
    "springboot",
    "react",
    "typescript",
    "javascript",
    "python",
    "fastapi",
    "postgresql",
    "mysql",
    "docker",
    "aws",
    "git",
    "hibernate",
    "jpa",
    "tailwind",
    "redux",
    "rest",
    "api",
    "sql",
}


def parse_resume_text(text: str) -> ResumeParseResponse:
    cleaned = clean_text(text)
    found_tokens = tokens(cleaned)
    skills = sorted(COMMON_SKILLS.intersection(found_tokens))
    return ResumeParseResponse(
        extracted_text=cleaned,
        skills=skills,
        email=extract_email(cleaned),
        phone=extract_phone(cleaned),
    )
