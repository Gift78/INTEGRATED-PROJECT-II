<script setup>
import { ref, onMounted, computed, onUpdated } from 'vue';
import { useRoute, useRouter } from "vue-router";
import Swal from 'sweetalert2';
import Title from '../components/Title.vue';
import TimezoneComponent from '../components/TimezoneComponent.vue';
import NavbarComponent from '../components/NavbarComponent.vue'
import { getDataAdminById, getAllCategories } from '@/composable/getData';
import { getFormattedDate, getFormattedTime, getFormattedDateTimeISO } from '@/composable/formatDatetime';

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
const userFiles = ref([])
const files = ref([])

onMounted(async () => {
    categoryItem.value = await getAllCategories();
    if (params?.id) {
        isUpdatePage.value = true;
        announcement.value = await getDataAdminById(params?.id, false);
        publishDate.value = getFormattedDate(announcement.value.publishDate);
        publishTime.value = getFormattedTime(announcement.value.publishDate);
        closeDate.value = getFormattedDate(announcement.value.closeDate);
        closeTime.value = getFormattedTime(announcement.value.closeDate);
        categoryId.value = getCategoryId(announcement.value.announcementCategory);
        display.value = announcement.value.announcementDisplay === 'Y';
        files.value = announcement.value.files.map((file) => file)
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

const appendFile = (e) => {
  if (userFiles.value.length + e.target.files.length > 5) {
    const maxFiles = 5 - userFiles.value.length;
    for (let i = 0; i < maxFiles; i++) {
      if (e.target.files[i] !== userFiles.value[i]) {
        userFiles.value.push(e.target.files[i]);
        files.value.push(e.target.files[i]);
      }
    }
    toastMixin.fire({
      title: "Error",
      text: "The announcement can have at most 5 attachments",
    });
  } else {
    for (let i = 0; i < e.target.files.length; i++) {
      if (e.target.files[i].size > 20000000) {
        toastMixin.fire({
          title: "Error",
          text: "The files size cannot be larger than 20 MB",
        });
      } else {
        if (e.target.files[i] !== userFiles.value[i]) {
          userFiles.value.push(e.target.files[i]);
          files.value.push(e.target.files[i]);
        }
      }
    }
  }
};

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

const setDefaultValueCloseTimeAndPublishTime = () => {
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
    return closeDate.value === null || closeDate.value === undefined || closeDate.value === ''
})

const disablePublishTime = computed(() => {
    return publishDate.value === null || publishDate.value === undefined || publishDate.value === ''
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

const prepareDelete = ref([])
const confirmDelete = ref([])
const deleteAttachFile = async (file) => {
    prepareDelete.value.push(file)
    files.value.splice(files.value.indexOf(file), 1)
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

    // update announcement
    if (isUpdatePage.value === true) {
        const response = await fetch(import.meta.env.VITE_ROOT_API + `/api/announcements/${id}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`,
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
        confirmDelete.value = prepareDelete.value.map((file) => file.uniqueFileName)
        confirmDelete.value.forEach(async (file) => {
            await fetch(import.meta.env.VITE_ROOT_API + `/api/file/${file}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("token")}`,
                }
            })
        })
        if (userFiles.value.length > 0) {
            const response_update = await response.json()
            const form = document.getElementById('form');
            const formData = new FormData(form);
            for (let i = 0; i < userFiles.value.length; i++) {
                let blob = new Blob([userFiles.value[i]], { type: 'application/octet-stream' });
                formData.append('files', blob, userFiles.value[i].name);
            }
            let blob = new Blob([response_update.id], { type: 'application/json' });
            formData.append('announcementId', blob, 'announcementId');
            const responseUploadFile = await fetch(import.meta.env.VITE_ROOT_API + `/api/file`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("token")}`,
                },
                body: formData
            })
            if (responseUploadFile.ok) {
                console.log('upload file success')
            } else {
                const errorData = await responseUploadFile.json();
                Swal.fire({
                    icon: 'error',
                    title: 'Error ' + errorData.status,
                    text: errorData.message,
                    confirmButtonColor: '#155e75',
                })
            }
        }
        // add new announcement
    } else {
        const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/announcements", {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })

        const response_post = await response.json()
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
        if (userFiles.value.length > 0) {
            console.log("add file?")
            const form = document.getElementById('form');
            const formData = new FormData(form);
            for (let i = 0; i < userFiles.value.length; i++) {
                let blob = new Blob([userFiles.value[i]], { type: 'application/octet-stream' });
                formData.append('files', blob, userFiles.value[i].name);
            }
            let blob = new Blob([response_post.id], { type: 'application/json' });
            formData.append('announcementId', blob, 'announcementId');

            const responseUploadFile = await fetch(import.meta.env.VITE_ROOT_API + `/api/file`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("token")}`,
                },
                body: formData
            })
            if (responseUploadFile.ok) {
                console.log('upload file success')
            } else {
                const errorData = await responseUploadFile.json();
                Swal.fire({
                    icon: 'error',
                    title: 'Error ' + errorData.status,
                    text: errorData.message,
                    confirmButtonColor: '#155e75',
                })
            }
        }
    }
}

const showBackButtonConfirmation = () => {
    if (announcement.value.announcementDescription !== "" || announcement.value.announcementTitle !== "" || announcement.value.closeDate !== "" || announcement.value.publishDate !== "") {
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
                router.push({ path: `/admin/announcement/${params.id}` })
            }
        })
    } else {
        router.push({ name: 'AdminAnnouncement' })
    }

}

