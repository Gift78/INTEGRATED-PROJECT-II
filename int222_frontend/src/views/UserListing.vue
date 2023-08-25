<script setup>
import TimezoneComponent from '../components/TimezoneComponent.vue'
import Title from '../components/Title.vue';
import NavbarComponent from '../components/NavbarComponent.vue'
import { getAllUsers } from '../composable/getData.js'
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { formatDatetimeLocalUTC } from '../composable/formatDatetime';

const router = useRouter();
const users = ref([])
onMounted(async () => {
    users.value = await getAllUsers();
});
</script>
 
<template>
    <div class="w-full  text-cyan-800 flex">
        <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
            <NavbarComponent />
        </div>
        <div class="w-1/6"></div>
        <div class="w-5/6 py-10 px-20">
            <Title text="User Management" />
            <div class="flex justify-between">
                <TimezoneComponent />
                <button
                    class="ann-button px-5 rounded-lg text-white bg-emerald-plus hover:bg-emerald-light hover:scale-110"
                    @click="router.push({ name: 'AddUser' })">
                    Add User
                </button>
            </div>
            <hr class="my-5">
            <!-- head table -->
            <div class="grid grid-cols-14 my-5">
                <div class="text-zinc-400 text-center">No.</div>
                <div class="text-zinc-400 col-span-2">Username</div>
                <div class="text-zinc-400 col-span-2">Name</div>
                <div class="text-zinc-400 col-span-2">E-mail</div>
                <div class="text-zinc-400 text-center">Role</div>
                <div class="text-zinc-400 col-span-2 text-center">Created On</div>
                <div class="text-zinc-400 col-span-2 text-center">Updated On</div>
                <div class="text-zinc-400 col-span-2 text-center">Action</div>
            </div>
            <!-- body table -->
            <div v-if="users === undefined || users.length === 0"
                class="text-center items-center justify-center text-gray-400 mt-48 text-2xl">No Users</div>
            <div v-else class="ann-item grid grid-cols-14 my-5 bg-white h-20 rounded-xl shadow-md"
                v-for="(user, index) in users" :key="user.id">
                <div class="my-auto text-center">{{ index + 1 }}</div>
                <div class="ann-username my-auto col-span-2">{{ user.username }}</div>
                <div class="ann-name my-auto col-span-2">{{ user.name }}</div>
                <div class="ann-email my-auto col-span-2">{{ user.email }}</div>
                <div class="ann-role my-auto text-center">{{ user.role }}</div>
                <div class="ann-created-on my-auto text-center col-span-2">{{ formatDatetimeLocalUTC(user.createdOn) }}</div>
                <div class="ann-updated-on my-auto text-center col-span-2">{{ formatDatetimeLocalUTC(user.updatedOn) }}</div>
                <div class="my-auto col-span-2 flex justify-center">
                    <button
                        class="ann-button text-orange-400 bg-orange-100 hover:bg-orange-200 hover:scale-110  rounded-lg w-16 h-12 shadow-sm mx-2"
                        @click="router.push({ name: 'UserDetail', params: { id: user.id } })">edit</button>
                    <button
                        class="ann-button text-red-400 bg-red-100 hover:bg-red-200 hover:scale-110  rounded-lg w-16 h-12 shadow-sm mx-2">Delete</button>
                </div>
            </div>
        </div>
    </div>
</template>
<style scoped></style>