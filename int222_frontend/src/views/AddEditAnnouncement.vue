<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from "vue-router";
import Swal from 'sweetalert2';
import Title from '../components/Title.vue';
import TimezoneComponent from '../components/TimezoneComponent.vue';
import NavbarComponent from '../components/NavbarComponent.vue'
import { getDataById, getAllCategories } from '../composable/getData.js';
import { getFormattedDate, getFormattedTime, getFormattedDateTimeISO } from '../composable/formatDatetime.js';

const params = useRoute().params;
const router = useRouter();

const announcement = ref({});
const categoryItem = ref([]);
const publishDate = ref('');
const publishTime = ref('');
const closeDate = ref('');
const closeTime = ref('');
const display = ref(false);
const categoryId = ref(1);
const isUpdatePage = ref(false);

onMounted(async () => {
    categoryItem.value = await getAllCategories();
    if (params?.id) {
        isUpdatePage.value = true;
        announcement.value = await getDataById(params?.id, false);
        publishDate.value = getFormattedDate(announcement.value.publishDate);
        publishTime.value = getFormattedTime(announcement.value.publishDate);
        closeDate.value = getFormattedDate(announcement.value.closeDate);
        closeTime.value = getFormattedTime(announcement.value.closeDate);
        categoryId.value = getCategoryId(announcement.value.announcementCategory);

        if (announcement.value.announcementDisplay === 'Y') {
            display.value = true;
        } else {
            display.value = false;
        }
    } else {
        isUpdatePage.value = false;
        announcement.value = {
            announcementTitle: '',
            announcementDescription: '',
            publishDate: '',
            closeDate: '',
            display: false
        }
    }
});

const validateTitle = computed(() => {
    return announcement.value.announcementTitle?.length > 0 && announcement.value.announcementTitle?.length <= 200
})

const validateDescription = computed(() => {
    return announcement.value.announcementDescription?.length > 0 && announcement.value.announcementDescription?.length <= 10000 && announcement.value.announcementDescription !== '<p><br></p>'
})

const validateDate = () => {
    const today = new Date();

    if (publishDateTime.value !== null) {
        if (new Date(getFormattedDateTimeISO(publishDate.value, publishTime.value)) <= today) {
            toastMixin.fire({
                title: 'Error',
                text: 'PublishDate must be a future date',
            });

            return false;
        }
    }

    if (closeDateTime.value !== null) {
        if (new Date(getFormattedDateTimeISO(closeDate.value, closeTime.value)) <= today) {
            toastMixin.fire({
                title: 'Error',
                text: 'CloseDate must be a future date',
            });

            return false;
        }
    }

    if (closeDateTime.value !== null && publishDateTime.value !== null) {
        if (new Date(getFormattedDateTimeISO(closeDate.value, closeTime.value)) <= new Date(getFormattedDateTimeISO(publishDate.value, publishTime.value))) {
            toastMixin.fire({
                title: 'Error',
                text: 'CloseDate must be after PublishDate',
            });

            return false;
        }
    }

    return true;
}

const setDefualtValueCloseTimeAndPublishTime = () => {
    if (closeDate.value?.length === 0 || closeDate.value === '') {
        closeTime.value = '';
    } else {
        closeTime.value = '18:00';
    }

    if (publishDate.value?.length === 0 || publishDate.value === '') {
        publishTime.value = '';
    } else {
        publishTime.value = '18:00';
    }
}

const isSubmitAllowed = computed(() => {
    // validateDate();
    let isDateValid = validateDate();
    return validateTitle.value && validateDescription.value && isDateValid;
})

const disableCloseTime = computed(() => {
    return closeDate.value == null || closeDate.value == undefined || closeDate.value == ''
})

const disablePublishTime = computed(() => {
    return publishDate.value == null || publishDate.value == undefined || publishDate.value == ''
})

const publishDateTime = computed(() => {
    return getFormattedDateTimeISO(publishDate.value, publishTime.value)
})

const closeDateTime = computed(() => {
    return getFormattedDateTimeISO(closeDate.value, closeTime.value)
})

const getCategoryId = (categoryName) => {
    const category = categoryItem.value.find((item) => item.categoryName === categoryName)
    return category.id
}

const AddEditAnnouncement = async (editedAnnounce, id) => {
    const data = {
        announcementTitle: editedAnnounce.announcementTitle,
        announcementDescription: editedAnnounce.announcementDescription,
        publishDate: publishDateTime.value,
        closeDate: closeDateTime.value,
        announcementDisplay: display.value ? 'Y' : 'N',
        categoryId: categoryId.value
    }

    if (isUpdatePage.value === true) {
        const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements/" + id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })

        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Success',
                text: 'Announcement has been updated',
                confirmButtonColor: '#155e75',
            }).then(() => {
                router.push({ name: 'AdminAnnouncement' })
            })
        } else {
            const errorData = await response.json();
            Swal.fire({
                icon: 'error',
                title: 'Error ' + errorData.status,
                text: errorData.message,
                confirmButtonColor: '#155e75',
            })
        }
    } else {
        const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })

        if (response.ok) {
            Swal.fire({
                icon: 'success',
                title: 'Success',
                text: 'Announcement has been added',
                confirmButtonColor: '#155e75',
            }).then(() => {
                router.push({ name: 'AdminAnnouncement' })
            })
        } else {
            const errorData = await response.json();
            Swal.fire({
                icon: 'error',
                title: 'Error ' + errorData.status,
                text: errorData.message,
                confirmButtonColor: '#155e75',
            })
        }
    }
}

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
            router.push({ name: 'AdminAnnouncement' })
        }
    })
}

