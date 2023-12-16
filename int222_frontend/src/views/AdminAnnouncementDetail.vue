<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useRouter } from 'vue-router';
import { formatDatetimeLocal } from '../composable/formatDatetime';
import Title from '../components/Title.vue';
import TimezoneComponent from '../components/TimezoneComponent.vue';
import NavbarComponent from '../components/NavbarComponent.vue'
import ViewCounter from '../components/icons/ViewCounter.vue';
import { getDataAdminById } from '../composable/getData';
import Swal from 'sweetalert2';
import DownloadIcon from '../components/icons/DownloadIcon.vue';


const { params } = useRoute();
const router = useRouter();
const data = ref({});

onMounted(async () => {
    data.value = await getDataAdminById(params?.id)

    if (data.value === undefined || data.value === null) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Sorry, the request page is not available',
            confirmButtonColor: '#155e75',
        }).then(() => {
            router.push({ name: 'AdminAnnouncement' })
        })
    }
})

const downloadFile = async (fileName) => {
    if (fileName) {
        const res = await fetch(import.meta.env.VITE_ROOT_API + `/api/file/${fileName}?announcementId=${params?.id}`, {
            method: 'GET',
        })
        if (res.ok) {
            const blob = await res.blob()
            const url = window.URL.createObjectURL(blob)
            const link = document.createElement('a')
            link.href = url
            link.setAttribute('download', fileName)
            document.body.appendChild(link)
            link.click()
            link.parentNode.removeChild(link)
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Sorry, the file is not available',
                confirmButtonColor: '#155e75',
            })
        }
    }
}
</script>
 
<template>
    <div class="w-full  text-cyan-800 flex">
        <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
            <NavbarComponent />
        </div>
        <div class="w-1/6"></div>
        <div class="w-5/6 py-10 px-20">
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
                    <div class="ql-editor w-full border rounded-lg" v-html="data?.announcementDescription"></div>

                </div>
                <div class="flex mt-5">
                    <div class="w-52 text-cyan-800 font-bold">Publish Date</div>
                    <div class="ann-publish-date text-cyan-800 w-full">{{ formatDatetimeLocal(data?.publishDate) || '-' }}
                    </div>
                </div>
                <div class="flex mt-5">
                    <div class="w-52 text-cyan-800 font-bold">Close Date</div>
                    <div class="ann-close-date text-cyan-800 w-full">{{ formatDatetimeLocal(data?.closeDate) || '-' }}</div>
                </div>
                <div class="flex mt-5">
                    <div class="w-52 text-cyan-800 font-bold">Display</div>
                    <div class="ann-display text-cyan-800 w-full">{{ data?.announcementDisplay }}</div>
                </div>
                <div class="flex mt-5">
                    <div class="w-52 text-cyan-800 font-bold">File Uploaded</div>
                    <div class="flex-col w-full">
                        <div v-for="file in data.files">
                            <button @click="downloadFile(file)"
                                class="flex justify-between rounded-lg bg-zinc-100 mb-2 hover:bg-zinc-200 transition-colors">
                                <div class=" px-5 py-2">{{ file }}</div>
                                <DownloadIcon class="my-auto mr-5" />
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- button -->
            <div class="flex justify-start mt-3" v-if="data">
                <button
                    class="ann-button text-cyan-400 bg-cyan-100 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                    @click="router.push({ name: 'AdminAnnouncement' })">
                    Back
                </button>
                <button
                    class="ann-button text-orange-400 mx-3 bg-orange-200 text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                    @click="router.push({ name: 'EditAnnouncement', params: { id: params.id } })">
                    Edit
                </button>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>
