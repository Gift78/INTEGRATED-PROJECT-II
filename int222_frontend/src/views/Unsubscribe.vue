<script setup>
import unsubIcon from '../components/icons/unsubIcon.vue'
import { useRoute } from 'vue-router';
const { query } = useRoute();

const generateUnsubToken = async () => {
    console.log(query?.email)
    const response = await fetch(import.meta.env.VITE_ROOT_API + `/api/subscription/unsubscribe/generate-token?email=${query?.email}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    if (response.ok) {
        const res = await response.json()
        // console.log(res)
        // localStorage.setItem('unsubToken', res.token)
        console.log("generate unsub token success")
        console.log(res.token)
    } else {
        const errorData = await response.json()
        console.log(errorData)
    }
}
</script>
 
<template>
    <div class="w-full flex justify-center">
        <div class="mt-56">
            <div class="text-4xl text-center">
                Unsubscribe
            </div>
            <div class="text-xl text-center">
                are you sure you wish to unsubscribe?
            </div>
            <div class="flex justify-center">
                <unsubIcon class="text-5xl text-emerald-plus" />
            </div>
            <div class="flex justify-center mt-5">
                <button
                    class="px-16 py-2 rounded-lg bg-emerald-plus text-white mx-2 hover:bg-emerald-light transition-colors duration-200">No</button>
                <button @click="generateUnsubToken"
                    class="px-16 py-2 rounded-lg bg-zinc-400 text-white mx-2 hover:bg-zinc-500 transition-colors duration-200">Yes</button>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>