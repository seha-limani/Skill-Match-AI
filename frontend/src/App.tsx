import { FormEvent, useMemo, useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { BriefcaseBusiness, Building2, FileText, LogOut, Search, Send, Sparkles, UserPlus } from "lucide-react";
import { useDispatch, useSelector } from "react-redux";
import { api, Application, Job, Resume } from "./api";
import { RootState, logout, setAuth } from "./store";
import { Button } from "./components/Button";
import { Field, TextArea } from "./components/Field";

function App() {
  const user = useSelector((state: RootState) => state.auth.user);
  const dispatch = useDispatch();
  const [query, setQuery] = useState("");
  const queryClient = useQueryClient();

  const jobs = useQuery({
    queryKey: ["jobs", query],
    queryFn: async () => (await api.get<Job[]>("/jobs", { params: query ? { q: query } : {} })).data,
  });

  const resumes = useQuery({
    queryKey: ["resumes"],
    queryFn: async () => (await api.get<Resume[]>("/resumes")).data,
    enabled: Boolean(user),
  });

  const applications = useQuery({
    queryKey: ["applications"],
    queryFn: async () => (await api.get<Application[]>("/applications/me")).data,
    enabled: Boolean(user),
  });

  const applyMutation = useMutation({
    mutationFn: async (jobId: number) =>
      (await api.post<Application>("/applications", { jobId, resumeId: resumes.data?.[0]?.id })).data,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["applications"] }),
  });

  return (
    <main className="min-h-screen bg-paper text-ink">
      <header className="border-b border-ink/10 bg-white/85 backdrop-blur">
        <div className="mx-auto flex max-w-7xl items-center justify-between px-4 py-4">
          <div className="flex items-center gap-3">
            <div className="grid size-10 place-items-center rounded-md bg-ink text-white">
              <Sparkles size={20} />
            </div>
            <div>
              <h1 className="text-xl font-bold">SkillMatch AI</h1>
              <p className="text-sm text-ink/60">Recruitment workspace</p>
            </div>
          </div>
          {user ? (
            <div className="flex items-center gap-3">
              <span className="hidden text-sm text-ink/70 sm:block">{user.name}</span>
              <Button variant="ghost" onClick={() => dispatch(logout())}>
                <LogOut size={16} />
                Logout
              </Button>
            </div>
          ) : null}
        </div>
      </header>

      <div className="mx-auto grid max-w-7xl gap-6 px-4 py-6 lg:grid-cols-[360px_1fr]">
        <aside className="grid gap-6 self-start">
          {user ? <AccountPanel /> : <AuthPanel />}
          {user ? <ResumePanel /> : null}
          {user?.role === "EMPLOYER" ? <EmployerPanel /> : null}
        </aside>

        <section className="grid gap-6">
          <div className="flex flex-col gap-3 rounded-md border border-ink/10 bg-white p-4 shadow-soft sm:flex-row sm:items-center">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-2.5 text-ink/45" size={18} />
              <input
                value={query}
                onChange={(event) => setQuery(event.target.value)}
                placeholder="Search title, description, skills"
                className="h-10 w-full rounded-md border border-ink/15 bg-white pl-10 pr-3 text-sm outline-none focus:border-ocean focus:ring-2 focus:ring-ocean/15"
              />
            </div>
            <div className="text-sm text-ink/60">{jobs.data?.length ?? 0} open roles</div>
          </div>

          <div className="grid gap-4 xl:grid-cols-2">
            {(jobs.data ?? []).map((job) => (
              <JobCard
                key={job.id}
                job={job}
                disabled={!user || applyMutation.isPending || !resumes.data?.length}
                onApply={() => applyMutation.mutate(job.id)}
              />
            ))}
            {!jobs.isLoading && !jobs.data?.length ? (
              <div className="rounded-md border border-dashed border-ink/20 bg-white p-8 text-center text-ink/60">
                No jobs yet. Register as an employer and post the first role.
              </div>
            ) : null}
          </div>

          {user ? <ApplicationsPanel applications={applications.data ?? []} /> : null}
        </section>
      </div>
    </main>
  );
}

