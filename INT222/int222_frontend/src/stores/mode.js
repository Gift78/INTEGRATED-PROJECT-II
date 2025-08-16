import { ref } from "vue";
import { defineStore } from "pinia";

export const useMode = defineStore("mode", () => {
  const mode = ref("active");
  const toggleMode = () => {
    if (mode.value === "active") {
      mode.value = "close";
    } else {
      mode.value = "active";
    }
  };

  return { mode, toggleMode };
});
