<script setup>
import { useRouter } from 'vue-router';
import { ref, onMounted, watch, computed } from 'vue';
import TimezoneComponent from '../components/TimezoneComponent.vue';
import Title from '../components/Title.vue';
import Published from '../components/icons/Published.vue'
import Unpublished from '../components/icons/Unpublished.vue'
import { getDataByPage } from '../composable/getData';
import { formatDatetimeLocal } from '../composable/formatDatetime.js'
import { useMode } from '../stores/mode';
import { storeToRefs } from 'pinia'
import { getAllCategories } from '../composable/getData';
import { getAnnoucementPageByCategoryId } from '../composable/getData';

const router = useRouter()
const modeStore = useMode()
const { mode } = storeToRefs(modeStore)
const { toggleMode } = modeStore
const activeButton = ref('')
const closedButton = ref('')
const data = ref([])
const categoryItem = ref([])
const selectedCategory = ref('')
const currentPage = ref(0)

onMounted(async () => {
    if (mode.value == 'active') {
        activeButton.value = 'text-white bg-emerald-light'
        closedButton.value = ''
    } else if (mode.value == 'close') {
        closedButton.value = 'text-white bg-red-500'
        activeButton.value = ''
    }

    categoryItem.value = await getAllCategories()
    data.value = await getDataByPage(mode.value, currentPage.value, 5);
})

watch([currentPage, selectedCategory, mode], async () => {
    if (selectedCategory.value === '') {
        if (currentPage.value === 0) {
            data.value = await getDataByPage(mode.value, 0, 5);
        } else {
            data.value = await getDataByPage(mode.value, currentPage.value, 5);
        }
    } else {
        if (currentPage.value === 0) {
            data.value = await getAnnoucementPageByCategoryId(mode.value, 0, 5, selectedCategory.value);
        } else {
            data.value = await getAnnoucementPageByCategoryId(mode.value, currentPage.value, 5, selectedCategory.value);
        }
    }
})

const changeMode = async () => {
    toggleMode()
    currentPage.value = 0
    if (mode.value == 'active') {
        activeButton.value = 'text-white bg-emerald-light'
        closedButton.value = ''
    } else if (mode.value == 'close') {
        closedButton.value = 'text-white bg-red-500'
        activeButton.value = ''
    }
}

const buttonsToShow = 10;
const displayedButtons = computed(() => {
    const from = Math.max(1, currentPage.value - Math.floor(buttonsToShow / 2) - 3);
    const to = Math.min(data.value.totalPages, from + buttonsToShow - 1);
    return Array.from({ length: to - from + 1 }, (_, index) => from + index);

});

const changePageButton = (page) => {
    currentPage.value = page;
};
</script>
 
<template>
    <div class="h-screen text-cyan-800">
        <div style="width: 80em;" class="mx-auto">
            <!-- header -->
            <Title text="SIT Announcement System (SAS)" />
            <!-- time zone bar -->
            <div class="flex justify-between">
                <TimezoneComponent />
                <div class="ann-button flex bg-white p-1 rounded-xl mx-1 cursor-pointer" @click="changeMode">
                    <div class="p-1 px-5 text-black rounded-lg flex" :class="activeButton">
                        <Published class="mr-2" />ACTIVE
                    </div>
                    <div class="p-1 px-5 text-black rounded-lg flex" :class="closedButton">
                        <Unpublished class="mr-2" />CLOSED
                    </div>
                </div>
            </div>
            <!-- dropdown-->
            <div class="flex mt-3">
                <div class="w-40 text-cyan-800 font-bold pt-3 my-auto">Choose Category :</div>
                <div class="flex pt-3">
                    <select class="ann-category-filter select select-bordered w-full max-w-xs font-normal bg-white"
                        v-model="selectedCategory">
                        <option value="">ทั้งหมด</option>
                        <option v-for="category in categoryItem" :value="category.id">
                            {{ category.categoryName }}
                        </option>
                    </select>
                </div>
            </div>

            <hr class="mt-4 border-2">
            <!-- head table -->
            <div class="grid grid-cols-9 my-5">
                <div class="text-center text-zinc-400">No.</div>
                <div class=" text-zinc-400 indent-10" :class="mode == 'active' ? 'col-span-7' : 'col-span-5'">
                    Title</div>
                <div class="text-zinc-400 col-span-2 text-center" v-if="mode == 'close'">Close Date</div>
                <div class="text-center text-zinc-400 ">Category</div>
            </div>

            <!-- content -->
            <div class="text-center items-center justify-center text-gray-400 mt-48 text-2xl"
                v-if="data === undefined || data.length === 0 || data.content.length === 0">No Announcement
            </div>
            <div v-else>
                <div v-for="(announcement, index) in data.content" :key="data.id"
                    class="ann-item grid grid-cols-9 bg-white hover:bg-slate-100 my-5 py-7 h-20 rounded-xl shadow-md cursor-pointer"
                    @click="router.push({name: 'UserAnnouncementDetail', params: {id: announcement.id}})">
                    <div class="text-center"> {{ index + 1 + (currentPage * 5) }}</div>
                    <div class="ann-title underline overflow-hidden"
                        :class="mode == 'active' ? 'col-span-7' : 'col-span-5'">
                        {{ announcement.announcementTitle }}
                    </div>
                    <div class="ann-close-date col-span-2 text-center" v-if="mode == 'close'">
                        {{ formatDatetimeLocal(announcement.closeDate) }}
                    </div>
                    <div class="ann-category text-center">{{ announcement.announcementCategory }}</div>
                </div>
            </div>

            <!-- pagination -->
            <div class="w-full my-10 flex justify-center" v-if="data?.totalElements > 5">
                <!-- previuous button -->
                <button class="ann-page-prev px-5 py-2 rounded-l-full hover:bg-slate-200"
                    @click="changePageButton(currentPage - 1)" :disabled="currentPage === 0"
                    :class="currentPage === 0 ? 'cursor-not-allowed' : ''">
                    &lt;Prev</button>
                <!-- page number button -->
                <button v-for="(button, index) in displayedButtons" :key="button" @click="changePageButton(button - 1)"
                    :class="`ann-page-${index}`, { active: currentPage === button },
                        currentPage == button - 1 ? 'bg-emerald-light text-white' : 'bg-zinc-300 hover:bg-zinc-200',
                        index == 0 ? 'rounded-l-lg' : '',
                        index == displayedButtons.length - 1 ? 'rounded-r-lg' : ''" class="w-20 h-10">
                    {{ button }}
                </button>
                <!-- next button -->
                <button class="ann-page-next px-5 py-2 rounded-r-full hover:bg-slate-200"
                    @click="changePageButton(currentPage + 1)" :disabled="currentPage === data.totalPages - 1"
                    :class="currentPage === data.totalPages - 1 ? 'cursor-not-allowed' : ''">Next &gt;</button>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>