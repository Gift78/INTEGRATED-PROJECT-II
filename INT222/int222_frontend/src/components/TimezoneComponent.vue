<script setup>
import IconGlobe from './icons/IconGlobe.vue';
const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
</script>

<template>
    <div class="flex">
        <div class="flex p-1 pr-3 rounded-lg" style="background-color: #12C980;">
            <IconGlobe class="pt-1" />
            <div class="text-white text-lg">Timezone :</div>
        </div>
        <div class="text-cyan-800 p-1 pl-3 text-lg">{{ timezone }}</div>
    </div>
</template>

<style scoped></style>