<script setup>
import { useRouter } from 'vue-router'
import CheckCircle from '../components/icons/CheckCircle.vue'
const router = useRouter();
const login = () => {
    router.push('UserLogin')
}

</script>
 
<template>
    <div class="py-60 text-cyan-800 h-screen">
        <div class="flex-col bg-white shadow-xl w-128 mx-auto h-72 rounded-xl">
            <CheckCircle class="pt-2 text-emerald-plus mx-auto" />
            <div class="text-3xl text-center pt-2">
                You have successfully to logged out
            </div>
            <div class="text-lg text-center pt-2">Sign in again to continue working on awesome things!</div>
            <div class="flex">
                <button
                    class="bg-emerald-plus text-white px-5 py-2 mt-5 rounded-lg hover:bg-emerald-light transition-colors duration-200 mx-auto"
                    @click="login">Sign in
                </button>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>

