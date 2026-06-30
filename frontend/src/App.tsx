import { useMemo, useState, type FormEventHandler } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  ArrowRight,
  Bell,
  Bookmark,
  BriefcaseBusiness,
  Building2,
  CheckCircle2,
  ChevronRight,
  Compass,
  CalendarDays,
  FileText,
  Heart,
  LayoutGrid,
  LogOut,
  Mail,
  MessageSquare,
  Plus,
  Search,
  ShieldCheck,
  Sparkles,
  Star,
  TrendingUp,
  Users,
  WandSparkles,
  Upload,
} from "lucide-react";
import { useDispatch, useSelector } from "react-redux";
import { Routes, Route, Navigate } from "react-router-dom";
import { api, Application, AuthResponse, Job, Notification, Resume, SavedJob } from "./api";
import { RootState, logout, setAuth } from "./store";
import { Button } from "./components/Button";
import { Field, TextArea } from "./components/Field";

function App() {
  const user = useSelector((state: RootState) => state.auth.user);
  const dispatch = useDispatch();
  const [query, setQuery] = useState("");
  const queryClient = useQueryClient();

  const isEmployer = user?.role === "EMPLOYER" || user?.role === "ADMIN";
  const hasIdentity = Boolean(user);

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

  const notifications = useQuery({
    queryKey: ["notifications"],
    queryFn: async () => (await api.get<Notification[]>("/notifications/me")).data,
    enabled: Boolean(user),
  });

  const savedJobs = useQuery({
    queryKey: ["savedJobs"],
    queryFn: async () => (await api.get<SavedJob[]>("/saved-jobs/me")).data,
    enabled: Boolean(user),
  });

  const employerJobs = useQuery({
    queryKey: ["employerJobs"],
    queryFn: async () => (await api.get<Job[]>("/jobs/me")).data,
    enabled: isEmployer,
  });

  const applyMutation = useMutation({
    mutationFn: async (jobId: number) =>
      (await api.post<Application>("/applications", { jobId, resumeId: resumes.data?.[0]?.id })).data,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["applications"] }),
  });

  const saveJobMutation = useMutation({
    mutationFn: async (jobId: number) => (await api.post<SavedJob>(`/saved-jobs/${jobId}`)).data,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["savedJobs"] }),
  });

  const unsaveJobMutation = useMutation({
    mutationFn: async (jobId: number) => (await api.delete(`/saved-jobs/${jobId}`)).data,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["savedJobs"] }),
  });

  const toggleSavedJob = (jobId: number) => {
    const isSaved = savedJobs.data?.some((savedJob) => savedJob.job.id === jobId);
    if (isSaved) {
      unsaveJobMutation.mutate(jobId);
      return;
    }
    saveJobMutation.mutate(jobId);
  };

  const averageMatchScore = useMemo(() => {
    const scoredApplications = applications.data?.filter((application) => application.matchScore != null) ?? [];
    if (!scoredApplications.length) {
      return 0;
    }
    const total = scoredApplications.reduce((sum, application) => sum + Number(application.matchScore ?? 0), 0);
    return Math.round(total / scoredApplications.length);
  }, [applications.data]);

  const unreadNotifications = useMemo(
    () => notifications.data?.filter((notification) => !notification.isRead).length ?? 0,
    [notifications.data],
  );

  const featuredSkills = useMemo(() => {
    const skills = new Set<string>();
    jobs.data?.forEach((job) => {
      job.requiredSkills
        ?.split(",")
        .map((skill) => skill.trim())
        .filter(Boolean)
        .forEach((skill) => skills.add(skill));
    });
    return Array.from(skills).slice(0, 8);
  }, [jobs.data]);

  const visibleJobs = jobs.data ?? [];
  const savedJobCount = savedJobs.data?.length ?? 0;
  const applicationCount = applications.data?.length ?? 0;
  const resumeCount = resumes.data?.length ?? 0;


  return (
    <main className="relative min-h-screen overflow-hidden text-slate-900">
      <div className="pointer-events-none absolute inset-0 -z-10 bg-[radial-gradient(circle_at_top_left,_rgba(56,189,248,0.22),_transparent_28%),radial-gradient(circle_at_top_right,_rgba(59,130,246,0.16),_transparent_24%),linear-gradient(180deg,_#f7fbff_0%,_#eef4fa_42%,_#e8eef7_100%)]" />

      <header className="sticky top-0 z-40 border-b border-white/60 bg-white/75 backdrop-blur-xl">
        <div className="mx-auto flex max-w-7xl items-center gap-3 px-4 py-4 lg:px-6">
          <div className="flex min-w-0 items-center gap-3">
            <div className="grid size-12 place-items-center rounded-2xl bg-slate-950 text-white shadow-[0_18px_35px_rgba(15,23,42,0.22)]">
              <Sparkles size={22} />
            </div>
            <div className="min-w-0">
              <h1 className="truncate text-lg font-black tracking-tight text-slate-950 sm:text-xl">SkillMatch AI</h1>
              <p className="truncate text-sm text-slate-500">A modern talent network for hiring, matching, and growth</p>
            </div>
          </div>

          <nav className="hidden flex-1 items-center justify-center gap-2 lg:flex">
            {[
              { label: "Feed", icon: LayoutGrid },
              { label: "Discover", icon: Compass },
              { label: "Messages", icon: MessageSquare },
              { label: "Alerts", icon: Bell },
            ].map((item) => (
              <button
                key={item.label}
                type="button"
                className="inline-flex items-center gap-2 rounded-full border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-600 shadow-sm transition hover:-translate-y-0.5 hover:border-sky-300 hover:text-slate-950"
              >
                <item.icon size={15} />
                {item.label}
              </button>
            ))}
          </nav>

          <div className="ml-auto flex items-center gap-2">
            {hasIdentity ? (
              <>
                <div className="hidden rounded-full border border-slate-200 bg-white px-4 py-2 text-sm text-slate-600 shadow-sm md:block">
                  Hi, <span className="font-semibold text-slate-900">{user?.name}</span>
                </div>
                <Button variant="ghost" onClick={() => dispatch(logout())} className="hidden sm:inline-flex">
                  <LogOut size={16} />
                  Logout
                </Button>
              </>
            ) : (
              <div className="hidden items-center gap-2 sm:flex">
                <span className="rounded-full border border-sky-200 bg-sky-50 px-3 py-2 text-sm font-semibold text-sky-700">For candidates</span>
                <span className="rounded-full border border-slate-200 bg-white px-3 py-2 text-sm font-semibold text-slate-600">For employers</span>
              </div>
            )}
          </div>
        </div>
      </header>

      <div className="mx-auto max-w-7xl px-4 pb-10 pt-6 lg:px-6">
        <Routes>
          <Route path="/" element={
            hasIdentity ? <Navigate to="/dashboard" replace /> : <LandingView jobsCount={visibleJobs.length} featuredSkills={featuredSkills} />
          } />
          <Route path="/dashboard" element={
            hasIdentity ? (
              <div className="grid gap-6 xl:grid-cols-[280px_minmax(0,1fr)_340px]">
                <aside className="grid gap-6 self-start">
              <ProfilePanel userName={user?.name ?? "Member"} userEmail={user?.email ?? ""} userRole={user?.role ?? "JOB_SEEKER"} />
              <QuickStatsPanel
                openJobs={visibleJobs.length}
                applications={applicationCount}
                savedJobs={savedJobCount}
                unreadNotifications={unreadNotifications}
              />
              <ShortcutsPanel isEmployer={isEmployer} />
              {isEmployer ? <EmployerPanel /> : <ResumePanel />}
            </aside>

            <section className="grid gap-6">
              <HeroPanel
                query={query}
                setQuery={setQuery}
                openJobs={visibleJobs.length}
                averageMatchScore={averageMatchScore}
                resumeCount={resumeCount}
              />

              <div className="grid gap-6 xl:grid-cols-[minmax(0,1fr)_320px]">
                <div className="grid gap-6">
                  {isEmployer ? (
                    <>
                      <FeedHeader title="Your posted jobs" subtitle="Review candidates and AI match scores" />
                      <EmployerJobsPanel jobs={employerJobs.data ?? []} />
                    </>
                  ) : (
                    <>
                      <FeedHeader title="Recommended jobs" subtitle="Fresh roles with AI-assisted relevance scores" />
                      <div className="grid gap-4">
                        {visibleJobs.map((job) => (
                          <JobCard
                            key={job.id}
                            job={job}
                            saved={savedJobs.data?.some((savedJob) => savedJob.job.id === job.id) ?? false}
                            applied={applications.data?.some((application) => application.job.id === job.id) ?? false}
                            disabled={!user || applyMutation.isPending || !resumes.data?.length}
                            onApply={() => applyMutation.mutate(job.id)}
                            onSave={() => toggleSavedJob(job.id)}
                          />
                        ))}

                        {jobs.isLoading || visibleJobs.length ? null : (
                          <EmptyState
                            title="No jobs available yet"
                            description="Create the first opportunity as an employer and it will appear here with the new feed layout."
                          />
                        )}
                      </div>
                      <ApplicationsPanel applications={applications.data ?? []} />
                    </>
                  )}
                </div>

                <div className="grid gap-6 self-start">
                  <InsightsPanel averageMatchScore={averageMatchScore} featuredSkills={featuredSkills} />
                  <NotificationsPanel notifications={notifications.data ?? []} />
                  <SavedJobsPanel
                    jobs={savedJobs.data ?? []}
                    onOpen={(jobId) => toggleSavedJob(jobId)}
                  />
                </div>
              </div>
            </section>
          </div>
            ) : (
              <Navigate to="/" replace />
            )
          } />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </main>
  );
}

