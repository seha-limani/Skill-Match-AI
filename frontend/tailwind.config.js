/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#17212b",
        paper: "#f7f8f4",
        moss: "#46624f",
        rust: "#b65f45",
        ocean: "#24788f",
        gold: "#d6a434",
      },
      boxShadow: {
        soft: "0 18px 45px rgba(23, 33, 43, 0.12)",
      },
    },
  },
  plugins: [],
};
