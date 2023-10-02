<script setup>
import NavbarComponent from '../components/NavbarComponent.vue';
import { ref } from 'vue';

const username = ref('')
const password = ref('')
const matchMsg = ref('')

const matchPassword = async () => {
    matchMsg.value = ''
    const data = {
        username: username.value,
        password: password.value
    }
    const response = await fetch(import.meta.env.VITE_ROOT_API + "/api/users/match", {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    if (response.ok) {
        matchMsg.value = 'Password Matched'
    } else {
        const errorData = await response.json()

        if (errorData.message?.slice(0, 4) === 'User') {
            matchMsg.value = 'The specified username DOES NOT exist'
        } else if (errorData.message === 'Password is not match') {
            matchMsg.value = 'Password NOT Matched'
        }
    }
}
</script>
<template>
    <div class="w-full h-screen bg-slate-100 items-center">
        <div class="flex flex-row w-full h-full">
            <div class="w-full  text-cyan-800 flex">
                <div class="w-1/6 bg-white rounded-br-3xl rounded-tr-3xl fixed h-screen shadow-2xl">
                    <NavbarComponent />
                </div>
                <div class="w-1/6 ">
                </div>
                <div class="w-3/5 my-32 py-12 px-32 mx-32 flex flex-col bg-white rounded-2xl shadow-2xl ">
                    <div class="text-4xl mt-5">Match Password</div>
                    <div v-if="matchMsg !== ''">
                        <div v-if="matchMsg === 'Password Matched'"
                            class="ann-message mt-10 border-2 p-5 border-emerald-500 bg-emerald-50 rounded-xl text-emerald-800">
                            {{ matchMsg }}
                        </div>
                        <div v-if="matchMsg === 'The specified username DOES NOT exist'"
                            class="ann-message mt-10 border-2 p-5 border-red-500 bg-red-50 rounded-xl text-red-800">
                            {{ matchMsg }}
                        </div>
                        <div v-if="matchMsg === 'Password NOT Matched'"
                            class="ann-message mt-10 border-2 p-5 border-red-500 bg-red-50 rounded-xl text-red-800">
                            {{ matchMsg }}
                        </div>
                    </div>
                    <div class="text-lg mt-10 text-cyan-800">Username</div>
                    <input type="text" class="ann-username border rounded-lg mt-3 pl-3 w-full h-12 bg-white"
                        v-model.trim="username" maxlength="45">
                    <span class="px-4 flex flex-col items-end">({{ username.length }}/45)</span>
                    <div class="text-lg mt-10 text-cyan-800" minlength="8" maxlength="14">Password</div>
                    <input type="password" class="ann-password border rounded-lg mt-3 pl-3 w-full h-12 bg-white"
                        v-model="password">
                    <div class="flex mt-20">
                        <button
                            class="ann-button text-white bg-cyan-800 hover:bg-cyan-700 border-0 shadow-lg transition-colors duration-200 w-28 h-12 rounded-lg"
                            @click="matchPassword">
                            Match or Not
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>