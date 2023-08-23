<script setup>
import { getAllUsers } from '../composable/getData.js'
import { ref, onMounted } from 'vue';

const users = ref([])
onMounted(async () => {
    users.value = await getAllUsers();
});


</script>
 
<template>
    <div class="w-full h-screen bg-slate-100">
        <div class="flex flex-row w-full h-full">
            <div class="w-full  text-cyan-800 flex">
                <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
                    <div class="ann-app-title ml-14 pt-20 text-4xl">SAS</div>
                    <div class="flex-col text-xl pt-10">
                        <hr>
                        <button
                            class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100">Announcement</button>
                        <hr>
                        <button
                            class="ann-menu py-5 w-full text-left pl-14 bg-emerald-100 hover:bg-emerald-100">User</button>
                        <hr>
                    </div>
                </div>
                <div class="w-1/6">
                </div>
                <div class="w-5/6 py-16 px-10">
                    <div class="flex flex-col w-full h-full bg-white rounded-2xl shadow-2xl">
                        <div class="text-2xl pl-9 mt-4">User Detail:</div>
                        <div class="mt-4">
                            <div class="text-lg text-black pl-9 mt-4">Username</div>
                            <div class=""></div>
                        </div>
                        <div class="mt-12">
                            <div class="text-lg text-black pl-9 mt-4">Name</div>
                            <div class=""></div>
                        </div>
                        <div class="mt-12">
                            <div class="text-lg text-black pl-9 mt-4">Email</div>
                            <div class=""></div>
                        </div>
                        <div class="mt-12">
                            <div class="text-lg text-black pl-9 mt-4">Role</div>
                            <div class=""></div>
                        </div>
                        <div class="mt-16 pl-9">
                            <button class="btn  bg-emerald-plus hover:bg-emerald-500">Save</button> &nbsp;
                            <button class="btn bg-red-600 hover:bg-red-700">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<style scoped></style>