function AuthPanel() {
  const dispatch = useDispatch();
  const [mode, setMode] = useState<"login" | "register">("register");
  const [form, setForm] = useState({
    firstName: "Demo",
    lastName: "User",
    email: "demo@skillmatch.local",
    password: "Password123",
    phone: "0771234567",
    role: "JOB_SEEKER",
  });

  const mutation = useMutation({
    mutationFn: async () => (await api.post(`/auth/${mode}`, form)).data,
    onSuccess: (data) => dispatch(setAuth(data)),
  });

  function submit(event: FormEvent) {
    event.preventDefault();
    mutation.mutate();
  }

  return (
    <form onSubmit={submit} className="grid gap-4 rounded-md border border-ink/10 bg-white p-4 shadow-soft">
      <div className="flex items-center justify-between">
        <h2 className="flex items-center gap-2 text-lg font-bold">
          <UserPlus size={19} /> Account
        </h2>
        <Button type="button" variant="ghost" onClick={() => setMode(mode === "login" ? "register" : "login")}>
          {mode === "login" ? "Register" : "Login"}
        </Button>
      </div>
      {mode === "register" ? (
        <div className="grid grid-cols-2 gap-3">
          <Field label="First name" value={form.firstName} onChange={(e) => setForm({ ...form, firstName: e.target.value })} />
          <Field label="Last name" value={form.lastName} onChange={(e) => setForm({ ...form, lastName: e.target.value })} />
        </div>
      ) : null}
      <Field label="Email" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
      <Field label="Password" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
      {mode === "register" ? (
        <>
          <Field label="Phone" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} />
          <label className="grid gap-1.5 text-sm font-medium">
            Role
            <select
              value={form.role}
              onChange={(e) => setForm({ ...form, role: e.target.value })}
              className="h-10 rounded-md border border-ink/15 bg-white px-3"
            >
              <option value="JOB_SEEKER">Job seeker</option>
              <option value="EMPLOYER">Employer</option>
            </select>
          </label>
        </>
      ) : null}
      <Button disabled={mutation.isPending}>{mode === "login" ? "Login" : "Create account"}</Button>
      {mutation.error ? <p className="text-sm text-rust">Request failed. Check backend validation or use login.</p> : null}
    </form>
  );
}

function AccountPanel() {
  const user = useSelector((state: RootState) => state.auth.user);
  return (
    <div className="rounded-md border border-ink/10 bg-white p-4 shadow-soft">
      <p className="text-sm uppercase tracking-wide text-ocean">{user?.role.replace("_", " ")}</p>
      <h2 className="mt-1 text-lg font-bold">{user?.name}</h2>
      <p className="text-sm text-ink/60">{user?.email}</p>
    </div>
  );
}

function ResumePanel() {
  const queryClient = useQueryClient();
  const [text, setText] = useState("Java Spring Boot React TypeScript PostgreSQL REST API developer with internship project experience.");
  const mutation = useMutation({
    mutationFn: async () => (await api.post("/resumes", { fileName: "profile-resume.txt", fileType: "text/plain", extractedText: text })).data,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["resumes"] }),
  });

  return (
    <div className="grid gap-3 rounded-md border border-ink/10 bg-white p-4 shadow-soft">
      <h2 className="flex items-center gap-2 text-lg font-bold">
        <FileText size={19} /> Resume
      </h2>
      <TextArea label="Resume text" value={text} onChange={(e) => setText(e.target.value)} />
      <Button onClick={() => mutation.mutate()} disabled={mutation.isPending}>
        <Send size={16} /> Save resume
      </Button>
    </div>
  );
}