const toastMixin = Swal.mixin({
    toast: true,
    icon: 'error',
    position: 'top-right',
    showConfirmButton: false,
    timer: 3000,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
});
</script>

<template>
    <div class="w-full  text-cyan-800 flex">
        <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
            <NavbarComponent :haveComfirmation="true" />
        </div>
        <div class="w-1/6"></div>
        <div class="w-5/6 py-10 px-20">
            <!-- header -->
            <Title text="Announcement Detail" />
            <TimezoneComponent />
            <hr class="mt-4 border-2">
            <form id="form" @submit.prevent="AddEditAnnouncement(announcement, params?.id)" enctype="multipart/form-data">
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
                            <QuillEditor theme="snow" toolbar="essential"
                                v-model:content="announcement.announcementDescription" content-type="html"
                                class="ann-description h-96" />
                        </div>
                    </div>

                    <!-- attachfile -->
                    <div class="flex mt-5">
                        <div class="w-44 text-cyan-800 font-bold pt-2 mt-3">
                            Attach File
                        </div>
                        <div>
                            <div class="flex-col">
                                <label for="file"
                                    class="flex hover:cursor-pointer border-2 py-1 px-1 rounded-lg border-dashed ">
                                    <div class="text-white py-2 px-4 rounded-md"
                                        :class="files.length >= 5 ? 'cursor-not-allowed bg-zinc-200': 'bg-emerald-plus hover:bg-emerald-light hover:transition-colors'">
                                        Upload</div>
                                    <div class="my-auto w-60 px-10 truncate text-zinc-400">
                                        Add an file here</div>
                                </label>
                                <input type="file" id="file" multiple hidden :disabled="files.length >= 5" @change="appendFile($event)"
                                    class="border-2 p-2 rounded-lg border-dashed text-zinc-400 file:bg-emerald-plus file:hover:bg-emerald-light 
                            file:transition-colors file:text-white file:py-3 file:px-3 file:mr-6 file:rounded-lg file:border-0" />
                            </div>
                            <!-- attach file -->
                            <div v-for="file in files" class="flex-col">
                                <div class="flex my-3">
                                    <div class="border-2 border-dashed rounded-lg p-3 w-96" v-if="isUpdatePage">
                                        {{ file.originalFileName }}
                                        {{ file.name }}
                                    </div>
                                    <div class="border-2 border-dashed rounded-lg p-3 w-96" v-else>
                                        {{ file.name }}
                                    </div>
                                    <button v-if="isUpdatePage" @click="deleteAttachFile(file)" type="reset"
                                        class="bg-red-500 hover:bg-red-400 hover:transition-colors text-white px-4 rounded-lg mx-5">clear</button>
                                    <button v-else type="reset"
                                        @click="files.splice(files.indexOf(file), 1), userFiles.splice(files.indexOf(file), 1)"
                                        class="bg-red-500 hover:bg-red-400 hover:transition-colors text-white px-4 rounded-lg mx-5">clear</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- new date & time input -->
                    <div class="flex mt-5">
                        <div class="text-cyan-800 w-40 py-3 font-bold">Publish Date</div>
                        <div class="flex ml-1">
                            <input type="date" class="ann-publish-date bg-white mx-3 border-2 rounded-lg px-10 py-2"
                                v-model="publishDate" @change="setDefaultValueCloseTimeAndPublishTime()">
                            <input type="time" class="ann-publish-time bg-white mx-3 border-2 rounded-lg px-10 py-2"
                                v-model="publishTime" :disabled="disablePublishTime"
                                :class="disablePublishTime ? 'cursor-not-allowed text-zinc-300' : ''">
                        </div>
                    </div>
                    <div class="flex mt-5">
                        <div class="text-cyan-800 w-40 py-3 font-bold">Close Date</div>
                        <div class="flex ml-1">
                            <input type="date" class="ann-close-date bg-white mx-3 border-2 rounded-lg px-10 py-2"
                                v-model="closeDate" @change="setDefaultValueCloseTimeAndPublishTime()">
                            <input type="time" class="ann-close-time bg-white mx-3 border-2 rounded-lg px-10 py-2"
                                v-model="closeTime" :disabled="disableCloseTime"
                                :class="disableCloseTime ? 'cursor-not-allowed text-zinc-300' : ''">
                        </div>
                    </div>
                    <div class="flex mt-5">
                        <div class="w-44 text-cyan-800 font-bold pt-2">Display</div>
                        <label class="cursor-pointer label">
                            <input type="checkbox" id="display" class="ann-display checkbox checkbox-success"
                                v-model="display" :checked="display" />
                            <label for="display" class="ml-5 text-cyan-800">Check to show this announcement.</label>
                        </label>
                    </div>
                </div>

                <!-- button -->
                <div class="flex justify-end mt-3 space-x-3 " v-if="announcement">
                    <button type="reset"
                        class="ann-button text-white bg-red-500 hover:bg-red-400 border-0 shadow-lg transition-colors duration-300 w-28 h-12 ml-5 rounded-lg"
                        @click="showBackButtonConfirmation()">
                        Back
                    </button>

                    <button type="submit"
                        class="ann-button text-white bg-emerald-plus hover:bg-emerald-light border-0 shadow-lg transition-colors duration-300 w-28 h-12 rounded-lg"
                        :class="{ 'opacity-50 cursor-not-allowed': !isSubmitAllowed, 'cursor-pointer': isSubmitAllowed }"
                        :disabled="!isSubmitAllowed">
                        {{ isUpdatePage ? 'Update' : 'Add' }}
                    </button>
                </div>
            </form>
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