function LandingView(props: Readonly<{ jobsCount: number; featuredSkills: string[] }>) {
  const { jobsCount, featuredSkills } = props;
  return (
    <div className="grid gap-6 xl:grid-cols-[minmax(0,1.2fr)_420px]">
      <section className="relative overflow-hidden rounded-[2rem] border border-white/70 bg-slate-950 p-8 text-white shadow-[0_30px_80px_rgba(15,23,42,0.25)] sm:p-10 lg:p-12">
        <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_top_right,_rgba(56,189,248,0.24),_transparent_28%),radial-gradient(circle_at_bottom_left,_rgba(99,102,241,0.18),_transparent_22%)]" />

        <div className="relative grid gap-8 xl:grid-cols-[minmax(0,1fr)_280px] xl:items-center">
          <div className="grid gap-6">
            <div className="inline-flex w-fit items-center gap-2 rounded-full border border-white/15 bg-white/10 px-4 py-2 text-sm font-semibold text-white/90 backdrop-blur">
              <ShieldCheck size={16} />
              Smart hiring with a social-network feel
            </div>

            <div className="grid gap-4">
              <h2 className="max-w-2xl text-4xl font-black tracking-tight sm:text-5xl lg:text-6xl">
                Build your career network, hire faster, and surface the right talent.
              </h2>
              <p className="max-w-2xl text-base leading-7 text-slate-300 sm:text-lg">
                SkillMatch AI blends a modern feed, clean profiles, and AI-assisted matching so candidates and employers feel like they are using a product built for the present.
              </p>
            </div>

            <div className="flex flex-wrap gap-3">
              <span className="rounded-full bg-white/10 px-4 py-2 text-sm font-semibold text-white/90">AI resume score</span>
              <span className="rounded-full bg-white/10 px-4 py-2 text-sm font-semibold text-white/90">Feed-style discovery</span>
              <span className="rounded-full bg-white/10 px-4 py-2 text-sm font-semibold text-white/90">Employer tools</span>
            </div>

            <div className="grid gap-3 sm:grid-cols-3">
              {[
                { label: "Open jobs", value: jobsCount.toString(), icon: BriefcaseBusiness },
                { label: "Featured skills", value: featuredSkills.length.toString(), icon: Star },
                { label: "AI signals", value: "Live", icon: WandSparkles },
              ].map((item) => (
                <div key={item.label} className="rounded-3xl border border-white/10 bg-white/8 p-4 backdrop-blur">
                  <item.icon size={18} className="text-sky-300" />
                  <p className="mt-4 text-3xl font-black">{item.value}</p>
                  <p className="mt-1 text-sm text-slate-300">{item.label}</p>
                </div>
              ))}
            </div>
          </div>

          <div className="relative rounded-[1.75rem] border border-white/10 bg-white/8 p-5 backdrop-blur-xl">
            <div className="flex items-center justify-between">
              <p className="text-sm font-semibold text-slate-200">Live preview</p>
              <span className="rounded-full bg-emerald-400/15 px-3 py-1 text-xs font-bold text-emerald-300">Online</span>
            </div>
            <div className="mt-5 grid gap-4">
              <div className="rounded-3xl bg-white/95 p-4 text-slate-900 shadow-xl">
                <div className="flex items-center justify-between gap-3">
                  <div>
                    <p className="text-sm text-slate-500">AI Match</p>
                    <h3 className="text-lg font-bold">Senior Full Stack Engineer</h3>
                  </div>
                  <span className="rounded-full bg-sky-100 px-3 py-1 text-sm font-bold text-sky-700">92%</span>
                </div>
                <div className="mt-4 grid gap-2 text-sm text-slate-600">
                  <p>React, TypeScript, Java, Spring Boot</p>
                  <p>Colombo • Hybrid • Competitive salary</p>
                </div>
              </div>

              <div className="grid gap-3 rounded-3xl border border-white/10 bg-slate-900/50 p-4 text-sm text-slate-200">
                {[
                  "Recruitment feed with clean browsing",
                  "Employer actions inside the dashboard",
                  "Resume, applications, notifications, and chat",
                ].map((item) => (
                  <div key={item} className="flex items-center gap-3 rounded-2xl bg-white/5 px-3 py-3">
                    <CheckCircle2 size={16} className="text-emerald-300" />
                    <span>{item}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      <AuthPanel />
    </div>
  );
}

function AuthPanel() {
  const dispatch = useDispatch();
  const [mode, setMode] = useState<"login" | "register">("register");
  const [form, setForm] = useState({
    firstName: "Demo",
    lastName: "User",
    email: "demo@skillmatch.local",
    password: "",
    phone: "0771234567",
    role: "JOB_SEEKER",
  });

  const mutation = useMutation({
    mutationFn: async () => (await api.post(`/auth/${mode}`, form)).data,
    onSuccess: (data: AuthResponse) =>
      dispatch(
        setAuth({
          token: data.accessToken,
          userId: data.user.id,
          email: data.user.email,
          name: `${data.user.firstName} ${data.user.lastName}`,
          role: data.user.role,
        }),
      ),
  });

  const submit: FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault();
    mutation.mutate();
  };

  return (
    <form
      onSubmit={submit}
      className="grid gap-5 rounded-[2rem] border border-white/70 bg-white/92 p-6 shadow-[0_30px_80px_rgba(15,23,42,0.12)] backdrop-blur-xl"
    >
      <div className="rounded-[1.5rem] bg-gradient-to-br from-slate-950 to-slate-800 p-5 text-white">
        <div className="inline-flex items-center gap-2 rounded-full bg-white/10 px-3 py-1 text-xs font-semibold uppercase tracking-[0.24em] text-white/80">
          <Sparkles size={12} />
          Join the network
        </div>
        <h2 className="mt-4 text-3xl font-black tracking-tight">{mode === "login" ? "Welcome back" : "Create your profile"}</h2>
        <p className="mt-2 text-sm leading-6 text-slate-300">
          Sign in to browse jobs, track applications, and manage your employer or candidate dashboard.
        </p>
      </div>

      <div className="flex rounded-full bg-slate-100 p-1">
        {[
          { key: "register", label: "Register" },
          { key: "login", label: "Login" },
        ].map((item) => (
          <button
            key={item.key}
            type="button"
            onClick={() => setMode(item.key as "login" | "register")}
            className={
              mode === item.key
                ? "flex-1 rounded-full bg-white px-4 py-3 text-sm font-bold text-slate-900 shadow"
                : "flex-1 rounded-full px-4 py-3 text-sm font-semibold text-slate-500 transition hover:text-slate-900"
            }
          >
            {item.label}
          </button>
        ))}
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
          <label className="grid gap-2 text-sm font-medium text-slate-700">
            Role
            
            <select
              value={form.role}
              onChange={(e) => setForm({ ...form, role: e.target.value })}
              className="h-12 rounded-2xl border border-slate-200 bg-white/90 px-4 text-sm shadow-sm outline-none focus:border-sky-300 focus:ring-4 focus:ring-sky-100"
            >
              <option value="JOB_SEEKER">Job seeker</option>
              <option value="EMPLOYER">Employer</option>
            </select>
          </label>
        </>
      ) : null}
      <Button disabled={mutation.isPending} className="w-full">
        <ArrowRight size={16} />
        {mode === "login" ? "Login" : "Create account"}
      </Button>
      {mutation.error ? <p className="text-sm text-rose-600">Request failed. Check backend validation or use login.</p> : null}
    </form>
  );
}

