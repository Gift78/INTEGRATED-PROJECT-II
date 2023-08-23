<script setup>
import { getAllUsers } from '../composable/getData.js'
import { ref ,onMounted} from 'vue';

const users = ref([])
onMounted(async () => {
    users.value = await getAllUsers();
});


</script>
 
<template>
    <div class="w-full  text-cyan-800 flex">
        <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
            <div class="ann-app-title ml-14 pt-20 text-4xl">SAS</div>
            <div class="flex-col text-xl pt-10">
                <hr>
                <button class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100">Announcement</button>
                <hr>
                <button class="ann-menu py-5 w-full text-left pl-14 bg-emerald-100 hover:bg-emerald-100">User</button>
                <hr>
            </div>
        </div>
        <div class="w-1/6">
        </div>
        <div class="w-5/6 py-20 px-5">
            <div></div>
        </div>
    </div>
</template>
<style scoped></style>