<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';
import { useMode } from '../stores/mode';
import TimezoneComponent from '../components/TimezoneComponent.vue';
import Title from '../components/Title.vue';
import Megaphone from '../components/icons/Megaphone.vue';
import { formatDatetimeLocal } from '../composable/formatDatetime';
import { storeToRefs } from 'pinia'
import { getDataUserById } from '../composable/getData';
import Swal from 'sweetalert2'
import DownloadIcon from '../components/icons/DownloadIcon.vue';

const router = useRouter();
const params = useRoute().params;
const data = ref([]);
const modeStore = useMode();
const { mode } = storeToRefs(modeStore);

onMounted(async () => {
    data.value = await getDataUserById(params?.id, true)
    console.log(data.value)

    if (data.value === undefined || data.value === null) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Sorry, the request page is not available',
            confirmButtonColor: '#155e75',
        }).then(() => {
            router.push({ name: 'UserAnnouncement' })
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
    <div style="width: 80em;" class="mx-auto">
        <Title text="SIT Announcement System (SAS)" />
        <TimezoneComponent />
        <hr class="mt-4 border-2">

        <div v-if="data" class="ann-item bg-white flex-col rounded-lg p-10 shadow-lg mt-5">
            <div class="flex ml-12">
                <div class="bg-gray-200 p-4 rounded-lg">
                    <Megaphone class="mr-2" />
                </div>
                <div :class="mode === 'close' ? 'flex-col' : ''">
                    <div :class="mode === 'close' ? 'flex' : 'flex-col'">
                        <div class="ann-title text-cyan-800 font-bold text-3xl ml-5">{{ data?.announcementTitle }}</div>
                        <div class="ann-category text-white font-bold text-base mt-2"
                            :class="mode === 'close' ? 'ml-2' : 'ml-5'">
                            <span class="bg-green-400 py-1 px-2 rounded-lg">
                                {{ data?.announcementCategory }}
                            </span>
                        </div>
                    </div>
                    <div v-if="mode === 'close'" class="flex text-white font-semibold text-sm ml-5 mt-2">
                        <span class=" bg-red-400 py-1 px-2 rounded-lg">
                            Closed on :
                        </span>
                        <span class="ann-close-date py-1 px-2 text-cyan-800">
                            {{ formatDatetimeLocal(data?.closeDate) }}
                        </span>
                    </div>
                </div>
            </div>
            <!-- description -->
            <div class="ql-editor mx-12" v-html="data?.announcementDescription"></div>
            <!-- file -->
            <div class="text-cyan-800 mx-12 mt-10" v-if="data.files?.length !== 0">
                <div v-for="file in data?.files" class="flex-col">
                    <button @click="downloadFile(file)"
                        class="flex justify-between rounded-lg bg-zinc-100 mb-2 hover:bg-zinc-200 transition-colors">
                        <div class=" px-5 py-2">{{ file }}</div>
                        <DownloadIcon class="my-auto mr-5" />
                    </button>
                </div>
            </div>
        </div>

        <div v-if="data" class="flex justify-end mt-4">
            <button
                class="ann-button bg-cyan-800 hover:bg-cyan-600 text-white rounded-lg py-2 px-6 transition-colors duration-200"
                @click="router.push({ name: 'UserAnnouncement' })">
                Back
            </button>
        </div>
    </div>
</template>

<style scoped></style>