function EmployerPanel() {
  const queryClient = useQueryClient();
  const [company, setCompany] = useState({ name: "SkillMatch Labs", description: "Technology company building recruitment products for modern teams.", website: "https://skillmatch.local", location: "Colombo" });
  const [job, setJob] = useState({
    title: "Full Stack Developer",
    description: "Build React and Spring Boot features for an AI-powered recruitment platform.",
    salary: 250000,
    location: "Remote",
    requiredSkills: "React, TypeScript, Java, Spring Boot, PostgreSQL",
    jobType: "FULL_TIME",
    experienceLevel: "JUNIOR",
  });

  const companyMutation = useMutation({ mutationFn: async () => (await api.post("/companies", company)).data });
  const jobMutation = useMutation({
    mutationFn: async () => (await api.post("/jobs", job)).data,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["jobs"] }),
  });

  return (
    <div className="grid gap-4 rounded-md border border-ink/10 bg-white p-4 shadow-soft">
      <h2 className="flex items-center gap-2 text-lg font-bold">
        <Building2 size={19} /> Employer tools
      </h2>
      <Field label="Company" value={company.name} onChange={(e) => setCompany({ ...company, name: e.target.value })} />
      <Button variant="secondary" onClick={() => companyMutation.mutate()} disabled={companyMutation.isPending}>
        Save company
      </Button>
      <Field label="Job title" value={job.title} onChange={(e) => setJob({ ...job, title: e.target.value })} />
      <TextArea label="Description" value={job.description} onChange={(e) => setJob({ ...job, description: e.target.value })} />
      <Field label="Skills" value={job.requiredSkills} onChange={(e) => setJob({ ...job, requiredSkills: e.target.value })} />
      <Button onClick={() => jobMutation.mutate()} disabled={jobMutation.isPending}>
        <BriefcaseBusiness size={16} /> Post job
      </Button>
    </div>
  );
}

function JobCard({ job, disabled, onApply }: { job: Job; disabled: boolean; onApply: () => void }) {
  return (
    <article className="grid gap-4 rounded-md border border-ink/10 bg-white p-5 shadow-soft">
      <div className="flex items-start justify-between gap-3">
        <div>
          <h3 className="text-lg font-bold">{job.title}</h3>
          <p className="text-sm text-ink/60">{job.company?.name ?? "Independent employer"} · {job.location ?? "Flexible"}</p>
        </div>
        <span className="rounded-md bg-gold/15 px-2.5 py-1 text-xs font-bold text-ink">{job.jobType.replace("_", " ")}</span>
      </div>
      <p className="text-sm leading-6 text-ink/75">{job.description}</p>
      <div className="flex flex-wrap gap-2">
        {(job.requiredSkills ?? "").split(",").filter(Boolean).map((skill) => (
          <span key={skill} className="rounded-md bg-ocean/10 px-2.5 py-1 text-xs font-semibold text-ocean">
            {skill.trim()}
          </span>
        ))}
      </div>
      <div className="flex items-center justify-between">
        <span className="text-sm font-bold">LKR {Number(job.salary).toLocaleString()}</span>
        <Button disabled={disabled} onClick={onApply}>
          Apply
        </Button>
      </div>
    </article>
  );
}

function ApplicationsPanel({ applications }: { applications: Application[] }) {
  const average = useMemo(() => {
    const scored = applications.filter((application) => application.matchScore != null);
    return scored.length ? Math.round(scored.reduce((sum, item) => sum + Number(item.matchScore), 0) / scored.length) : 0;
  }, [applications]);

  return (
    <div className="rounded-md border border-ink/10 bg-white p-5 shadow-soft">
      <div className="flex items-center justify-between">
        <h2 className="text-lg font-bold">Applications</h2>
        <span className="text-sm text-ink/60">Average AI score {average}%</span>
      </div>
      <div className="mt-4 grid gap-3">
        {applications.map((application) => (
          <div key={application.id} className="grid gap-1 rounded-md border border-ink/10 p-3 sm:grid-cols-[1fr_auto] sm:items-center">
            <div>
              <p className="font-semibold">{application.job.title}</p>
              <p className="text-sm text-ink/60">{application.aiSummary ?? "Waiting for AI score"}</p>
            </div>
            <span className="rounded-md bg-moss/10 px-3 py-1 text-sm font-bold text-moss">
              {application.matchScore ?? 0}% · {application.status}
            </span>
          </div>
        ))}
        {!applications.length ? <p className="text-sm text-ink/60">No applications yet.</p> : null}
      </div>
    </div>
  );
}

export default App;
