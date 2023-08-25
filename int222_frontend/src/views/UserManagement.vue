<script setup>
import NavbarComponent from '../components/NavbarComponent.vue'
import { getUserById } from '../composable/getData.js'
import { formatDatetimeLocalUTC } from '../composable/formatDatetime';
import { useRoute, useRouter } from 'vue-router';
import Swal from 'sweetalert2'
import { ref, onMounted, computed } from 'vue';

const router = useRouter();
const params = useRoute().params;
const user = ref([]);
const isAddUserPage = ref(false);
const username = ref('');
const name = ref('');
const email = ref('');
const role = ref('announcer');

onMounted(async () => {
    if (params?.id) {
        user.value = await getUserById(params?.id);

        if (user.value === undefined || user.value === null) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Sorry, the request page is not available',
            confirmButtonColor: '#155e75',
            }).then(() => {
                router.push({ name: 'UserListing' })
            })
        } else {
            username.value = user.value.username;
            name.value = user.value.name;
            email.value = user.value.email;
            role.value = user.value.role;
        }
    } else {
        isAddUserPage.value = true;
    }
})

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
            router.push({ name: 'UserListing' })
        }
    })
}

const AddEditUser = async () => {
    const data = {
        username: username.value,
        name: name.value,
        email: email.value,
        role: role.value
    }

    if (isSubmitAllowed.value !== true) {
        toastMixin.fire({
            icon: 'error',
            title: 'Please fill in all the fields',
        })
        return;
    }

    if (isAddUserPage.value !== true) {
        const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/users/" + params.id, {
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
                text: 'User has been updated',
                confirmButtonColor: '#155e75',
            }).then(() => {
                router.push({ name: 'UserListing' })
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
        const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/users", {
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
                text: 'User has been added',
                confirmButtonColor: '#155e75',
            }).then(() => {
                router.push({ name: 'UserListing' })
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

const isSubmitAllowed = computed(() => {
    if (isSameValue.value === true || isEmpty.value === true) {
        return false;
    } else {
        return true;
    }
})

const isSameValue = computed(() => {
    if (username.value === user.value.username &&
        name.value === user.value.name &&
        email.value === user.value.email &&
        role.value === user.value.role) {
        return true;
    } else {
        return false;
    }
})

const isEmpty = computed(() => {
    if (username.value === '' ||
        name.value === '' ||
        email.value === '' ||
        role.value === '') {
        return true;
    } else {
        return false;
    }
})

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
    <div class="w-full h-screen bg-slate-100">
        <div class="flex flex-row w-full h-full">
            <div class="w-full  text-cyan-800 flex">
                <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
                    <NavbarComponent :have-comfirmation="true" />
                </div>
                <div class="w-1/6">
                </div>
                <div class="w-5/6 my-32 py-12 px-32 mx-32 flex flex-col bg-white rounded-2xl shadow-2xl">
                    <div class="text-4xl mt-4">User Detail:</div>
                    <div class="my-4">
                        <div class="text-lg text-black mt-4">Username</div>
                        <input type="text" class="ann-username border rounded-lg mt-3 pl-3 w-full h-12 bg-white" v-model.trim="username">
                        <div class="text-lg text-black mt-4">Name</div>
                        <input type="text" class="ann-name border rounded-lg mt-3 pl-3 w-full h-12 bg-white" v-model.trim="name" maxlength="100">
                        <div class="text-lg text-black mt-4">Email</div>
                        <input type="email" class="ann-email border rounded-lg mt-3 pl-3 w-full h-12 bg-white" v-model.trim="email" maxlength="150">
                        <div class="text-lg text-black mt-4">Role</div>
                        <select class="ann-role select select-bordered bg-white mt-3" v-model="role">
                            <option>admin</option>
                            <option>announcer</option>
                        </select>
                        <div class="flex mt-4" v-if="!isAddUserPage">
                            <div class="mr-10">
                                <span class="font-bold mr-3">Created On</span>  
                                <p class="ann-created-on">{{ formatDatetimeLocalUTC(user.createdOn) }}</p>
                            </div>
                            <div>
                                <span class="font-bold mr-3">Updated On</span>
                                <p class="ann-updated-on">{{ formatDatetimeLocalUTC(user.updatedOn) }}</p>
                            </div>
                        </div>
                        <div class="flex mt-4">
                            <button 
                                class="ann-button text-white bg-emerald-plus hover:bg-emerald-light border-0 shadow-lg hover:scale-110 w-28 h-12 rounded-lg"
                                :class="{ 'opacity-50 cursor-not-allowed': !isSubmitAllowed, 'cursor-pointer': isSubmitAllowed }"
                                @click="AddEditUser()">
                                Save
                            </button>
                            <button 
                                class="text-white bg-red-500 hover:bg-red-400 border-0 shadow-lg hover:scale-110 w-28 h-12 ml-5 rounded-lg"
                                @click="showBackButtonConfirmation()">
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<style scoped></style>