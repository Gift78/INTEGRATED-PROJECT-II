<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import Swal from 'sweetalert2';
import { useAuth } from '../stores/auth';
import UserIcon from '../components/icons/UserIcon.vue'


const router = useRouter();
const props = defineProps({
    haveComfirmation: {
        type: Boolean,
    }
})

const auth = useAuth()
const { getRole, getUsername } = auth

const goToPage = (pageName) => {
    if (props.haveComfirmation) {
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
                router.push({ name: pageName })
            }
        })
    } else {
        router.push({ name: pageName })
    }
}

const logout = (pageName) => {
    Swal.fire({
        icon: 'question',
        title: 'Log out?',
        text: 'Are you sure want to log out?',
        confirmButtonColor: '#155e75',
        showCancelButton: true,
        confirmButtonText: 'log out',
        cancelButtonText: 'cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.clear()
            router.push({ name: pageName })
        }
    })
}
</script>
 
<template>
    <div>
        <div class="ann-app-title ml-14 pt-10 text-4xl">SAS</div>
        <div class="flex-col">
            <div class="flex-col text-lg pt-5">
                <div class="w-full pl-3 py-5 flex">
                    <UserIcon class="fixed" />
                    <p class="pt-1 pl-12 text-2xl overflow-hidden">{{ getUsername() }}</p>
                    <p :class="getRole()==='admin'?'bg-sky-600':'bg-yellow-600'" class="mt-3 ml-3 text-sm text-white h-6 px-1 rounded-lg">{{ getRole() }}</p>
                </div>
                <hr>
                <button
                    class="ann-menu py-5 w-full text-left pl-10 bg-white hover:bg-emerald-100 transition-colors duration-200"
                    @click="goToPage('UserAnnouncement')">Announcement(Viewer)</button>
                <hr>
                <button
                    class="ann-menu py-5 w-full text-left pl-10 bg-white hover:bg-emerald-100 transition-colors duration-200"
                    @click="goToPage('AdminAnnouncement')">Announcement</button>
                <hr v-if="getRole() === 'admin'">
                <button
                    class="ann-menu py-5 w-full text-left pl-10 bg-white hover:bg-emerald-100 transition-colors duration-200"
                    @click="goToPage('UserListing')" v-if="getRole() === 'admin'">User</button>
                <hr v-if="getRole() === 'admin'">
                <button
                    class="ann-menu py-5 w-full text-left pl-10 bg-white hover:bg-emerald-100 transition-colors duration-200"
                    @click="goToPage('UserMatchPassword')" v-if="getRole() === 'admin'">Match Password</button>
                <hr>
            </div>
            <div>
                <button
                    class="text-xl bg-red-500 text-white px-6 py-3 rounded-lg text-center ml-14 mt-5 hover:bg-red-400 transition-colors duration-200"
                    @click="logout('UserLogout')">Sign Out</button>
            </div>
        </div>

    </div>
</template>
 
<style scoped></style>