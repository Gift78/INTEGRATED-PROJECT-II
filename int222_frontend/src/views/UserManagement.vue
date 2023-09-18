<script setup>
import NavbarComponent from '../components/NavbarComponent.vue'
import { getUserById } from '../composable/getData.js'
import { formatDatetimeLocal } from '../composable/formatDatetime';
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
const password = ref('')
const confirmPass = ref('')

const usernameError = ref("")
const nameError = ref("")
const emailError = ref("")
const passwordError = ref("")

const isEmailValid = computed(() => {
    const emailRegex = /^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$/;
    return emailRegex.test(email.value);

});

const isPasswordValid = computed(() => {
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).+$/
    return passwordRegex.test(password.value)
})

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
    usernameError.value = ""
    nameError.value = ""
    emailError.value = ""
    passwordError.value = ""
    const data = {
        username: username.value,
        name: name.value,
        email: email.value,
        role: role.value
    }

    if (!isEmailValid.value) {
        return;
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
            for (const detail of errorData?.detail) {
                if (detail.field === "username") {
                    usernameError.value = detail?.errorMessage
                }
                if (detail.field === "name") {
                    nameError.value = detail?.errorMessage
                }
                if (detail.field === "email") {
                    emailError.value = detail?.errorMessage
                }

            }
            Swal.fire({
                icon: 'error',
                title: 'Error ' + errorData.status,
                text: errorData.message,
                confirmButtonColor: '#155e75',
            })
        }
    } else {
        const newData = {
            username: username.value,
            name: name.value,
            email: email.value,
            role: role.value,
            password: password.value
        }
        validPassword()
        if (matchPassword.value !== true) {
            return;
        }
        const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/users", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newData)
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
            console.log(errorData)
            for (const detail of errorData?.detail) {
                if (detail.field === "username") {
                    usernameError.value = detail?.errorMessage
                }
                if (detail.field === "name") {
                    nameError.value = detail?.errorMessage
                }
                if (detail.field === "email") {
                    emailError.value = detail?.errorMessage
                }
                if (detail.field === "password") {
                    if (password.value.length < 8 || password.length > 14) {
                        passwordError.value = 'Password size must be between 8 and 14'
                    } else if (isPasswordValid) {
                        passwordError.value = 'must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters'
                    }
                }
            }
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

const matchPassword = ref(true)
const validPassword = () => {
    if (password.value !== confirmPass.value) {
        matchPassword.value = false
    } else {
        matchPassword.value = true
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
    <div class="w-full h-screen bg-slate-100">
        <div class="flex flex-row w-full h-full">
            <div class="w-full  text-cyan-800 flex">
                <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
                    <NavbarComponent :have-comfirmation="true" />
                </div>
                <div class="w-1/6">
                </div>
                <div class="w-5/6 my-8 py-12 px-32 mx-32 flex flex-col bg-white rounded-2xl shadow-2xl">
                    <div class="text-4xl mt-4 ">User Detail:</div>
                    <div class="my-4">
                        <div class="text-lg mt-4 text-cyan-800">Username</div>
                        <input type="text" required class="ann-username border rounded-lg mt-3 pl-3 w-full h-12 bg-white"
                            v-model.trim="username" maxlength="45">
                        <!-- for error and length -->
                        <div class="flex justify-between">
                            <span v-if="username == '' || usernameError !== ''"
                                class="ann-error-username px-4 text-red-500">{{
                                    usernameError }}</span>
                            <span class="px-4">({{ username.length }}/45)</span>
                        </div>

                        <!-- password field -->
                        <div v-if="isAddUserPage">
                            <div class="text-lg mt-4 text-cyan-800">Password</div>
                            <input type="password" required
                                class="ann-password border rounded-lg mt-3 pl-3 w-full h-12 bg-white" minlength="8"
                                maxlength="14" v-model.trim="password">
                            <!-- for error and length -->
                            <div class="ann-error-password px-4 text-red-500" v-if="passwordError !== ''">
                                {{ passwordError }}</div>
                            <div class="ann-error-password px-4 text-red-500" v-if="!matchPassword">The password DOES NOT match</div>

                            <div class="text-lg mt-4 text-cyan-800">Confirm Password</div>
                            <input type="password" required
                                class="ann-confirm-password border rounded-lg mt-3 pl-3 w-full h-12 bg-white" minlength="8"
                                maxlength="14" v-model.trim="confirmPass">
                        </div>


                        <div class="text-lg text-cyan-800 mt-4">Name</div>
                        <input type="text" required class="ann-name border rounded-lg mt-3 pl-3 w-full h-12 bg-white"
                            v-model.trim="name" maxlength="100">
                        <!-- for error and length -->
                        <div class="flex justify-between">
                            <span class="ann-error-name px-4 text-red-500">{{ nameError }}</span>
                            <span class="px-4">({{ name.length }}/100)</span>
                        </div>

                        <div class="text-lg text-cyan-800 mt-4">Email</div>
                        <input type="email" required class="ann-email border rounded-lg mt-3 pl-3 w-full h-12 bg-white"
                            v-model.trim="email" maxlength="150">
                        <!-- for error and length -->
                        <div class="flex justify-between">
                            <div class="flex flex-col">
                                <span v-if="!isEmailValid && email !== ''" class="text-red-500 pl-3">
                                    Email must be a well-formed email address*
                                </span>
                                <span class="ann-error-email text-red-500 pl-3">{{ emailError }}</span>
                            </div>
                            <span class="px-4">({{ email.length }}/150)</span>
                        </div>
                        <div class="text-lg text-cyan-800 mt-4">Role</div>
                        <select class="ann-role select select-bordered bg-white mt-3" v-model="role">
                            <option>admin</option>
                            <option>announcer</option>
                        </select>
                        <div class="flex mt-5" v-if="!isAddUserPage">
                            <div class="mr-10">
                                <span class="font-bold mr-3">Created On</span>
                                <p class="ann-created-on">{{ formatDatetimeLocal(user.createdOn) }}</p>
                            </div>
                            <div>
                                <span class="font-bold mr-3">Updated On</span>
                                <p class="ann-updated-on">{{ formatDatetimeLocal(user.updatedOn) }}</p>
                            </div>
                        </div>
                        <div class="flex mt-10">
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