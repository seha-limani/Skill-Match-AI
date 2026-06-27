import { configureStore, createSlice, PayloadAction } from "@reduxjs/toolkit";

export type AuthUser = {
  token: string;
  userId: number;
  email: string;
  name: string;
  role: "ADMIN" | "EMPLOYER" | "JOB_SEEKER";
};

const stored = localStorage.getItem("skillmatch.auth");

const authSlice = createSlice({
  name: "auth",
  initialState: { user: stored ? (JSON.parse(stored) as AuthUser) : null },
  reducers: {
    setAuth(state, action: PayloadAction<AuthUser>) {
      state.user = action.payload;
      localStorage.setItem("skillmatch.auth", JSON.stringify(action.payload));
    },
    logout(state) {
      state.user = null;
      localStorage.removeItem("skillmatch.auth");
    },
  },
});

export const { setAuth, logout } = authSlice.actions;

export const store = configureStore({
  reducer: {
    auth: authSlice.reducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
