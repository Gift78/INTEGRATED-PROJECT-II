<script setup>
import { getUserById } from '../composable/getData.js'
import { useRoute, useRouter } from 'vue-router';
import Swal from 'sweetalert2'
import { ref, onMounted } from 'vue';

const router = useRouter();
const params = useRoute().params;
const users = ref([])

onMounted(async () => {
    users.value = await getUserById(params?.id)
    if (users.value === undefined || users.value === null) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Sorry, the request page is not available',
            confirmButtonColor: '#155e75',
        }).then(() => {
            router.push({ name: 'userListing' })
        })
    }
})

const showBackButtonConfirmation = () => {
    Swal.fire({
        icon: 'warning',
        title: 'Are you sure?',
        text: "You will lose all the changes you made!",
        confirmButtonColor: '#155e75',
        showCancelButton: true,
        confirmButtonText: 'Yes, go back',
        cancelButtonText: 'No, keep it'
    }).then((result) => {
        if (result.isConfirmed) {
            router.push({ name: 'userListing' })
        }
    })
}

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
                <div class="w-5/6 my-32 py-12 px-32 mx-32 flex flex-col bg-white rounded-2xl shadow-2xl">
                    <div class="text-4xl mt-4">User Detail:</div>
                    <div class="my-4">
                        <div class="text-lg text-black mt-4">Username</div>
                        <input type="text" class="border rounded-lg mt-3 pl-3 w-full h-12" v-model="users.username">
                        <div class="text-lg text-black mt-4">Name</div>
                        <input type="text" class="border rounded-lg mt-3 pl-3 w-full h-12" v-model="users.name">
                        <div class="text-lg text-black mt-4">Email</div>
                        <input type="text" class="border rounded-lg mt-3 pl-3 w-full h-12" v-model="users.email">
                        <div class="text-lg text-black mt-4">Role</div>
                        <select class="select select-bordered bg-white mt-3" v-model="users.role">
                            <option>admin</option>
                            <option>announcer</option>
                        </select>
                        <div class="flex">
                            <button
                                class="text-white bg-emerald-plus hover:bg-emerald-light border-0 shadow-lg hover:scale-110 w-28 h-12 mt-10 rounded-lg">Save</button>
                            <button
                                class="text-white bg-red-500 hover:bg-red-400 border-0 shadow-lg hover:scale-110 w-28 h-12 mt-10 ml-5 rounded-lg"
                                @click="showBackButtonConfirmation()">
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<style scoped></style>