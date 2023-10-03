<script setup>
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2';

const router = useRouter();
const props = defineProps({
    haveComfirmation: {
        type: Boolean,
    }
})

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
            <div class="flex-col text-xl pt-10">
                <hr>
                <button
                    class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100 transition-colors duration-200"
                    @click="goToPage('AdminAnnouncement')">Announcement</button>
                <hr>
                <button
                    class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100 transition-colors duration-200"
                    @click="goToPage('UserListing')">User</button>
                <hr>
                <button
                    class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100 transition-colors duration-200"
                    @click="goToPage('UserMatchPassword')">Match Password</button>
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