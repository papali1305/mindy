/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./app/**/*.{js,jsx,ts,tsx}", "./components/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
      },
      fontFamily: {
        suRegular: ["Segoe UI", "sans-serif"],
        suBold: ["Segoe UI Bold", "sans-serif"],
        suBoldItalic: ["Segoe UI Bold Italic", "sans-serif"],
        suItalic: ["Segoe UI Italic", "sans-serif"],
        mpBold: ["MYRIADPRO-BOLD", "sans-serif"],
        mpBoldItalic: ["MYRIADPRO-BOLDIT", "sans-serif"],
        mpRegular: ["MYRIADPRO-REGULAR", "sans-serif"],
        mpSemiBold: ["MYRIADPRO-SEMIBOLD", "sans-serif"],
        mpSemiBoldItalic: ["MYRIADPRO-SEMIBOLDIT", "sans-serif"],
        mpLight: ["MYRIADPRO-LIGHT", "sans-serif"],
      }
    },
  },
  plugins: [],
}

