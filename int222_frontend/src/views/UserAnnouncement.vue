<script setup>
import { useRouter } from 'vue-router';
import { ref, onMounted, watch, computed, onUpdated } from 'vue';
import TimezoneComponent from '../components/TimezoneComponent.vue';
import Title from '../components/Title.vue';
import Published from '../components/icons/Published.vue'
import Unpublished from '../components/icons/Unpublished.vue'
import { formatDatetimeLocal } from '../composable/formatDatetime.js'
import { useMode } from '../stores/mode';
import { storeToRefs } from 'pinia'
import LoginIcon from "../components/icons/LoginIcon.vue"
import { useAuth } from '../stores/auth';
import ArrowRight from '../components/icons/ArrowRight.vue'
import Swal from 'sweetalert2';
import { getDataByPage, getAllCategories, getAnnoucementPageByCategoryId } from '../composable/getData';

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
const auth = useAuth()
const { isRefreshTokenExpired } = auth
const buttonText = ref('Login')

onMounted(async () => {
    if (mode.value == 'active') {
        activeButton.value = 'text-white bg-emerald-plus'
        closedButton.value = ''
    } else if (mode.value == 'close') {
        closedButton.value = 'text-white bg-red-500'
        activeButton.value = ''
    }
    categoryItem.value = await getAllCategories()
    data.value = await getDataByPage(mode.value, currentPage.value, 5);
    if (localStorage.getItem("token") !== null && !isRefreshTokenExpired()) {
        buttonText.value = 'Announcement List'
    } else {
        buttonText.value = 'Login'
    }
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
        activeButton.value = 'text-white bg-emerald-plus'
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
}

const goToLoginPage = () => {
    if (localStorage.getItem("token") !== null && !isRefreshTokenExpired()) {
        router.push({ name: "AdminAnnouncement" })
    } else {
        router.push({ name: "UserLogin" })
    }
}

const openSubModal = ref(false)
const openSecondSubModal = ref(false)
const subscribeSuccess = () => {
    Swal.fire({
        icon: 'success',
        title: 'Subscribe Success',
        text: 'You will receive an email when there is an announcement.',
        confirmButtonColor: '#12C980',
        confirmButtonText: 'OK',
    })
}
const chooseCategory = ref([])
const email = ref('')
const otp = ref('')
const emailInvalid = ref(false)
const categoryInvalid = ref(false)

const sendOTP = async () => {
    const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/subscription/generate-otp", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: email.value,
        })
    })

    if (response.status === 200) {

    } else {
        const errorData = await response.json();
        Swal.fire({
            icon: 'error',
            title: `Error ${errorData.status}`,
            text: errorData.message,
            confirmButtonColor: '#155e75',
        })
    }
}
const subscribe = async () => {
    console.log(chooseCategory.value)
    const isEmailValid = computed(() => {
        const emailRegex = /^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$/;
        return emailRegex.test(email.value);
    })
    if (email.value === '' || !isEmailValid.value) {
        emailInvalid.value = true
    } else {
        emailInvalid.value = false
    }
    if (chooseCategory.value.length === 0) {
        categoryInvalid.value = true
    } else {
        categoryInvalid.value = false
    }
    if (emailInvalid.value === false && categoryInvalid.value === false) {
        openSecondSubModal.value = true
        sendOTP()
    }
}

const isInvalidOTP = ref(false)
const verifyOTP = async () => {
    const data = {
        email: email.value,
        otp: otp.value,
        categoryIds: chooseCategory.value,
        action: "subscribe"
    }
    console.log(data)
    const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/subscription/verify-otp", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })

    if (response.status === 200) {
        isInvalidOTP.value = false
        console.log("success")
        openSubModal.value = false
        openSecondSubModal.value = false
        subscribeSuccess()
    } else {
        isInvalidOTP.value = true
        otp.value = ''
    }
}

</script>
 
