<script setup>
const props = defineProps({
    text: String
})
</script>

<template>
    <div class="text-center text-3xl text-cyan-800 py-10">{{ props.text }}</div>
</template>

<style scoped></style>