/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors:{
        "black":"#000000",
        "white":"	#FFFFFF", 
        "emerald-plus":"#12C980",
        "emerald-light":"#28DD95",
        "background":"#F3F5F7",
        "smoke":"#C9C9C9",
        "smoke-light":"#EBEBEB"
      },
    },
    
  },
  plugins: [require("daisyui")],
}