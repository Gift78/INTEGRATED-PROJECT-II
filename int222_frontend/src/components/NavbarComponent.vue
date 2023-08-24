<script setup>
import { useRouter } from 'vue-router'

const router = useRouter();
</script>
 
<template>
    <div>
        <div class="ann-app-title ml-14 pt-20 text-4xl">SAS</div>
        <div class="flex-col text-xl pt-10">
            <hr>
            <button class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100" @click="router.push({ name: 'AdminAnnouncement' })">Announcement</button>
            <hr>
            <button class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100" @click="router.push({ name: 'userListing' })">User</button>
            <hr>
        </div>
    </div>
</template>
 
<style scoped></style>