<template>
    <div class="h-screen text-cyan-800">
        <div style="width: 80em;" class="mx-auto">
            <!-- header -->
            <div class="w-full flex justify-end">
                <button @click="goToLoginPage()"
                    class=" flex border pr-5 pl-3 py-2 rounded-lg mt-5 text-white text-lg bg-emerald-plus hover:bg-emerald-light transition-colors duration-200">
                    <LoginIcon class="text-white mx-1" />
                    {{ buttonText }}
                </button>
            </div>
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
            <div class="flex justify-between mt-5">
                <div class="flex">
                    <div class="w-40 text-cyan-800 font-bold my-auto">Choose Category :</div>
                    <div class="flex">
                        <select class="ann-category-filter select select-bordered w-full max-w-xs font-normal bg-white"
                            v-model="selectedCategory">
                            <option value="">ทั้งหมด</option>
                            <option v-for="category in categoryItem" :value="category.id">
                                {{ category.categoryName }}
                            </option>
                        </select>
                    </div>
                </div>
                <button @click="openSubModal = true"
                    class="bg-emerald-plus my-auto text-white px-7 py-2 rounded-lg hover:bg-emerald-light transition-colors duration-200">SUBSCRIBE
                </button>
            </div>

            <!-- modal -->
            <div v-if="openSubModal" class="fixed inset-0 flex items-center justify-center backdrop-brightness-75">
                <div class="bg-white w-128 h-100 p-4 rounded-3xl shadow-2xl">
                    <!-- first modal -->
                    <div v-if="!openSecondSubModal">
                        <div class="flex justify-end">
                            <button @click="openSubModal = false"
                                class="hover:scale-150 transition-all duration-200 px-3 py-1 hover:bg-slate-50 hover:rounded-full">X</button>
                        </div>
                        <div class="text-center text-2xl text-cyan-800 font-semibold mb-5">SUBSCRIBE</div>
                        <div class="flex justify-center">
                            <div
                                class="border-2 border-emerald-plus bg-emerald-plus rounded-full mx-3 px-2 font-semibold text-white">
                                1</div>
                            <div class="border-t border-emerald-plus mt-3 w-1/3"></div>
                            <div
                                class="border-2 border-emerald-plus rounded-full mx-3 px-2 font-semibold text-emerald-plus">
                                2
                            </div>
                        </div>
                        <div class="flex justify-center mt-5">
                            <div class="font-semibold text-cyan-800">Choose Category : </div>
                            <div class="grid grid-cols-2">
                                <div class="text-left ml-5 flex mb-2" v-for="category in categoryItem">
                                    <input type="checkbox" :id="category.id" :value="category.id"
                                        class="checkbox checkbox-success" v-model="chooseCategory">
                                    <label :for="category.id" class="text-cyan-800 pl-3">{{ category.categoryName }}</label>
                                </div>
                            </div>
                        </div>

                        <div class="flex justify-center mt-2">
                            <div class="font-semibold text-cyan-800 my-auto">Email : </div>
                            <input type="text" placeholder="Your email address." v-model.trim="email" maxlength="150"
                                class="border border-emerald-plus rounded-lg p-3 w-2/3 ml-5" @keydown.enter="subscribe()" />
                        </div>
                        <!-- for error -->
                        <div class="flex justify-center text-red-500" v-if="categoryInvalid">
                            Category must be selected*
                        </div>
                        <div class="flex justify-center text-red-500" v-if="emailInvalid">
                            <div>Email must be a well-formed email address*</div>
                        </div>
                        <div class="flex justify-center mt-4">
                            <button @click="subscribe()"
                                :class="categoryInvalid || emailInvalid ? 'cursor-not-allowed ' : ''"
                                class="border flex justify-center border-emerald-plus text-emerald-plus rounded-lg w-40 p-3 hover:bg-emerald-plus hover:text-white transition-colors duration-200">
                                Send OTP
                                <ArrowRight class="bg-emerald-plus text-white rounded-full ml-3" />
                            </button>
                        </div>
                    </div>
                    <!-- second modal -->
                    <div v-else>
                        <div class="flex justify-end">
                            <button @click="openSubModal = false, openSecondSubModal = false"
                                class="hover:scale-150 transition-all duration-200 px-3 py-1 hover:bg-slate-50 hover:rounded-full">X</button>
                        </div>
                        <div class="text-center text-2xl text-cyan-800 font-semibold mb-5">SUBSCRIBE</div>
                        <div class="flex justify-center">
                            <div
                                class="border-2 border-emerald-plus rounded-full mx-3 px-2 font-semibold text-emerald-plus">
                                1
                            </div>
                            <div class="border-t border-emerald-plus mt-3 w-1/3"></div>
                            <div
                                class="border-2 border-emerald-plus bg-emerald-plus rounded-full mx-3 px-2 font-semibold text-white">
                                2
                            </div>
                        </div>
                        <div class="text-center px-16 mt-5">
                            <div class="text-xl font-semibold">Check your email!</div>
                            <div>Please enter the 6-digit verification code that was sent. The code is valid 5 minutes.
                            </div>
                        </div>
                        <div class="flex justify-center mt-5">
                            <div class="text-cyan-800 font-semibold my-auto mx-3">OTP : </div>
                            <input type="text" placeholder="OTP" maxlength="6" class="border p-2 rounded-lg" v-model="otp" @keydown.enter="verifyOTP()">
                        </div>
                        <div class="flex justify-center">
                            <div v-if="isInvalidOTP" class="flex justify-center text-red-500">Invalid OTP provided</div>
                        </div>
                        <div class="flex justify-center mt-4">
                            <button @click="verifyOTP()"
                                class="border flex justify-center border-emerald-plus text-emerald-plus rounded-lg w-40 p-3 hover:bg-emerald-plus hover:text-white transition-colors duration-200">SUBSCRIBE</button>
                        </div>
                    </div>

                </div>
            </div>
            <hr class=" mt-4 border-2">
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
                    class="ann-item grid grid-cols-9 bg-white hover:bg-slate-100 transition-colors duration-200 my-5 py-7 h-20 rounded-xl shadow-md cursor-pointer"
                    @click="router.push({ name: 'UserAnnouncementDetail', params: { id: announcement.id } })">
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
                    :class="currentPage === data.totalPages - 1 ? 'cursor-not-allowed' : ''">Next
                    &gt;</button>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>