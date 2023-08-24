<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { formatDatetimeLocal } from '../composable/formatDatetime';
import Title from '../components/Title.vue';
import TimezoneComponent from '../components/TimezoneComponent.vue';
import NavbarComponent from '../components/NavbarComponent.vue'
import ViewCounter from '../components/icons/ViewCounter.vue';
import { getDataById } from '../composable/getData';
import Swal from 'sweetalert2';

const { params } = useRoute();
const router = useRouter();
const data = ref({});

onMounted(async () => {
    data.value = await getDataById(params?.id)

    if (data.value === undefined || data.value === null) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Sorry, the request page is not available',
            confirmButtonColor: '#155e75',
        }).then(() => {
            router.push({name: 'AdminAnnouncement'})
        })
    }
})
</script>
 
<template>
    <div style="width: 80em;" class="mx-auto">
        <!-- header -->
        <Title text="Announcement Detail" />

        <div class="flex justify-between">
            <TimezoneComponent />
            <div class="flex p-1 pr-3  rounded-lg text-white bg-emerald-plus">
                <ViewCounter class="pt-1" />
                <div class="text-white text-lg">View : {{ data?.viewCount }}</div>
            </div>
        </div>

        <hr class="mt-4 border-2">

        <!-- content -->
        <div class="ann-item bg-white flex-col rounded-lg p-10 shadow-lg mt-5" v-if="data">
            <div class="flex">
                <div class="w-52 text-cyan-800 font-bold">Title</div>
                <div class="ann-title text-cyan-800 w-full">{{ data?.announcementTitle }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Category</div>
                <div class="ann-category text-cyan-800 w-full">{{ data?.announcementCategory }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Description</div>
                <div class="w-full" v-html="data?.announcementDescription"></div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Publish Date</div>
                <div class="ann-publish-date text-cyan-800 w-full">{{ formatDatetimeLocal(data?.publishDate) || '-' }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Close Date</div>
                <div class="ann-close-date text-cyan-800 w-full">{{ formatDatetimeLocal(data?.closeDate) || '-' }}</div>
            </div>
            <div class="flex mt-5">
                <div class="w-52 text-cyan-800 font-bold">Display</div>
                <div class="ann-display text-cyan-800 w-full">{{ data?.announcementDisplay }}</div>
            </div>
        </div>

        <!-- button -->
        <div class="flex justify-start mt-3" v-if="data">
            <button
                class="ann-button text-cyan-400 bg-cyan-100 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                @click="router.push({name: 'AdminAnnouncement'})">
                Back
            </button>
            <button
                class="ann-button text-orange-400 mx-3 bg-orange-200 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                @click="router.push({name: 'EditAnnouncement', params: {id: params.id}})">
                Edit
            </button>
        </div>
    </div>
</template>
 
<style scoped></style>
