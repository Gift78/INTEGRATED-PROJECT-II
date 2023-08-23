<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAllData } from '../composable/getData.js';
import { formatDatetimeLocal } from '../composable/formatDatetime';
import Title from '../components/Title.vue';
import TimezoneComponent from '../components/TimezoneComponent.vue';
import NavbarComponent from '../components/NavbarComponent.vue'
import Swal from 'sweetalert2';

const data = ref([]);
const router = useRouter();

onMounted(async () => {
    data.value = await getAllData('admin');
});

const deleteAnnouncement = async (id) => {
    try {
        const res = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements/" + id, {
            method: 'DELETE'
        })
        if (res.status === 200) {
            data.value = data.value.filter((ann) => {
                return ann.id !== id
            })
        } else {
            const errorData = await res.json();
            Swal.fire({
                icon: 'error',
                title: `Error ${errorData.status}`,
                text: errorData.message,
                confirmButtonColor: '#155e75',
            })
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: `Error ${error.status}`,
            text: error.message,
            confirmButtonColor: '#155e75',
        })
    }
}

const showDeleteModal = (id) => {
    Swal.fire({
        icon: 'warning',
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        confirmButtonColor: '#155e75',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete it!',
        cancelButtonText: 'No, keep it'
    }).then((result) => {
        if (result.isConfirmed) {
            deleteAnnouncement(id)
        }
    })
}
</script>

<template>
    <div class="w-full  text-cyan-800 flex">
        <!-- header -->
        <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
            <NavbarComponent />
        </div>
        <div class="w-1/6"></div>
        <div class="w-5/6 py-10 px-20">
            <Title text="SIT Announcement System (SAS)" />
            <!-- time zone bar -->
            <div class="flex justify-between">
                <TimezoneComponent />
                <div class="ann-button text-center rounded-md text-white p-1 px-4 bg-emerald-plus hover:bg-emerald-light hover:scale-105 cursor-pointer"
                    @click="router.push({ name: 'AddAnnouncement' })">
                    + Add Announcement
                </div>

            </div>
            <hr class="mt-4 border-2">

            <!-- head table -->
            <div class="grid grid-cols-12 my-5">
                <div class="text-center text-zinc-400">No.</div>
                <div class="text-zinc-400 col-span-2">Title</div>
                <div class="text-center text-zinc-400">Category</div>
                <div class="text-center text-zinc-400 col-span-2">Publish Date</div>
                <div class="text-center text-zinc-400 col-span-2">Close Date</div>
                <div class="text-center text-zinc-400">Display</div>
                <div class="text-center text-zinc-400">Views</div>
                <div class="text-center text-zinc-400 col-span-2">Action</div>
            </div>

            <div class="text-center items-center justify-center text-gray-400 mt-48 text-2xl"
                v-if="data === undefined || data.length === 0">
                No Announcement
            </div>
            <div v-else>
                <!-- show data -->
                <div v-for="(announcement, index) in data" :key="data.id"
                    class="ann-item grid grid-cols-12 bg-white my-5 h-20 rounded-xl shadow-md">
                    <div class="text-cyan-800 my-auto text-center">{{ index + 1 }} </div>
                    <div class="ann-title text-cyan-800 my-auto col-span-2 overflow-hidden">{{
                        announcement.announcementTitle }}
                    </div>
                    <div class="ann-category text-cyan-800 my-auto text-center capitalize">{{
                        announcement.announcementCategory }}</div>
                    <div class="ann-publish-date text-cyan-800 my-auto text-center col-span-2">{{
                        formatDatetimeLocal(announcement.publishDate) || '-'
                    }} </div>
                    <div class="ann-close-date text-cyan-800 my-auto text-center col-span-2">{{
                        formatDatetimeLocal(announcement.closeDate) || '-'
                    }} </div>
                    <div class="ann-display text-cyan-800 pt-2 my-auto mx-auto text-center w-10 h-10 rounded-full"
                        :class="announcement.announcementDisplay == 'Y' ? 'bg-emerald-100 text-emerald-400' : 'bg-red-100 text-red-400'">
                        {{ announcement.announcementDisplay }}
                    </div>
                    <div class="ann-views text-cyan-800 my-auto text-center">{{ announcement.viewCount }}</div>
                    <div class="flex ml-10 col-span-2">
                        <div class="ann-button mx-2 text-cyan-400 my-auto text-center bg-cyan-100 hover:bg-cyan-200 hover:scale-110  rounded-lg pt-2 w-16 h-10 shadow-sm cursor-pointer"
                            @click="router.push({ name: 'AdminAnnouncementDetail', params: { id: announcement.id } })">
                            View
                        </div>
                        <!-- delete -->
                        <button
                            class="ann-button mx-2 text-red-400 my-auto text-center bg-red-100 hover:bg-red-200 hover:scale-110  rounded-lg w-16 h-10 shadow-sm cursor-pointer"
                            @click="showDeleteModal(announcement.id)">Delete</button>
                    </div>
                </div>
            </div>


        </div>




    </div>
</template>

<style scoped></style>
