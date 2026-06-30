import axios from "axios";
import { store } from "./store";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? "http://localhost:8080/api",
});

api.interceptors.request.use((config) => {
  const token = store.getState().auth.user?.token;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export type Job = {
  id: number;
  title: string;
  description: string;
  salary: number;
  location?: string;
  requiredSkills?: string;
  jobType: string;
  status: string;
  experienceLevel?: string;
  company?: { id: number; name: string };
  matchScore?: number;
};

export type UserResponse = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  profileImage?: string | null;
  bio?: string | null;
  role: "ADMIN" | "EMPLOYER" | "JOB_SEEKER";
};

export type AuthResponse = {
  tokenType: string;
  accessToken: string;
  user: UserResponse;
};

export type Resume = {
  id: number;
  fileName: string;
  extractedText: string;
};

export type Notification = {
  id: number;
  title: string;
  message: string;
  isRead: boolean;
  createdAt?: string;
};

export type SavedJob = {
  id: number;
  savedAt?: string;
  job: Job;
};

export type Application = {
  id: number;
  status: string;
  matchScore?: number;
  aiSummary?: string;
  job: Job;
  applicant?: UserResponse;
};
