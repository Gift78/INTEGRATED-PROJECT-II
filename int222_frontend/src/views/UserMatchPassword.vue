<script setup>
import NavbarComponent from '../components/NavbarComponent.vue';
import { useRoute } from 'vue-router';
import { ref, onMounted, computed } from 'vue';

const params = useRoute().params;
const username = ref('');


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
                    <div class="text-4xl mt-2 ">Match Password</div>
                    <div class="text-lg mt-2 text-cyan-800">Username</div>
                    <input type="text" class="ann-username border rounded-lg mt-3 pl-3 w-full h-12 bg-white"
                        v-model.trim="username" maxlength="45">
                    <span class="px-4 mb-2 flex flex-col items-end">({{ username.length }}/45)</span>
                    <div class="text-lg mt-2 text-cyan-800">Password</div>
                    <input type="text" class="ann-username border rounded-lg mt-3 pl-3 w-full h-12 bg-white">

                    <div class="flex mt-3">
                        <button
                            class="ann-button text-white bg-cyan-800 hover:bg-cyan-700 border-0 shadow-lg hover:scale-110 w-28 h-12 rounded-lg">
                            Match or Not
                        </button>
                    </div>
                </div>

            </div>

        </div>

    </div>
</template>