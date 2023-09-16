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
</script>
 
<template>
    <div>
        <div class="ann-app-title ml-14 pt-10 text-4xl">SAS</div>
        <div class="flex-col text-xl pt-10">
            <hr>
            <button class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100" @click="goToPage('AdminAnnouncement')">Announcement</button>
            <hr>
            <button class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100" @click="goToPage('UserListing')">User</button>
            <hr>
            <button class="ann-menu py-5 w-full text-left pl-14 bg-white hover:bg-emerald-100" @click="goToPage('UserMatchPassword')">Match Password</button>
            <hr>
        </div>
    </div>
</template>
 
<style scoped></style>