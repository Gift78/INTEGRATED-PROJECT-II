<script setup>
import { ref } from 'vue';

const username = ref('')
const password = ref('')
const displayMsg = ref('')

const login = async () => {
    const data = {
        username: username.value,
        password: password.value
    }
    const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/token", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    if (response.ok) {
        const res = await response.json()
        localStorage.setItem('token', res.token)
        localStorage.setItem('refreshToken', res.refreshToken)
        displayMsg.value = 'Login Successful'
    } else {
        const errorData = await response.json()
        if (errorData.status === 401) {
            displayMsg.value = 'Password Incorrect'
        } else if (errorData.status === 404) {
            displayMsg.value = 'A user with specified username DOES NOT exist'
        } else {
            displayMsg.value = errorData.message
        }
    }
}
</script>
<template>
    <div class="w-full h-screen bg-slate-100 items-center text-cyan-800">
        <div class="flex flex-row w-full h-full">
            <div class="mx-auto my-56 py-7 px-7 w-128 flex flex-col bg-white rounded-2xl shadow-2xl ">
                <div class="text-4xl mb-5">SAS Login</div>
                <!-- login status -->
                <div v-if="displayMsg === 'Login Successful'"
                    class="px-5 py-4 rounded-xl mt-5 border border-emerald-500 bg-emerald-50 text-emerald-500">
                    {{ displayMsg }}
                </div>
                <div v-if="displayMsg !== 'Login Successful' && displayMsg !== ''"
                    class="px-5 py-4 rounded-xl mt-5 border border-red-500 bg-red-50 text-red-500">{{ displayMsg }}
                </div>

                <div class="text-lg mt-5 text-cyan-800">Username</div>
                <input type="text" class="ann-username border rounded-lg mt-3 pl-3 w-full h-12 bg-white"
                    v-model.trim="username" maxlength="45">
                <span class="px-4 flex flex-col items-end">({{ username.length }}/45)</span>
                <div class="text-lg text-cyan-800 ">Password</div>
                <input type="password" v-model="password"
                    class="ann-password border rounded-lg mt-3 pl-3 w-full h-12 bg-white">

                <div class="flex mt-10">
                    <button @click="login"
                        class="ann-button text-white bg-slate-500 hover:bg-slate-400 border-0 shadow-lg transition-colors duration-300 w-28 h-12 rounded-lg">
                        Login
                    </button>
                </div>
            </div>


        </div>

    </div>
</template>