function ProfilePanel(props: Readonly<{ userName: string; userEmail: string; userRole: string }>) {
  const { userName, userEmail, userRole } = props;
  return (
    <div className="rounded-[1.75rem] border border-white/70 bg-white/90 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
      <div className="flex items-start justify-between gap-4">
        <div>
          <div className="inline-flex items-center gap-2 rounded-full bg-sky-50 px-3 py-1 text-xs font-bold uppercase tracking-[0.2em] text-sky-700">
            <Users size={12} />
            {userRole.replace("_", " ")}
          </div>
          <h2 className="mt-4 text-2xl font-black tracking-tight text-slate-950">{userName}</h2>
          <p className="mt-1 text-sm text-slate-500">{userEmail}</p>
        </div>
        <div className="grid size-14 place-items-center rounded-2xl bg-gradient-to-br from-sky-500 to-indigo-500 text-white shadow-lg">
          <span className="text-lg font-black">{userName.slice(0, 1).toUpperCase()}</span>
        </div>
      </div>

      <div className="mt-5 grid grid-cols-3 gap-2 text-center text-xs font-semibold text-slate-600">
        {[
          { label: "Network", value: "82" },
          { label: "Match", value: "91%" },
          { label: "Views", value: "1.4k" },
        ].map((item) => (
          <div key={item.label} className="rounded-2xl bg-slate-100 px-2 py-3">
            <p className="text-sm font-black text-slate-950">{item.value}</p>
            <p className="mt-1">{item.label}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

function QuickStatsPanel({
  openJobs,
  applications,
  savedJobs,
  unreadNotifications,
}: {
  openJobs: number;
  applications: number;
  savedJobs: number;
  unreadNotifications: number;
}) {
  const items = [
    { label: "Open jobs", value: openJobs, icon: BriefcaseBusiness, tone: "from-sky-500 to-cyan-500" },
    { label: "Applications", value: applications, icon: Mail, tone: "from-violet-500 to-fuchsia-500" },
    { label: "Saved jobs", value: savedJobs, icon: Bookmark, tone: "from-emerald-500 to-teal-500" },
    { label: "Unread alerts", value: unreadNotifications, icon: Bell, tone: "from-amber-500 to-orange-500" },
  ];

  return (
    <div className="grid gap-3 rounded-[1.75rem] border border-white/70 bg-white/90 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
      {items.map((item) => (
        <div key={item.label} className="flex items-center justify-between rounded-2xl bg-slate-50 p-4">
          <div>
            <p className="text-sm font-semibold text-slate-500">{item.label}</p>
            <p className="mt-1 text-2xl font-black text-slate-950">{item.value}</p>
          </div>
          <div className={`grid size-11 place-items-center rounded-2xl bg-gradient-to-br ${item.tone} text-white shadow-lg`}>
            <item.icon size={18} />
          </div>
        </div>
      ))}
    </div>
  );
}

function ShortcutsPanel({ isEmployer }: { isEmployer: boolean }) {
  const shortcuts = isEmployer
    ? [
        { label: "Create job", icon: Plus },
        { label: "View applicants", icon: Users },
        { label: "Schedule interviews", icon: CalendarDays },
      ]
    : [
        { label: "Upload resume", icon: FileText },
        { label: "Search jobs", icon: Search },
        { label: "Track applications", icon: TrendingUp },
      ];

  return (
    <div className="grid gap-3 rounded-[1.75rem] border border-white/70 bg-white/90 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
      <div>
        <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">Quick actions</p>
        <h3 className="mt-2 text-lg font-bold text-slate-950">Shortcuts that feel native</h3>
      </div>
      {shortcuts.map((item) => (
        <button
          key={item.label}
          type="button"
          className="flex items-center justify-between rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3 text-left text-sm font-semibold text-slate-700 transition hover:-translate-y-0.5 hover:border-sky-300 hover:bg-white"
        >
          <span className="inline-flex items-center gap-3">
            <item.icon size={16} className="text-sky-600" />
            {item.label}
          </span>
          <ChevronRight size={15} className="text-slate-400" />
        </button>
      ))}
    </div>
  );
}

function EmployerPanel() {
  const queryClient = useQueryClient();
  const [company, setCompany] = useState({
    name: "SkillMatch Labs",
    description: "Technology company building recruitment products for modern teams.",
    website: "https://skillmatch.local",
    location: "Colombo",
  });
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
    <div className="grid gap-4 rounded-[1.75rem] border border-white/70 bg-white/90 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
      <div>
        <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">Employer studio</p>
        <h2 className="mt-2 flex items-center gap-2 text-lg font-black text-slate-950">
          <Building2 size={18} className="text-sky-600" /> Company & job composer
        </h2>
      </div>

      <Field label="Company" value={company.name} onChange={(e) => setCompany({ ...company, name: e.target.value })} />
      <TextArea label="Company description" value={company.description} onChange={(e) => setCompany({ ...company, description: e.target.value })} />
      <div className="grid grid-cols-2 gap-3">
        <Field label="Website" value={company.website} onChange={(e) => setCompany({ ...company, website: e.target.value })} />
        <Field label="Location" value={company.location} onChange={(e) => setCompany({ ...company, location: e.target.value })} />
      </div>
      <Button variant="secondary" onClick={() => companyMutation.mutate()} disabled={companyMutation.isPending} className="w-full">
        <Building2 size={16} /> Save company
      </Button>

      <div className="h-px bg-slate-200" />

      <Field label="Job title" value={job.title} onChange={(e) => setJob({ ...job, title: e.target.value })} />
      <TextArea label="Description" value={job.description} onChange={(e) => setJob({ ...job, description: e.target.value })} />
      <div className="grid grid-cols-2 gap-3">
        <Field label="Skills" value={job.requiredSkills} onChange={(e) => setJob({ ...job, requiredSkills: e.target.value })} />
        <Field label="Salary" type="number" value={job.salary} onChange={(e) => setJob({ ...job, salary: Number(e.target.value) })} />
      </div>
      <Button onClick={() => jobMutation.mutate()} disabled={jobMutation.isPending} className="w-full">
        <BriefcaseBusiness size={16} /> Post job
      </Button>
    </div>
  );
}

function EmployerJobsPanel(props: Readonly<{ jobs: Job[] }>) {
  const { jobs } = props;
  return (
    <div className="grid gap-4">
      {jobs.map((job) => (
        <EmployerJobCard key={job.id} job={job} />
      ))}
      {jobs.length ? null : <EmptyState title="No jobs posted yet" description="Post a job using the composer on the left to start receiving AI-matched candidates." />}
    </div>
  );
}

function EmployerJobCard(props: Readonly<{ job: Job }>) {
  const { job } = props;
  const queryClient = useQueryClient();
  const applications = useQuery({
    queryKey: ["jobApplications", job.id],
    queryFn: async () => (await api.get<Application[]>(`/applications/job/${job.id}`)).data,
  });

  const updateStatusMutation = useMutation({
    mutationFn: async ({ appId, status, jobId }: { appId: number; status: string; jobId: number }) =>
      (await api.put(`/applications/${appId}`, { status, jobId })).data,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["jobApplications", job.id] }),
  });

  const scheduleInterviewMutation = useMutation({
    mutationFn: async ({ appId, jobId }: { appId: number; jobId: number }) =>
      (await api.post("/interviews", {
        applicationId: appId,
        jobId,
        interviewDate: new Date(Date.now() + 86400000 * 2).toISOString(),
        meetingLink: "https://meet.google.com/abc-defg-hij",
        status: "SCHEDULED",
        notes: "Initial screening interview",
      })).data,
  });

  return (
    <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <div className="flex flex-wrap items-start justify-between gap-3">
        <div>
          <h3 className="text-lg font-bold text-slate-950">{job.title}</h3>
          <p className="mt-1 text-sm text-slate-500">{job.location} · {job.jobType}</p>
        </div>
        <span className="rounded-full bg-slate-100 px-3 py-1 text-sm font-semibold text-slate-600">
          {applications.data?.length ?? 0} Applicants
        </span>
      </div>

      <div className="mt-4 grid gap-2">
        {applications.data?.map((app) => (
          <div key={app.id} className="flex items-center justify-between rounded-xl bg-slate-50 p-3 border border-slate-100">
            <div className="flex items-center gap-3">
              <div className="grid size-8 place-items-center rounded-full bg-indigo-100 text-indigo-700 font-bold text-xs">
                {app.applicant?.firstName?.[0] ?? "U"}
              </div>
              <div>
                <p className="text-sm font-semibold text-slate-900">{app.applicant?.firstName} {app.applicant?.lastName}</p>
                <p className="text-xs text-slate-500">{app.applicant?.email}</p>
              </div>
            </div>
            <div className="flex items-center gap-2">
              <span className="rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-bold text-emerald-700">
                {app.matchScore ?? 0}% Match
              </span>
              <select
                value={app.status}
                onChange={(e) => updateStatusMutation.mutate({ appId: app.id, status: e.target.value, jobId: job.id })}
                disabled={updateStatusMutation.isPending}
                className="rounded-full border border-slate-200 bg-white px-2.5 py-1 text-xs font-semibold text-slate-700 outline-none transition focus:border-sky-300 focus:ring-2 focus:ring-sky-100"
              >
                <option value="APPLIED">Applied</option>
                <option value="REVIEWING">Reviewing</option>
                <option value="INTERVIEWING">Interviewing</option>
                <option value="OFFERED">Offered</option>
                <option value="REJECTED">Rejected</option>
              </select>
              <button
                type="button"
                onClick={() => scheduleInterviewMutation.mutate({ appId: app.id, jobId: job.id })}
                disabled={scheduleInterviewMutation.isPending}
                className="rounded-full bg-indigo-50 p-1.5 text-indigo-600 transition hover:bg-indigo-100 disabled:opacity-50"
                title="Auto-schedule 2 days from now"
              >
                <CalendarDays size={14} />
              </button>
            </div>
          </div>
        ))}
        {applications.data?.length ? null : <p className="text-sm text-slate-500">Waiting for candidates to apply.</p>}
      </div>
    </div>
  );
}

        function HeroPanel(props: Readonly<{
          query: string;
          setQuery: (value: string) => void;
          openJobs: number;
          averageMatchScore: number;
          resumeCount: number;
        }>) {
          const { query, setQuery, openJobs, averageMatchScore, resumeCount } = props;
          return (
            <div className="rounded-[2rem] border border-white/70 bg-white/88 p-6 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
              <div className="grid gap-4 lg:grid-cols-[minmax(0,1fr)_320px] lg:items-center">
                <div>
                  <div className="inline-flex items-center gap-2 rounded-full bg-sky-50 px-3 py-1 text-xs font-bold uppercase tracking-[0.22em] text-sky-700">
                    <TrendingUp size={12} />
                    Career feed
                  </div>
                  <h2 className="mt-4 text-3xl font-black tracking-tight text-slate-950 sm:text-4xl">Discover opportunities that feel curated, not cluttered.</h2>
                  <p className="mt-3 max-w-2xl text-sm leading-7 text-slate-500 sm:text-base">
                    Search roles, save them, apply with a resume, and track the whole hiring journey inside a social-style product experience.
                  </p>
                </div>

                <div className="grid gap-3 rounded-[1.5rem] bg-slate-950 p-5 text-white shadow-[0_20px_50px_rgba(15,23,42,0.2)]">
                  {[
                    { label: "Open jobs", value: openJobs },
                    { label: "Avg. match", value: `${averageMatchScore}%` },
                    { label: "Resumes", value: resumeCount },
                  ].map((item) => (
                    <div key={item.label} className="flex items-center justify-between rounded-2xl bg-white/6 px-4 py-3">
                      <p className="text-sm text-slate-300">{item.label}</p>
                      <p className="text-lg font-black">{item.value}</p>
                    </div>
                  ))}
                </div>
              </div>

              <div className="mt-6 grid gap-3 lg:grid-cols-[minmax(0,1fr)_auto]">
                <div className="relative">
                  <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
                  <input
                    value={query}
                    onChange={(event) => setQuery(event.target.value)}
                    placeholder="Search jobs, skills, companies, keywords"
                    className="h-14 w-full rounded-full border border-slate-200 bg-white px-12 pr-4 text-sm shadow-sm outline-none transition placeholder:text-slate-400 focus:border-sky-300 focus:ring-4 focus:ring-sky-100"
                  />
                </div>
                <Button variant="secondary" className="w-full lg:w-auto">
                  Filter feed
                  <ChevronRight size={16} />
                </Button>
              </div>

              <div className="mt-5 flex flex-wrap gap-2">
                {[
                  "Remote",
                  "Frontend",
                  "Backend",
                  "AI",
                  "Internships",
                  "Full-time",
                ].map((chip) => (
                  <span key={chip} className="rounded-full border border-slate-200 bg-slate-50 px-4 py-2 text-sm font-semibold text-slate-600">
                    {chip}
                  </span>
                ))}
              </div>
            </div>
          );
        }

        function JobCard(props: Readonly<{
          job: Job;
          saved: boolean;
          applied: boolean;
          disabled: boolean;
          onApply: () => void;
          onSave: () => void;
        }>) {
          const { job, saved, applied, disabled, onApply, onSave } = props;
          const skillTags = (job.requiredSkills ?? "")
            .split(",")
            .map((skill) => skill.trim())
            .filter(Boolean);

          return (
            <article className="group overflow-hidden rounded-[1.85rem] border border-white/70 bg-white/92 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] transition duration-300 hover:-translate-y-1 hover:shadow-[0_35px_80px_rgba(14,165,233,0.14)]">
              <div className="flex items-start gap-4">
                <div className="grid size-14 shrink-0 place-items-center rounded-2xl bg-gradient-to-br from-sky-500 to-indigo-500 text-white shadow-lg">
                  <Building2 size={20} />
                </div>

                <div className="min-w-0 flex-1">
                  <div className="flex flex-wrap items-start justify-between gap-3">
                    <div className="min-w-0">
                      <h3 className="truncate text-xl font-black text-slate-950">{job.title}</h3>
                      <p className="mt-1 text-sm text-slate-500">
                        {job.company?.name ?? "Independent employer"} • {job.location ?? "Flexible"}
                      </p>
                    </div>
                    <div className="flex items-center gap-2">
                      {applied ? (
                        <span className="rounded-full bg-emerald-50 px-3 py-1 text-xs font-bold text-emerald-700">Applied</span>
                      ) : null}
                      <span className="rounded-full border border-slate-200 bg-slate-50 px-3 py-1 text-xs font-bold uppercase tracking-[0.16em] text-slate-500">
                        {job.jobType.replace("_", " ")}
                      </span>
                    </div>
                  </div>

                  <p className="mt-4 text-sm leading-7 text-slate-600">{job.description}</p>

                  <div className="mt-4 flex flex-wrap gap-2">
                    {skillTags.map((skill) => (
                      <span key={skill} className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-600">
                        {skill}
                      </span>
                    ))}
                  </div>

                  <div className="mt-5 flex flex-wrap items-center justify-between gap-3 border-t border-slate-100 pt-4">
                    <div>
                      <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">Salary</p>
                      <p className="mt-1 text-lg font-black text-slate-950">LKR {Number(job.salary).toLocaleString()}</p>
                    </div>

                    <div className="flex flex-wrap gap-2">
                      <Button variant="secondary" onClick={onSave} className="min-w-28">
                        {saved ? <Heart size={16} className="fill-current text-rose-500" /> : <Bookmark size={16} />}
                        {saved ? "Saved" : "Save"}
                      </Button>
                      <Button disabled={disabled} onClick={onApply} className="min-w-28">
                        Apply now
                        <ArrowRight size={16} />
                      </Button>
                    </div>
                  </div>
                </div>
              </div>
            </article>
          );
        }

        function ApplicationsPanel(props: Readonly<{ applications: Application[] }>) {
          const { applications } = props;
          const average = useMemo(() => {
            const scored = applications.filter((application) => application.matchScore != null);
            return scored.length ? Math.round(scored.reduce((sum, item) => sum + Number(item.matchScore), 0) / scored.length) : 0;
          }, [applications]);

          return (
            <div className="rounded-[1.85rem] border border-white/70 bg-white/92 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
              <div className="flex items-center justify-between gap-3">
                <div>
                  <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">Applications</p>
                  <h2 className="mt-2 text-lg font-black text-slate-950">Your hiring journey</h2>
                </div>
                <span className="rounded-full bg-slate-950 px-3 py-1 text-sm font-bold text-white">Average AI score {average}%</span>
              </div>

              <div className="mt-4 grid gap-3">
                {applications.map((application) => (
                  <div key={application.id} className="rounded-2xl border border-slate-200 bg-slate-50/90 p-4">
                    <div className="flex flex-wrap items-start justify-between gap-3">
                      <div>
                        <p className="text-base font-bold text-slate-950">{application.job.title}</p>
                        <p className="mt-1 text-sm text-slate-500">{application.aiSummary ?? "Waiting for AI score"}</p>
                      </div>
                      <span className="rounded-full bg-emerald-50 px-3 py-1 text-sm font-bold text-emerald-700">
                        {application.matchScore ?? 0}% · {application.status}
                      </span>
                    </div>
                  </div>
                ))}
                {applications.length ? null : <EmptyState title="No applications yet" description="Apply to a role and your timeline will appear here." compact />}
              </div>
            </div>
          );
        }

        function InsightsPanel(props: Readonly<{ averageMatchScore: number; featuredSkills: string[] }>) {
          const { averageMatchScore, featuredSkills } = props;
          return (
            <div className="grid gap-4 rounded-[1.85rem] border border-white/70 bg-white/92 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
              <div>
                <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">AI insights</p>
                <h2 className="mt-2 text-lg font-black text-slate-950">Resume and market signals</h2>
              </div>

              <div className="rounded-3xl bg-gradient-to-br from-slate-950 to-slate-800 p-5 text-white">
                <p className="text-sm text-slate-300">Average match score</p>
                <p className="mt-3 text-5xl font-black tracking-tight">{averageMatchScore}%</p>
                <p className="mt-3 text-sm leading-6 text-slate-300">Use your resume and saved jobs to keep the AI engine tuned to the roles you actually want.</p>
              </div>

              <div>
                <p className="text-sm font-semibold text-slate-500">Trending skills</p>
                <div className="mt-3 flex flex-wrap gap-2">
                  {featuredSkills.length ? (
                    featuredSkills.map((skill) => (
                      <span key={skill} className="rounded-full bg-sky-50 px-3 py-1 text-xs font-bold text-sky-700">
                        {skill}
                      </span>
                    ))
                  ) : (
                    <span className="text-sm text-slate-500">No skill data yet.</span>
                  )}
                </div>
              </div>
            </div>
          );
        }

        function NotificationsPanel(props: Readonly<{ notifications: Notification[] }>) {
          const { notifications } = props;
          return (
            <div className="grid gap-4 rounded-[1.85rem] border border-white/70 bg-white/92 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
              <div className="flex items-center justify-between gap-3">
                <div>
                  <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">Notifications</p>
                  <h2 className="mt-2 text-lg font-black text-slate-950">Activity stream</h2>
                </div>
                <Bell size={18} className="text-sky-600" />
              </div>

              <div className="grid gap-3">
                {notifications.length ? (
                  notifications.slice(0, 4).map((notification) => (
                    <div key={notification.id} className="rounded-2xl border border-slate-200 bg-slate-50 p-4">
                      <div className="flex items-start justify-between gap-3">
                        <div>
                          <p className="text-sm font-bold text-slate-950">{notification.title}</p>
                          <p className="mt-1 text-sm leading-6 text-slate-500">{notification.message}</p>
                        </div>
                        {notification.isRead ? null : <span className="size-2.5 rounded-full bg-sky-500" />}
                      </div>
                    </div>
                  ))
                ) : (
                  <EmptyState title="You’re all caught up" description="New status updates, interview invites, and system alerts will appear here." compact />
                )}
              </div>
            </div>
          );
        }

        function SavedJobsPanel(props: Readonly<{ jobs: SavedJob[]; onOpen: (jobId: number) => void }>) {
          const { jobs, onOpen } = props;
          return (
            <div className="grid gap-4 rounded-[1.85rem] border border-white/70 bg-white/92 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
              <div>
                <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">Saved jobs</p>
                <h2 className="mt-2 text-lg font-black text-slate-950">Your shortlist</h2>
              </div>

              <div className="grid gap-3">
                {jobs.length ? (
                  jobs.slice(0, 4).map((savedJob) => (
                    <button
                      key={savedJob.id}
                      type="button"
                      onClick={() => onOpen(savedJob.job.id)}
                      className="rounded-2xl border border-slate-200 bg-slate-50 p-4 text-left transition hover:-translate-y-0.5 hover:border-sky-300 hover:bg-white"
                    >
                      <div className="flex items-start justify-between gap-3">
                        <div>
                          <p className="text-sm font-bold text-slate-950">{savedJob.job.title}</p>
                          <p className="mt-1 text-xs text-slate-500">{savedJob.job.company?.name ?? "Saved opportunity"}</p>
                        </div>
                        <ChevronRight size={16} className="text-slate-400" />
                      </div>
                    </button>
                  ))
                ) : (
                  <EmptyState title="No saved jobs" description="Tap save on any role to build a shortlist like a modern hiring app." compact />
                )}
              </div>
            </div>
          );
        }

        function FeedHeader(props: Readonly<{ title: string; subtitle: string }>) {
          const { title, subtitle } = props;
          return (
            <div className="flex flex-wrap items-end justify-between gap-3">
              <div>
                <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">Feed</p>
                <h2 className="mt-2 text-2xl font-black tracking-tight text-slate-950">{title}</h2>
              </div>
              <p className="max-w-md text-sm leading-6 text-slate-500">{subtitle}</p>
            </div>
          );
        }

        function EmptyState(props: Readonly<{ title: string; description: string; compact?: boolean }>) {
          const { title, description, compact = false } = props;
          return (
            <div className={compact ? "rounded-2xl border border-dashed border-slate-200 bg-white p-4" : "rounded-[1.75rem] border border-dashed border-slate-200 bg-white p-8 text-center"}>
              <div className="mx-auto flex size-12 items-center justify-center rounded-2xl bg-slate-100 text-slate-500">
                <Compass size={18} />
              </div>
              <h3 className="mt-4 text-base font-bold text-slate-950">{title}</h3>
              <p className="mt-2 text-sm leading-6 text-slate-500">{description}</p>
            </div>
          );
        }

function ResumePanel() {
  const queryClient = useQueryClient();
  const [resumeText, setResumeText] = useState("");
  const mutation = useMutation({
    mutationFn: async () => (await api.post("/resumes/upload", { content: resumeText })).data,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["resumes"] });
      setResumeText("");
    },
  });

  return (
    <div className="grid gap-4 rounded-[1.75rem] border border-white/70 bg-white/90 p-5 shadow-[0_25px_60px_rgba(15,23,42,0.12)] backdrop-blur-xl">
      <div>
        <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">Your profile</p>
        <h2 className="mt-2 flex items-center gap-2 text-lg font-black text-slate-950">
          <FileText size={18} className="text-sky-600" /> Upload Resume
        </h2>
      </div>
      <TextArea label="Paste your resume text here" value={resumeText} onChange={(e) => setResumeText(e.target.value)} />
      <Button onClick={() => mutation.mutate()} disabled={mutation.isPending || !resumeText} className="w-full">
        <Upload size={16} /> Save & Parse
      </Button>
    </div>
  );
}

export default App;
