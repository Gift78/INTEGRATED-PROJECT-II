<script setup>
import { ref, onMounted } from 'vue';
import Swal from 'sweetalert2';
import unsubIcon from '../components/icons/unsubIcon.vue'
import { useRoute } from 'vue-router';
import { getAllCategories } from '../composable/getData.js'
import router from '../router';

const { query } = useRoute();
const categoryName = ref('')

onMounted(async () => {
    const category = await getAllCategories()
    categoryName.value = category.find((cat) => {
        return cat.id == Number(query?.categoryId)
    }).categoryName
})

const unsubscribe = async () => {
    const response = await fetch(import.meta.env.VITE_ROOT_API + `/api/subscription/unsubscribe`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ token: query?.token, categoryId: Number(query?.categoryId) })
    })
    if (response.ok) {
        Swal.fire({
            title: 'Unsubscribed!',
            text: `You have been unsubscribed from ${categoryName.value}`,
            icon: 'success',
            confirmButtonText: 'Ok'
        }).then(() => {
            window.location.href = '/'
        })
    } else {
        const errorData = await response.json()
        console.log(errorData)
    }
}

const cancelButton = () => {
    Swal.fire({
        title: 'Are you sure?',
        text: "You will be redirected to the home page",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#155e75',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, cancel!'
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire(
                'Cancelled!',
                'You have been redirected to the home page',
                'success'
            ).then(() => {
                router.push({ name: '/'  })
            })
        }
    })
}
</script>
 
<template>
    <div class="w-full flex justify-center">
        <div class="mt-56">
            <div class="text-4xl text-center">
                Unsubscribe from {{ categoryName }}
            </div>
            <div class="text-xl text-center mt-5">
                are you sure you wish to unsubscribe from this category?
            </div>
            <div class="flex justify-center">
                <unsubIcon class="text-5xl text-emerald-plus" />
            </div>
            <div class="flex justify-center mt-5">
                <button @click="cancelButton"
                    class="px-16 py-2 rounded-lg bg-emerald-plus text-white mx-2 hover:bg-emerald-light transition-colors duration-200">No</button>
                <button @click="unsubscribe()"
                    class="px-16 py-2 rounded-lg bg-zinc-400 text-white mx-2 hover:bg-zinc-500 transition-colors duration-200">Yes</button>
            </div>
        </div>
    </div>
</template>
 
<style scoped></style>