const toastMixin = Swal.mixin({
    toast: true,
    icon: 'error',
    position: 'top-right',
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
});
</script>

<template>
    <div class="w-full  text-cyan-800 flex">
        <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
            <NavbarComponent :haveComfirmation="true"/>
        </div>
        <div class="w-1/6"></div>
        <div class="w-5/6 py-10 px-20">
            <!-- header -->
            <Title text="Announcement Detail" />
            <TimezoneComponent />
            <hr class="mt-4 border-2">

            <!-- content -->
            <div class="ann-item bg-white flex-col rounded-lg p-10 shadow-lg mt-5" v-if="announcement">
                <div class="flex">
                    <div class="w-52 text-cyan-800 font-bold pt-2">
                        Title
                        <span class="text-rose-700 pl-1">*</span>
                    </div>
                    <input type="text" class="ann-title h-10 w-full bg-white rounded-lg pl-4 border" maxlength="200"
                        v-model="announcement.announcementTitle" />
                </div>
                <div class="flex mt-5">
                    <div class="w-44 text-cyan-800 font-bold pt-3">
                        Category
                        <span class="text-rose-700 pl-1">*</span>
                    </div>

                    <!-- dropdown button -->
                    <select class="ann-category select select-bordered bg-white" v-model="categoryId">
                        <option v-for="category in categoryItem" :value="category.id">
                            {{ category.categoryName }}
                        </option>
                    </select>
                </div>
                <!-- Description -->
                <div class="flex mt-5">
                    <div class="w-52 text-cyan-800 font-bold pt-2">
                        Description
                        <span class="text-rose-700 pl-1">*</span>
                    </div>
                    <div class="w-full mx-auto">
                        <QuillEditor theme="snow" toolbar="full" v-model:content="announcement.announcementDescription"
                            content-type="html" class="ann-description h-96" />
                    </div>
                </div>

                <!-- new date & time input -->
                <div class="flex mt-5">
                    <div class="text-cyan-800 w-40 py-3 font-bold">Publish Date</div>
                    <div class="flex ml-1">
                        <input type="date" class="ann-publish-date bg-white mx-3 border-2 rounded-lg px-10 py-2"
                            v-model="publishDate" @change="setDefualtValueCloseTimeAndPublishTime()">
                        <input type="time" class="ann-publish-time bg-white mx-3 border-2 rounded-lg px-10 py-2"
                            v-model="publishTime" :disabled="disablePublishTime"
                            :class="disablePublishTime ? 'cursor-not-allowed text-zinc-300' : ''">
                    </div>
                </div>
                <div class="flex mt-5">
                    <div class="text-cyan-800 w-40 py-3 font-bold">Close Date</div>
                    <div class="flex ml-1">
                        <input type="date" class="ann-close-date bg-white mx-3 border-2 rounded-lg px-10 py-2"
                            v-model="closeDate" @change="setDefualtValueCloseTimeAndPublishTime()">
                        <input type="time" class="ann-close-time bg-white mx-3 border-2 rounded-lg px-10 py-2"
                            v-model="closeTime" :disabled="disableCloseTime"
                            :class="disableCloseTime ? 'cursor-not-allowed text-zinc-300' : ''">
                    </div>
                </div>

                <div class="flex mt-5">
                    <div class="w-44 text-cyan-800 font-bold pt-2">Display</div>
                    <label class="cursor-pointer label">
                        <input type="checkbox" id="display" class="ann-display checkbox checkbox-success" v-model="display"
                            :checked="display" />
                        <label for="display" class="ml-5 text-cyan-800">Check to show this announcement.</label>
                    </label>
                </div>
            </div>

            <!-- button -->
            <div class="flex justify-end mt-3 space-x-3 " v-if="announcement">
                <button
                    class="ann-button text-black bg-white text-center rounded-lg shadow-md cursor-pointer px-5 py-2 w-20 h-10"
                    @click="showBackButtonConfirmation()">
                    Back
                </button>

                <button
                    class="ann-button text-white bg-emerald-plus text-center rounded-lg shadow-md px-5 py-2 w-20 h-10 mb-24"
                    :class="{ 'opacity-50 cursor-not-allowed': !isSubmitAllowed, 'cursor-pointer': isSubmitAllowed }"
                    :disabled="!isSubmitAllowed" @click="AddEditAnnouncement(announcement, params?.id)">
                    {{ isUpdatePage ? 'Update' : 'Add' }}
                </button>
            </div>
        </div>
    </div>
</template>

<style scoped>
input[type="time"]::-webkit-calendar-picker-indicator,
input[type="date"]::-webkit-calendar-picker-indicator {
    filter: invert(50%);
}

select {
    background-color: #fff;
}
</style>