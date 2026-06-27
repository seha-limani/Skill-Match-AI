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

export type Resume = {
  id: number;
  fileName: string;
  extractedText: string;
};

export type Application = {
  id: number;
  status: string;
  matchScore?: number;
  aiSummary?: string;
  job: Job;
};
